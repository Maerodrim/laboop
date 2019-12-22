package ru.ssau.tk.sergunin.lab.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class SettingsController implements Initializable, Openable {
    @FXML
    WebView webView;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    ComboBox<Boolean> strictComboBox;
    @FXML
    ComboBox<Boolean> unmodifiableComboBox;
    private Stage stage;
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private Map<String, TabulatedFunctionFactory> comboBoxMap;

    @FXML
    private void save() {
        parentController.setFactory(factory);
        webView.getEngine().load(null);
        stage.close();
    }

    void start() {
        stage.show();
        comboBox.setValue(comboBox.getItems().get(factory.getClass().getDeclaredAnnotation(SelectableItem.class).priority() - 1));
        strictComboBox.setValue(((TableController) parentController).isStrict());
        unmodifiableComboBox.setValue(((TableController) parentController).isUnmodifiable());
        WebEngine webEngine = webView.getEngine();
        String url = "https://www.youtube.com/watch?v=pYWocUFndO8";
        webEngine.load(url);
        stage.setOnCloseRequest(windowEvent -> webEngine.load(null));
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxMap = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(SelectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(SelectableItem.class).type() == Item.FACTORY)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(SelectableItem.class).priority()))
                .forEach(clazz -> {
                    try {
                        comboBoxMap.put(clazz.getDeclaredAnnotation(SelectableItem.class).name(), (TabulatedFunctionFactory) clazz.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
        comboBox.getItems().addAll(comboBoxMap.keySet());
        strictComboBox.getItems().addAll(true, false);
        unmodifiableComboBox.getItems().addAll(true, false);
    }

    @FXML
    @SuppressWarnings("SuspiciousMethodCalls")
    private void doOnClickOnComboBox(ActionEvent event) {
        factory = comboBoxMap.get(((ComboBox) event.getSource()).getValue());
    }

    @FXML
    private void doOnClickOnUnmodifiableComboBox() {
        ((TableController) parentController).setUnmodifiable(unmodifiableComboBox.getValue());
    }

    @FXML
    private void doOnClickOnStrictComboBox() {
        ((TableController) parentController).setStrict(strictComboBox.getValue());
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
}
