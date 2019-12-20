package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.media.Media;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class CompositeFunctionController implements Initializable, Openable {
    private Stage stage;
    private Openable parentController;
    private Map<String, MathFunction> comboBoxMap;
    private InputParameterController inputParameterController = new InputParameterController();

    public void setComboBoxMap(Map<String, MathFunction> comboBoxMap) {
        this.comboBoxMap = comboBoxMap;
    }

    private MathFunction mathFunction;
    private TabulatedFunctionFactory factory;
    @FXML
    ComboBox<String> comboBox;

    @FXML
    public void doOnClickOnComboBox(ActionEvent event) {
        if (comboBoxMap.get(((ComboBox) event.getSource()).getValue().toString()).getClass().getDeclaredAnnotation(Selectable.class).parameter()) {
            inputParameterController.getStage().show();
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputParameterController = Functions.initializeModalityWindow("src/main/java/ru/ssau/tk/sergunin/lab/alt_ui/fxml/inputParameter.fxml", inputParameterController);
        inputParameterController.getStage().initOwner(stage);
        inputParameterController.getStage().setTitle("Input parameter");
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }


    @FXML
    public void composeFunction() {
        TabulatedFunction parentFunction = ((TableController) parentController).getFunction();
        if (comboBoxMap.get(comboBox.getValue()).getClass().getDeclaredAnnotation(Selectable.class).parameter()) {
            try {
                comboBoxMap.replace(comboBox.getValue(), comboBoxMap.get(comboBox.getValue()).getClass().getDeclaredConstructor(Double.TYPE).newInstance(inputParameterController.getParameter()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            TabulatedFunction function = factory.create(
                    comboBoxMap.get(comboBox.getValue()).andThen(parentFunction),
                    parentFunction.leftBound(), parentFunction.rightBound(),
                    parentFunction.getCount(),
                    true,
                    false);
            ((TableController)parentController).createTab(function);
            stage.close();
        } catch (NullPointerException | NumberFormatException nfe) {
            AlertWindows.showWarning("Введите корректные значения");
        } catch (IllegalArgumentException e) {
            AlertWindows.showWarning("Укажите положительное > 2 количество точек");
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
        this.factory = factory;
    }

    @Override
    public void setParentController(Openable controller) {
        this.parentController = controller;
    }
}
