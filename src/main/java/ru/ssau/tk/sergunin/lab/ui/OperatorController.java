package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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

public class OperatorController implements Initializable, Openable {

    private Stage stage;
    private Openable parentController;
    @FXML
    ComboBox<String> comboBox;
    private Map<String, Method> operatorMap;
    private Map<Method, Class<?>> classes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        operatorMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(SelectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(SelectableItem.class).type() == Item.OPERATOR)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(SelectableItem.class).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(SelectableItem.class))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(SelectableItem.class).priority()))
                        .forEach(method -> {
                            operatorMap.put(method.getDeclaredAnnotation(SelectableItem.class).name(), method);
                            classes.put(method, clazz);
                        }));
        comboBox.getItems().addAll(operatorMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
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
        try {
            TabulatedFunction function = (TabulatedFunction) operatorMap.get(comboBox.getValue()).invoke(
                    classes.get(operatorMap.get(comboBox.getValue())).getDeclaredConstructor(TabulatedFunctionFactory.class)
                            .newInstance(((TableController) parentController).getFactory()),
                    ((TableController) parentController).getFunction());
            function.offerStrict(((TableController) parentController).isStrict());
            function.offerUnmodifiable(((TableController) parentController).isUnmodifiable());
            ((TableController) parentController).createTab(function);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
