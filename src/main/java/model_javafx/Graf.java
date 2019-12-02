package model_javafx;

import functions.*;
import functions.basic.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public class Graf {

    public void grafFunction(Stage stage, TabulatedFunction tabulatedFunction) {

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Add Function");

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window

        newWindow.initOwner(stage);

        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 300);
        newWindow.setY(stage.getY() + 150);
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis, yAxis);
        areaChart.setTitle("Function");

        areaChart.setLegendSide(Side.LEFT);

        // Series
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        series.setName("graf");

        for (int i = 0; i < tabulatedFunction.getCount(); i++) {
            series.getData().add(new XYChart.Data<Number, Number>(tabulatedFunction.getX(i), tabulatedFunction.getY(i)));
        }
        newWindow.setTitle("Grafik");
        Scene scene = new Scene(areaChart, 400, 300);
        areaChart.getData().addAll(series);
        newWindow.setScene(scene);
        newWindow.show();
    }
}
