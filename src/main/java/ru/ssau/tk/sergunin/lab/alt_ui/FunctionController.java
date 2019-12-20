package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class FunctionController implements Initializable, Openable {
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
    private Stage stage;
    private InputParameterController inputParameterController = new InputParameterController();
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private Map<String, MathFunction> comboBoxMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxMap = new LinkedHashMap<>();

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
        inputParameterController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/inputParameter.fxml", inputParameterController);
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
            ((TableController)parentController).createTab(function);
            stage.close();
        } catch (NullPointerException | NumberFormatException nfe) {
            AlertWindows.showWarning("Введите корректные значения");
        } catch (IllegalArgumentException e) {
            AlertWindows.showWarning("Укажите положительное > 2 количество точек");
        }
    }

    @FXML
    private void doOnClickOnComboBox(ActionEvent event) {
        if (comboBoxMap.get(((ComboBox) event.getSource()).getValue().toString()).getClass().getDeclaredAnnotation(Selectable.class).parameter()) {
            inputParameterController.getStage().show();
        }
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
