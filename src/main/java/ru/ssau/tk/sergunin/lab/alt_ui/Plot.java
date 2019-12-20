package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

import java.util.Collection;
import java.util.List;

public class Plot {

    public static void plotFunction(Stage stage, Collection<Point> list) {

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add");

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        newWindow.initOwner(stage);

        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX());
        newWindow.setY(stage.getY());
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Function");

        // Series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        list.forEach(point -> series.getData().add(new XYChart.Data<>(point.x, point.y)));

        newWindow.setTitle("Plot");
        Scene scene = new Scene(areaChart, 400, 300);
        areaChart.getData().addAll(series);
        newWindow.setScene(scene);
        newWindow.show();
    }
}
