package ru.ssau.tk.sergunin.lab.alt_ui;

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
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

public class TableController implements Initializable, Openable {

    private Stage stage;
    private final TableColumn<Point, Double> x = new TableColumn<>("X");
    private String defaultDirectory;
    private int numberId = 1;
    private final TableColumn<Point, Double> y = new TableColumn<>("Y");
    private TabulatedFunctionFactory factory;
    private Map<Tab, TabulatedFunction> map;
    private Tab currentTab;
    private FunctionController functionController = new FunctionController();
    private TabulatedFunctionController tabulatedFunctionController = new TabulatedFunctionController();
    private Functions functions;
    private AddPoint addPoint = new AddPoint();
    private DeletePoint deletePoint = new DeletePoint();
    private Calc calc = new Calc();
    private About about = new About();
    private Settings settings = new Settings();

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
    @FXML
    private MenuItem plot;
    @FXML
    private MenuItem loadItem;
    @FXML
    private MenuItem saveItem;
    @FXML
    private MenuItem saveAsItem;

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
    }

    private void initializeWindowControllers() {
        functionController = initializeWindowController(functionController,
                "src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/newFunction.fxml", "Create new function");
        tabulatedFunctionController = initializeWindowController(tabulatedFunctionController,
                "src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/newTabulatedFunction.fxml", "Create new function");
        addPoint = initializeWindowController(addPoint,
                "src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/addPoint.fxml", "Add Point.");
        deletePoint = initializeWindowController(deletePoint,
                "src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/DeletePoint.fxml", "Delete Point.");
        calc = initializeWindowController(calc,
                "src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/Calc.fxml", "...");
        about = initializeWindowController(about,
                "src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/About.fxml", "About");
        settings = initializeWindowController(settings,
                "src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/Settings.fxml", "Settings");
    }

    public <T extends Openable> T initializeWindowController(T controller, String path, String windowName) {
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
        tab.setOnClosed(event -> {
            map.remove(tab);
            if (map.isEmpty()) {
                mainPane.getChildren().remove(bottomPane);
                currentTab = null;
            }
        });
    }

    void createTab(TableView table) {
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
        tab.setOnClosed(event -> {
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

    @FXML
    private void newFunctionButtonEvent() {
        functionController.getStage().show();
    }

    @FXML
    private void join() {
        functionController.getStage().show();
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

    public ObservableList<Point> getObservableList() {
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
                    ? new File(defaultDirectory + "\\" + currentTab.getText() + ".txt")
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
                deletePoint.getStage().show();
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void addPoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {
                addPoint.getStage().show();
            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void calculate() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {
                calc.getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void about() {
            about.play();
    }

    @FXML
    private void setting() {
        settings.start();
    }

    @FXML
    private void newTabulatedFunction() {
        tabulatedFunctionController.getStage().show();
        tabulatedFunctionController.getStage().setResizable(false);
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

    public void sort() {
        getObservableList().sort(Comparator.comparingDouble(point1 -> point1.x));
    }
}