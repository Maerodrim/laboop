package ru.ssau.tk.itenion.ui;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@ConnectableItem(name = "Vector function create", type = Item.CONTROLLER, pathFXML = "vectorFunction.fxml")
public class VectorFunctionController implements Initializable, Openable, MathFunctionAccessible, CompositeFunctionAccessible, TabulatedFunctionAccessible {
    private Stage stage;
    private Openable parentController;
    //функции, которые будут использоваться при сборке
    private Map<Pair<Integer, Integer>, MathFunction> currentFunctions;
    // карта текстовых полей, содержащих названия функций
    private Map<Pair<Integer, Integer>, JFXTextField> functionTextFields;
    private Map<Pair<Integer, Integer>, JFXComboBox> operationComboBoxes;
    private Map<Pair<Integer, Integer>, Menu> currentFunctionMenuItems;
    private Map<Pair<Integer, Integer>, Menu> mathFunctionMenuItems;
    private Map<Pair<Integer, Integer>, Menu> compositeFunctionMenuItems;
    private Map<String, Method> operationMap;
    private Map<String, MathFunction> tabulatedFunctionMap;
    private Map<String, MathFunction> mathFunctionMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private Map<Method, Class<?>> classes;
    //определяет количество узлов
    private ObservableList<Pair<Integer, Integer>> nodesGrid = FXCollections.observableArrayList(
            new Pair<>(1, 1),
            new Pair<>(1, 2),
            new Pair<>(1, 3));
    @FXML
    private AnchorPane pane;

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        stage.setOnShown(windowEvent -> nodesGrid.forEach(this::initGroup));
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    public void load(ActionEvent event) {
    }

    public void initGroup(Pair<Integer, Integer> nodeGrid) {
        MenuBar functionMenu = new MenuBar();
        functionMenu.getMenus().add(mathFunctionMenuItems.get(nodeGrid).getParentMenu());
        AnchorPane.setTopAnchor(functionMenu, 10.);
        AnchorPane.setLeftAnchor(functionMenu, 10. + (nodeGrid.getValue()-1)*150);
        JFXTextField textField = functionTextFields.get(nodeGrid);
        AnchorPane.setTopAnchor(textField, 50.);
        AnchorPane.setLeftAnchor(textField, 10. + (nodeGrid.getValue()-1)*150);
        pane.getChildren().addAll(functionMenu, textField);
    }

    private void addMenuItems(Pair<Integer, Integer> nodeGrid, Menu menu, Map<String, MathFunction> map) {
        menu.getItems()
                .addAll(map.keySet()
                        .stream()
                        .map(MenuItem::new)
                .filter(menuItem -> !menu.getItems()
                        .contains(menuItem))
                        .collect(Collectors
                                .toList()));
        menu.getItems()
                .forEach(f -> f
                        .setOnAction(e -> {
            currentFunctions.computeIfPresent(nodeGrid, (integerIntegerPair, presentFunction) -> map.get(f.getText()));
            currentFunctions.putIfAbsent(nodeGrid, map.get(f.getText()));
            if (menu.getText().equals("Мат. функции")) {
                ConnectableItem item = map.get(f.getText()).getClass().getDeclaredAnnotation(ConnectableItem.class);
                if (item.hasParameter()) {
                    IO.setActualParameter(map, f.getText(), IO.getValue(item));
                }
            }
            functionTextFields.get(nodeGrid).setText(map.get(f.getText()).getName());
        }));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mathFunctionMenuItems = new LinkedHashMap<>();
        currentFunctionMenuItems = new LinkedHashMap<>();
        compositeFunctionMenuItems = new LinkedHashMap<>();
        functionTextFields = new LinkedHashMap<>();
        nodesGrid.forEach(nodeGrid -> {
            mathFunctionMenuItems.put(nodeGrid, new Menu("Мат. функции"));
            currentFunctionMenuItems.put(nodeGrid, new Menu("Текущие функции"));
            compositeFunctionMenuItems.put(nodeGrid, new Menu("Сохраненные композиции"));
            Menu functionMenu = new Menu("F" + nodeGrid.getValue() + "(" + Variable.values()[nodeGrid.getKey() - 1] + ")");
            functionMenu.getItems().addAll(
                    mathFunctionMenuItems.get(nodeGrid),
                    currentFunctionMenuItems.get(nodeGrid),
                    compositeFunctionMenuItems.get(nodeGrid));
            functionTextFields.put(nodeGrid, new JFXTextField());
        });
        currentFunctions = FXCollections.observableHashMap();
        tabulatedFunctionMap = new LinkedHashMap<>();
        operationMap = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        Map[] maps = IO.initializeMap(classes, operationMap, Item.OPERATION);
        classes = (Map<Method, Class<?>>) maps[0];
        operationMap = (Map<String, Method>) maps[1];
    }

    @Override
    public void setMathFunctionNode() {
        mathFunctionMenuItems.forEach((nodeGrid, menu) -> addMenuItems(nodeGrid, menu, mathFunctionMap));
    }

    @Override
    public void setMathFunctionMap(Map<String, MathFunction> mathFunctionMap) {
        this.mathFunctionMap = mathFunctionMap;
    }

    @Override
    public Openable getParentController() {
        return parentController;
    }

    @Override
    public Map<String, ? super MathFunction> getTabulatedFunctionMap() {
        return tabulatedFunctionMap;
    }

    @Override
    public void updateTabulatedFunctionNode() {
        if (!tabulatedFunctionMap.isEmpty()) {
            currentFunctionMenuItems.forEach((nodeGrid, menu) -> addMenuItems(nodeGrid, menu, tabulatedFunctionMap));
        } else {
            currentFunctionMenuItems.forEach((nodeGrid, menu) -> menu.getItems().clear());
        }
    }

    @Override
    public void updateCompositeFunctionNode() {
        compositeFunctionMenuItems.forEach((nodeGrid, menu) -> addMenuItems(nodeGrid, menu, compositeFunctionMap));
    }

    @Override
    public void updateCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap) {
        this.compositeFunctionMap = compositeFunctionMap;
    }
}
