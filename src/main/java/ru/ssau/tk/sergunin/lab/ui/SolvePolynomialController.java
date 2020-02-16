package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.numericalMethods.NewtonMethod;
import ru.ssau.tk.sergunin.lab.numericalMethods.NumericalMethods;
import ru.ssau.tk.sergunin.lab.functions.PolynomialFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

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
    private Map<String, Class<? extends NumericalMethods>> numericalMethodMap;
    private NumericalMethods numMethod = new NewtonMethod();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numericalMethodMap = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.NUMERICAL_METHOD)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> numericalMethodMap.put(clazz.getDeclaredAnnotation(ConnectableItem.class).name(), clazz));
        method.getItems().addAll(numericalMethodMap.keySet());
        method.setValue(method.getItems().get(0));
    }

    @FXML
    private void ok() {
        ListX.setItems(FXCollections.observableList(numMethod.solve(
                ((PolynomialFunction) ((TableController) parentController).getFunction().getMathFunction()),
                Double.parseDouble(left.getText()), Double.parseDouble(right.getText()),
                Integer.parseInt(count.getText()), Double.parseDouble(eps.getText()))));
        ListX.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListX.getSelectionModel().selectAll();
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
