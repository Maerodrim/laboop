package ru.ssau.tk.itenion.ui;

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
import ru.ssau.tk.itenion.enums.SupportedSign;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.numericalMethods.NumericalMethods;
import ru.ssau.tk.itenion.operations.TabulatedFunctionOperationService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static java.lang.String.format;

@ConnectableItem(name = "Plot", type = Item.CONTROLLER, pathFXML = "plot.fxml")
public class PlotController implements TabVisitor, FactoryAccessible, Initializable, OpenableWindow {
    private final Map<MathFunction, Color> functionColorMap = new HashMap<>();
    private Stage stage;
    @FXML
    private StackPane stackPane;
    @FXML
    private LineChart<Number, Number> lineChart;
    private AnchorPane detailsWindow;
    private PlotController.DetailsPopup detailsPopup;
    private double strokeWidth = 0.5;
    NumericalMethods numMethod = IO.getNumericalMethodFactory().create(0., 0., new double[]{0,0}, 1E-6);

    public static void removeLegend(LineChart<Number, Number> lineChart) {
        ((Legend) lineChart.lookup(".chart-legend")).getItems().clear();
    }

    private static String toRGBCode(Color color) {
        return format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailsWindow = new AnchorPane();
        lineChart.setCreateSymbols(false);
        bindMouseEvents(lineChart, this.strokeWidth);
        ChartPanManager panner = new ChartPanManager(lineChart);
        panner.setMouseFilter(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY) {
                mouseEvent.consume();
            }
        });
        panner.start();
        JFXChartUtil.addDoublePrimaryClickAutoRangeHandler(lineChart);
        JFXChartUtil.setupZooming(lineChart, mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY)
                mouseEvent.consume();
        });
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addSeries(ObservableList<XYChart.Data<Number, Number>> data, TabulatedFunction function, Variable variable) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setData(data);
        series.setName(function.getName());
        lineChart.getData().add(series);
        removeLegend(lineChart);
        functionColorMap.putIfAbsent(function.getMathFunction(), getColorFromCSS(series));
        detailsPopup.addPopupRow(function, variable);
    }

    public void setSeries(ObservableList<XYChart.Data<Number, Number>> data, TabulatedFunction function, Variable variable) {
        lineChart.getData().clear();
        detailsPopup.clear();
        addSeries(data, function, variable);
    }

    public void setSeries() {
        state().accept(this);
    }

    @Override
    public void visit(TabController.TFState tfState) {
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        setSeries(
                asSeriesData(tfState.getFunction(), Variable.x),
                tfState.getFunction(), Variable.x);
    }

    @Override
    public void visit(TabController.VMFState vmfState) {
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        if (vmfState.getFunction().isCanBePlotted()) {
            Map<Variable, List<TabulatedFunction>> functions = new HashMap<>();
            vmfState.getFunction().getIndexForPlot().ifPresent(matrix -> {
                lineChart.getData().clear();
                detailsPopup.clear();
                TabulatedFunction tabulatedFunction;
                for (int i = 0; i < 2; i++) {
                    Variable variable = Variable.values()[(int) matrix.get(i, 0)];
                    MathFunction function = vmfState.getFunction().getMathFunction(variable, i);
                    if (vmfState.getFunction().getGeneratedSigns(i).get(0).equals(SupportedSign.SUM)){
                        function = function.negate();
                    }
                    tabulatedFunction = tabulate(function);
                    addSeries(asSeriesData(tabulatedFunction, variable), tabulatedFunction, variable);
                }
            });
        } else {
            AlertWindows.showWarning("Unsupported operation");
        }
    }

    private ObservableList<XYChart.Data<Number, Number>> asSeriesData(TabulatedFunction function, Variable variable) {
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        switch (variable) {
            case x: {
                for (Point point : function) {
                    data.add(new XYChart.Data<>(point.x, point.y));
                }
                break;
            }
            case y: {
                forInverseSeriesObservableList(function).forEach(point -> data.add(new XYChart.Data<>(point.x, point.y)));
                break;
            }
        }
        return data;
    }

    public ObservableList<Point> forInverseSeriesObservableList(TabulatedFunction function) {
        List<Point> listPoints = TabulatedFunctionOperationService.asSymmetricalWithRespectToTheYAxis(function);
        List<Point> newPoints = new ArrayList<>();
        for (Point point : listPoints) {
            newPoints.add(new Point(point.y, -point.x));
        }
        newPoints.sort(Comparator.comparingDouble(point -> point.y));
        return FXCollections.observableArrayList(newPoints);
    }

    private TabulatedFunction tabulate(MathFunction function){
        return factory().create(function, -10, 10, 1001);
    }

    // хождение за два привата
    private Color getColorFromCSS(XYChart.Series<Number, Number> series) {
        Method[] methods = series.getClass().getClass().getDeclaredMethods();
        Method getPrivate = null;
        for (Method method : methods) {
            if (method.getName().equals("privateGetDeclaredMethods")) {
                getPrivate = method;
                break;
            }
        }
        assert getPrivate != null;
        getPrivate.setAccessible(true);
        Method getStyleMap = null;
        Color color = null;
        try {
            methods = (Method[]) getPrivate.invoke(series.getNode().getClass().getSuperclass().getSuperclass(), false);
            for (Method method : methods) {
                if (method.getName().equals("getStyleMap")) {
                    getStyleMap = method;
                    break;
                }
            }
            assert getStyleMap != null;
            getStyleMap.setAccessible(true);
            Object[] arrayOfListsOfStyles = ((ObservableMap<StyleableProperty<?>, List<Style>>) getStyleMap.invoke(series.getNode())).values().toArray();
            for (Object arrayOfListsOfStyle : arrayOfListsOfStyles) {
                Object value = ((Style) (((ArrayList<Style>) arrayOfListsOfStyle).toArray())[0]).getDeclaration().getParsedValue().getValue();
                if (value instanceof Color) {
                    color = (Color) value;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            AlertWindows.showError(e);
        }
        return color;
    }

    private void bindMouseEvents(LineChart<Number, Number> baseChart, Double strokeWidth) {
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

        private ArrayList<HBox> onlySeriesNamePopupRows = new ArrayList<>();
        private ObservableList<TabulatedFunction> functions = FXCollections.observableArrayList();


        private DetailsPopup() {
            setStyle("-fx-border-width: 1; -fx-padding: 5 5 5 5; -fx-border-color: gray; -fx-background-color: whitesmoke;");
            setVisible(false);
        }

        public void addPopupRow(TabulatedFunction function, Variable variable) {
            if (!functions.contains(function)) {
                switch (variable) {
                    case x: {
                        functions.add(function);
                        break;
                    }
                    case y: {
                        Label onlySeriesName = new Label();
                        onlySeriesName.setTextFill(functionColorMap.get(function.getMathFunction()));
                        onlySeriesName.setText(function.getName().replaceAll("x","y"));
                        onlySeriesNamePopupRows.add(new HBox(10, onlySeriesName, new Label("Not a function")));
                        break;
                    }
                }
            }
        }

        public void clear() {
            functions.clear();
            onlySeriesNamePopupRows.clear();
        }

        public void showChartDescription(MouseEvent event) {
            getChildren().clear();
            double x = lineChart.getXAxis().getValueForDisplay(event.getX()).doubleValue();

            for (TabulatedFunction function : functions) {
                HBox popupRow = buildPopupRow(event, x, function);
                getChildren().add(popupRow);
            }
            for (HBox onlySeriesNamePopupRow : onlySeriesNamePopupRows) {
                getChildren().add(onlySeriesNamePopupRow);
            }
        }

        private HBox buildPopupRow(MouseEvent event, double x, TabulatedFunction function) {
            Label seriesName = new Label(function.getName());
            seriesName.setTextFill(functionColorMap.get(function.getMathFunction()));

            double y = function.isMathFunctionExist()
                    ? function.getMathFunction().apply(x)
                    : function.apply(x);

            double yValueLower = normalizeYValue(lineChart, event.getY() - 2);
            double yValueUpper = normalizeYValue(lineChart, event.getY() + 2);
            double yValueUnderMouse = lineChart.getYAxis().getValueForDisplay(event.getY()).doubleValue();

            // make series name bold when mouse is near given chart's line
            if (isMouseNearLine(y, yValueUnderMouse, Math.abs(yValueLower - yValueUpper))) {
                seriesName.setStyle("-fx-font-weight: bold");
            }

            return new HBox(10, seriesName, new Label("x: [" + x + "]\ny: [" + y + "]"));
        }

        private double normalizeYValue(LineChart<Number, Number> lineChart, double value) {
            return lineChart.getYAxis().getValueForDisplay(value).doubleValue();
        }

        private boolean isMouseNearLine(Double realYValue, Double yValueUnderMouse, Double tolerance) {
            return (Math.abs(yValueUnderMouse - realYValue) < tolerance);
        }
    }


}
