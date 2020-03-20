package ru.ssau.tk.itenion.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.polynomial.PolynomialParser;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParameterSupplier {
    private static final TextInputDialog dialog = new TextInputDialog();
    private static BooleanBinding booleanBinding;
    private static Button textInputDialogOkButton;
    private static TextField textInputDialogInputField;
    private static Function<ConnectableItem, ?> itemOptionalFunction = connectableItem -> {
        if (connectableItem.parameterInstance().equals(Double.class)) {
            AtomicReference<Double> doubleAtomicReference = new AtomicReference<>();
            getValue("Parameter: ", IO.isDouble).ifPresent(s -> doubleAtomicReference.set(Double.parseDouble(s)));
            return doubleAtomicReference.get();
        } else if (connectableItem.parameterInstance().equals(Integer.class)) {
            AtomicReference<Integer> integerAtomicReference = new AtomicReference<>();
            getValue("Parameter: ", IO.isInteger).ifPresent(s -> integerAtomicReference.set(Integer.parseInt(s)));
            return integerAtomicReference.get();
        } else {
            switch (connectableItem.name()) {
                case "Полином": {
                    AtomicReference<String> stringAtomicReference = new AtomicReference<>();
                    getValue("Polynomial: ", PolynomialParser.isPolynomial).ifPresent(stringAtomicReference::set);
                    return stringAtomicReference.get();
                }
                // add rules for further functions here
            }
        }
        return null;
    };

    static {
        dialog.setOnShowing(dialogEvent -> dialog.getEditor().setText(""));
    }

    public static Optional<String> getValue(String title, String headerText, String contentText, Predicate<String> isValid) {
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        textInputDialogOkButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        textInputDialogInputField = dialog.getEditor();
        booleanBinding = Bindings.createBooleanBinding(() -> isValid.test(textInputDialogInputField.getText()), textInputDialogInputField.textProperty());
        textInputDialogOkButton.disableProperty().bind(booleanBinding.not());
        return dialog.showAndWait();
    }

    public static Optional<String> getValue(String contentText, Predicate<String> isValid) {
        return getValue("Input parameter window", null, contentText, isValid);
    }

    public static Optional<?> getValue(ConnectableItem item) {
        if (Objects.isNull(item)) {
            return Optional.empty();
        } else return Optional.of(item).filter(ConnectableItem::hasParameter).map(itemOptionalFunction);
    }

    public static void setActualParameter(Map<String, MathFunction> map, String selectedValue, Optional<?> value) {
        ConnectableItem item = map.get(selectedValue).getClass().getDeclaredAnnotation(ConnectableItem.class);
        if (Objects.isNull(item)) {
            return;
        }
        value.ifPresent(value1 -> {
            if (item.hasParameter()) {
                try {
                    if (item.hasParameter()) {
                        map.replace(selectedValue, map.get(selectedValue).getClass()
                                .getDeclaredConstructor(item.parameterInstance()).newInstance(value1));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    AlertWindows.showError(e);
                }
            }
        });
    }

    public static MathFunction setActualParameter(MathFunction function, Optional<?> value) {
        AtomicReference<MathFunction> functionAtomicReference = new AtomicReference<>();
        ConnectableItem item = function.getClass().getDeclaredAnnotation(ConnectableItem.class);
        if (Objects.isNull(item)) {
            return function;
        }
        value.ifPresent(value1 -> {
            if (item.hasParameter()) {
                try {
                    if (item.hasParameter()) {
                        functionAtomicReference.set(function.getClass().getDeclaredConstructor(item.parameterInstance()).newInstance(value1));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    AlertWindows.showError(e);
                }
            }
        });
        return functionAtomicReference.get();
    }
}
