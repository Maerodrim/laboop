package ru.ssau.tk.sergunin.lab.ui;

import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.Point;

import java.util.Collection;

class Plot {

    static void plotFunction(Stage stage, Collection<Point> list, String name) {
        Stage newWindow = new Stage();
        newWindow.setTitle("Add");
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.setX(stage.getX());
        newWindow.setY(stage.getY());
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        //areaChart.setTitle(name);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        list.forEach(point -> series.getData().add(new XYChart.Data<>(point.x, point.y)));
        newWindow.setTitle("Plot");
        Scene scene = new Scene(areaChart, 400, 300);
        areaChart.getData().add(series);
        newWindow.setScene(scene);
        newWindow.show();
    }
}
