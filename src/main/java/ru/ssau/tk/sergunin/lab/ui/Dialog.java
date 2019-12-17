package ru.ssau.tk.sergunin.lab.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;

import java.io.File;

public class Dialog {

    private Functions functions;

    Dialog() {
        functions = new Functions(new ArrayTabulatedFunctionFactory());
    }

    Dialog(Functions functions) {
        this.functions = functions;
    }

    public static File load(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load function");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        return fileChooser.showOpenDialog(stage);
    }

    public static File save(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save function");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        return fileChooser.showSaveDialog(stage);
    }

    public void showDeleteDialog(TableWindows window) {
        FlowPane secondaryLayout = new FlowPane();
        secondaryLayout.setPadding(new Insets(10));

        Scene secondScene = new Scene(secondaryLayout, 250, 200);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add function");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        newWindow.initOwner(window.getStage());

        // Set position of second window, related to primary window.
        newWindow.setX(window.getStage().getX() + 300);
        newWindow.setY(window.getStage().getY() + 150);

        TextField textField = new TextField("");
        textField.setMinWidth(200);
        Label label1 = new Label("   X   ");

        // Add
        Button buttonAdd = new Button("Ok");
        buttonAdd.setOnAction(event -> window.setFunction(functions.remove(Double.parseDouble(textField.getText())))
        );

        // Clear
        Button buttonClear = new Button("Cancel");
        buttonClear.setOnAction(event -> {
            textField.clear();
            newWindow.close();
        });

        secondaryLayout.getChildren().addAll(
                label1, textField,
                buttonAdd, buttonClear);
        newWindow.show();
    }

    public void showAddPointDialog(TableWindows window) {
        FlowPane secondaryLayout = new FlowPane();
        secondaryLayout.setPadding(new Insets(10));

        Scene secondScene = new Scene(secondaryLayout, 250, 200);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add function");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        newWindow.initOwner(window.getStage());

        // Set position of second window, related to primary window.
        newWindow.setX(window.getStage().getX() + 300);
        newWindow.setY(window.getStage().getY() + 150);

        TextField textField = new TextField("");
        textField.setMinWidth(200);
        TextField textField2 = new TextField("");
        textField.setMinWidth(200);
        Label label1 = new Label("X");
        Label label2 = new Label("Y");

        // Add
        Button buttonAdd = new Button("Ok");
        buttonAdd.setOnAction(event ->
                window.setFunction(functions.insert(
                        Double.parseDouble(textField.getText()),
                        Double.parseDouble(textField2.getText())))
        );

        // Clear
        Button buttonClear = new Button("Cancel");
        buttonClear.setOnAction(event -> {
            textField.clear();
            textField2.clear();
            newWindow.close();
        });

        secondaryLayout.getChildren().addAll(
                label1, textField,
                label2, textField2,
                buttonAdd, buttonClear);
        newWindow.show();
    }
}

