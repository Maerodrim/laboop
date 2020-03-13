package ru.ssau.tk.itenion.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;

@ConnectableItem(name = "Delete point", type = Item.CONTROLLER, pathFXML = "deletePoint.fxml")
public class DeletePointController extends TFTabVisitor implements Openable {
    @FXML
    TextField x;
    private Stage stage;

    @FXML
    private void delete() {
        state.accept(this);
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
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    void visit(TabController.TFState tfState) {
        try {
            int index = tfState.getFunction().indexOfX(Double.parseDouble(x.getText()));
            if (index == -1) {
                AlertWindows.showWarning("Point doesn't exist");
            } else {
                ObservableList<Point> observableList = tfState.getObservableList();
                observableList.remove(index);
                IO.sort(observableList);
                tfState.getFunction().remove(index);
                stage.close();
            }
        } catch (NumberFormatException e) {
            AlertWindows.showWarning("Введите корретное значение");
        }
    }
}
