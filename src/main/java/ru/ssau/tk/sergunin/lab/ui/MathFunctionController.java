package ru.ssau.tk.sergunin.lab.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.exceptions.NaNException;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

@ConnectableItem(name = "Create new math function", type = Item.CONTROLLER, pathFXML = "mathFunction.fxml")
public class MathFunctionController implements Initializable, Openable, MathFunctionAccessible, CompositeFunctionAccessible {
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
    private Map<String, MathFunction> functionMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputParameterController = IO.initializeModalityWindow(
                IO.FXML_PATH + "inputParameter.fxml", inputParameterController);
        inputParameterController.getStage().initOwner(stage);
        inputParameterController.getStage().setTitle("Input parameter");
    }

    @FXML
    private void createFunction() {
        ConnectableItem item = functionMap.get(comboBox.getValue()).getClass().getDeclaredAnnotation(ConnectableItem.class);
        if (!Objects.isNull(item) && item.hasParameter()) {
            try {
                if (item.parameterInstanceOfDouble()) {
                    functionMap.replace(comboBox.getValue(), functionMap.get(comboBox.getValue()).getClass()
                            .getDeclaredConstructor(Double.TYPE).newInstance(inputParameterController.getDoubleParameter()));
                } else {
                    functionMap.replace(comboBox.getValue(), functionMap.get(comboBox.getValue()).getClass()
                            .getDeclaredConstructor(String.class).newInstance(inputParameterController.getParameter()));
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            TabulatedFunction function = factory.create(functionMap.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()),
                    isStrict.isSelected(),
                    isUnmodifiable.isSelected());
            ((TableController) parentController).createTab(function);
            stage.close();
        } catch (NullPointerException | NumberFormatException nfe) {
            AlertWindows.showWarning("Введите корректные значения");
        } catch (IllegalArgumentException e) {
            AlertWindows.showWarning("Укажите положительное > 2 количество точек");
        } catch (NaNException e) {
            AlertWindows.showWarning("Результат не существует");
        }
    }

    @FXML
    private void doOnClickOnComboBox(ActionEvent event) {
        if (!Objects.isNull(functionMap.get(((ComboBox) event.getSource()).getValue()))) {
            ConnectableItem item = functionMap.get(((ComboBox) event.getSource()).getValue().toString()).getClass()
                    .getDeclaredAnnotation(ConnectableItem.class);
            if (!Objects.isNull(item) && item.hasParameter()) {
                inputParameterController.getStage().show();
                inputParameterController.setTypeOfParameter(item.parameterInstanceOfDouble() ? Double.TYPE : String.class);
            }
        }
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    @Override
    public void setMathFunctionNode() {
        comboBox.getItems().addAll(functionMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }

    @Override
    public void setMathFunctionMap(Map<String, MathFunction> functionMap) {
        this.functionMap = functionMap;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Openable getParentController() {
        return parentController;
    }

    @Override
    public void updateCompositeFunctionNode() {
        comboBox.getItems().clear();
        comboBox.getItems().setAll(functionMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }

    @Override
    public void updateCompositeFunctionMap(Map<String, MathFunction> functionMap) {
        this.functionMap.putAll(functionMap);
    }
}
