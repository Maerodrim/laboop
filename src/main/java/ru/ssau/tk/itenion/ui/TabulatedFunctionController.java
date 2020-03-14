package ru.ssau.tk.itenion.ui;

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
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.ui.states.State;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ConnectableItem(name = "Create new tabulated function", type = Item.CONTROLLER, pathFXML = "tabulatedFunction.fxml")
public class TabulatedFunctionController implements TabVisitorSimple, Initializable, Openable {
    private final Map<String, Boolean> existingPoints = new LinkedHashMap<>();
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
    private TabulatedFunctionFactory factory;
    private ObservableList<Point> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        list = FXCollections.observableArrayList(new ArrayList<>());
        functionTable.setItems(list);
    }

//    public void setFunctionTable(TableView<Point> functionTable) {
//        this.functionTable = functionTable;
//    }

    @FXML
    private void addRow() {
        state().changeState(State.TF);
        try {
            double x = Double.parseDouble(textX.getText());
            double y = Double.parseDouble(textY.getText());
            if (!(textX.getText().isEmpty() && textY.getText().isEmpty())) {
                if (!existingPoints.getOrDefault(x + "", false)) {
                    list.add(new Point(x, y));
                }
                existingPoints.putIfAbsent(x + "", true);
                IO.sort(list);
            } else if (textX.getText().isEmpty()) {
                AlertWindows.showWarning("X field is empty");
            } else if (textY.getText().isEmpty()) {
                AlertWindows.showWarning("Y field is empty");
            }
        } catch (NumberFormatException e) {
            AlertWindows.showWarning("Input correct values");
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
            state().accept((TFTabVisitor) tfState -> tfState.createTab(functionTable.getItems()));
            functionTable.setItems(FXCollections.observableArrayList(new ArrayList<>()));
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
            int index = functionTable.selectionModelProperty().getValue().getFocusedIndex();
            existingPoints.remove(list.get(index).x + "");
            list.remove(index);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }
}
