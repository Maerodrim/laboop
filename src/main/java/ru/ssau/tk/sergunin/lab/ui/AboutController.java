package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ConnectableItem(name = "About function", type = Item.CONTROLLER, pathFXML = "about.fxml")
public class AboutController implements Openable, Initializable {
    @FXML
    Label baseMathFunction;
    @FXML
    Label leftBorder;
    @FXML
    Label rightBorder;
    @FXML
    Label numberOfPoints;
    Openable parentController;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baseMathFunction.setWrapText(true);
    }

    public void setInfo() {
        TabulatedFunction function = ((TableController)parentController).getFunction();
        baseMathFunction.setText(function.getMathFunction().toString());
        leftBorder.setText(function.leftBound() + "");
        rightBorder.setText(function.rightBound() + "");
        numberOfPoints.setText(function.getCount() + "");
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
        parentController = controller;
    }

}
