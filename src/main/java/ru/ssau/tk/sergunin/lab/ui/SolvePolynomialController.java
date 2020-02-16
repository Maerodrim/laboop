package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.CompositeFunction;
import ru.ssau.tk.sergunin.lab.numericalMethods.NumericalMethods;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ConnectableItem(name = "Solve polynomial", type = Item.CONTROLLER, pathFXML = "solvePolynomial.fxml")
public class SolvePolynomialController implements Initializable, Openable {
    @FXML
    ComboBox<String> numericalMethodsBox;
    @FXML
    TextField left, right, count, eps;
    @FXML
    ListView<Double> listOfRoots;

    private Stage stage;
    private Openable parentController;
    private Map<String, Method> numericalMethodMap;
    private NumericalMethods numMethod;
    private Map<Method, Class<?>> classes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numericalMethodMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.NUMERICAL_METHOD)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(ConnectableItem.class))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                        .forEach(method -> {
                            numericalMethodMap.put(method.getDeclaredAnnotation(ConnectableItem.class).name(), method);
                            classes.put(method, clazz);
                        }));
        numericalMethodsBox.getItems().addAll(numericalMethodMap.keySet());
        numericalMethodsBox.setValue(numericalMethodsBox.getItems().get(0));
    }

    @FXML
    public void doOnClickOnComboBox(ActionEvent event){
        ConnectableItem item = numericalMethodMap.get(((ComboBox) event.getSource()).getValue().toString())
                .getDeclaredAnnotation(ConnectableItem.class);
        if (!Objects.isNull(item) && item.methodOnlyForPolynomialFunction()
                && ((TableController)parentController).getFunction().getMathFunction() instanceof CompositeFunction) {
            AlertWindows.showWarning("In the current version of the program, the Newton method only works for polynomial functions");
            numericalMethodsBox.getSelectionModel().select(0);
        }
    }

    @FXML
    private void ok() {
        numMethod = new NumericalMethods(Double.parseDouble(left.getText()), Double.parseDouble(right.getText()),
                Integer.parseInt(count.getText()), Double.parseDouble(eps.getText()));
        try {
            listOfRoots.setItems(FXCollections.observableList((List<Double>)numericalMethodMap
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
    public void setFactory(TabulatedFunctionFactory factory){
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }
}
