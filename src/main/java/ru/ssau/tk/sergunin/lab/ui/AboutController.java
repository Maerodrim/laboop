package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

@ConnectableItem(name = "About function", type = Item.CONTROLLER, pathFXML = "about.fxml")
public class AboutController implements Openable {
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

    public void setInfo() {
        TabulatedFunction function = ((TableController)parentController).getFunction();
        baseMathFunction.setText(function.getMathFunction().toString().split(":")[1]);
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
