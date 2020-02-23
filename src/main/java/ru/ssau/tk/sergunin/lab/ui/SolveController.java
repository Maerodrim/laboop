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
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.numericalMethods.NumericalMethods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ConnectableItem(name = "Solve", type = Item.CONTROLLER, pathFXML = "solve.fxml")
public class SolveController implements Initializable, Openable {
    @FXML
    ComboBox<String> numericalMethodsBox;
    @FXML
    TextField left, right, count, eps;
    @FXML
    ListView<Double> listOfRoots;

    private Stage stage;
    private Openable parentController;
    private Map<String, Method> numericalMethodMap;
    private Map<Method, Class<?>> classes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numericalMethodMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        Map[] maps = IO.initializeMap(classes, numericalMethodMap, Item.NUMERICAL_METHOD);
        classes = (Map<Method, Class<?>>)maps[0];
        numericalMethodMap = (Map<String, Method>)maps[1];
        numericalMethodsBox.getItems().addAll(numericalMethodMap.keySet());
        numericalMethodsBox.setValue(numericalMethodsBox.getItems().get(0));
    }

    @FXML
    private void ok() {
        NumericalMethods numMethod = new NumericalMethods(Double.parseDouble(left.getText()), Double.parseDouble(right.getText()),
                Integer.parseInt(count.getText()), Double.parseDouble(eps.getText()));
        try {
            listOfRoots.setItems(FXCollections.observableList((List<Double>) numericalMethodMap
                    .get(numericalMethodsBox.getValue()).invoke(numMethod,
                            ((TableController) parentController).getFunction().getMathFunction())));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        listOfRoots.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listOfRoots.getSelectionModel().selectAll();
    }

    @FXML
    private void cancel() {
        stage.close();
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
        this.parentController = controller;
    }
}
