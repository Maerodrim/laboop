package ru.ssau.tk.itenion.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

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
    private Map<Tab, VMF> VMFMap;
    private Map<String, MathFunction> mathFunctionMap;
    private Map<String, Openable> controllerMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private Tab currentTab;
    private IO io;
    private boolean isStrict = false;
    private boolean isUnmodifiable = false;
    private boolean isVMF = false;
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

    public static ObservableList<Point> getList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerMap = new HashMap<>();
        tabulatedFunctionMap = new LinkedHashMap<>();
        VMFMap = new LinkedHashMap<>();
        compositeFunctionMap = new LinkedHashMap<>();
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        x.setPrefWidth(300);
        y.setPrefWidth(300);
        tabPane.getSelectionModel().selectLast();
        factory = new ArrayTabulatedFunctionFactory();
        io = new IO(factory);
        boolean isCreated = new File(IO.DEFAULT_DIRECTORY).mkdir();
        clear(bottomPane);
        initializeMathFunctionMap();
        initializeWindowControllers();
        connectMathFunctionMap();
    }

    private void initializeMathFunctionMap() {
        mathFunctionMap = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.FUNCTION)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> {
                    try {
                        mathFunctionMap.put(clazz.getDeclaredAnnotation(ConnectableItem.class).name(),
                                (MathFunction) clazz.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        AlertWindows.showError(e);
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
                        AlertWindows.showError(e);
                    }
                });
    }

    private void connectMathFunctionMap() {
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

    void createTab(TabulatedFunction function) {
        createTab(getList(function), function);
    }

    void createTab(ObservableList<Point> list) {
        TabulatedFunction function = getFunction(list);
        TabulatedFunctionController controller = (TabulatedFunctionController) lookupController("tabulatedFunction");
        function.offerUnmodifiable(controller.isUnmodifiable());
        function.offerStrict(controller.isStrict());
        io.wrap(function);
        createTab(list, function);
    }

    private void createTab(ObservableList<Point> list, TabulatedFunction function) {
        TableView<Point> table1 = new TableView<>();
        table1.setItems(list);
        table1.getColumns().addAll(x, y);
        Tab tab = new Tab("Function" + numberId, table1);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        tabulatedFunctionMap.put(tab, function);
        notifyAboutAccessibility(function);
        currentTab = tab;
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                currentTab = tab;
                mainPane.setBottom(null);
                mainPane.setBottom(bottomPane);
                isVMF = !tabulatedFunctionMap.containsKey(tab);
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

    public void createTab(AnchorPane pane) {
        Tab tab = new Tab("Function" + numberId, pane);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        currentTab = tab;
        isVMF = true;
        //VMFMap.put(tab, function);
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                isVMF = !tabulatedFunctionMap.containsKey(tab);
                mainPane.setBottom(null);
                currentTab = tab;
            }
        });
        tab.setOnCloseRequest(event -> {
            //tabulatedFunctionMap.remove(tab);
            if (VMFMap.isEmpty()) {
                currentTab = null;
            }
        });
    }

    private void notifyAboutAccessibility(TabulatedFunction function) {
        boolean isStrict = function.isStrict();
        boolean isUnmodifiable = function.isUnmodifiable();
        List<Node> list = new ArrayList<>();
        list.add(calculateValueButton);
        list.add(label);
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
    public void setParentController(Openable controller) {
    }

    @FXML
    private void loadFunction() {
        File file = IO.load(stage);
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
                    ? new File(IO.DEFAULT_DIRECTORY + "\\" + getFunction().getMathFunction() + ".fnc")
                    : IO.save(stage);
            if (!Objects.equals(file, null)) {
                io.saveFunctionAs(file, getFunction());
            }
        }
    }

    public TabulatedFunction getFunction() {
        return tabulatedFunctionMap.get(currentTab);
    }

    public boolean isVMF() {
        return isVMF;
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

    private TabulatedFunction getFunction(ObservableList<Point> points) {
        for (Tab tab : tabulatedFunctionMap.keySet()) {
            if (((TableView<Point>) tab.getContent()).getItems().equals(points)) {
                return tabulatedFunctionMap.get(tab);
            }
        }
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

    private Openable lookupController() {
        return lookupController(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    private void show(boolean isResizable) {
        Stage stage = lookupController(Thread.currentThread().getStackTrace()[2].getMethodName()).getStage();
        stage.setResizable(isResizable);
        stage.showAndWait();
    }

    private Openable lookupController(String path) {
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
        show(true);
    }

    @FXML
    public void vectorFunction() {
        show(true);
    }

    @FXML
    private void deletePoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {
                show(true);
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void addPoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {
                show(true);
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void calculate() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                show(true);
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void about() {
        if (isTabExist()) {
            if (getFunction().isMathFunctionExist()) {
                Openable controller = lookupController();
                ((AboutController) controller).setInfo();
                controller.getStage().show();
            } else {
                AlertWindows.showWarning("Function doesn't have base math function");
            }
        }
    }

    @FXML
    public void plot() {
        if (isTabExist()) {
            PlotController controller = (PlotController) lookupController();
            controller.setSeries();
            controller.getStage().show();
        }
    }

    @FXML
    private void compose() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                show(true);
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void solve() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                SolveController controller = (SolveController) lookupController();
                controller.getStage().setResizable(false);
                controller.getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void settings() {
        ((SettingsController) lookupController()).start();
    }

    @FXML
    private void tabulatedFunction() {
        show(false);
    }

    @FXML
    private void apply() {
        if (isTabExist()) {
            show(false);
        }
    }

    @FXML
    private void operator() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                show(true);
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

    public void addCompositeFunction(MathFunction function) {
        compositeFunctionMap.put(function.getName(), function);
    }

    public Tab getCurrentTab() {
        return currentTab;
    }

    public Map<String, MathFunction> getCompositeFunctionMap() {
        return compositeFunctionMap;
    }

}