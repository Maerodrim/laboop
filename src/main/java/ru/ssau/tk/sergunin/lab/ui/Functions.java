package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.io.FunctionsIO;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Functions {

    private TabulatedFunctionFactory factory;

    public Functions(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public void newFunction(TableWindows window) {
        FlowPane secondaryLayout = new FlowPane();
        secondaryLayout.setPadding(new Insets(10));

        Scene secondScene = new Scene(secondaryLayout, 215, 300);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Create function");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        newWindow.initOwner(window.getStage());

        // Set position of second window, related to primary window.
        newWindow.setX(window.getStage().getX() + 300);
        newWindow.setY(window.getStage().getY() + 150);

        TextField textField1 = new TextField("temp");
        textField1.setMinWidth(200);
        TextField textField = new TextField("");
        textField.setMinWidth(200);
        TextField textField2 = new TextField("");
        textField.setMinWidth(200);
        Label label1 = new Label("Left domain: ");
        Label label2 = new Label("Right domain:");
        Label label3 = new Label("Count:");
        Label label4 = new Label("File: ");

        Map<String, MathFunction> map = new TreeMap<>();
        map.put("Ноль-функция", new ZeroFunction());
        map.put("Тождественная функция", new IdentityFunction());
        map.put("Квадратичная функция", new SqrFunction());
        map.put("Экспонента", new ExpFunction());
        ComboBox<String> functionsComboBox = new ComboBox<>(FXCollections.observableList(new ArrayList<>(map.keySet())));
        functionsComboBox.setValue("Тождественная функция");

        final Spinner<Integer> spinner = new Spinner<>();

        // Editable.
        spinner.setEditable(true);

        // Item List.
        ObservableList<Integer> items = FXCollections.observableArrayList(2, 3, 5, 10, 20, 30, 50, 100, 200, 300, 500, 1000);

        // Value Factory:
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(items);

        // The converter to convert between text and item object.
        valueFactory.setConverter(new Converter());

        spinner.setValueFactory(valueFactory);

        spinner.getEditor().setOnAction(event -> {
            String text = spinner.getEditor().getText();
            SpinnerValueFactory.ListSpinnerValueFactory<Integer>//
                    valueFactory1 = (SpinnerValueFactory.ListSpinnerValueFactory<Integer>) spinner.getValueFactory();

            StringConverter<Integer> converter = valueFactory1.getConverter();
            Integer enterValue = converter.fromString(text);

            // If the list does not contains 'enterValue'.
            if (!valueFactory1.getItems().contains(enterValue)) {
                // Add new item to list
                valueFactory1.getItems().add(enterValue);
                // Set to current
                valueFactory1.setValue(enterValue);
            } else {
                // Set to current
                valueFactory1.setValue(enterValue);
            }

        });

        // Add

        Button buttonAdd = new Button("Ok");
        buttonAdd.setOnAction(event -> {
            try {
                TabulatedFunction function = factory.create(
                        map.get(functionsComboBox.getValue()),
                        Double.parseDouble(textField.getText()),
                        Double.parseDouble(textField2.getText()),
                        spinner.getValue());
                window.setFunction(function);
                saveFunctionAs(new File(textField1.getText() + ".txt"), function);
                saveFunction(function);
                newWindow.close();
            } catch (Exception e) {
                AlertWindows.showError(e);
            }
        });

        // Clear
        Button buttonClear = new Button("Cancel");
        buttonClear.setOnAction(event -> {
            textField.clear();
            textField2.clear();
            newWindow.close();
        });

        secondaryLayout.getChildren().addAll(
                functionsComboBox,
                label4, textField1,
                label1, textField,
                label2, textField2,
                label3, spinner,
                buttonAdd, buttonClear);
        newWindow.show();
    }

    public void saveFunction(TabulatedFunction function) {
        saveFunctionAs(new File("temp.txt"), function);
    }

    public void saveFunctionAs(File file, TabulatedFunction function) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            FunctionsIO.writeTabulatedFunction(outputStream, function);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
    }

    public TabulatedFunction loadFunction() {
        return loadFunctionAs(new File("temp.txt"));
    }

    public TabulatedFunction loadFunctionAs(File file) {
        TabulatedFunction function = null;
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            function = FunctionsIO.readTabulatedFunction(inputStream, factory);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
        return function;
    }

    public TabulatedFunction insert(double x, double y) {
        TabulatedFunction function = loadFunction();
        String functionClassName = function.getClass().getSimpleName();
        if (functionClassName.equals("ArrayTabulatedFunction") || functionClassName.equals("LinkedListTabulatedFunction")) {
            ((ArrayTabulatedFunction) function).insert(x, y);
        } else {
            AlertWindows.showError(new UnsupportedOperationException());
        }
        saveFunction(function);
        return function;
    }

    public TabulatedFunction remove(double x) {
        TabulatedFunction function = loadFunction();
        String functionClassName = function.getClass().getSimpleName();
        if (functionClassName.equals("ArrayTabulatedFunction") || functionClassName.equals("LinkedListTabulatedFunction")) {
            ((ArrayTabulatedFunction) function).remove(function.indexOfX(x));
        } else {
            AlertWindows.showError(new UnsupportedOperationException());
        }
        saveFunction(function);
        return function;
    }

    private static class Converter extends StringConverter<Integer> {

        @Override
        public String toString(Integer object) {
            return object + "";
        }

        @Override
        public Integer fromString(String string) {
            return Integer.parseInt(string);
        }

    }

}
