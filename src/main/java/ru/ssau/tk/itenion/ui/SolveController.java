package ru.ssau.tk.itenion.ui;

import Jama.Matrix;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.numericalMethods.NumericalMethods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

@ConnectableItem(name = "Solve", type = Item.CONTROLLER, pathFXML = "solve.fxml")
public class SolveController implements Initializable, Openable {
    @FXML
    ComboBox<String> numericalMethodsBox;
    @FXML
    TextField left, right, initialApproximationTextField, eps, numberOfIterations;
    @FXML
    ListView<Double> listOfRoots;
    @FXML
    ListView<Double> listOfEps;

    private Stage stage;
    private Openable parentController;
    private Map<String, Method> numericalMethodMap;
    private Map<String, Method> numericalMethodMapForVMF;
    private Map<Method, Class<?>> classes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numericalMethodMap = new LinkedHashMap<>();
        numericalMethodMapForVMF = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        Map[] maps = IO.initializeMap(classes, numericalMethodMap, Item.NUMERICAL_METHOD, item -> !item.forVMF());
        classes = (Map<Method, Class<?>>) maps[0];
        numericalMethodMap = (Map<String, Method>) maps[1];
        numericalMethodMapForVMF = (Map<String, Method>) IO.initializeMap(classes, numericalMethodMapForVMF, Item.NUMERICAL_METHOD, ConnectableItem::forVMF)[1];
    }

    @FXML
    private void ok() {
        double[] initialApproximation = Arrays.stream(
                initialApproximationTextField.getText()
                        .split(";"))
                .mapToDouble(Double::parseDouble).toArray();
        if (Arrays.equals(initialApproximation, new double[]{})) {
            initialApproximation = new double[]{Double.parseDouble(initialApproximationTextField.getText())};
        }
        NumericalMethods numMethod = new NumericalMethods(Double.parseDouble(left.getText()),
                Double.parseDouble(right.getText()),
                initialApproximation,
                Double.parseDouble(eps.getText()));
        try {
            StringJoiner joiner = new StringJoiner("; ");
            ArrayList<Double> listOfEpsItems = new ArrayList<>();
            ArrayList<Double> listOfRootsItems = new ArrayList<>();
            if (((TableController) parentController).isVMF()) {
                Matrix roots = (Matrix) numericalMethodMapForVMF
                        .get(numericalMethodsBox.getValue())
                        .invoke(numMethod, ((TableController) parentController)
                                .getFunction());
                Matrix residuals = ((VMF)((TableController) parentController)
                        .getFunction()).apply(roots.transpose());
                for (int i = 0; i < roots.getRowDimension(); i++) {
                    listOfRootsItems.add(roots.get(i, 0));
                    listOfEpsItems.add(residuals.get(i, 0));
                    joiner.add(numMethod.getIterationsNumber() + "");
                }
                int n = 5;
            } else {
                Map<Double, Map.Entry<Double, Integer>> roots;
                roots = (Map<Double, Map.Entry<Double, Integer>>) numericalMethodMap
                        .get(numericalMethodsBox.getValue())
                        .invoke(numMethod, ((TabulatedFunction) ((TableController) parentController).getFunction()).getMathFunction());
                roots.values().forEach(entry -> {
                    listOfEpsItems.add(entry.getKey());
                    joiner.add(entry.getValue().toString());
                });
                listOfRootsItems = new ArrayList<>(roots.keySet());
            }
            listOfRoots.setItems(FXCollections.observableList(listOfRootsItems));
            listOfEps.setItems(FXCollections.observableList(listOfEpsItems));
            numberOfIterations.setText(joiner.toString());
        } catch (IllegalAccessException | InvocationTargetException e) {
            AlertWindows.showError(e);
        }
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        stage.setOnShown(windowEvent -> {
            if (((TableController) parentController).isVMF()) {
                numericalMethodsBox.getItems().setAll(numericalMethodMapForVMF.keySet());
            } else {
                numericalMethodsBox.getItems().setAll(numericalMethodMap.keySet());
            }
            numericalMethodsBox.setValue(numericalMethodsBox.getItems().get(0));
        });
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }

    @FXML
    public void plot() {
        ((TableController) parentController).plot();
    }
}
