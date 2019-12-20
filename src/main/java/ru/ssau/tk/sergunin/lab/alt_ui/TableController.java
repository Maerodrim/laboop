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
    private TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    private String defaultDirectory;
    private int numberId = 1;
    private TableColumn<Point, Double> x = new TableColumn<>("X");
    private TableColumn<Point, Double> y = new TableColumn<>("Y");

    private FunctionController functionController; // контроллер окна создания новой функции
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
    private Tab currentTab;
    private Map<Tab, Map.Entry<ObservableList<Point>, TabulatedFunction>> map;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        map = new LinkedHashMap<>();
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        x.setPrefWidth(300);
        y.setPrefWidth(300);
        tabPane.getSelectionModel().selectLast();
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
    private void plot() {
        if (!Objects.equals(currentTab, null)) {
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
        if (!Objects.equals(currentTab, null)) {
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
    private void deletePoint(ActionEvent event) {
    }

    @FXML
    private void addPoint(ActionEvent event) {
    }

    @FXML
    private void calculate(ActionEvent event) {
    }

    private ObservableList<Point> getModelFunctionList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }
}