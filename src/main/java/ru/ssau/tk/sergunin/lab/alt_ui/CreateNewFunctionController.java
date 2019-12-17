package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.ssau.tk.sergunin.lab.functions.*;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.*;

public class CreateNewFunctionController implements Initializable {

    TableController primaryController;

    int numberId = 1;
    List<Tab> tabs = new ArrayList<>();
    ObservableList<Point> list;
    TableColumn<Point, Double> x = new TableColumn<>("X");
    TableColumn<Point, Double> y = new TableColumn<>("Y");
    private Map<String, MathFunction> map;

    @FXML
    ComboBox<String> comboBox;
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

    public void setPrimaryController(TableController primaryController) {
        this.primaryController = primaryController;
    }

    TabulatedFunction function = new ArrayTabulatedFunctionFactory().getIdentity();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        map = new TreeMap<>();
        map.put("Ноль-функция", new ZeroFunction());
        map.put("Тождественная функция", new IdentityFunction());
        map.put("Квадратичная функция", new SqrFunction());
        map.put("Экспонента", new ExpFunction());
    }

    @FXML
    private void doOnCreate() {
        Tab tab = new Tab();
        tab.setText("Function" + numberId);
        tab.setId("function" + numberId++);
        tab.setClosable(true);
        primaryController.tabPane.getTabs().add(tab);
        if (isUnmodifiable.isSelected() && isStrict.isSelected()) {
            function = primaryController.factory.createStrictUnmodifiable(
                    map.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()));
        } else if (isUnmodifiable.isSelected()) {
            function = primaryController.factory.createUnmodifiable(
                    map.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()));
        } else if (isStrict.isSelected()) {
            function = primaryController.factory.createStrict(
                    map.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()));
        } else {
            function = primaryController.factory.create(
                    map.get(comboBox.getValue()),
                    Double.parseDouble(leftBorder.getText()),
                    Double.parseDouble(rightBorder.getText()),
                    Integer.parseInt(numberOfPoints.getText()));
        }
        list = getModelFunctionList(function);
        TableView<Point> table = new TableView<>();
        table.setItems(list);
        table.getColumns().addAll(x, y);
        tab.setContent(table);
        tabs.add(tab);
    }

    private ObservableList<Point> getModelFunctionList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }
}
