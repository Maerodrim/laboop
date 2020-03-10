package ru.ssau.tk.itenion.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

@ConnectableItem(name = "Differentiate/Integrate", type = Item.CONTROLLER, pathFXML = "operator.fxml")
public class OperatorController implements Initializable, Openable {

    @FXML
    ComboBox<String> comboBox;
    TabulatedFunctionFactory factory;
    private Stage stage;
    private Openable parentController;
    private Map<String, Method> operatorMap;
    private Map<Method, Class<?>> classes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        operatorMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        Map[] maps = IO.initializeMap(classes, operatorMap, Item.OPERATOR);
        classes = (Map<Method, Class<?>>) maps[0];
        operatorMap = (Map<String, Method>) maps[1];
        comboBox.getItems().addAll(operatorMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }

    public void doOnClickOnTheComboBox(ActionEvent event) {
        ConnectableItem item = operatorMap.get(((ComboBox<String>) event.getSource()).getValue())
                .getDeclaredAnnotation(ConnectableItem.class);
        if (!Objects.isNull(item) && !item.numericalOperator()
                && !((TabulatedFunction) ((TableController) parentController).getFunction()).isMathFunctionExist()) {
            AlertWindows.showWarning("Function doesn't have base math function");
            comboBox.getSelectionModel().select(0);
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    @FXML
    public void cancel() {
        stage.close();
    }

    @FXML
    public void ok() {
        TabulatedFunction sourceFunction = ((TabulatedFunction) ((TableController) parentController).getFunction());
        TabulatedFunction function = null;
        ConnectableItem item = operatorMap.get(comboBox.getSelectionModel().getSelectedItem())
                .getDeclaredAnnotation(ConnectableItem.class);
        if (item.numericalOperator()) {
            try {
                function = (TabulatedFunction) operatorMap.get(comboBox.getValue()).invoke(
                        classes.get(operatorMap.get(comboBox.getValue())).getDeclaredConstructor(TabulatedFunctionFactory.class)
                                .newInstance(((TableController) parentController).getFactory()),
                        ((TableController) parentController).getFunction());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                AlertWindows.showError(e);
            }
        } else {
            MathFunction mathFunction = null;
            try {
                mathFunction = (MathFunction) operatorMap.get(comboBox.getValue()).invoke(
                        classes.get(operatorMap.get(comboBox.getValue())).getDeclaredConstructor().newInstance(), sourceFunction.getMathFunction());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                AlertWindows.showError(e);
            }
            function = factory.create(
                    mathFunction,
                    sourceFunction.leftBound(), sourceFunction.rightBound(),
                    sourceFunction.getCount(),
                    ((TableController) parentController).isStrict(),
                    ((TableController) parentController).isUnmodifiable());
            function.setMathFunction(mathFunction);
        }
        assert function != null;
        function.offerStrict(((TableController) parentController).isStrict());
        function.offerUnmodifiable(((TableController) parentController).isUnmodifiable());
        ((TableController) parentController).createTab(function);
        stage.close();
    }

}

