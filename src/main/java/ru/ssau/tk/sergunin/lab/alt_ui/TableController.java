package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TableController implements Initializable {

    Stage stage;
    TabulatedFunction function;
    TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();

    CreateNewFunctionController createNewFunctionController; // контроллер окна создания новой функции

    @FXML
    TabPane tabPane;
    @FXML
    Button addFunction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getSelectionModel().selectLast();
        this.function = factory.getIdentity();
        initializeWindowControllers();
    }

    private void initializeWindowControllers() {
        createNewFunctionController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/newFunction.fxml", CreateNewFunctionController.class);
        createNewFunctionController.getStage().initOwner(stage);
        createNewFunctionController.getStage().setTitle("Create new function");
        createNewFunctionController.setFactory(factory);
        createNewFunctionController.setTabPane(tabPane);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void newFunctionButtonEvent() {
        //createNewFunctionController.create.setText("Создать");
        createNewFunctionController.getStage().show();
    }

    @FXML
    private void join() {
        //createNewFunctionController.create.setText("Присоединить");
        createNewFunctionController.getStage().show();
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }
}