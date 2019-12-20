package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class TabulatedFunctionController implements Initializable, Openable {
    @FXML
    TextField textX;
    @FXML
    TextField textY;
    @FXML
    CheckBox isUnmodifiable;
    @FXML
    CheckBox isStrict;
    @FXML
    TableView<Point> functionTable;
    @FXML
    TableColumn<Point, Double> x;
    @FXML
    TableColumn<Point, Double> y;

    private Stage stage;
    private Openable parentController;
    private TabulatedFunctionFactory factory;
    private ObservableList<Point> list;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        list = FXCollections.observableArrayList(new ArrayList<>());
        functionTable.setItems(list);
    }

    @FXML
    private void addRow() {
        list.add(new Point(Double.parseDouble(textX.getText()), Double.parseDouble(textY.getText())));
        functionTable.setItems(list);
    }

    public boolean isUnmodifiable() {
        return isUnmodifiable.isSelected();
    }

    public boolean isStrict() {
        return isStrict.isSelected();
    }

    @FXML
    private void save() {
        ((TableController)parentController).createTab(functionTable);
        stage.close();
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    @FXML
    private void deleteRow() {
        if (list.size() != 0) {
            list.remove(list.size() - 1);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
