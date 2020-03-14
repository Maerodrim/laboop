package ru.ssau.tk.itenion.ui;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.ssau.tk.itenion.enums.SupportedSign;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMFSV;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.states.State;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ConnectableItem(name = "Vector function create", type = Item.CONTROLLER, pathFXML = "vectorFunction.fxml")
public class VectorFunctionController implements Initializable, FactoryAccessible, Openable, MathFunctionAccessible, CompositeFunctionAccessible, TabulatedFunctionAccessible, VMFTabVisitor {
    private Stage stage;
    private Map<Pair<Variable, Integer>, MathFunction> currentFunctions;
    private Map<Pair<Variable, Integer>, JFXTextField> functionTextFields;
    private Map<Pair<Variable, Integer>, JFXTextField> extraTextFields;
    private Map<Pair<Variable, Integer>, Menu> currentFunctionMenuItems;
    private Map<Pair<Variable, Integer>, Menu> mathFunctionMenuItems;
    private Map<Pair<Variable, Integer>, Menu> compositeFunctionMenuItems;
    private Map<Pair<Variable, Integer>, MenuItem> loadFunctionMenu;
    private Map<String, MathFunction> tabulatedFunctionMap;
    private Map<String, MathFunction> mathFunctionMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private RegexValidator validator;
    private int width;
    private ObservableList<Pair<Variable, Integer>> nodesGrid;
    @FXML
    private AnchorPane creatingPane;
    private AnchorPane createdPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nodesGrid = FXCollections.observableArrayList();

        validator = new RegexValidator();
        validator.setRegexPattern("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$");
        validator.setMessage("Incorrect");

        Arrays.stream(Variable.values())
                .forEach(variable -> IntStream.range(1, Variable.values().length + 1)
                        .forEach(value -> nodesGrid.add(new Pair<>(variable, value))));
        createdPane = new AnchorPane();

        loadFunctionMenu = new LinkedHashMap<>();
        mathFunctionMenuItems = new LinkedHashMap<>();
        currentFunctionMenuItems = new LinkedHashMap<>();
        compositeFunctionMenuItems = new LinkedHashMap<>();
        extraTextFields = new LinkedHashMap<>();
        functionTextFields = new LinkedHashMap<>();

        nodesGrid.forEach(nodeGrid -> {
            mathFunctionMenuItems.put(nodeGrid, new Menu("Мат. функции"));
            currentFunctionMenuItems.put(nodeGrid, new Menu("Текущие функции"));
            compositeFunctionMenuItems.put(nodeGrid, new Menu("Сохраненные композиции"));
            loadFunctionMenu.put(nodeGrid, new MenuItem("Загрузить"));
            loadFunctionMenu.get(nodeGrid).setOnAction(event -> load(nodeGrid));
            Menu functionMenu = new Menu("F" + nodeGrid.getValue() + "(" + Variable.values()[nodeGrid.getKey().ordinal()] + ")");
            functionMenu.getItems().addAll(
                    mathFunctionMenuItems.get(nodeGrid),
                    currentFunctionMenuItems.get(nodeGrid),
                    compositeFunctionMenuItems.get(nodeGrid),
                    loadFunctionMenu.get(nodeGrid));
            functionTextFields.put(nodeGrid, new JFXTextField());
            extraTextFields.put(nodeGrid, new JFXTextField());
        });

        currentFunctions = new LinkedHashMap<>();
        tabulatedFunctionMap = new LinkedHashMap<>();
        nodesGrid.forEach(this::initGroup);
    }

    public void load(Pair<Variable, Integer> nodeGrid) {
        File file = IO.loadTF(stage);
        if (!Objects.equals(file, null)) {
            MathFunction function = new IO(factory()).loadTabulatedFunctionAs(file);
            if (((TabulatedFunction) function).isMathFunctionExist()) {
                function = ((TabulatedFunction) function).getMathFunction();
            }
            MathFunction finalFunction = function;
            currentFunctions.computeIfPresent(nodeGrid, (integerIntegerPair, presentFunction) -> finalFunction);
            currentFunctions.putIfAbsent(nodeGrid, function);
            functionTextFields.get(nodeGrid).setText(function.getName(nodeGrid.getKey()));
        }
    }

    private void extractConstantsToXVariableFunctions() {
        extraTextFields.forEach((variableIntegerPair, textField) -> {
            if (variableIntegerPair.getKey().ordinal() == Variable.values().length - 1) {
                currentFunctions.computeIfPresent(new Pair<>(Variable.x, variableIntegerPair.getValue()),
                        (variableIntegerPair1, function) -> function.subtract(Double.parseDouble(textField.getText())));
            }
        });
    }

    private Map<Pair<Variable, Integer>, SupportedSign> getSignMap() {
        Map<Pair<Variable, Integer>, SupportedSign> currentSigns = new LinkedHashMap<>();
        extraTextFields.forEach((variableIntegerPair, textField) -> {
            if (variableIntegerPair.getKey().ordinal() != Variable.values().length - 1) {
                currentSigns.put(variableIntegerPair, SupportedSign.valueOf(textField.getText()));
            }
        });
        return currentSigns;
    }

    private List<SupportedSign> getSignList() {
        List<SupportedSign> currentSigns = new ArrayList<>();
        extraTextFields.forEach((variableIntegerPair, textField) -> {
            if (variableIntegerPair.getKey().ordinal() == 0) {
                currentSigns.add(SupportedSign.get(textField.getText()));
            }
        });
        return currentSigns;
    }

    @Override
    public void visit(TabController.VMFState vmfState) {
        unbindFutureGroups();
        extractConstantsToXVariableFunctions();
        List<SupportedSign> supportedSignList = getSignList();
        vmfState.createTab(createdPane, new VMFSV(currentFunctions, supportedSignList));
        stage.close();
    }

    @FXML
    public void ok() {
        state().changeState(State.VMF);
        state().accept(this);
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        stage.setOnShowing(windowEvent -> {
            functionTextFields.forEach((variableIntegerPair, textField) -> textField.setText(""));
            currentFunctions.clear();
            createdPane = new AnchorPane();
            initFutureGroups();
        });
        stage.setWidth(width);
        this.stage = stage;
    }

    public void initGroup(Pair<Variable, Integer> nodeGrid) {
        MenuBar functionsMenuBar = new MenuBar();
        functionsMenuBar.getMenus().add(mathFunctionMenuItems.get(nodeGrid).getParentMenu());
        functionsMenuBar.setPrefSize(100, 35);
        AnchorPane.setTopAnchor(functionsMenuBar, 15. + (nodeGrid.getValue() - 1) * 100);
        AnchorPane.setLeftAnchor(functionsMenuBar, 15. + nodeGrid.getKey().ordinal() * 180);

        JFXTextField textField = functionTextFields.get(nodeGrid);
        textField.setEditable(false);
        textField.setPrefSize(140, 30);
        AnchorPane.setTopAnchor(textField, 60. + (nodeGrid.getValue() - 1) * 100);
        AnchorPane.setLeftAnchor(textField, 15. + nodeGrid.getKey().ordinal() * 180);

        JFXTextField extraTextField = extraTextFields.get(nodeGrid);

        // относится к полям знаков
        if (nodeGrid.getKey().ordinal() != Variable.values().length - 1) {
            JFXComboBox<String> operationComboBox = new JFXComboBox<>();
            operationComboBox.setPrefSize(55, 35);
            AnchorPane.setTopAnchor(operationComboBox, 15. + (nodeGrid.getValue() - 1) * 100);
            AnchorPane.setLeftAnchor(operationComboBox, 125. + nodeGrid.getKey().ordinal() * 180);
            operationComboBox.getItems().addAll(Arrays.stream(SupportedSign.values()).map(SupportedSign::toString).collect(Collectors.toList()));
            operationComboBox.getSelectionModel().selectFirst();

            extraTextField.setPrefSize(40, 30);
            extraTextField.setAlignment(Pos.BOTTOM_CENTER);
            extraTextField.setFont(Font.font(14));
            extraTextField.setStyle("-fx-font-weight: bold;");
            AnchorPane.setTopAnchor(extraTextField, 60. + (nodeGrid.getValue() - 1) * 100);
            AnchorPane.setLeftAnchor(extraTextField, 155. + nodeGrid.getKey().ordinal() * 180);
            extraTextField.textProperty().bind(operationComboBox.valueProperty());

            creatingPane.getChildren().addAll(functionsMenuBar, textField, operationComboBox, extraTextField);
        }
        // относится к полям констант справа
        else {
            extraTextField.setPrefSize(70, 30);
            AnchorPane.setTopAnchor(extraTextField, 60. + (nodeGrid.getValue() - 1) * 100);
            AnchorPane.setLeftAnchor(extraTextField, 215. + nodeGrid.getKey().ordinal() * 180);
            extraTextField.getValidators().add(validator);
            extraTextField.textProperty().setValue("0");
            extraTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue)
                    extraTextField.validate();
            });

            Label equalSign = new Label("=");
            equalSign.setFont(Font.font(80));
            equalSign.setStyle("-fx-font-weight: bold;");
            AnchorPane.setTopAnchor(equalSign, (nodeGrid.getValue() - 1.) * 100);
            AnchorPane.setLeftAnchor(equalSign, 155. + nodeGrid.getKey().ordinal() * 180);

            creatingPane.getChildren().addAll(functionsMenuBar, textField, equalSign, extraTextField);
            width = 315 + nodeGrid.getKey().ordinal() * 180;
        }
    }

    private void processFutureGroups(BiConsumer<Pair<Variable, Integer>, JFXTextField> consumer) {
        functionTextFields.forEach(consumer);
        extraTextFields.forEach(consumer);
    }

    private void initFutureGroups() {
        processFutureGroups((variableIntegerPair, textField) -> {
            JFXTextField futureTextField = new JFXTextField();
            futureTextField.setEditable(false);
            futureTextField.textProperty().bind(textField.textProperty());
            futureTextField.setPrefSize(textField.getPrefWidth(), textField.getPrefHeight());
            AnchorPane.setTopAnchor(futureTextField, AnchorPane.getTopAnchor(textField));
            AnchorPane.setLeftAnchor(futureTextField, AnchorPane.getLeftAnchor(textField));
            createdPane.getChildren().add(futureTextField);
        });
    }

    private void unbindFutureGroups() {
        createdPane.getChildren().filtered(node -> node instanceof JFXTextField).forEach(node -> ((JFXTextField) node).textProperty().unbind());
    }

    private void addMenuItems(Pair<Variable, Integer> nodeGrid, Menu menu, Map<String, MathFunction> map) {
        menu.getItems()
                .addAll(map.keySet()
                        .stream()
                        .map(MenuItem::new)
                        .filter(menuItem -> !menu.getItems()
                                .contains(menuItem))
                        .collect(Collectors
                                .toList()));
    }

    private void bindMenuItem(Pair<Variable, Integer> nodeGrid, Menu menu, Map<String, MathFunction> map) {
        menu.getItems()
                .forEach(f -> f
                        .setOnAction(e -> {
                            if (menu.getText().equals("Мат. функции")) {
                                ConnectableItem item = map.get(f.getText()).getClass().getDeclaredAnnotation(ConnectableItem.class);
                                if (item.hasParameter()) {
                                    IO.setActualParameter(map, f.getText(), IO.getValue(item));
                                }
                            }
                            currentFunctions.computeIfPresent(nodeGrid, (integerIntegerPair, presentFunction) -> map.get(f.getText()));
                            currentFunctions.putIfAbsent(nodeGrid, map.get(f.getText()));
                            functionTextFields.get(nodeGrid).setText(map.get(f.getText()).getName(nodeGrid.getKey()));
                        }));
    }

    @Override
    public void setMathFunctionNode() {
        mathFunctionMenuItems.forEach((nodeGrid, menu) -> {
            addMenuItems(nodeGrid, menu, mathFunctionMap);
            bindMenuItem(nodeGrid, menu, mathFunctionMap);
        });
    }

    @Override
    public void setMathFunctionsMap(Map<String, MathFunction> mathFunctionMap) {
        this.mathFunctionMap = mathFunctionMap;
    }

    @Override
    public Map<String, MathFunction> getFittingTabulatedFunctionsMap() {
        return tabulatedFunctionMap;
    }

    @Override
    public void updateTabulatedFunctionNode() {
        if (!tabulatedFunctionMap.isEmpty()) {
            currentFunctionMenuItems.forEach((nodeGrid, menu) -> {
                addMenuItems(nodeGrid, menu, tabulatedFunctionMap);
                bindMenuItem(nodeGrid, menu, tabulatedFunctionMap);
            });
        } else {
            currentFunctionMenuItems.forEach((nodeGrid, menu) -> menu.getItems().clear());
        }
    }

    @Override
    public void updateCompositeFunctionNode() {
        compositeFunctionMenuItems.forEach((nodeGrid, menu) -> {
            addMenuItems(nodeGrid, menu, compositeFunctionMap);
            bindMenuItem(nodeGrid, menu, compositeFunctionMap);
        });
    }

    @Override
    public void updateCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap) {
        this.compositeFunctionMap = compositeFunctionMap;
    }

}
