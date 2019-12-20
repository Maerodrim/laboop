package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DeletePoint implements Initializable, Openable {
    @FXML
    TextField x;
    private Stage stage;
    private Openable parentController;

    @FXML
    private void delete() {
        int index = ((TableController)parentController).getFunction().indexOfX(Double.parseDouble(x.getText()));
        ((TableController)parentController).getObservableList().remove(index);
        ((TableController)parentController).getFunction().remove(index);
        stage.close();
    }
    @FXML
    private void cancel() {
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
