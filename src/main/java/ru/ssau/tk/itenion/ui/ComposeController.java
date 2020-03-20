package ru.ssau.tk.itenion.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.exceptions.NaNException;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.ssau.tk.itenion.ui.ParameterSupplier.getValue;
import static ru.ssau.tk.itenion.ui.ParameterSupplier.setActualParameter;

@ConnectableItem(name = "Compose", type = Item.CONTROLLER, pathFXML = "compose.fxml")
public class ComposeController implements OpenableWindow, FactoryAccessible, MathFunctionAccessible, CompositeFunctionAccessible, TFTabVisitor {
    @FXML
    public CheckBox isStorable;
    @FXML
    ComboBox<String> comboBox;
    private Stage stage;
    private Map<String, MathFunction> functionMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private Optional<?> value = Optional.empty();
    private boolean isEditing = true;

    @FXML
    public void composeFunction() {
        state().accept(this);
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> {
            value = Optional.empty();
            isEditing = true;
        });
        this.stage = stage;
    }

    @Override
    public void setMathFunctionNode() {
        comboBox.getItems().addAll(functionMap.keySet());
        comboBox.setValue(comboBox.getItems().get(0));
    }

    @Override
    public void setMathFunctionsMap(Map<String, MathFunction> functionMap) {
        this.functionMap = functionMap;
    }

    @FXML
    public void doOnShow() {
        if (isEditing) {
            synchronized (comboBox) {
                functionMap.putAll(compositeFunctionMap);
                comboBox.getItems().addAll(new ArrayList<>(compositeFunctionMap.keySet()).stream()
                        .filter(item -> !comboBox.getItems().contains(item))
                        .collect(Collectors.toList()));
                isEditing = false;
            }
        } else {
            value = getValue(functionMap.get(comboBox.getSelectionModel().getSelectedItem())
                    .getClass().getDeclaredAnnotation(ConnectableItem.class));
        }
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
    public void visit(TabController.TFState tfState) {
        TabulatedFunction parentFunction = tfState.getFunction();
        value.ifPresent(unwrapValue -> setActualParameter(functionMap, comboBox.getValue(), value));
        try {
            MathFunction mathFunction = functionMap.get(comboBox.getValue()).andThen(parentFunction.getMathFunction());
            TabulatedFunction function = factory().create(
                    mathFunction,
                    parentFunction.leftBound(), parentFunction.rightBound(),
                    parentFunction.getCount());
            if (isStorable.isSelected()) {
                anyState().addCompositeFunction(mathFunction);
            }
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
}
