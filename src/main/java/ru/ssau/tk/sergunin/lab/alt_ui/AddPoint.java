package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AddPoint implements Initializable, Openable {
    @FXML
    TextField x;
    @FXML
    TextField y;

    private Stage stage;
    private Openable parentController;

    @FXML
    private void add() {
        Point point = new Point(Double.parseDouble(x.getText()), Double.parseDouble(y.getText()));
        int index = ((TableController) parentController).getFunction().indexOfX(point.x);
        if (index == -1) {
            ((TableController) parentController).getObservableList().add(point);
            ((TableController) parentController).sort();
            ((TableController) parentController).getFunction().insert(point.x, point.y);
            stage.close();
        } else {
            AlertWindows.showWarning("Point already exists");
        }
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
