package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
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

public class FunctionController implements Initializable {

    private Stage stage;
    private int numberId = 1;
    private Map<Tab, Map.Entry<ObservableList<Point>, TabulatedFunction>> map;
    private TableColumn<Point, Double> x = new TableColumn<>("X");
    private TableColumn<Point, Double> y = new TableColumn<>("Y");
    private InputParameterController inputParameterController;
    private TabulatedFunctionFactory factory;
    private TabPane tabPane;
    private Tab currentTab;
    private Map<String, MathFunction> comboBoxMap;
    private BorderPane mainPane;
    private Label label;
    private BorderPane labelPane;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxMap = new LinkedHashMap<>();
        map = new LinkedHashMap<>();
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        x.setPrefWidth(300);
        y.setPrefWidth(300);

        StreamSupport.stream(ClassIndex.getAnnotated(Selectable.class).spliterator(), false)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(Selectable.class).priority()))
                .forEach(clazz -> {
                    try {
                        if (clazz.getDeclaredAnnotation(Selectable.class).parameter()) {
                            comboBoxMap.put(clazz.getDeclaredAnnotation(Selectable.class).name(), (MathFunction) clazz.getDeclaredConstructor(Double.TYPE).newInstance(0));
                        } else {
                            comboBoxMap.put(clazz.getDeclaredAnnotation(Selectable.class).name(), (MathFunction) clazz.getDeclaredConstructor().newInstance());
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
        comboBox.getItems().addAll(comboBoxMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
        initializeWindowControllers();
    }

    private void initializeWindowControllers() {
        inputParameterController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/inputParameter.fxml", InputParameterController.class);
        inputParameterController.getStage().initOwner(stage);
        inputParameterController.getStage().setTitle("Input parameter");
    }

    @FXML
    private void createFunction() {
        if (comboBoxMap.get(comboBox.getValue()).getClass().getDeclaredAnnotation(Selectable.class).parameter()) {
            try {
                comboBoxMap.replace(comboBox.getValue(), comboBoxMap.get(comboBox.getValue()).getClass().getDeclaredConstructor(Double.TYPE).newInstance(inputParameterController.getParameter()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            TabulatedFunction function = factory.create(comboBoxMap.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()),
                    isStrict.isSelected(),
                    isUnmodifiable.isSelected());
            createTab(function);
            stage.close();
        } catch (NullPointerException | NumberFormatException nfe) {
            AlertWindows.showWarning("Введите корректные значения");
        } catch (IllegalArgumentException e) {
            AlertWindows.showWarning("Укажите положительное количество точек > 2");
        }
        /*if ((((Button) event.getSource()).getText()).equals("Присоединить")) {
            primaryController.function = TabulatedFunction.join(primaryController.function, function, primaryController.factory);
        } else {
        }*/
    }

    public void createTab(TabulatedFunction function) {
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
                mainPane.getChildren().remove(labelPane);
                currentTab = null;
            }
        });
    }

    private void notifyAboutAccessibility(TabulatedFunction function) {
        boolean isStrict = function.isStrict();
        boolean isUnmodifiable = function.isUnmodifiable();
        if (isUnmodifiable && isStrict) {
            label.setText("Function is strict and unmodifiable");
            mainPane.setBottom(labelPane);
        } else if (isUnmodifiable) {
            label.setText("Function is unmodifiable");
            mainPane.setBottom(labelPane);
        } else if (isStrict) {
            label.setText("Function is strict");
            mainPane.setBottom(labelPane);
        } else {
            mainPane.getChildren().remove(labelPane);
        }
    }

    @FXML
    private void doOnClickOnComboBox(ActionEvent event) {
        if (comboBoxMap.get(((ComboBox) event.getSource()).getValue().toString()).getClass().getDeclaredAnnotation(Selectable.class).parameter()) {
            inputParameterController.getStage().show();
        }
    }

    void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public TabulatedFunction getFunction() {
        return map.get(currentTab).getValue();
    }

    ObservableList<Point> getObservableList() {
        return map.get(currentTab).getKey();
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

    void setLabelPane(BorderPane labelPane) {
        this.labelPane = labelPane;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
    }

    Tab getCurrentTab() {
        return currentTab;
    }

    private ObservableList<Point> getModelFunctionList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }

}
