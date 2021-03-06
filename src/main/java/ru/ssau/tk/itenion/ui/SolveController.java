package ru.ssau.tk.itenion.ui;

import Jama.Matrix;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.labNumericalMethods.lab1.SolveNonlinearEquations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static ru.ssau.tk.itenion.ui.Initializer.initializeMap;

@ConnectableItem(name = "Solve", type = Item.CONTROLLER, pathFXML = "solve.fxml")
public class SolveController implements TabVisitor, PlotAccessible, Initializable, OpenableWindow {
    @FXML
    ComboBox<String> numericalMethodsBox;
    @FXML
    Label leftLabel, rightLabel;
    @FXML
    TextField left, right, initialApproximationTextField, accuracy, numberOfIterations;
    @FXML
    ListView<Double> listOfRoots;
    @FXML
    ListView<Double> listOfEps;

    private Stage stage;
    private Map<String, Method> numericalMethodMap;
    private Map<Method, Class<?>> classes;
    private SolveNonlinearEquations numMethod;
    private ArrayList<Double> listOfEpsItems;
    private ArrayList<Double> listOfRootsItems;
    private StringJoiner joiner;
    BooleanBinding isBorderRequired;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numericalMethodMap = new LinkedHashMap<>();
        listOfEpsItems = new ArrayList<>();
        listOfRootsItems = new ArrayList<>();
        classes = new LinkedHashMap<>();
        accuracy.setText("0.001");
        left.setText("0");
        right.setText("0");
        isBorderRequired = Bindings.createBooleanBinding(
                () -> numericalMethodMap.get(numericalMethodsBox.getSelectionModel().getSelectedItem()).getDeclaredAnnotation(ConnectableItem.class).isBorderRequired(),
                numericalMethodsBox.valueProperty());
    }

    public void bind(){
        leftLabel.visibleProperty().bind(isBorderRequired);
        rightLabel.visibleProperty().bind(isBorderRequired);
        left.visibleProperty().bind(isBorderRequired);
        right.visibleProperty().bind(isBorderRequired);
    }

    public void unbind(){
        leftLabel.visibleProperty().unbind();
        rightLabel.visibleProperty().unbind();
        left.visibleProperty().unbind();
        right.visibleProperty().unbind();
    }

    @FXML
    private void ok() {
        listOfRoots.getItems().clear();
        listOfEps.getItems().clear();
        joiner = new StringJoiner("; ");
        double[] initialApproximation = Arrays.stream(initialApproximationTextField.getText().split(";"))
                .mapToDouble(Double::parseDouble).toArray();
        if (Arrays.equals(initialApproximation, new double[]{})) {
            initialApproximation = new double[]{Double.parseDouble(initialApproximationTextField.getText())};
        }
        numMethod = IO.getNumericalMethodFactory().create(Double.parseDouble(left.getText()),
                Double.parseDouble(right.getText()),
                initialApproximation,
                Double.parseDouble(accuracy.getText()));
        state().accept(this);
        listOfRoots.setItems(FXCollections.observableList(listOfRootsItems));
        listOfEps.setItems(FXCollections.observableList(listOfEpsItems));
        numberOfIterations.setText(joiner.toString());
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
            numericalMethodMap.clear();
            AtomicReference<Predicate<ConnectableItem>> predicateAtomicReference = new AtomicReference<>();
            state().accept(new TabVisitor() {
                @Override
                public void visit(TabController.TFState tfState) {
                    predicateAtomicReference.set(item -> !item.forVMF());
                }

                @Override
                public void visit(TabController.VMFState vmfState) {
                    predicateAtomicReference.set(ConnectableItem::forVMF);
                }
            });
            Map[] maps = initializeMap(classes, numericalMethodMap, Item.SOLVE_NUMERICAL_METHOD,
                    item -> item.belongTo().equals(IO.belongTo),
                    predicateAtomicReference.get());
            classes = (Map<Method, Class<?>>) maps[0];
            numericalMethodMap = (Map<String, Method>) maps[1];
            unbind();
            numericalMethodsBox.getItems().setAll(numericalMethodMap.keySet());
            numericalMethodsBox.setValue(numericalMethodsBox.getItems().get(0));
            bind();
        });
        this.stage = stage;
    }

    @FXML
    public void locateTheRoots() {
        plot();
    }

    @Override
    public void visit(TabController.TFState tfState) {
        if (initialApproximationTextField.getText().isEmpty()){
            initialApproximationTextField.setText("0");
        }
        Map<Double, Map.Entry<Double, Integer>> roots = null;
        try {
            roots = (Map<Double, Map.Entry<Double, Integer>>) numericalMethodMap
                    .get(numericalMethodsBox.getValue())
                    .invoke(numMethod, tfState.getFunction().getMathFunction());
            roots.values().forEach(entry -> {
                listOfEpsItems.add(entry.getKey());
                joiner.add(entry.getValue().toString());
            });
            listOfRootsItems = new ArrayList<>(roots.keySet());
        } catch (IllegalAccessException | InvocationTargetException e) {
            AlertWindows.showError(e);
        }
    }

    @Override
    public void visit(TabController.VMFState vmfState) {
        if (initialApproximationTextField.getText().isEmpty()){
            initialApproximationTextField.setText("0;0;");
        }
        Matrix roots = null;
        try {
            roots = (Matrix) numericalMethodMap
                    .get(numericalMethodsBox.getValue())
                    .invoke(numMethod, vmfState.getFunction());
            Matrix residuals = vmfState.getFunction().apply(roots.transpose());
            for (int i = 0; i < roots.getRowDimension(); i++) {
                listOfRootsItems.add(roots.get(i, 0));
                listOfEpsItems.add(residuals.get(i, 0));
            }
            joiner.add(numMethod.getIterationsNumber() + "");
        } catch (IllegalAccessException | InvocationTargetException e) {
            AlertWindows.showError(e);
        }
    }
}
