package ru.ssau.tk.sergunin.lab.ui;

import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.Style;
import javafx.css.StyleableProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static java.lang.String.format;

@ConnectableItem(name = "Plot", type = Item.CONTROLLER, pathFXML = "plot.fxml")
public class PlotController implements Initializable, Openable {
    private final int NUMBER_OF_METHOD = 41;

    private Stage stage;
    @FXML
    private StackPane stackPane;
    @FXML
    private LineChart<Double, Double> lineChart;
    private final Map<TabulatedFunction, Color> functionColorMap = new HashMap<>();
    private AnchorPane detailsWindow;
    private Openable parentController;
    private PlotController.DetailsPopup detailsPopup;
    private double strokeWidth = 1;
    private int numberOfSeries = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailsWindow = new AnchorPane();
        lineChart.setCreateSymbols(false);
        bindMouseEvents(lineChart, this.strokeWidth);
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

    public void addSeriesInGeneral(ObservableList<Point> data, TabulatedFunction function) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName(function.getName());
        lineChart.getData().add(series);
        removeLegend(lineChart);
        detailsPopup.addPopupRow(function);
        data.forEach(point -> series.getData().add(new XYChart.Data<>(point.x, point.y)));
        functionColorMap.put(function, getColorFromCSS(series));
        numberOfSeries++;
        ChartPanManager panner = new ChartPanManager(lineChart);
        panner.setMouseFilter(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                mouseEvent.consume();
            }
        });
        panner.start();
        JFXChartUtil.setupZooming(lineChart, mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY)
                mouseEvent.consume();
        });
    }

    public static void removeLegend(LineChart<Double, Double> lineChart) {
        ((Legend) lineChart.lookup(".chart-legend")).getItems().clear();
    }

    // хождение за два привата
    private Color getColorFromCSS(XYChart.Series<Double, Double> series) {
        Method getPrivate = series.getClass().getClass().getDeclaredMethods()[NUMBER_OF_METHOD];
        getPrivate.setAccessible(true);
        Method getStyleMap = null;
        Color color = null;
        try {
            Method[] methods = (Method[]) getPrivate.invoke(series.getNode().getClass().getSuperclass().getSuperclass(), false);
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("getStyleMap")) {
                    getStyleMap = methods[i];
                    break;
                }
            }
            getStyleMap.setAccessible(true);
            Object[] arrayOfListsOfStyles = ((ObservableMap<StyleableProperty<?>, List<Style>>) getStyleMap.invoke(series.getNode())).values().toArray();
            for (int i = 0; i < arrayOfListsOfStyles.length; i++) {
                Object value = ((Style) (((ArrayList<Style>) (arrayOfListsOfStyles[i])).toArray())[0]).getDeclaration().getParsedValue().getValue();
                if (value instanceof Color) {
                    color = (Color) value;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            AlertWindows.showError(e);
        }
        return color;
    }

    public void setSeriesInGeneral(ObservableList<Point> data, TabulatedFunction function) {
        lineChart.getData().clear();
        detailsPopup.clear();
        numberOfSeries = 0;
        addSeriesInGeneral(data, function);
    }

    public void addSeries() {
        addSeriesInGeneral(((TableController) parentController).getObservableList(), ((TableController) parentController).getFunction());
    }

    public void setSeries() {
        setSeriesInGeneral(((TableController) parentController).getObservableList(), ((TableController) parentController).getFunction());
    }

    private String toRGBCode(Color color) {
        return format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void bindMouseEvents(LineChart<Double, Double> baseChart, Double strokeWidth) {
        detailsPopup = new PlotController.DetailsPopup();
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

        yLine.setFill(Color.GREEN);
        xLine.setFill(Color.GREEN);
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

        private ObservableList<TabulatedFunction> functions = FXCollections.observableArrayList();

        private DetailsPopup() {
            setStyle("-fx-border-width: 1px; -fx-padding: 5 5 5 5px; -fx-border-color: gray; -fx-background-color: whitesmoke;");
            setVisible(false);
        }

        public void addPopupRow(TabulatedFunction function) {
            functions.add(function);
        }

        public void clear() {
            functions.clear();
        }

        public void showChartDescription(MouseEvent event) {
            getChildren().clear();
            double x = (Double) lineChart.getXAxis().getValueForDisplay(event.getX());

            for (TabulatedFunction function : functions) {
                HBox popupRow = buildPopupRow(event, x, function);
                getChildren().add(popupRow);
            }
        }

        private HBox buildPopupRow(MouseEvent event, double x, TabulatedFunction function) {
            Label seriesName = new Label(function.getName());
            seriesName.setTextFill(functionColorMap.get(function));

            double y = function.isMathFunctionExist()
                    ? function.getMathFunction().apply(x)
                    : function.apply(x);

            double yValueLower = normalizeYValue(lineChart, event.getY() - 2);
            double yValueUpper = normalizeYValue(lineChart, event.getY() + 2);
            double yValueUnderMouse = lineChart.getYAxis().getValueForDisplay(event.getY());

            // make series name bold when mouse is near given chart's line
            if (isMouseNearLine(y, yValueUnderMouse, Math.abs(yValueLower - yValueUpper))) {
                seriesName.setStyle("-fx-font-weight: bold");
            }

            HBox popupRow = new HBox(10, seriesName, new Label("x: [" + x + "]\ny: [" + y + "]"));
            return popupRow;
        }

        private double normalizeYValue(LineChart<Double, Double> lineChart, double value) {
            return lineChart.getYAxis().getValueForDisplay(value);
        }

        private boolean isMouseNearLine(Double realYValue, Double yValueUnderMouse, Double tolerance) {
            return (Math.abs(yValueUnderMouse - realYValue) < tolerance);
        }
    }

    public int getNumberOfSeries() {
        return numberOfSeries;
    }


}
