package ru.ssau.tk.itenion.ui;

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

import static ru.ssau.tk.itenion.ui.Initializer.initializeMap;

@ConnectableItem(name = "Differentiate/Integrate", type = Item.CONTROLLER, pathFXML = "operator.fxml")
public class OperatorController implements TFTabVisitor, FactoryAccessible, Initializable, OpenableWindow {

    @FXML
    ComboBox<String> comboBox;
    private Stage stage;
    private Map<String, Method> operatorMap;
    private Map<Method, Class<?>> classes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        operatorMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        Map[] maps = initializeMap(classes, operatorMap, Item.OPERATOR);
        classes = (Map<Method, Class<?>>) maps[0];
        operatorMap = (Map<String, Method>) maps[1];
        comboBox.getItems().addAll(operatorMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }

    public void doOnClickOnTheComboBox() {
        state().accept((TFTabVisitor) tfState -> {
            ConnectableItem item = operatorMap.get(comboBox.getSelectionModel().getSelectedItem())
                    .getDeclaredAnnotation(ConnectableItem.class);
            if (!Objects.isNull(item) && !item.numericalOperator() && !tfState.getFunction().isMathFunctionExist()) {
                AlertWindows.showWarning("Function doesn't have base math function");
                comboBox.getSelectionModel().selectFirst();
            }
        });
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void cancel() {
        stage.close();
    }

    @FXML
    public void ok() {
        state().accept(this);
    }

    @Override
    public void visit(TabController.TFState tfState) {
        TabulatedFunction sourceFunction = tfState.getFunction();
        TabulatedFunction function = null;
        ConnectableItem item = operatorMap.get(comboBox.getSelectionModel().getSelectedItem())
                .getDeclaredAnnotation(ConnectableItem.class);
        if (item.numericalOperator()) {
            try {
                function = (TabulatedFunction) operatorMap
                        .get(comboBox.getValue())
                        .invoke(classes.get(operatorMap.get(comboBox.getValue()))
                                .getDeclaredConstructor(TabulatedFunctionFactory.class)
                                .newInstance(factory()), tfState.getFunction());
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
            function = factory().create(
                    mathFunction,
                    sourceFunction.leftBound(), sourceFunction.rightBound(),
                    sourceFunction.getCount());
            function.setMathFunction(mathFunction);
        }
        tfState.createTab(function);
        stage.close();
    }
}

