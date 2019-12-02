package model_javafx;

import functions.Function;
import functions.FunctionPoint;
import functions.basic.*;
import functions.meta.Composition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import java.util.ArrayList;
import java.util.List;

public class TableWindows {
    public void table(Stage stage, ModelFunction modelFunction) {
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu tableMenu = new Menu("Table");
        Menu tabuleMenu = new Menu("Tabulated");

        MenuItem newItem = new MenuItem("New Function");
        MenuItem openFileItem = new MenuItem("Load Function");
        MenuItem saveFileItem = new MenuItem("Save Function");
        MenuItem table2 = new MenuItem("Table");
        MenuItem exit = new MenuItem("Exit");
        MenuItem newPoint = new MenuItem("New Point");
        MenuItem deletePoint = new MenuItem("Delete Point");
        MenuItem graf = new MenuItem("Graf");
        MenuItem cos = new MenuItem("cos");
        MenuItem exp = new MenuItem("exp");
        MenuItem sin = new MenuItem("sin");
        MenuItem tan = new MenuItem("tan");
        MenuItem log = new MenuItem("log");

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem, openFileItem, saveFileItem, exit);
        tableMenu.getItems().addAll(table2, graf, newPoint, deletePoint);
        tabuleMenu.getItems().addAll(cos, sin, tan, exp, log);
        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, tableMenu, tabuleMenu);
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        openFileItem.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        saveFileItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        exit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        table2.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        graf.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));
        TableView<FunctionPoint> table = new TableView<FunctionPoint>();

        table2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    ModelFunction modelFunction = new ModelFunction(new MadeFunction().loadFunction());
                    TableWindows madeFunction = new TableWindows();
                    madeFunction.table(stage, modelFunction);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        graf.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    ModelFunction modelFunction = new ModelFunction(new MadeFunction().loadFunction());
                    Graf graf1 = new Graf();
                    graf1.grafFunction(stage, modelFunction.getX());
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        newPoint.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Dialog dialog = new Dialog();
                    dialog.showAddPointDialog(stage);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        deletePoint.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Dialog dialog = new Dialog();
                    dialog.showDeleteDialog(stage);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });


        newItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                MadeFunction madeFunction = new MadeFunction();
                madeFunction.newFunction(stage);
            }
        });
        openFileItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Dialog dialog = new Dialog();
                    MadeFunction madeFunction = new MadeFunction();
                    ModelFunction modelFunction=new ModelFunction();
                    switch(dialog.showLoadDialog()) {
                        case "stringload":
                            modelFunction.setX(madeFunction.loadFunctionAs(dialog.showInputTextDialog()));
                            break;
                        case "byteload":
                            modelFunction.setX(madeFunction.loadByteFunctionAs(dialog.showInputTextDialog()));
                            break;
                    }
                    madeFunction.saveFunction(modelFunction.getX());
                    TableWindows tableFunction = new TableWindows();
                    tableFunction.table(stage, modelFunction);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        saveFileItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Dialog dialog = new Dialog();
                    MadeFunction madeFunction = new MadeFunction();
                    madeFunction.saveFunctionAs(dialog.showInputTextDialog(), modelFunction.getX());
                    TableWindows tableFunction = new TableWindows();
                    tableFunction.table(stage, modelFunction);
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    stage.close();
                } catch (Exception e) {
                    ErrorWindows errorWindows = new ErrorWindows();
                    errorWindows.showError(e);
                }
            }
        });


        cos.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    MadeFunction madeFunction = new MadeFunction();
                    Function function = new Cos();
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
                    Function function = new Sin();
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
                    Function function = new Tan();
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
                    Function function = new Exp();
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
                    Function function = new Log(Double.parseDouble(dialog.showInputDoubleDialog()));
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


        // Create column.
        TableColumn<FunctionPoint, Double> pointX //
                = new TableColumn<FunctionPoint, Double>("pointX");

        // Create column.
        TableColumn<FunctionPoint, Double> pointY//
                = new TableColumn<FunctionPoint, Double>("pointY");


        // Defines how to fill data for each cell.
        // Get value from property.
        pointX.setCellValueFactory(new PropertyValueFactory<>("pointX"));
        pointY.setCellValueFactory(new PropertyValueFactory<>("pointY"));

        // Display row data
        ObservableList<FunctionPoint> list = getModelFunctionList(modelFunction);
        table.setItems(list);

        table.getColumns().addAll(pointX, pointY);

        BorderPane root2 = new BorderPane();
        root2.setTop(menuBar);
        root2.setCenter(table);
        StackPane root = new StackPane();
        root.setPadding(new

                Insets(5));
        root.getChildren().

                addAll(root2);

        stage.setTitle("Pain");

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
        stage.show();
    }

    private ObservableList<FunctionPoint> getModelFunctionList(ModelFunction modelFunction) {
        List<FunctionPoint> listPoint = new ArrayList<FunctionPoint>();
        ;
        for (int i = 0; i < modelFunction.getX().getPointCount(); i++) {
            listPoint.add(new FunctionPoint(modelFunction.getX().getPointX(i), modelFunction.getX().getPointY(i)));
        }
        ObservableList<FunctionPoint> list = FXCollections.observableArrayList(listPoint);
        return list;
    }

}
