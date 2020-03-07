package ru.ssau.tk.itenion.ui;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.exceptions.NaNException;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@ConnectableItem(name = "Apply", type = Item.CONTROLLER, pathFXML = "apply.fxml")
public class ApplyController implements Initializable, Openable, TabulatedFunctionAccessible, MathFunctionAccessible, CompositeFunctionAccessible {
    /**
     * chosenMenu = 0 - via MathFunction
     * chosenMenu = 1 - via function, is opened in the tab
     * chosenMenu = 2 - via saved compositeMathFunction
     * chosenMenu = 3 - via load function
     */
    int chosenMenu = 0;
    private Stage stage;
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private Map<String, Method> operationMap;
    private Map<String, MathFunction> tabulatedFunctionMap;
    private Map<String, MathFunction> mathFunctionMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private Map<Method, Class<?>> classes;
    private MathFunction currentFunction;
    private String selectedMathMenuItem;
    private Optional<?> value;
    @FXML
    private Menu mathFunctionMenu, compositeFunctionMenu, currentFunctionMenu;

    @FXML
    private ComboBox<String> operationComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabulatedFunctionMap = new LinkedHashMap<>();
        operationMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        Map[] maps = IO.initializeMap(classes, operationMap, Item.OPERATION);
        classes = (Map<Method, Class<?>>) maps[0];
        operationMap = (Map<String, Method>) maps[1];
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
        this.stage.setOnCloseRequest(windowEvent -> tabulatedFunctionMap.clear());
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @FXML
    public void apply() {
        if (chosenMenu == 0) {
            IO.setActualParameter(mathFunctionMap, selectedMathMenuItem, value);
            currentFunction = mathFunctionMap.get(selectedMathMenuItem);
        }
        try {
            if (currentFunction instanceof TabulatedFunction) {
                currentFunction = ((TabulatedFunction) currentFunction).getMathFunction();
            }
            TabulatedFunction function = (TabulatedFunction) operationMap.get(operationComboBox.getValue())
                    .invoke(classes.get(operationMap.get(operationComboBox.getValue())).getDeclaredConstructor(TabulatedFunctionFactory.class)
                                    .newInstance(((TableController) parentController).getFactory()),
                            ((TableController) parentController).getFunction(),
                            currentFunction);
            function.offerStrict(((TableController) parentController).isStrict());
            function.offerUnmodifiable(((TableController) parentController).isUnmodifiable());
            ((TableController) parentController).createTab(function);
            stage.close();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            AlertWindows.showError(e);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof NaNException) {
                AlertWindows.showWarning("Результат не существует");
            } else if (e.getTargetException() instanceof NullPointerException) {
                AlertWindows.showWarning("Выберите функцию");
            } else {
                AlertWindows.showError(e);
            }
        }
    }

    @FXML
    public void cancel() {
        stage.close();
    }

    @Override
    public Openable getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    public Map<String, ? super MathFunction> getTabulatedFunctionMap() {
        return tabulatedFunctionMap;
    }

    private void setMenuItems(Menu menu, Map<String, MathFunction> map, int chosenMenu) {
        menu.getItems().setAll(map.keySet().stream().map(MenuItem::new).collect(Collectors.toList()));
        menu.getItems().forEach(f -> f.setOnAction(e -> {
            MathFunction function = map.get(f.getText());
            currentFunction = function;
            selectedMathMenuItem = chosenMenu == 0 ? f.getText() : "";
            this.chosenMenu = chosenMenu;
            ConnectableItem item = (function.getClass().getDeclaredAnnotation(ConnectableItem.class));
            if (!Objects.isNull(item)) {
                value = IO.getValue(item);
            }
        }));
    }

    @Override
    public void updateTabulatedFunctionNode() {
        if (!tabulatedFunctionMap.isEmpty()) {
            setMenuItems(currentFunctionMenu, tabulatedFunctionMap, 1);
        } else {
            currentFunctionMenu.getItems().clear();
        }
    }

    @Override
    public void setMathFunctionNode() {
        setMenuItems(mathFunctionMenu, mathFunctionMap, 0);
    }

    @Override
    public void setMathFunctionMap(Map<String, MathFunction> functionMap) {
        mathFunctionMap = functionMap;
    }

    @FXML
    public void load() {
        File file = IO.load(stage);
        if (!Objects.equals(file, null)) {
            currentFunction = new IO(factory).loadFunctionAs(file);
            if (((TabulatedFunction) currentFunction).isMathFunctionExist()) {
                currentFunction = ((TabulatedFunction) currentFunction).getMathFunction();
                chosenMenu = 3;
            }
        }
    }

    @Override
    public void updateCompositeFunctionNode() {
        setMenuItems(compositeFunctionMenu, compositeFunctionMap, 2);
    }

    @Override
    public void updateCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap) {
        this.compositeFunctionMap = compositeFunctionMap;
    }
}
