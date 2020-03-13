package ru.ssau.tk.itenion.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.exceptions.NaNException;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.states.State;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ConnectableItem(name = "Create new math function", type = Item.CONTROLLER, pathFXML = "mathFunction.fxml")
public class MathFunctionController extends TabVisitor implements Openable, MathFunctionAccessible, CompositeFunctionAccessible {
    @FXML
    TextField leftBorder;
    @FXML
    TextField rightBorder;
    @FXML
    TextField numberOfPoints;
    @FXML
    CheckBox isUnmodifiable;
    @FXML
    CheckBox isStrict;
    @FXML
    private ComboBox<String> comboBox;
    private Stage stage;
    private boolean isEditing = true;

    private Optional<?> value = Optional.empty();
    private TabulatedFunctionFactory factory;
    private Map<String, MathFunction> functionMap;
    private Map<String, MathFunction> compositeFunctionMap;

    @FXML
    private void createFunction() {
        state.changeState(State.TF);
        state.accept(this);
    }

    @FXML
    public void doOnAction() {
        if (isEditing) {
            synchronized (comboBox) {
                functionMap.putAll(compositeFunctionMap);
                comboBox.getItems().addAll(new ArrayList<>(compositeFunctionMap.keySet()).stream()
                        .filter(item -> !comboBox.getItems().contains(item))
                        .collect(Collectors.toList()));
                isEditing = false;
            }
        } else {
            value = IO.getValue(functionMap.get(comboBox.getSelectionModel().getSelectedItem())
                    .getClass().getDeclaredAnnotation(ConnectableItem.class));
        }
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setMathFunctionNode() {
        comboBox.getItems().addAll(functionMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }

    @Override
    public void setMathFunctionMap(Map<String, MathFunction> functionMap) {
        this.functionMap = functionMap;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(windowEvent -> {
            value = Optional.empty();
            isEditing = true;
        });
    }

    @Override
    public void updateCompositeFunctionNode() {
        comboBox.fireEvent(new ActionEvent());
    }

    @Override
    public void updateCompositeFunctionMap(Map<String, MathFunction> compositeFunctionMap) {
        this.compositeFunctionMap = compositeFunctionMap;
    }

    @Override
    void visit(TabController.TFState tfState) {
        value.ifPresent(unwrapValue -> IO.setActualParameter(functionMap, comboBox.getValue(), value));
        try {
            TabulatedFunction function = factory.create(functionMap.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()),
                    isStrict.isSelected(),
                    isUnmodifiable.isSelected());
            function.setMathFunction(functionMap.get(comboBox.getValue()));
            tfState.createTab(function);
            value = Optional.empty();
            isEditing = true;
            stage.close();
        } catch (NullPointerException | NumberFormatException nfe) {
            AlertWindows.showWarning("Введите корректные значения");
        } catch (IllegalArgumentException e) {
            AlertWindows.showWarning("Укажите положительное > 2 количество точек");
        } catch (NaNException e) {
            AlertWindows.showWarning("Результат не существует");
        }
    }

    @Override
    void visit(TabController.VMFState vmf) {}
}
