package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ApplyController implements Initializable, Openable {
    private Stage stage;
    private Openable parentController;

    private Map<String, Method> operationMap;
    private Map<String, TabulatedFunction> functionMap;

    private Map<Method, Class<?>> classes;

    @FXML
    private ComboBox<String> operationComboBox;
    @FXML
    private ComboBox<String> functionComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        operationMap = new LinkedHashMap<>();
        functionMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(SelectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(SelectableItem.class).type() == Item.OPERATION)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(SelectableItem.class).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(SelectableItem.class))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(SelectableItem.class).priority()))
                        .forEach(method -> {
                            operationMap.put(method.getDeclaredAnnotation(SelectableItem.class).name(), method);
                            classes.put(method, clazz);
                        }));
        operationComboBox.getItems().addAll(operationMap.keySet());
        operationComboBox.setValue(operationComboBox.getItems().get(0));
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(windowEvent -> {
            functionMap.clear();
            functionComboBox.getItems().clear();
        });
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    @FXML
    public void apply() {
        try {
            TabulatedFunction function = (TabulatedFunction) operationMap.get(operationComboBox.getValue())
                    .invoke(classes.get(operationMap.get(operationComboBox.getValue())).getDeclaredConstructor(TabulatedFunctionFactory.class)
                                    .newInstance(((TableController) parentController).getFactory()),
                            ((TableController) parentController).getFunction(),
                            functionMap.get(functionComboBox.getValue()));
            function.offerStrict(((TableController) parentController).isStrict());
            function.offerUnmodifiable(((TableController) parentController).isUnmodifiable());
            ((TableController) parentController).createTab(function);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        functionMap.clear();
        functionComboBox.getItems().clear();
        stage.close();
    }

    @FXML
    public void cancel() {
        functionMap.clear();
        functionComboBox.getItems().clear();
        stage.close();
    }

    ComboBox<String> getFunctionComboBox() {
        return functionComboBox;
    }

    Map<String, TabulatedFunction> getFunctionMap() {
        return functionMap;
    }

    void setFunctionMap(Map<Tab, TabulatedFunction> functionMap) {
        functionMap.forEach((tab, function) -> {
            if (function != ((TableController) parentController).getFunction() &&
                    !function.isStrict() &&
                    ((TableController) parentController).getFunction().isCanBeComposed(function)) {
                this.functionMap.put(tab.getText(), function);
            }
        });
    }
}
