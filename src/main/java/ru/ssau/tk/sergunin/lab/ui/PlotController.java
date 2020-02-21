package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.net.URL;
import java.util.ResourceBundle;

@ConnectableItem(name = "Plot", type = Item.CONTROLLER, pathFXML = "plot.fxml")
public class PlotController implements Initializable, Openable {
    private Stage stage;
    @FXML
    private LineChart<Number, Number> lineChart;
    private double x1, y1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lineChart.setCreateSymbols(false);
        lineChart.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            x1 = e.getX();
            y1 = e.getY();
        });
        lineChart.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> System.out.println("x: " + x1 + " y: " + y1));
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setFactory(TabulatedFunctionFactory factory) {
    }

    @Override
    public void setParentController(Openable controller) {
    }

    public void addSeries(ObservableList<Point> data, String name, String color) {
        lineChart.setStyle("CHART_COLOR_1: " + color + " ;");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.getData().add(series);
        series.setName(name);
        data.forEach(point -> series.getData().add(new XYChart.Data<>(point.x, point.y)));
        lineChart.lookup(".series" + 0).setStyle("-fx-stroke: " + color + "; -fx-stroke-width: 1;");
    }

    public void setSeries(ObservableList<Point> data, String name, String color) {
        lineChart.getData().clear();
        addSeries(data, name, color);
    }
}
