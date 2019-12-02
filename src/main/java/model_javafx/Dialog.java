package model_javafx;

import functions.FunctionPoint;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

import java.awt.print.Book;
import java.util.Optional;

public class Dialog {

    private Label label;

    public String showInputTextDialog() {

        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Dialog Windows");
        dialog.setHeaderText("Enter your parametr:");
        dialog.setContentText("Text:");

        Optional<String> result = dialog.showAndWait();


        return result.get();

    }

    public String showInputDoubleDialog() {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Dialog Windows");
        dialog.setHeaderText("Enter your base Log:");
        dialog.setContentText("Value:");

        Optional<String> result = dialog.showAndWait();


        return result.get();

    }

    public String showLoadDialog() {
        String stringload = "stringload";
        String byteload = "byteload";
        ChoiceDialog<String> dialog = new ChoiceDialog<String>(stringload, byteload);

        dialog.setTitle("Load Dialog");
        dialog.setHeaderText("Choose type of loading:");
        dialog.setContentText("Type:");

        Optional<String> result = dialog.showAndWait();

        return result.get();

    }

    public void showDeleteDialog(final Stage primaryStage) {
        FlowPane secondaryLayout = new FlowPane();
        secondaryLayout.setPadding(new Insets(10));

        Scene secondScene = new Scene(secondaryLayout, 250, 200);
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

        TextField textField = new TextField("");
        textField.setMinWidth(200);
        Label label1 = new Label("   X   ");

        // Add
        Button buttonAdd = new Button("Ok");
        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    model_javafx.MadeFunction madeFunction = new model_javafx.MadeFunction();
                    TabulatedFunction tabulatedFunction = madeFunction.loadFunction();
                    tabulatedFunction.deletePoint(tabulatedFunction.indexOfX(Double.parseDouble(textField.getText())));
                    madeFunction.saveFunction(tabulatedFunction);
                    newWindow.close();
                } catch (Exception e) {
                    model_javafx.ErrorWindows errorWindows = new model_javafx.ErrorWindows();
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
                newWindow.close();
            }
        });

        secondaryLayout.getChildren().addAll(
                label1, textField,
                buttonAdd, buttonClear);
        newWindow.show();
    }

    public void showAddPointDialog(final Stage primaryStage) {
        FlowPane secondaryLayout = new FlowPane();
        secondaryLayout.setPadding(new Insets(10));

        Scene secondScene = new Scene(secondaryLayout, 250, 200);
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

        TextField textField = new TextField("");
        textField.setMinWidth(200);
        TextField textField2 = new TextField("");
        textField.setMinWidth(200);
        Label label1 = new Label("X");
        Label label2 = new Label("Y");

        // Add
        Button buttonAdd = new Button("Ok");
        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    model_javafx.MadeFunction madeFunction = new MadeFunction();
                    TabulatedFunction tabulatedFunction = madeFunction.loadFunction();
                    FunctionPoint[] functionPoints = new FunctionPoint[tabulatedFunction.getCount()];
                    for (int i = 0; i < tabulatedFunction.getCount(); i++) {
                        functionPoints[i] = new FunctionPoint(tabulatedFunction.getX(i), tabulatedFunction.getY(i));
                    }
                    LinkedListTabulatedFunction linkedListTabulatedFunction = new LinkedListTabulatedFunction(functionPoints);
                    linkedListTabulatedFunction.addPoint(new FunctionPoint(
                            Double.parseDouble(textField.getText()),
                            Double.parseDouble(textField2.getText())));
                    madeFunction.saveFunction((TabulatedFunction) linkedListTabulatedFunction);
                    newWindow.close();
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
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
                label1, textField,
                label2, textField2,
                buttonAdd, buttonClear);
        newWindow.show();
    }
}

