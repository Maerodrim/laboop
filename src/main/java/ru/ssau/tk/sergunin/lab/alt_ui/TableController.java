package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class TableController implements Initializable {

    private Stage stage;
    private final TableColumn<Point, Double> x = new TableColumn<>("X");
    private String defaultDirectory;
    private int numberId = 1;
    private final TableColumn<Point, Double> y = new TableColumn<>("Y");
    private TabulatedFunctionFactory factory;
    private Map<Tab, Map.Entry<ObservableList<Point>, TabulatedFunction>> map;
    private Tab currentTab;
    private FunctionController functionController;
    private TabulatedFunctionController tabulatedFunctionController;
    private Functions functions;

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
        functionController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/newFunction.fxml", FunctionController.class);
        functionController.getStage().initOwner(stage);
        functionController.getStage().setTitle("Create new function");
        functionController.setFactory(factory);
        functionController.setParentController(this);

        tabulatedFunctionController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/newTabulatedFunction.fxml", TabulatedFunctionController.class);
        tabulatedFunctionController.getStage().initOwner(stage);
        tabulatedFunctionController.getStage().setTitle("Create new function");
        tabulatedFunctionController.setFactory(factory);
        tabulatedFunctionController.setParentController(this);
    }

    void createTab(TabulatedFunction function) {
        Tab tab = new Tab();
        tab.setText("Function" + numberId);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        ObservableList<Point> list = getModelFunctionList(function);
        TableView<Point> table = new TableView<>();
        table.setItems(list);
        table.getColumns().addAll(x, y);
        tab.setContent(table);
        tabPane.getSelectionModel().select(tab);
        map.put(tab, Map.entry(list, function));
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

    private void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
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
        if (file.exists()) {
            createTab(functions.loadFunctionAs(file));
        }
    }

    private TabulatedFunction getFunction() {
        return map.get(currentTab).getValue();
    }

    private ObservableList<Point> getObservableList() {
        return map.get(currentTab).getKey();
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
            if (file.exists()) {
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

            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void addPoint() {
        if (isTabExist()) {
            if (!getFunction().isUnmodifiable()) {

            } else {
                AlertWindows.showWarning("Function is unmodifiable");
            }
        }
    }

    @FXML
    private void calculate() {
        if (isTabExist()) {
            if (!getFunction().isStrict()) {

            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void newTabulatedFunction(){
        tabulatedFunctionController.getStage().show();
    }

    private boolean isTabExist() {
        return !Objects.equals(currentTab, null);
    }

    private ObservableList<Point> getModelFunctionList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }
}