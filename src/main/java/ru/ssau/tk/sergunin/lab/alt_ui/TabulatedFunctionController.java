package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.beans.Observable;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


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
    private Map<String, Boolean> existingPoints = new LinkedHashMap<>();

    private Stage stage;
    private Openable parentController;
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
        if (!(textX.getText().isEmpty() && textY.getText().isEmpty())) {
            if (Objects.equals(existingPoints.get(textX.getText()), null)) {
                list.add(new Point(Double.parseDouble(textX.getText()), Double.parseDouble(textY.getText())));
            }
            existingPoints.putIfAbsent(textX.getText(), true);
            ((TableController) parentController).sort(list);
            functionTable.setItems(list);
        } else if (textX.getText().isEmpty()) {
            AlertWindows.showWarning("X field is empty");
        } else if (textY.getText().isEmpty()) {
            AlertWindows.showWarning("Y field is empty");
        }
    }

    public boolean isUnmodifiable() {
        return isUnmodifiable.isSelected();
    }

    public boolean isStrict() {
        return isStrict.isSelected();
    }

    @FXML
    private void save() {
        if (list.size() > 1) {
            ((TableController) parentController).createTab(functionTable);
            stage.close();
        } else if (list.isEmpty()) {
            AlertWindows.showWarning("Empty function");
        } else {
            AlertWindows.showWarning("Input more points");
        }
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
    }

    @Override
    public void setParentController(Openable controller) {
        parentController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
