package model_javafx;

import functions.Function;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.*;

public class MadeFunction {
    public MadeFunction() {
    }
    public MadeFunction(String fileName) {
        this.fileName=fileName;
    }
    public void setFunction(TabulatedFunction function) {
        this.function = function;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private  TabulatedFunction function;
    private  String fileName;


    public void newFunction(final Stage primaryStage) {
        FlowPane secondaryLayout = new FlowPane();
        secondaryLayout.setPadding(new Insets(10));

        Scene secondScene = new Scene(secondaryLayout, 215, 300);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add Function");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        newWindow.initOwner(primaryStage);

        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 300);
        newWindow.setY(primaryStage.getY() + 150);

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
        final Spinner<Integer> spinner = new Spinner<Integer>();

        // Editable.
        spinner.setEditable(true);

        // Item List.
        ObservableList<Integer> items = FXCollections.observableArrayList(1, 2, 3);

        // Value Factory:
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.ListSpinnerValueFactory<>(items);

        // The converter to convert between text and item object.
        MyConverter converter = new MyConverter();
        valueFactory.setConverter(converter);

        spinner.setValueFactory(valueFactory);

        spinner.getEditor().setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String text = spinner.getEditor().getText();
                SpinnerValueFactory.ListSpinnerValueFactory<Integer>//
                        valueFactory = (SpinnerValueFactory.ListSpinnerValueFactory<Integer>) spinner.getValueFactory();

                StringConverter<Integer> converter = valueFactory.getConverter();
                Integer enterValue = converter.fromString(text);

                // If the list does not contains 'enterValue'.
                if (!valueFactory.getItems().contains(enterValue)) {
                    // Add new item to list
                    valueFactory.getItems().add(enterValue);
                    // Set to current
                    valueFactory.setValue(enterValue);
                } else {
                    // Set to current
                    valueFactory.setValue(enterValue);
                }

            }
        });
        // Add
        Button buttonAdd = new Button("Ok");
        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public  void handle(ActionEvent event) {
                try {
                    TabulatedFunction function =
                            new LinkedListTabulatedFunction(
                                    Double.parseDouble(textField.getText()),
                                    Double.parseDouble(textField2.getText()),
                                    spinner.getValue());
                    saveFunctionAs(textField1.getText(),function);
                    saveFunction(function);
                    System.out.println(function.toString());
                    newWindow.close();
                } catch (Exception e) {
                    ErrorWindows errorWindows = new  ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });

        // Clear
        Button buttonClear = new Button("Cancel");
        buttonClear.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                textField.clear();
                textField2.clear();
                newWindow.close();
            }
        });

        secondaryLayout.getChildren().addAll(
                label4,textField1,
                label1, textField,
                label2, textField2,
                label3, spinner,
                buttonAdd, buttonClear);
        newWindow.show();
    };

    public  void saveFunction(TabulatedFunction function) throws IOException {
        FileWriter writer = new FileWriter("temp.txt");
        TabulatedFunctions.writeTabulatedFunction(function, writer);
        writer.flush();
        writer.close();
    }
    public  void saveFunctionAs(String fileName, TabulatedFunction function) throws IOException {
        FileWriter writer = new FileWriter(fileName+".txt");
        TabulatedFunctions.writeTabulatedFunction(function, writer);
        writer.flush();
        writer.close();
    }
    public  TabulatedFunction loadFunction() throws IOException {
        FileReader reader = new FileReader("temp.txt");
        TabulatedFunction function = TabulatedFunctions.readTabulatedFunction(reader);
        reader.close();
        return function;
    }
    public  TabulatedFunction loadFunctionAs(String fileName) throws IOException {
        FileReader reader = new FileReader(fileName+".txt");
        TabulatedFunction function = TabulatedFunctions.readTabulatedFunction(reader);
        reader.close();
        return function;
    }
    public  TabulatedFunction loadByteFunctionAs(String fileName) throws IOException {
        InputStream input = new FileInputStream(fileName+".txt");
        TabulatedFunction function = TabulatedFunctions.inputTabulatedFunction(input);
        input.close();
        return function;
    }
    public TabulatedFunction tabulateFunction(Function function, double leftX, double rightX, int pointsCount) throws IOException {
        TabulatedFunction functions = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        saveFunction(functions);
        return functions;
    }
    class MyConverter extends StringConverter<Integer> {

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
