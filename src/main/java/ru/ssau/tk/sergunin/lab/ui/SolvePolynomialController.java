package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import ru.ssau.tk.sergunin.lab.NumericalMethods.NewtonMethod;
import ru.ssau.tk.sergunin.lab.NumericalMethods.NumericalMethod;
import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

@ConnectableItem(name = "Solve Polynomial", type = Item.CONTROLLER, pathFXML = "solvePolynomial.fxml")
public class SolvePolynomialController implements Initializable, Openable {
    @FXML
    ComboBox method;
    @FXML
    TextField left, right, count, eps;
    @FXML
    ListView ListX;

    private Stage stage;
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private Map<String, TabulatedFunctionFactory> comboBoxMap;
    private NumericalMethod numMethod = new NewtonMethod();

    @FXML
    private void ok() {
        ListX = new ListView(FXCollections.observableList(numMethod.solve(
                ((PolynomialFunction) ((TableController) parentController).getFunction().getMathFunction()),
                Double.parseDouble(left.getText()), Double.parseDouble(right.getText()),
                Integer.parseInt(count.getText()), Double.parseDouble(eps.getText()))));
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }

}
