package ru.ssau.tk.itenion.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.ResourceBundle;

@ConnectableItem(name = "Calculate", type = Item.CONTROLLER, pathFXML = "calculate.fxml")
public class CalculateController implements Initializable, Openable, TFTabVisitor {
    @FXML
    TextField x;
    @FXML
    TextField y;
    private Stage stage;

    @FXML
    private void cancel() {
        stage.close();
    }

    @FXML
    private void calc() {
        state().accept(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        y.setEditable(false);
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
    public void visit(TabController.TFState tfState) {
        try {
            y.setText("" + tfState.getFunction().apply(Double.parseDouble(x.getText())));
        } catch (NumberFormatException e) {
            AlertWindows.showWarning("Введите корректное значение");
        }
    }
}
