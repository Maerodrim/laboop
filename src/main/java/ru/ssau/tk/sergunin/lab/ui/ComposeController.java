package ru.ssau.tk.sergunin.lab.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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

@ConnectableItem(name = "Compose", type = Item.CONTROLLER, pathFXML = "compose.fxml")
public class ComposeController implements Initializable, Openable, MathFunctionAccessible {
    @FXML
    public CheckBox isSaveable;
    @FXML
    ComboBox<String> comboBox;
    private Stage stage;
    private Openable parentController;
    private Map<String, MathFunction> functionMap;
    private InputParameterController inputParameterController = new InputParameterController();
    private TabulatedFunctionFactory factory;

    @FXML
    public void doOnClickOnComboBox(ActionEvent event) {
        /*if (functionMap.get(((ComboBox) event.getSource()).getValue().toString()).getClass().getDeclaredAnnotation(ConnectableItem.class).hasParameter()) {
            inputParameterController.getStage().show();
        }*/
        ConnectableItem item = functionMap.get(((ComboBox) event.getSource()).getValue().toString()).getClass()
                .getDeclaredAnnotation(ConnectableItem.class);
        if (!Objects.isNull(item) && item.hasParameter()) {
            inputParameterController.getStage().show();
            inputParameterController.setTypeOfParameter(item.parameterInstanceOfDouble() ? Double.TYPE : String.class);
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputParameterController = IO.initializeModalityWindow(IO.FXML_PATH + "inputParameter.fxml", inputParameterController);
        inputParameterController.getStage().initOwner(stage);
        inputParameterController.getStage().setTitle("Input parameter");
    }

    @FXML
    public void composeFunction() {
        TabulatedFunction parentFunction = ((TableController) parentController).getFunction();
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
            MathFunction mathFunction = functionMap.get(comboBox.getValue()).andThen(parentFunction);
            TabulatedFunction function = factory.create(
                    functionMap.get(comboBox.getValue()).andThen(parentFunction),
                    parentFunction.leftBound(), parentFunction.rightBound(),
                    parentFunction.getCount(),
                    ((TableController) parentController).isStrict(),
                    ((TableController) parentController).isUnmodifiable());
            if (isSaveable.isSelected()) {
                ((TableController) parentController)
                        .addCompositeFunction(comboBox.getValue() + "(" + ((TableController) parentController)
                                .getCurrentTab().getText() + ")", mathFunction);
            }
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
    private void cancel() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
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
}
