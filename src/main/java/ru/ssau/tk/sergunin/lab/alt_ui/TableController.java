package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TableController implements Initializable {

    Stage stage;
    TabulatedFunction function;
    TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    String defaultDirectory;

    FunctionController functionController; // контроллер окна создания новой функции
    Functions functions;
    @FXML
    TabPane tabPane;
    @FXML
    Button addFunction;
    @FXML
    BorderPane mainPane;
    @FXML
    BorderPane labelPane;
    @FXML
    Label label;
    @FXML
    MenuItem plot;
    @FXML
    MenuItem loadItem;
    @FXML
    MenuItem saveItem;
    @FXML
    MenuItem saveAsItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getSelectionModel().selectLast();
        this.function = factory.getIdentity();
        functions = new Functions(factory);
        defaultDirectory = System.getenv("APPDATA") + "\\tempFunctions";
        new File(defaultDirectory).mkdir();
        initializeWindowControllers();
    }

    private void initializeWindowControllers() {
        functionController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/newFunction.fxml", FunctionController.class);
        functionController.getStage().initOwner(stage);
        functionController.getStage().setTitle("Create new function");
        functionController.setFactory(factory);
        functionController.setTabPane(tabPane);
        functionController.setMainPane(mainPane);
        functionController.setLabelPane(labelPane);
        functionController.setLabel(label);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void newFunctionButtonEvent() {
        //createNewFunctionController.create.setText("Создать");
        functionController.getStage().show();
    }

    @FXML
    private void join() {
        //createNewFunctionController.create.setText("Присоединить");
        functionController.getStage().show();
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @FXML
    public void plot() {
        if (!Objects.equals(functionController.getCurrentTab(), null)) {
            Plot.plotFunction(stage, functionController.getObservableList());
        }
    }

    @FXML
    public void loadFunction() {
        File file = Functions.load(stage, defaultDirectory);
        if (!Objects.equals(file, null)) {
            functionController.createTab(functions.loadFunctionAs(file));
        }
    }

    @FXML
    public void saveFunction() {
        save(true);
    }

    @FXML
    public void saveAsFunction() {
        save(false);
    }

    private void save(boolean toTempPath) {
        if (!Objects.equals(functionController.getCurrentTab(), null)) {
            File file = toTempPath
                    ? new File(defaultDirectory + "\\" + functionController.getCurrentTab().getText() + ".txt")
                    : Functions.save(stage);
            if (!Objects.equals(file, null)) {
                functions.saveFunctionAs(file, functionController.getFunction());
            }
        }
    }

    @FXML
    public void exit() {
        stage.close();
    }
}