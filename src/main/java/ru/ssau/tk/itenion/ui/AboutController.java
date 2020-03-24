package ru.ssau.tk.itenion.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Nameable;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

@ConnectableItem(name = "About function", type = Item.CONTROLLER, pathFXML = "about.fxml")
public class AboutController implements TabVisitor, OpenableWindow, Initializable {
    @FXML
    private AnchorPane pane;
    @FXML
    private Label baseMathFunction;
    @FXML
    private Label leftBorder;
    @FXML
    private Label rightBorder;
    @FXML
    private Label numberOfPoints;
    @FXML
    private Label rightBorderLabel;
    @FXML
    private Label leftBorderLabel;
    @FXML
    private Label numberOfPointsLabel;

    private Stage stage;
    private double defaultStageHeight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baseMathFunction.setWrapText(true);
    }

    @FXML
    public void setInfo() {
        state().accept(this);
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
        defaultStageHeight = stage.getHeight();
    }

    @Override
    public void visit(TabController.TFState tfState) {
        clearTFTextFields();
        setTFTextFields();
        stage.setHeight(defaultStageHeight);
        TabulatedFunction function = tfState.getFunction();
        try {
            baseMathFunction.setText(function.getName());
        } catch (ClassCastException e) {
            baseMathFunction.setText(getNameForce(function.getMathFunction()));
        }
        leftBorder.setText(function.leftBound() + "");
        rightBorder.setText(function.rightBound() + "");
        numberOfPoints.setText(function.getCount() + "");
    }

    @Override
    public void visit(TabController.VMFState vmfState) {
        clearTFTextFields();
        stage.setHeight(85);
        baseMathFunction.setText(vmfState.getFunction().getName());
    }

    private void clearTFTextFields(){
        pane.getChildren().remove(rightBorderLabel);
        pane.getChildren().remove(leftBorderLabel);
        pane.getChildren().remove(numberOfPointsLabel);
        pane.getChildren().remove(rightBorder);
        pane.getChildren().remove(leftBorder);
        pane.getChildren().remove(numberOfPoints);
    }

    private void setTFTextFields(){
        pane.getChildren().add(rightBorderLabel);
        pane.getChildren().add(leftBorderLabel);
        pane.getChildren().add(numberOfPointsLabel);
        pane.getChildren().add(rightBorder);
        pane.getChildren().add(leftBorder);
        pane.getChildren().add(numberOfPoints);
    }
}
