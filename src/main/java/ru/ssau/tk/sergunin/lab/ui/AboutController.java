package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Nameable;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

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
        TabulatedFunction function = ((TableController) parentController).getFunction();
        try {
            baseMathFunction.setText(function.getMathFunction().getName());
        } catch (ClassCastException e) {
            baseMathFunction.setText(getNameForce(function.getMathFunction()));
        }
        leftBorder.setText(function.leftBound() + "");
        rightBorder.setText(function.rightBound() + "");
        numberOfPoints.setText(function.getCount() + "");
    }

    private String getNameForce(MathFunction mathFunction) {
        Field field = mathFunction.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        try {
            return ((Nameable) field.get(mathFunction)).getName();
        } catch (IllegalAccessException e) {
            AlertWindows.showError(e);
        } catch (ClassCastException e) {
            try {
                getNameForce((MathFunction) field.get(mathFunction));
            } catch (IllegalAccessException ex) {
                AlertWindows.showError(ex);
            }
        }
        return "";
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
