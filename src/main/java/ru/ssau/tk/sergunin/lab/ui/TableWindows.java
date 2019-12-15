package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableWindows {

    TableView<Point> table = new TableView<>();
    TabulatedFunctionFactory factory;
    TabulatedFunction function;
    File temp;
    private Stage stage;

    public TableWindows(Stage stage, TabulatedFunctionFactory factory) {
        this.stage = stage;
        this.factory = factory;
        table(stage, factory.getIdentity());
        temp = new File("temp.txt");
        try {
            temp.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setFunction(TabulatedFunction function) {
        this.function = function;
        table.setItems(getModelFunctionList(function));
    }

    private void table(Stage stage, TabulatedFunction tabulatedFunction) {

        function = tabulatedFunction;

        Functions functions = new Functions(factory);
        ru.ssau.tk.sergunin.lab.ui.Dialog dialog = new ru.ssau.tk.sergunin.lab.ui.Dialog(functions);

        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu tableMenu = new Menu("Table");
        Menu tabulatedMenu = new Menu("Tabulated");

        MenuItem newItem = new MenuItem("New");
        MenuItem openFileItem = new MenuItem("Load");
        MenuItem saveAsFileItem = new MenuItem("Save as");
        MenuItem saveFileItem = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        MenuItem newPoint = new MenuItem("New point");
        MenuItem deletePoint = new MenuItem("Delete point");
        MenuItem plot = new MenuItem("Plot");
        MenuItem cos = new MenuItem("cos");
        MenuItem exp = new MenuItem("exp");
        MenuItem sin = new MenuItem("sin");
        MenuItem tan = new MenuItem("tan");
        MenuItem log = new MenuItem("log");

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem, openFileItem, saveFileItem, saveAsFileItem, exit);
        tableMenu.getItems().addAll(plot, newPoint, deletePoint);
        tabulatedMenu.getItems().addAll(cos, sin, tan, exp, log);
        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, tableMenu, tabulatedMenu);
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        openFileItem.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        saveFileItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveAsFileItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        exit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        plot.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));

        plot.setOnAction(event -> {
            if (function.getCount() == 0) {
                AlertWindows.showWarning("No contents in table");
            } else {
                new Plot().plotFunction(stage, function);
            }
        });
        newPoint.setOnAction(event -> {
            if (function.getCount() == 0) {
                AlertWindows.showWarning("No contents in table");
            } else {
                dialog.showAddPointDialog(this);
            }
        });
        deletePoint.setOnAction(event -> {
            if (function.getCount() == 0) {
                AlertWindows.showWarning("No contents in table");
            } else {
                dialog.showDeleteDialog(this);
            }
        });
        newItem.setOnAction(event -> functions.newFunction(this));
        openFileItem.setOnAction(event -> {
            File file = Dialog.load(stage);
            if (!Objects.equals(null, file)) {
                setFunction(functions.loadFunctionAs(file));
                table.setItems(getModelFunctionList(function));
            }
        });
        saveFileItem.setOnAction(event -> functions.saveFunction(function));
        saveAsFileItem.setOnAction(event -> {
            File file = Dialog.save(stage);
            if (!Objects.equals(null, file)) functions.saveFunctionAs(file, function);
        });
        exit.setOnAction(event -> {
            try {
                stage.close();
            } catch (Exception e) {
                AlertWindows.showError(e);
            }
        });


        /*cos.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    MadeFunction madeFunction = new MadeFunction();
                    MadeFunction function = new Cos();
                    ModelFunction modelFunction1 = new ModelFunction(madeFunction.tabulateFunction(function,
                            modelFunction.getX().leftBound(),
                            modelFunction.getX().rightBound(),
                            modelFunction.getX().getCount()));
                    madeFunction.saveFunction(modelFunction1.getX());
                    TableWindows tableFunction = new TableWindows();
                    tableFunction.table(stage, modelFunction1);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        sin.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    MadeFunction madeFunction = new MadeFunction();
                    MadeFunction function = new Sin();
                    ModelFunction modelFunction1 = new ModelFunction(madeFunction.tabulateFunction(function,
                            modelFunction.getX().getLeftDomainBorder(),
                            modelFunction.getX().getRightDomainBorder(),
                            modelFunction.getX().getPointCount()));
                    madeFunction.saveFunction(modelFunction1.getX());
                    TableWindows tableFunction = new TableWindows();
                    tableFunction.table(stage, modelFunction1);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        tan.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    MadeFunction madeFunction = new MadeFunction();
                    MadeFunction function = new Tan();
                    ModelFunction modelFunction1 = new ModelFunction(madeFunction.tabulateFunction(function,
                            modelFunction.getX().getLeftDomainBorder(),
                            modelFunction.getX().getRightDomainBorder(),
                            modelFunction.getX().getPointCount()));
                    madeFunction.saveFunction(modelFunction1.getX());
                    TableWindows tableFunction = new TableWindows();
                    tableFunction.table(stage, modelFunction1);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        exp.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    MadeFunction madeFunction = new MadeFunction();
                    MadeFunction function = new Exp();
                    ModelFunction modelFunction1 = new ModelFunction(madeFunction.tabulateFunction(function,
                            modelFunction.getX().getLeftDomainBorder(),
                            modelFunction.getX().getRightDomainBorder(),
                            modelFunction.getX().getPointCount()));
                    madeFunction.saveFunction(modelFunction1.getX());
                    TableWindows tableFunction = new TableWindows();
                    tableFunction.table(stage, modelFunction1);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        log.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Dialog dialog = new Dialog();
                    MadeFunction madeFunction = new MadeFunction();
                    MadeFunction function = new Log(Double.parseDouble(dialog.showInputDoubleDialog()));
                    ModelFunction modelFunction1 = new ModelFunction(madeFunction.tabulateFunction(function,
                            modelFunction.getX().getLeftDomainBorder(),
                            modelFunction.getX().getRightDomainBorder(),
                            modelFunction.getX().getPointCount()));
                    madeFunction.saveFunction(modelFunction1.getX());
                    TableWindows tableFunction = new TableWindows();
                    tableFunction.table(stage, modelFunction1);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });*/

        // Create column.
        TableColumn<Point, Double> x = new TableColumn<>("X");

        // Create column.
        TableColumn<Point, Double> y = new TableColumn<>("Y");

        // Defines how to fill data for each cell.
        // Get value from property.
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));

        // Display row data
        table.setItems(getModelFunctionList(function));

        table.getColumns().addAll(x, y);

        BorderPane root2 = new BorderPane();
        root2.setTop(menuBar);
        root2.setCenter(table);
        StackPane root = new StackPane();
        root.setPadding(new Insets(2));
        root.getChildren().addAll(root2);
        stage.setTitle("Tion");

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> temp.delete());
    }

    private ObservableList<Point> getModelFunctionList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }

}
