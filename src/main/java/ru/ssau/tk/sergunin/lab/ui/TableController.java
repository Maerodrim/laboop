package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.StreamSupport;

public class TableController implements Initializable, Openable {

    private final TableColumn<Point, Double> x = new TableColumn<>("X");
    private final TableColumn<Point, Double> y = new TableColumn<>("Y");
    private Stage stage;
    private int numberId = 1;
    private TabulatedFunctionFactory factory;
    private Map<Tab, TabulatedFunction> tabulatedFunctionMap;
    private Map<String, MathFunction> mathFunctionMap;
    private Map<String, Openable> controllerMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private Tab currentTab;
    private IO io;
    private boolean isStrict = true;
    private boolean isUnmodifiable = false;
    @FXML
    private TabPane tabPane;
    @FXML
    private BorderPane mainPane, bottomPane;
    @FXML
    private Pane labelPane;
    @FXML
    private Button addPointButton, deletePointButton, calculateValueButton;
    @FXML
    private Label label;

    @Override
    @SuppressWarnings(value= "ResultOfMethodCallIgnored")
    public void initialize(URL location, ResourceBundle resources) {
        controllerMap = new HashMap<>();
        tabulatedFunctionMap = new LinkedHashMap<>();
        compositeFunctionMap = new LinkedHashMap<>();
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        x.setPrefWidth(300);
        y.setPrefWidth(300);
        tabPane.getSelectionModel().selectLast();
        factory = new ArrayTabulatedFunctionFactory();
        io = new IO(factory);
        new File(IO.DEFAULT_DIRECTORY).mkdir();
        clear(bottomPane);
        initializeMathFunctionMap();
        initializeWindowControllers();
    }

    private void initializeMathFunctionMap() {
        mathFunctionMap = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.FUNCTION)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> {
                    try {
                        if (clazz.getDeclaredAnnotation(ConnectableItem.class).hasParameter()) {
                            if (clazz.getDeclaredAnnotation(ConnectableItem.class).parameterInstanceOfDouble()) {
                                mathFunctionMap.put(clazz.getDeclaredAnnotation(ConnectableItem.class).name(),
                                        (MathFunction) clazz.getDeclaredConstructor(Double.TYPE).newInstance(0));
                            } else {
                                mathFunctionMap.put(clazz.getDeclaredAnnotation(ConnectableItem.class).name(),
                                        (MathFunction) clazz.getDeclaredConstructor(String.class).newInstance(""));
                            }
                        } else {
                            mathFunctionMap.put(clazz.getDeclaredAnnotation(ConnectableItem.class).name(),
                                    (MathFunction) clazz.getDeclaredConstructor().newInstance());
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void initializeWindowControllers() {
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.CONTROLLER)
                .forEach(clazz -> {
                    try {
                        controllerMap.put(clazz.getDeclaredAnnotation(ConnectableItem.class).pathFXML(),
                                initializeWindowController((Openable) clazz.getConstructor().newInstance()));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
        controllerMap.values().stream()
                .filter(f -> f instanceof MathFunctionAccessible)
                .forEach(f -> ((MathFunctionAccessible) f).connectMathFunctionMap(mathFunctionMap));
    }

    private <T extends Openable> T initializeWindowController(T controller) {
        String path = IO.FXML_PATH + controller.getClass().getDeclaredAnnotation(ConnectableItem.class).pathFXML();
        controller = IO.initializeModalityWindow(path, controller);
        controller.getStage().initOwner(stage);
        controller.getStage().setTitle(controller.getClass().getDeclaredAnnotation(ConnectableItem.class).name());
        controller.setFactory(factory);
        controller.setParentController(this);
        return controller;
    }

    @SuppressWarnings("unchecked")
    void createTab(TabulatedFunction function) {
        Tab tab = new Tab();
        tab.setText("Function" + numberId);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        ObservableList<Point> list = getList(function);
        TableView<Point> table = new TableView<>();
        table.setItems(list);
        table.getColumns().addAll(x, y);
        tab.setContent(table);
        tabPane.getSelectionModel().select(tab);
        tabulatedFunctionMap.put(tab, function);
        notifyAboutAccessibility(function);
        currentTab = tab;
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                currentTab = tab;
                notifyAboutAccessibility(getFunction());
            }
        });
        tab.setOnCloseRequest(event -> {
            tabulatedFunctionMap.remove(tab);
            if (tabulatedFunctionMap.isEmpty()) {
                mainPane.getChildren().remove(bottomPane);
                currentTab = null;
            }
        });
    }

    void createTab(TableView<Point> table) {
        TabulatedFunction function = getFunction(table.getItems());
        TabulatedFunctionController controller = (TabulatedFunctionController)getController("tabulatedFunction");
        function.offerUnmodifiable(controller.isUnmodifiable());
        function.offerStrict(controller.isStrict());
        io.wrap(function);
        Tab tab = new Tab();
        tab.setText("Function" + numberId);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tab.setContent(table);
        tabPane.getSelectionModel().select(tab);
        tabulatedFunctionMap.put(tab, function);
        notifyAboutAccessibility(function);
        currentTab = tab;
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                currentTab = tab;
                notifyAboutAccessibility(getFunction());
            }
        });
        tab.setOnCloseRequest(event -> {
            tabulatedFunctionMap.remove(tab);
            if (tabulatedFunctionMap.isEmpty()) {
                mainPane.getChildren().remove(bottomPane);
                currentTab = null;
            }
        });
    }

    private void notifyAboutAccessibility(TabulatedFunction function) {
        boolean isStrict = function.isStrict();
        boolean isUnmodifiable = function.isUnmodifiable();
        List<Node> list = List.of(calculateValueButton, label);
        if (tabulatedFunctionMap.size() == 1) {
            mainPane.setBottom(bottomPane);
        }
        clear(bottomPane);
        if (isUnmodifiable && isStrict) {
            bottomPane.setLeft(null);
            bottomPane.setCenter(labelPane);
            bottomPane.setRight(null);
            labelPane.getChildren().removeAll(list);
            labelPane.getChildren().add(label);
            label.setText("Function is strict and unmodifiable");
        } else if (isUnmodifiable) {
            bottomPane.setLeft(null);
            bottomPane.setCenter(labelPane);
            bottomPane.setRight(null);
            labelPane.getChildren().removeAll(list);
            labelPane.getChildren().add(calculateValueButton);
            calculateValueButton.layoutXProperty().setValue(225);
        } else if (isStrict) {
            bottomPane.setLeft(addPointButton);
            BorderPane.setMargin(addPointButton, new Insets(0, 0, 0, 80));
            BorderPane.setMargin(deletePointButton, new Insets(0, 80, 0, 0));
            bottomPane.setRight(deletePointButton);
        } else {
            bottomPane.setLeft(addPointButton);
            BorderPane.setMargin(addPointButton, new Insets(0, 0, 0, 30));
            BorderPane.setMargin(deletePointButton, new Insets(0, 30, 0, 0));
            bottomPane.setRight(deletePointButton);
            bottomPane.setCenter(labelPane);
            labelPane.getChildren().removeAll(list);
            labelPane.getChildren().add(calculateValueButton);
            calculateValueButton.layoutXProperty().setValue(45);
        }
    }

    private void clear(BorderPane pane) {
        pane.setTop(null);
        pane.setLeft(null);
        pane.setCenter(null);
        pane.setRight(null);
        pane.setBottom(null);
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setParentController(Openable controller) {}

    @FXML
    private void plot() {
        if (isTabExist()) {
            Plot.plotFunction(stage, getObservableList());
        }
    }

    @FXML
    private void loadFunction() {
        File file = IO.load(stage, IO.DEFAULT_DIRECTORY);
        if (!Objects.equals(file, null)) {
            createTab(io.loadFunctionAs(file));
        }
    }

    @FXML
    private void saveFunction() {
        save(true);
    }

    @FXML
    private void saveAsFunction() {
        save(false);
    }

    private void save(boolean toTempPath) {
        if (isTabExist()) {
            File file = toTempPath
                    ? new File(IO.DEFAULT_DIRECTORY + "\\" + currentTab.getText() + ".fnc")
                    : IO.save(stage);
            if (!Objects.equals(file, null)) {
                io.saveFunctionAs(file, getFunction());
            }
        }
    }

    public TabulatedFunction getFunction() {
        return tabulatedFunctionMap.get(currentTab);
    }

    @SuppressWarnings("unchecked")
    ObservableList<Point> getObservableList() {
        return ((TableView<Point>) currentTab.getContent()).getItems();
    }

    @FXML
    private void exit() {
        stage.close();
    }

    private boolean isTabExist() {
        return !Objects.equals(currentTab, null);
    }

    private ObservableList<Point> getList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }

    private TabulatedFunction getFunction(ObservableList<Point> points) {
        double[] valuesX = new double[points.size()];
        double[] valuesY = new double[points.size()];
        int i = 0;
        for (Point point : points) {
            valuesX[i] = point.x;
            valuesY[i++] = point.y;
        }
        return factory.create(valuesX, valuesY);
    }

    void sort(@NotNull ObservableList<Point> list) {
        list.sort(Comparator.comparingDouble(point -> point.x));
    }

    void sort() {
        sort(getObservableList());
    }

    private Openable getController(){
        return getController(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    private Openable getController(String path){
        Openable controller = controllerMap.get(path + ".fxml");
        if (controller instanceof TabulatedFunctionAccessible) {
            ((TabulatedFunctionAccessible) controller).connectTabulatedFunctionMap();
        }
        if (controller instanceof CompositeFunctionAccessible) {
            ((CompositeFunctionAccessible) controller).connectCompositeFunctionMap();
        }
        return controllerMap.get(path + ".fxml");
    }

    @FXML
    public void mathFunction() {
        getController().getStage().show();
    }

    @FXML
    private void deletePoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {
                getController().getStage().show();
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void addPoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {
                getController().getStage().show();
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void calculate() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                getController().getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void about() {
        if (isTabExist()) {
            Openable controller = getController();
            ((AboutController)controller).setInfo();
            controller.getStage().show();
        }
    }

    @FXML
    private void compose() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                getController().getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }
    @FXML
    private void solvePolynomial() {
        if (isTabExist()) {
            if (getFunction().getMathFunction().toString().split(":")[0].equals("PolynomialFunction")) {
                getController().getStage().show();
            } else {
                AlertWindows.showWarning("Function isn't polynomial function");
            }
        }
    }
    @FXML
    private void settings() {
        ((SettingsController)getController()).start();
    }

    @FXML
    private void tabulatedFunction() {
        Stage stage = getController().getStage();
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void apply() {
        if (isTabExist()) {
            Stage stage = getController().getStage();
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    private void operator() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                getController().getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    public boolean isStrict() {
        return isStrict;
    }

    void setStrict(boolean strict) {
        isStrict = strict;
    }

    public boolean isUnmodifiable() {
        return isUnmodifiable;
    }

    void setUnmodifiable(boolean unmodifiable) {
        isUnmodifiable = unmodifiable;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    Map<Tab, TabulatedFunction> getTabulatedFunctionMap() {
        return tabulatedFunctionMap;
    }

    public void addCompositeFunction(String name, MathFunction function) {
        compositeFunctionMap.put(name, function);
    }

    public Tab getCurrentTab() {
        return currentTab;
    }

    public Map<String, MathFunction> getCompositeFunctionMap() {
        return compositeFunctionMap;
    }
}