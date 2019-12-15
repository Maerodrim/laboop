package ru.ssau.tk.sergunin.lab.ui;

import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public class Plot {

    public void plotFunction(Stage stage, TabulatedFunction tabulatedFunction) {

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add");

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        newWindow.initOwner(stage);

        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 300);
        newWindow.setY(stage.getY() + 150);
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Function");

        areaChart.setLegendSide(Side.LEFT);

        // Series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("");

        for (int i = 0; i < tabulatedFunction.getCount(); i++) {
            series.getData().add(new XYChart.Data<>(tabulatedFunction.getX(i), tabulatedFunction.getY(i)));
        }
        newWindow.setTitle("Plot");
        Scene scene = new Scene(areaChart, 400, 300);
        areaChart.getData().addAll(series);
        newWindow.setScene(scene);
        newWindow.show();
    }
}
