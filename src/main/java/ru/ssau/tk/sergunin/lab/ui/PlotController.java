package ru.ssau.tk.sergunin.lab.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.testAxes.MultipleAxesLineChart;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@ConnectableItem(name = "Plot", type = Item.CONTROLLER, pathFXML = "plot.fxml")
public class PlotController implements Initializable, Openable {
    private Stage stage;
    @FXML
    private StackPane stackPane;
    @FXML
    private LineChart<Number, Number> baseChart;
    private AnchorPane detailsWindow;
    private Openable parentController;
    private double strokeWidth = 0.3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailsWindow = new AnchorPane();
        baseChart.setCreateSymbols(false);
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
        parentController = controller;
    }

    public void addSeries(ObservableList<Point> data, String name, String color) {
        stackPane.getChildren().add(baseChart);
        baseChart.setStyle("CHART_COLOR_1: " + color + " ;");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        baseChart.getData().add(series);
        series.setName(name);
        data.forEach(point -> series.getData().add(new XYChart.Data<>(point.x, point.y)));
        baseChart.lookup(".series" + 0).setStyle("-fx-stroke: " + color + "; -fx-stroke-width: 1;");
        bindMouseEvents(baseChart, ((TableController)parentController).getFunction(), this.strokeWidth);
    }

    public void setSeries(ObservableList<Point> data, String name, String color) {
        stackPane.getChildren().clear();
        baseChart.getData().clear();
        addSeries(data, name, color);
    }

    private void bindMouseEvents(LineChart<Number, Number> baseChart, TabulatedFunction baseFunction, Double strokeWidth) {
        final PlotController.DetailsPopup detailsPopup = new PlotController.DetailsPopup(baseFunction);
        stackPane.getChildren().add(detailsWindow);
        detailsWindow.getChildren().add(detailsPopup);
        detailsWindow.prefHeightProperty().bind(stackPane.heightProperty());
        detailsWindow.prefWidthProperty().bind(stackPane.widthProperty());
        detailsWindow.setMouseTransparent(true);

        stackPane.setOnMouseMoved(null);
        stackPane.setMouseTransparent(false);

        final Axis xAxis = baseChart.getXAxis();
        final Axis yAxis = baseChart.getYAxis();

        final Line xLine = new Line();
        final Line yLine = new Line();
        yLine.setFill(Color.GRAY);
        xLine.setFill(Color.GRAY);
        yLine.setStrokeWidth(strokeWidth / 2);
        xLine.setStrokeWidth(strokeWidth / 2);
        xLine.setVisible(false);
        yLine.setVisible(false);

        final Node chartBackground = baseChart.lookup(".chart-plot-background");
        for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }
        chartBackground.setCursor(Cursor.CROSSHAIR);
        chartBackground.setOnMouseEntered((event) -> {
            chartBackground.getOnMouseMoved().handle(event);
            detailsPopup.setVisible(true);
            xLine.setVisible(true);
            yLine.setVisible(true);
            detailsWindow.getChildren().addAll(xLine, yLine);
        });
        chartBackground.setOnMouseExited((event) -> {
            detailsPopup.setVisible(false);
            xLine.setVisible(false);
            yLine.setVisible(false);
            detailsWindow.getChildren().removeAll(xLine, yLine);
        });
        chartBackground.setOnMouseMoved(event -> {
            double x = event.getX() + chartBackground.getLayoutX();
            double y = event.getY() + chartBackground.getLayoutY();

            double a = 10;
            double b = 5;

            xLine.setStartX(a);
            xLine.setEndX(detailsWindow.getWidth() - a);
            xLine.setStartY(y + b);
            xLine.setEndY(y + b);

            yLine.setStartX(x + b);
            yLine.setEndX(x + b);
            yLine.setStartY(a);
            yLine.setEndY(detailsWindow.getHeight() - a);

            detailsPopup.showChartDescription(event);

            if (y + detailsPopup.getHeight() + a < stackPane.getHeight()) {
                AnchorPane.setTopAnchor(detailsPopup, y + a);
            } else {
                AnchorPane.setTopAnchor(detailsPopup, y - a - detailsPopup.getHeight());
            }

            if (x + detailsPopup.getWidth() + a < stackPane.getWidth()) {
                AnchorPane.setLeftAnchor(detailsPopup, x + a);
            } else {
                AnchorPane.setLeftAnchor(detailsPopup, x - a - detailsPopup.getWidth());
            }
        });
    }

    private class DetailsPopup extends VBox {

        private TabulatedFunction function;

        private DetailsPopup(TabulatedFunction function) {
            this.function = function;
            setStyle("-fx-border-width: 1px; -fx-padding: 5 5 5 5px; -fx-border-color: gray; -fx-background-color: whitesmoke;");
            setVisible(false);
        }

        public void showChartDescription(MouseEvent event) {
            getChildren().clear();
            Number xValueLong = baseChart.getXAxis().getValueForDisplay(event.getX());

            HBox baseChartPopupRow = buildPopupRow(event, xValueLong, baseChart);
            if (baseChartPopupRow != null) {
                getChildren().add(baseChartPopupRow);
            }

            /*for (LineChart lineChart : backgroundCharts) {
                HBox popupRow = buildPopupRow(event, xValueLong, lineChart);
                if (popupRow == null) continue;

                getChildren().add(popupRow);
            }*/
        }

        private HBox buildPopupRow(MouseEvent event, Number xValueLong, LineChart lineChart) {
            Label seriesName = new Label(function.getName());
            //Label seriesName = new Label(lineChart.getYAxis().getLabel());
            //seriesName.setTextFill(chartColorMap.get(lineChart));

            Number yValueForChart = getYValueForX(lineChart, xValueLong.doubleValue());
            if (yValueForChart == null) {
                return null;
            }
            /*Number yValueLower = Math.round(normalizeYValue(lineChart, event.getY() - 10));
            Number yValueUpper = Math.round(normalizeYValue(lineChart, event.getY() + 10));
            Number yValueUnderMouse = Math.round((double) lineChart.getYAxis().getValueForDisplay(event.getY()));

            // make series name bold when mouse is near given chart's line
            if (isMouseNearLine(yValueForChart, yValueUnderMouse, Math.abs(yValueLower.doubleValue() - yValueUpper.doubleValue()))) {
                seriesName.setStyle("-fx-font-weight: bold");
            }*/

            HBox popupRow = new HBox(10, seriesName, new Label("[" + yValueForChart + "]"));
            return popupRow;
        }

        private double normalizeYValue(LineChart<Number, Number> lineChart, double value) {
            return lineChart.getYAxis().getValueForDisplay(value).doubleValue();
        }

        private boolean isMouseNearLine(Number realYValue, Number yValueUnderMouse, Double tolerance) {
            return (Math.abs(yValueUnderMouse.doubleValue() - realYValue.doubleValue()) < tolerance);
        }

        public Number getYValueForX(LineChart<Double, Double> chart, Double xValue) {
//            List<XYChart.Data> dataList = ((List<XYChart.Data>) ((XYChart.Series) chart.getData().get(0)).getData());
//            for (XYChart.Data data : dataList) {
//                if (Math.abs((Double)data.getXValue() - xValue.doubleValue()) < 1E-6 ) {
//                    return (Number) data.getYValue();
//                }
//            }
//            return null;
            if (Objects.isNull(function.getMathFunction())) {
                return function.apply(xValue);
            } else {
                return function.getMathFunction().apply(xValue);
            }
        }
    }

}
