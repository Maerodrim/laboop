package ru.ssau.tk.sergunin.lab.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.OPERATOR)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(ConnectableItem.class))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                        .forEach(method -> {
                            operatorMap.put(method.getDeclaredAnnotation(ConnectableItem.class).name(), method);
                            classes.put(method, clazz);
                        }));
        comboBox.getItems().addAll(operatorMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }

    public void doOnClickOnTheComboBox(ActionEvent event) {
        ConnectableItem item = operatorMap.get(((ComboBox<String>) event.getSource()).getValue())
                .getDeclaredAnnotation(ConnectableItem.class);
        if (!Objects.isNull(item) && !item.numericalOperator()
                && Objects.isNull(((TableController) parentController).getFunction().getMathFunction())) {
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
        TabulatedFunction sourceFunction = ((TableController) parentController).getFunction();
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
                e.printStackTrace();
            }
        } else {
            MathFunction mathFunction = null;
            try {
                mathFunction = (MathFunction) operatorMap.get(comboBox.getValue()).invoke(
                        classes.get(operatorMap.get(comboBox.getValue())).getDeclaredConstructor().newInstance(), sourceFunction.getMathFunction());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            function = factory.create(
                    mathFunction,
                    sourceFunction.leftBound(), sourceFunction.rightBound(),
                    sourceFunction.getCount(),
                    ((TableController) parentController).isStrict(),
                    ((TableController) parentController).isUnmodifiable());
            function.setMathFunction(mathFunction);
        }
        if (function != null) {
            function.offerStrict(((TableController) parentController).isStrict());
            function.offerUnmodifiable(((TableController) parentController).isUnmodifiable());
        }
        ((TableController) parentController).createTab(function);
        stage.close();
    }

}

