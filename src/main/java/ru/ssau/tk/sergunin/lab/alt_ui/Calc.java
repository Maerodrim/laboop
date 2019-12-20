package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Calc implements Initializable, Openable {
    private Stage stage;
    private Openable parentController;

    @FXML
    TextField x;
    @FXML
    TextField y;



    @FXML
    private void cancel() {
        stage.close();
    }
    @FXML
    private void calc() {
        y.setText(""+((TableController)parentController).getFunction().apply(Double.parseDouble(x.getText())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    y.setEditable(false);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {}

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }
}
