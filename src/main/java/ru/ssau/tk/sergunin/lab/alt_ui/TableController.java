package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class TableController implements Initializable {

    Stage stage;

    TabulatedFunction function;
    TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
    Stage createNewFunctionStage;
    CreateNewFunctionController createNewFunctionController;

    @FXML
    TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getSelectionModel().selectLast();
        this.function = factory.getIdentity();
        initializeCreateFunctionWindow();
    }

    private void initializeCreateFunctionWindow(){
        FXMLLoader loader;
        Parent createNewFunction = null;
        try {
            loader = new FXMLLoader(Paths.get("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/newFunction.fxml").toUri().toURL());
            createNewFunction = loader.load();
            createNewFunctionController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createNewFunctionController.setPrimaryController(this);
        createNewFunctionStage = new Stage();
        createNewFunctionStage.setTitle("Create new function");
        createNewFunctionStage.setScene(new Scene(createNewFunction));
        createNewFunctionStage.initModality(Modality.WINDOW_MODAL);
        createNewFunctionStage.initOwner(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void newFunctionButtonEvent() {
        createNewFunctionStage.show();
    }

}