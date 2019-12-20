package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.media.Media;
import javafx.scene.web.WebEngine;
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

public class CompositeFunctionController implements Initializable, Openable{
    private Stage stage;
    private Openable parentController;
    private Map<String, MathFunction> comboBoxMap;
    private MathFunction mathFunction;
    private TabulatedFunctionFactory factory;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    public void doOnClickOnComboBox(ActionEvent actionEvent){
       mathFunction = comboBoxMap.get(((ComboBox) actionEvent.getSource()).getValue());
    }

    @FXML
    public void pain() {
        TabulatedFunction function = ((TableController)parentController).getFunction();
        ((TableController) parentController).createTab(factory.create(mathFunction.andThen(function), function.leftBound(), function.rightBound(), function.getCount()));
        stage.close();
    }

    @FXML
    private void cancel() {
        stage.close();
    }

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
    }
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory=factory;
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }
}
