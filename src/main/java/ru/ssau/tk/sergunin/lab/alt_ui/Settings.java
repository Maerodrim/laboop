package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class Settings implements Initializable, Openable {
    private Stage stage;
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private Map<String, TabulatedFunctionFactory> comboBoxMap;

    @FXML
    WebView webView;

    @FXML
    ComboBox<String> comboBox;

    @FXML
    private void save() {
        parentController.setFactory(factory);
        stage.close();
    }
    public void start() {
        stage.show();
        WebEngine webEngine = webView.getEngine();
        String url = "https://www.youtube.com/watch?v=pYWocUFndO8";
        webEngine.load(url);
    }
    @FXML
    private void cancel() {
        stage.close();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxMap = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(SelectableFactory.class).spliterator(), false)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(SelectableFactory.class).priority()))
                .forEach(clazz -> {
                    try {
                        comboBoxMap.put(clazz.getDeclaredAnnotation(SelectableFactory.class).name(), (TabulatedFunctionFactory) clazz.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
        comboBox.getItems().addAll(comboBoxMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }
    @FXML
    private void doOnClickOnComboBox(ActionEvent event) {
        factory = comboBoxMap.get(((ComboBox) event.getSource()).getValue());
    }
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }
}
