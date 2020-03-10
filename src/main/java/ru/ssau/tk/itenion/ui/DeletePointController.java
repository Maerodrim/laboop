package ru.ssau.tk.itenion.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

@ConnectableItem(name = "Delete point", type = Item.CONTROLLER, pathFXML = "deletePoint.fxml")
public class DeletePointController implements Openable {
    @FXML
    TextField x;
    private Stage stage;
    private Openable parentController;

    @FXML
    private void delete() {
        try {
            int index = ((TabulatedFunction) ((TableController) parentController).getFunction()).indexOfX(Double.parseDouble(x.getText()));
            if (index == -1) {
                AlertWindows.showWarning("Point doesn't exist");
            } else {
                ((TableController) parentController).getObservableList().remove(index);
                ((TableController) parentController).sort();
                ((TabulatedFunction) ((TableController) parentController).getFunction()).remove(index);
                stage.close();
            }
        } catch (NumberFormatException e) {
            AlertWindows.showWarning("Введите корретное значение");
        }
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
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }

}
