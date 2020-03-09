package ru.ssau.tk.itenion.ui;

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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private MathFunction applyFunction;

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
        this.stage.setOnCloseRequest(windowEvent -> {
            tabulatedFunctionMap.clear();
        });
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @FXML
    public void apply() {
        try {
            if (applyFunction instanceof TabulatedFunction) {
                applyFunction = ((TabulatedFunction) applyFunction).getMathFunction();
            }
            TabulatedFunction function = (TabulatedFunction) operationMap.get(operationComboBox.getValue())
                    .invoke(classes.get(operationMap.get(operationComboBox.getValue())).getDeclaredConstructor(TabulatedFunctionFactory.class)
                                    .newInstance(((TableController) parentController).getFactory()),
                            ((TableController) parentController).getFunction(),
                            applyFunction);
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

    public Map<String, MathFunction> getTabulatedFunctionMap() {
        return tabulatedFunctionMap;
    }

    private void setMenuItems(Menu menu, Map<String, MathFunction> map) {
        menu.getItems().setAll(map.keySet().stream().map(MenuItem::new).collect(Collectors.toList()));
        menu.getItems().forEach(f -> f.setOnAction(e -> {
            applyFunction = map.get(f.getText());
            if (menu.getText().equals("Мат. функции")) {
                ConnectableItem item = applyFunction.getClass().getDeclaredAnnotation(ConnectableItem.class);
                if (item.hasParameter()) {
                    applyFunction = IO.setActualParameter(applyFunction, IO.getValue(item));
                }
            }
        }));
    }

    @Override
    public void updateTabulatedFunctionNode() {
        if (!tabulatedFunctionMap.isEmpty()) {
            setMenuItems(currentFunctionMenu, tabulatedFunctionMap);
        } else {
            currentFunctionMenu.getItems().clear();
        }
    }

    @Override
    public void setMathFunctionNode() {
        setMenuItems(mathFunctionMenu, mathFunctionMap);
    }

    @Override
    public void setMathFunctionMap(Map<String, MathFunction> functionMap) {
        mathFunctionMap = functionMap;
    }

    @FXML
    public void load() {
        File file = IO.load(stage);
        if (!Objects.equals(file, null)) {
            applyFunction = new IO(factory).loadFunctionAs(file);
            if (((TabulatedFunction) applyFunction).isMathFunctionExist()) {
                applyFunction = ((TabulatedFunction) applyFunction).getMathFunction();
            }
        }
    }

    @Override
    public void updateCompositeFunctionNode() {
        setMenuItems(compositeFunctionMenu, compositeFunctionMap);
    }

    @Override
    public void updateCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap) {
        this.compositeFunctionMap = compositeFunctionMap;
    }
}
