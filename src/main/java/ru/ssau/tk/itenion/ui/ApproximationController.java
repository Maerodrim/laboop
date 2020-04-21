package ru.ssau.tk.itenion.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.AbsFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.labNumericalMethods.lab2.Approximation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;

import static ru.ssau.tk.itenion.ui.Initializer.initializeMap;

@ConnectableItem(name = "Approximation", type = Item.CONTROLLER, pathFXML = "approximation.fxml")
public class ApproximationController implements TFTabVisitor, PlotAccessible, Initializable, OpenableWindow {
    private Stage stage;
    private Map<String, Method> approximationStrategies;
    private Map<Method, Class<?>> classes;
    private Map<String, TabulatedFunction> functions;
    private TabulatedFunction function;
    private List<Double> yPoints = new ArrayList<>();
    private List<Label> polynomialAccuracyLabels;
    private List<Label> splineAccuracyLabels;
    private double[] xPoints;
    private double left, right, h;
    private int numberOfPointsForPlot;
    @FXML
    private Label lEps0, lEps1, lEps2, lEps3, sEps0, sEps1, sEps2, sEps3;
    @FXML
    ComboBox<String> approximationStrategyComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functions = new HashMap<>();
        polynomialAccuracyLabels = List.of(lEps0, lEps1, lEps2, lEps3);
        splineAccuracyLabels = List.of(sEps0, sEps1, sEps2, sEps3);
        xPoints = new double[polynomialAccuracyLabels.size()];
        approximationStrategies = new LinkedHashMap<>();
        classes = new LinkedHashMap<>();
        Map[] maps = initializeMap(classes, approximationStrategies, Item.APPROXIMATION_NUMERICAL_METHOD);
        classes = (Map<Method, Class<?>>) maps[0];
        approximationStrategies = (Map<String, Method>) maps[1];
        approximationStrategyComboBox.getItems().setAll(approximationStrategies.keySet());
        approximationStrategyComboBox.getItems().add("Построить полиномы Лагранжа и Ньютона");
        approximationStrategyComboBox.getItems().add("Построить полином и линейный сплайн");
        approximationStrategyComboBox.getItems().add("Построить полином и параболический сплайн");
        approximationStrategyComboBox.getItems().add("Построить полином и кубический сплайн");
        approximationStrategyComboBox.getSelectionModel().selectFirst();
    }

    private void setAccuracy(TabulatedFunction function, List<Label> labels) {
        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setText("" +
                    round(Math.abs(yPoints.get(i) - function.apply(10E-12 + i * h)), 16));
        }
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        stage.setOnShown(windowEvent -> {
            state().accept(this);
            setAccuracy(function, polynomialAccuracyLabels);
        });
        this.stage = stage;
    }

    @FXML
    public void ok() {
        switch (approximationStrategyComboBox.getValue()) {
            case "Построить полиномы Лагранжа и Ньютона":
                break;
            case "Построить полином и линейный сплайн":
                setAccuracy(functions.get("Интерполяция линейным сплайном"), splineAccuracyLabels);
                break;
            case "Построить полином и параболический сплайн":
                setAccuracy(functions.get("Интерполяция параболическим сплайном"), splineAccuracyLabels);
                break;
            case "Построить полином и кубический сплайн":
                setAccuracy(functions.get("Интерполяция кубическим сплайном"), splineAccuracyLabels);
                break;
            case "Интерполяция линейным сплайном":
                setAccuracy(functions.get("Интерполяция линейным сплайном"), splineAccuracyLabels);
                state().accept(this);
                break;
            case "Интерполяция параболическим сплайном":
                setAccuracy(functions.get("Интерполяция параболическим сплайном"), splineAccuracyLabels);
                state().accept(this);
                break;
            case "Интерполяция кубическим сплайном":
                setAccuracy(functions.get("Интерполяция кубическим сплайном"), splineAccuracyLabels);
                state().accept(this);
                break;
            default:
                state().accept(this);
                break;
        }
    }

    @FXML
    public void plotApproximatedFunction() {
        try {
            switch (approximationStrategyComboBox.getValue()) {
                case "Построить полиномы Лагранжа и Ньютона":
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция полиномами Лагранжа"), left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция полиномами Ньютона"), left, right, numberOfPointsForPlot));
                    state().accept((TFTabVisitor) tfState -> anyState().getPlotController().addSeries(tfState.getFunction()));
                    break;
                case "Построить полином и линейный сплайн":
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция линейным сплайном"), left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция полиномами Ньютона"), left, right, numberOfPointsForPlot));
                    state().accept((TFTabVisitor) tfState -> anyState().getPlotController().addSeries(tfState.getFunction()));
                    break;
                case "Построить полином и параболический сплайн":
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция параболическим сплайном"), left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция полиномами Ньютона"), left, right, numberOfPointsForPlot));
                    state().accept((TFTabVisitor) tfState -> anyState().getPlotController().addSeries(tfState.getFunction()));
                    break;
                case "Построить полином и кубический сплайн":
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция кубическим сплайном"), left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(functions.get("Интерполяция полиномами Ньютона"), left, right, numberOfPointsForPlot));
                    state().accept((TFTabVisitor) tfState -> anyState().getPlotController().addSeries(tfState.getFunction()));
                    break;
                default:
                    anyState().getPlotController().setSeries(function);
                    state().accept((TFTabVisitor) tfState -> anyState().getPlotController().addSeries(tfState.getFunction()));
                    break;
            }
            anyState().getPlotController().getStage().show();
        } catch (NullPointerException e) {
            AlertWindows.showWarning("Nothing is selected");
        }
    }

    @FXML
    public void plotAccuracy() {
        try {
            final MathFunction[] accuracyFunction = new MathFunction[2];
            switch (approximationStrategyComboBox.getValue()) {
                case "Построить полиномы Лагранжа и Ньютона":
                    state().accept((TFTabVisitor) tfState -> {
                        accuracyFunction[0] = new AbsFunction()
                                .andThen(functions.get("Интерполяция полиномами Лагранжа").subtract(tfState.getFunction()));
                        accuracyFunction[1] = new AbsFunction()
                                .andThen(functions.get("Интерполяция полиномами Ньютона").subtract(tfState.getFunction()));
                    });
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(accuracyFunction[0], left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(accuracyFunction[1], left, right, numberOfPointsForPlot));
                    break;
                case "Построить полином и линейный сплайн":
                    state().accept((TFTabVisitor) tfState -> {
                        accuracyFunction[0] = new AbsFunction()
                                .andThen(functions.get("Интерполяция линейным сплайном").subtract(tfState.getFunction()));
                        accuracyFunction[1] = new AbsFunction()
                                .andThen(functions.get("Интерполяция полиномами Ньютона").subtract(tfState.getFunction()));
                    });
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(accuracyFunction[0], left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(accuracyFunction[1], left, right, numberOfPointsForPlot));
                    break;
                case "Построить полином и параболический сплайн":
                    state().accept((TFTabVisitor) tfState -> {
                        accuracyFunction[0] = new AbsFunction()
                                .andThen(functions.get("Интерполяция параболическим сплайном").subtract(tfState.getFunction()));
                        accuracyFunction[1] = new AbsFunction()
                                .andThen(functions.get("Интерполяция полиномами Ньютона").subtract(tfState.getFunction()));
                    });
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(accuracyFunction[0], left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(accuracyFunction[1], left, right, numberOfPointsForPlot));
                    break;
                case "Построить полином и кубический сплайн":
                    state().accept((TFTabVisitor) tfState -> {
                        accuracyFunction[0] = new AbsFunction()
                                .andThen(functions.get("Интерполяция кубическим сплайном").subtract(tfState.getFunction()));
                        accuracyFunction[1] = new AbsFunction()
                                .andThen(functions.get("Интерполяция полиномами Ньютона").subtract(tfState.getFunction()));
                    });
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(accuracyFunction[0], left, right, numberOfPointsForPlot));
                    anyState().getPlotController().addSeries(anyState().getFactory()
                            .create(accuracyFunction[1], left, right, numberOfPointsForPlot));
                    break;
                default:
                    state().accept((TFTabVisitor) tfState -> {
                        accuracyFunction[0] = new AbsFunction()
                                .andThen(function.subtract(tfState.getFunction()));
                        accuracyFunction[1] = new MathFunction() {
                            private static final long serialVersionUID = -6036766437877741124L;

                            @Override
                            public double apply(double x) {
                                return 42170.4 * Math.abs(Approximation.w(4, x, xPoints)) / factorial(polynomialAccuracyLabels.size());
                            }

                            @Override
                            public MathFunction differentiate() {
                                return null;
                            }

                            @Override
                            public String getName() {
                                return "Теоретический максимум";
                            }
                        };
                    });
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(accuracyFunction[0], left, right, numberOfPointsForPlot));
//          anyState().getPlotController().setSeries(anyState().getFactory()
//                    .create(accuracyFunction[1], function.leftBound(), function.rightBound(), function.getCount()));
                    break;
            }
            anyState().getPlotController().getStage().show();
        } catch (NullPointerException e) {
            AlertWindows.showWarning("Nothing is selected");
        }
    }

    @Override
    public void visit(TabController.TFState tfState) {
        left = tfState.getFunction().leftBound();
        right = tfState.getFunction().rightBound();
        numberOfPointsForPlot = tfState.getFunction().getCount();
        h = (right - left) / (polynomialAccuracyLabels.size() - 1);
        for (int i = 0; i < polynomialAccuracyLabels.size(); i++) {
            xPoints[i] = tfState.getFunction().leftBound() + i * h;
        }
        for (int i = 0; i < polynomialAccuracyLabels.size(); i ++) {
            yPoints.add(tfState.getFunction().apply(10E-12 + i * h));
        }
        double[] yPointsArray = new double[polynomialAccuracyLabels.size()];
        for (int i = 0; i < yPointsArray.length; i++) {
            yPointsArray[i] = yPoints.get(i);
        }
        approximationStrategies.keySet().forEach(value -> {
            try {
                functions.put(
                        value, (TabulatedFunction)
                                approximationStrategies.get(value).invoke(classes.get(approximationStrategies.get(value))
                                                .getDeclaredConstructor(Double.TYPE, Double.TYPE, Integer.TYPE, TabulatedFunctionFactory.class)
                                                .newInstance(tfState.getFunction().leftBound(),
                                                        tfState.getFunction().rightBound(),
                                                        polynomialAccuracyLabels.size(),
                                                        anyState().getFactory()),
                                        yPointsArray
                                ));
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        function = functions.get(approximationStrategyComboBox.getValue());
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int factorial(int n) {
        int ret = 1;
        for (int i = 1; i <= n; ++i) ret *= i;
        return ret;
    }
}
