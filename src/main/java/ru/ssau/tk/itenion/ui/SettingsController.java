package ru.ssau.tk.itenion.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

@ConnectableItem(name = "Settings", type = Item.CONTROLLER, pathFXML = "settings.fxml")
public class SettingsController implements AnyTabVisitor, Initializable, OpenableWindow {
    @FXML
    WebView webView;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    ComboBox<Boolean> strictComboBox;
    @FXML
    ComboBox<Boolean> unmodifiableComboBox;
    private Stage stage;
    private Map<String, TabulatedFunctionFactory> comboBoxMap;
    private TabulatedFunctionFactory factory;

    @FXML
    private void save() {
        webView.getEngine().load(null);
        stage.close();
    }

    void start() {
        stage.show();
        comboBox.setValue(comboBox.getItems().get(anyState().getFactory().getClass().getDeclaredAnnotation(ConnectableItem.class).priority() - 1));
        strictComboBox.setValue(anyState().isStrict());
        unmodifiableComboBox.setValue(anyState().isUnmodifiable());
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
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.FACTORY)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> {
                    try {
                        comboBoxMap.put(clazz.getDeclaredAnnotation(ConnectableItem.class).name(), (TabulatedFunctionFactory) clazz.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        AlertWindows.showError(e);
                    }
                });
        comboBox.getItems().addAll(comboBoxMap.keySet());
        strictComboBox.getItems().addAll(true, false);
        unmodifiableComboBox.getItems().addAll(true, false);
    }

    @FXML
    private void doOnClickOnComboBox(ActionEvent event) {
        factory = comboBoxMap.get(((ComboBox<String>) event.getSource()).getValue());
    }

    @FXML
    private void doOnClickOnStrictComboBox() {
        anyState().setStrict(strictComboBox.getValue());
    }

    @FXML
    private void doOnClickOnUnmodifiableComboBox() {
        anyState().setUnmodifiable(unmodifiableComboBox.getValue());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void visit(TabController.AnyTabHolderState anyTabHolderState) {
        anyTabHolderState.setFactory(factory);
    }
}
