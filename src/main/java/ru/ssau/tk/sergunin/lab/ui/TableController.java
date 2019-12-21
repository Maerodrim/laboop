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
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.StreamSupport;

public class TableController implements Initializable, Openable {

    private Stage stage;
    private final TableColumn<Point, Double> x = new TableColumn<>("X");
    private String defaultDirectory;
    private int numberId = 1;
    private final TableColumn<Point, Double> y = new TableColumn<>("Y");
    private TabulatedFunctionFactory factory;
    private Map<Tab, TabulatedFunction> map;
    private Map<String, MathFunction> comboBoxMap;
    private Tab currentTab;
    private String fxmlPath;
    //<controllers>
    private FunctionController functionController = new FunctionController();
    private TabulatedFunctionController tabulatedFunctionController = new TabulatedFunctionController();
    private Functions functions;
    private AddPointController addPointController = new AddPointController();
    private DeletePointController deletePointController = new DeletePointController();
    private CalculateController calculateController = new CalculateController();
    private AboutController aboutController = new AboutController();
    private SettingsController settingsController = new SettingsController();
    private CompositeFunctionController compositeFunctionController = new CompositeFunctionController();
    private ApplyController applyController = new ApplyController();
    private OperatorController operatorController = new OperatorController();
    //</controllers>
    private boolean isStrict = true;
    private boolean isUnmodifiable = false;
    @FXML
    private TabPane tabPane;
    @FXML
    private BorderPane mainPane;
    @FXML
    private BorderPane bottomPane;
    @FXML
    private Pane labelPane;
    @FXML
    private Button addPointButton;
    @FXML
    private Button deletePointButton;
    @FXML
    private Button calculateValueButton;
    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        map = new LinkedHashMap<>();
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        x.setPrefWidth(300);
        y.setPrefWidth(300);
        tabPane.getSelectionModel().selectLast();
        factory = new ArrayTabulatedFunctionFactory();
        functions = new Functions(factory);
        defaultDirectory = System.getenv("APPDATA") + "\\tempFunctions";
        new File(defaultDirectory).mkdir();
        mainPane.getChildren().remove(bottomPane);
        initializeWindowControllers();
        initializeComboBoxMap();
    }

    private void initializeComboBoxMap() {
        comboBoxMap = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(SelectableFunction.class).spliterator(), false)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(SelectableFunction.class).priority()))
                .forEach(clazz -> {
                    try {
                        if (clazz.getDeclaredAnnotation(SelectableFunction.class).parameter()) {
                            comboBoxMap.put(clazz.getDeclaredAnnotation(SelectableFunction.class).name(), (MathFunction) clazz.getDeclaredConstructor(Double.TYPE).newInstance(0));
                        } else {
                            comboBoxMap.put(clazz.getDeclaredAnnotation(SelectableFunction.class).name(), (MathFunction) clazz.getDeclaredConstructor().newInstance());
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
        functionController.setComboBoxMap(comboBoxMap);
        compositeFunctionController.setComboBoxMap(comboBoxMap);
        functionController.getComboBox().getItems().addAll(comboBoxMap.keySet());
        functionController.getComboBox().setValue(functionController.getComboBox().getItems().get(0));
        compositeFunctionController.getComboBox().getItems().addAll(comboBoxMap.keySet());
        compositeFunctionController.getComboBox().setValue(compositeFunctionController.getComboBox().getItems().get(0));
        applyController.setFunctionMap(map);
    }

    private void initializeWindowControllers() {
        functionController = initializeWindowController(functionController,
                "newFunction.fxml", "Create new function");
        tabulatedFunctionController = initializeWindowController(tabulatedFunctionController,
                "newTabulatedFunction.fxml", "Create new tabulated function");
        addPointController = initializeWindowController(addPointController,
                "addPoint.fxml", "Add point");
        deletePointController = initializeWindowController(deletePointController,
                "DeletePoint.fxml", "Delete point");
        calculateController = initializeWindowController(calculateController,
                "Calc.fxml", "Calculate");
        aboutController = initializeWindowController(aboutController,
                "About.fxml", "About");
        settingsController = initializeWindowController(settingsController,
                "Settings.fxml", "Settings");
        compositeFunctionController = initializeWindowController(compositeFunctionController,
                "CompositeFunctionController.fxml", "Compose");
        applyController = initializeWindowController(applyController,
                "Apply.fxml", "Apply");
        operatorController = initializeWindowController(operatorController,
                "OperatorController.fxml", "Differentiate/Integrate");
    }

    private <T extends Openable> T initializeWindowController(T controller, String path, String windowName) {
        path = Functions.FXML_PATH + path;
        controller = Functions.initializeModalityWindow(path, controller);
        controller.getStage().initOwner(stage);
        controller.getStage().setTitle(windowName);
        controller.setFactory(factory);
        controller.setParentController(this);
        return controller;
    }

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
        map.put(tab, function);
        notifyAboutAccessibility(function);
        currentTab = tab;
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                currentTab = tab;
                notifyAboutAccessibility(getFunction());
            }
        });
        tab.setOnCloseRequest(event -> {
            map.remove(tab);
            if (map.isEmpty()) {
                mainPane.getChildren().remove(bottomPane);
                currentTab = null;
            }
        });
    }

    void createTab(TableView<Point> table) {
        TabulatedFunction function = getFunction(table.getItems());
        function.offerUnmodifiable(tabulatedFunctionController.isUnmodifiable());
        function.offerStrict(tabulatedFunctionController.isStrict());
        functions.wrap(function);
        Tab tab = new Tab();
        tab.setText("Function" + numberId);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        tab.setContent(table);
        tabPane.getSelectionModel().select(tab);
        map.put(tab, function);
        notifyAboutAccessibility(function);
        currentTab = tab;
        tab.setOnSelectionChanged(event -> {
            if (tab.isSelected()) {
                currentTab = tab;
                notifyAboutAccessibility(getFunction());
            }
        });
        tab.setOnCloseRequest(event -> {
            map.remove(tab);
            if (map.isEmpty()) {
                mainPane.getChildren().remove(bottomPane);
                currentTab = null;
            }
        });
    }

    private void notifyAboutAccessibility(TabulatedFunction function) {
        boolean isStrict = function.isStrict();
        boolean isUnmodifiable = function.isUnmodifiable();
        List<Node> list = List.of(calculateValueButton, label);
        if (map.size() == 1) {
            mainPane.setBottom(bottomPane);
        }
        if (isUnmodifiable && isStrict) {
            clear(bottomPane);
            bottomPane.setLeft(null);
            bottomPane.setCenter(labelPane);
            bottomPane.setRight(null);
            labelPane.getChildren().removeAll(list);
            labelPane.getChildren().add(label);
            label.setText("Function is strict and unmodifiable");
        } else if (isUnmodifiable) {
            clear(bottomPane);
            bottomPane.setLeft(null);
            bottomPane.setCenter(labelPane);
            bottomPane.setRight(null);
            labelPane.getChildren().removeAll(list);
            labelPane.getChildren().add(calculateValueButton);
            calculateValueButton.layoutXProperty().setValue(225);
        } else if (isStrict) {
            clear(bottomPane);
            bottomPane.setLeft(addPointButton);
            BorderPane.setMargin(addPointButton, new Insets(0, 0, 0, 80));
            BorderPane.setMargin(deletePointButton, new Insets(0, 80, 0, 0));
            bottomPane.setRight(deletePointButton);
        } else {
            clear(bottomPane);
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
        return null;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
    }

    @FXML
    public void newMathFunction() {
        functionController.getStage().show();
    }

    @FXML
    private void plot() {
        if (isTabExist()) {
            Plot.plotFunction(stage, getObservableList());
        }
    }

    @FXML
    private void loadFunction() {
        File file = Functions.load(stage, defaultDirectory);
        if (!Objects.equals(file, null)) {
            createTab(functions.loadFunctionAs(file));
        }
    }

    public TabulatedFunction getFunction() {
        return map.get(currentTab);
    }

    ObservableList<Point> getObservableList() {
        return ((TableView<Point>) currentTab.getContent()).getItems();
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
                    ? new File(defaultDirectory + "\\" + currentTab.getText() + ".fnc")
                    : Functions.save(stage);
            if (!Objects.equals(file, null)) {
                functions.saveFunctionAs(file, getFunction());
            }
        }
    }

    @FXML
    private void exit() {
        stage.close();
    }

    @FXML
    private void deletePoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {
                deletePointController.getStage().show();
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void addPoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {
                addPointController.getStage().show();
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void calculate() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                calculateController.getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void about() {
        aboutController.play();
    }

    @FXML
    private void compose() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                compositeFunctionController.getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void settings() {
        settingsController.start();
    }

    @FXML
    private void newTabulatedFunction() {
        tabulatedFunctionController.getStage().show();
        tabulatedFunctionController.getStage().setResizable(false);
    }

    @FXML
    private void apply() {
        if (isTabExist()) {
            applyController.setFunctionMap(map);
            if (applyController.getFunctionMap().isEmpty()) {
                AlertWindows.showWarning("Отсутствуют подходящие функции");
            } else {
                applyController.getFunctionComboBox().getItems().addAll(applyController.getFunctionMap().keySet());
                applyController.getFunctionComboBox().setValue(applyController.getFunctionComboBox().getItems().get(0));
                applyController.getStage().show();
            }
        }
    }

    @FXML
    private void operate() {
        if (isTabExist()) {
            operatorController.getStage().show();
        }
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

    void sort(ObservableList<Point> list) {
        list.sort(Comparator.comparingDouble(point -> point.x));
    }

    void sort() {
        sort(getObservableList());
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
}