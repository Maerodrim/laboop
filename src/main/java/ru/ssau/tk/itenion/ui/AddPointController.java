package ru.ssau.tk.itenion.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.operations.TabulatedFunctionOperationService;

@ConnectableItem(name = "Add point", type = Item.CONTROLLER, pathFXML = "addPoint.fxml")
public class AddPointController implements TFTabVisitor, OpenableWindow {
    @FXML
    TextField x;
    @FXML
    TextField y;

    private Stage stage;

    @FXML
    private void add() {
        state().accept(this);
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void visit(TabController.TFState tfState) {
        try {
            Point point = new Point(Double.parseDouble(x.getText()), Double.parseDouble(y.getText()));
            int index = tfState.getFunction().indexOfX(point.x);
            if (index == -1) {
                ObservableList<Point> observableList = tfState.getObservableList();
                observableList.add(point);
                TabulatedFunctionOperationService.sort(observableList);
                tfState.getFunction().insert(point.x, point.y);
                tfState.getFunction().setMathFunction(null);
                stage.close();
            } else {
                AlertWindows.showWarning("Point already exists");
            }
        } catch (NumberFormatException e) {
            AlertWindows.showWarning("Введите корректное значение");
        }
    }
}
