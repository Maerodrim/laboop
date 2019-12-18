package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.StreamSupport;

public class CreateNewFunctionController implements Initializable {

    Stage stage;
    int numberId = 1;
    List<Tab> tabs = new ArrayList<>();
    ObservableList<Point> list;
    TableColumn<Point, Double> x = new TableColumn<>("X");
    TableColumn<Point, Double> y = new TableColumn<>("Y");
    InputParameterController inputParameterController;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    TextField leftBorder;
    @FXML
    TextField rightBorder;
    @FXML
    TextField numberOfPoints;
    @FXML
    CheckBox isUnmodifiable;
    @FXML
    CheckBox isStrict;
    @FXML
    Button create;
    private TabulatedFunction function;
    private TabulatedFunctionFactory factory;
    private TabPane tabPane;
    private Map<String, MathFunction> map;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        map = new LinkedHashMap<>();
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));

        StreamSupport.stream(ClassIndex.getAnnotated(Selectable.class).spliterator(), false)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(Selectable.class).priority()))
                .forEach(clazz -> {
                    try {
                        if (clazz.getDeclaredAnnotation(Selectable.class).parameter()) {
                            map.put(clazz.getDeclaredAnnotation(Selectable.class).name(), (MathFunction) clazz.getDeclaredConstructor(Double.TYPE).newInstance(0));
                        } else {
                            map.put(clazz.getDeclaredAnnotation(Selectable.class).name(), (MathFunction) clazz.getDeclaredConstructor().newInstance());
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
        comboBox.getItems().addAll(map.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
        initializeWindowControllers();
    }

    private void initializeWindowControllers() {
        inputParameterController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/inputParameter.fxml", InputParameterController.class);
        inputParameterController.getStage().initOwner(stage);
        inputParameterController.getStage().setTitle("Input parameter");
    }

    @FXML
    private void doOnCreate(ActionEvent event) {
        Tab tab = new Tab();
        tab.setText("Function" + numberId);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        if (map.get(comboBox.getValue()).getClass().getDeclaredAnnotation(Selectable.class).parameter()) {
            try {
                map.replace(comboBox.getValue(), map.get(comboBox.getValue()).getClass().getDeclaredConstructor(Double.TYPE).newInstance(Double.parseDouble(inputParameterController.getTextFieldValue())));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            function = factory.create(map.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()),
                    isStrict.isSelected(),
                    isUnmodifiable.isSelected());
            list = getModelFunctionList(function);
        } catch (NullPointerException | NumberFormatException nfe) {
            AlertWindows.showWarning("Введите недостающие значения");
        }
        /*if ((((Button) event.getSource()).getText()).equals("Присоединить")) {
            primaryController.function = TabulatedFunction.join(primaryController.function, function, primaryController.factory);
        } else {
        }*/
        TableView<Point> table = new TableView<>();
        table.setItems(list);
        table.getColumns().addAll(x, y);
        tab.setContent(table);
        tabs.add(tab);
    }

    @FXML
    private void doOnClickOnComboBox(ActionEvent event) {
        if (map.get(((ComboBox) event.getSource()).getValue().toString()).getClass().getDeclaredAnnotation(Selectable.class).parameter()) {
            inputParameterController.getStage().show();
        }
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public TabulatedFunction getFunction() {
        return function;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ObservableList<Point> getModelFunctionList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }

}
