package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.exceptions.NaNException;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ConnectableItem(name = "Apply", type = Item.CONTROLLER, pathFXML = "apply.fxml")
public class ApplyController implements Initializable, Openable, TabulatedFunctionAccessible, MathFunctionAccessible, CompositeFunctionAccessible {
    private Stage stage;
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private Map<String, Method> operationMap;
    private Map<String, MathFunction> tabulatedFunctionMap;
    private Map<String, MathFunction> mathFunctionMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private Map<Method, Class<?>> classes;
    private MathFunction currentFunction;
    @FXML
    private Menu mathFunctionMenu, compositeFunctionMenu, currentFunctionMenu;

    @FXML
    private ComboBox<String> operationComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        operationMap = new LinkedHashMap<>();
        tabulatedFunctionMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == Item.OPERATION)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(ConnectableItem.class))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                        .forEach(method -> {
                            operationMap.put(method.getDeclaredAnnotation(ConnectableItem.class).name(), method);
                            classes.put(method, clazz);
                        }));
        operationComboBox.getItems().addAll(operationMap.keySet());
        operationComboBox.setValue(operationComboBox.getItems().get(0));
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(windowEvent -> {
            tabulatedFunctionMap.clear();
            currentFunctionMenu.getItems().clear();
        });
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    @FXML
    public void apply() {
        try {
            TabulatedFunction function = (TabulatedFunction) operationMap.get(operationComboBox.getValue())
                    .invoke(classes.get(operationMap.get(operationComboBox.getValue())).getDeclaredConstructor(TabulatedFunctionFactory.class)
                                    .newInstance(((TableController) parentController).getFactory()),
                            ((TableController) parentController).getFunction(),
                            currentFunction);
            function.offerStrict(((TableController) parentController).isStrict());
            function.offerUnmodifiable(((TableController) parentController).isUnmodifiable());
            ((TableController) parentController).createTab(function);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            AlertWindows.showError(e);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NaNException){
                AlertWindows.showWarning("Результат не существует");
            } else {
                AlertWindows.showError(e);
            }
        }
        stage.close();
    }

    @FXML
    public void cancel() {
        stage.close();
    }

    @Override
    public Openable getParentController() {
        return parentController;
    }

    public Map<String, ? super MathFunction> getTabulatedFunctionMap() {
        return tabulatedFunctionMap;
    }

    private void addMenuItems(Menu menu, Map<String, MathFunction> map) {
        menu.getItems().addAll(map.keySet().stream().map(MenuItem::new).collect(Collectors.toList()));
        menu.getItems().forEach(f -> f.setOnAction(e -> currentFunction = map.get(f.getText())));
    }

    @Override
    public void updateTabulatedFunctionNode() {
        addMenuItems(currentFunctionMenu, tabulatedFunctionMap);
    }

    @Override
    public void setMathFunctionNode() {
        addMenuItems(mathFunctionMenu, mathFunctionMap);
    }

    @Override
    public void setMathFunctionMap(Map<String, MathFunction> functionMap) {
        mathFunctionMap = functionMap;
    }

    @FXML
    public void load() {
        File file = IO.load(stage, IO.DEFAULT_DIRECTORY);
        if (!Objects.equals(file, null)) {
            currentFunction = new IO(factory).loadFunctionAs(file);
        }
    }

    @Override
    public void updateCompositeFunctionNode() {
        addMenuItems(compositeFunctionMenu, compositeFunctionMap);
    }

    @Override
    public void updateCompositeFunctionMap(Map<String, MathFunction> functionMap) {
        compositeFunctionMap = functionMap;
    }
}
