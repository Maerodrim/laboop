package ru.ssau.tk.itenion.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.functions.AbsFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.labNumericalMethods.lab2.Approximation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static ru.ssau.tk.itenion.ui.Initializer.initializeMap;

@ConnectableItem(name = "Approximation", type = Item.CONTROLLER, pathFXML = "approximation.fxml")
public class ApproximationController implements TFTabVisitor, PlotAccessible, Initializable, OpenableWindow {
    private Stage stage;
    private Map<String, Method> approximationStrategies;
    private Map<Method, Class<?>> classes;
    private Map<String, TabulatedFunction> functions;
    private List<Label> polynomialAccuracyLabels;
    private List<Label> splineAccuracyLabels;
    private int numberOfPointsForApproximation;
    private Map<String, TabulatedFunction> sourceFunctions;
    @FXML
    private Label lEps0, lEps1, lEps2, lEps3, sEps0, sEps1, sEps2, sEps3;
    @FXML
    ComboBox<String> approximationStrategyComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sourceFunctions = new HashMap<>();
        functions = new HashMap<>();
        polynomialAccuracyLabels = List.of(lEps0, lEps1, lEps2, lEps3);
        numberOfPointsForApproximation = polynomialAccuracyLabels.size();
        splineAccuracyLabels = List.of(sEps0, sEps1, sEps2, sEps3);
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
        approximationStrategyComboBox.getItems().add("Построить сплайны");
        approximationStrategyComboBox.getSelectionModel().selectFirst();
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        stage.setOnShown(windowEvent -> {
            state().accept(this);
            setAccuracy("Интерполяция полиномами Ньютона", polynomialAccuracyLabels);
        });
        this.stage = stage;
    }

    @FXML
    public void ok() {
        switch (approximationStrategyComboBox.getValue()) {
            case "Интерполяция полиномами Лагранжа":
            case "Интерполяция полиномами Ньютона":
            case "Построить полиномы Лагранжа и Ньютона":
                break;
            case "Построить полином и линейный сплайн":
                setAccuracy("Интерполяция линейным сплайном", splineAccuracyLabels);
                break;
            case "Построить полином и параболический сплайн":
                setAccuracy("Интерполяция параболическим сплайном", splineAccuracyLabels);
                break;
            case "Построить полином и кубический сплайн":
                setAccuracy("Интерполяция кубическим сплайном", splineAccuracyLabels);
                break;
            default:
                setAccuracy(approximationStrategyComboBox.getValue(), splineAccuracyLabels);
                break;
          }
    }

    private void setAccuracy(String name, List<Label> labels) {
        TabulatedFunction sourceFunction = sourceFunctions.get(name);
        double h = (sourceFunction.rightBound() - sourceFunction.leftBound()) / (numberOfPointsForApproximation - 1);
        double[] yPoints = new double[numberOfPointsForApproximation];
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = sourceFunction.apply(sourceFunction.leftBound() + i * h);
        }
        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setText("" + round(Math.abs(yPoints[i] - functions.get(name).apply(sourceFunction.leftBound() + i * h)), 10));
        }
    }

    @FXML
    public void plotApproximatedFunction() {
        try {
            switch (approximationStrategyComboBox.getValue()) {
                case "Построить полиномы Лагранжа и Ньютона":
                    plotAllFunctions(
                            "Интерполяция полиномами Лагранжа",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить полином и линейный сплайн":
                    plotAllFunctions(
                            "Интерполяция линейным сплайном",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить полином и параболический сплайн":
                    plotAllFunctions(
                            "Интерполяция параболическим сплайном",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить полином и кубический сплайн":
                    plotAllFunctions(
                            "Интерполяция кубическим сплайном",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить сплайны":
                    plotAllFunctions(
                            "Интерполяция линейным сплайном",
                            "Интерполяция параболическим сплайном",
                            "Интерполяция кубическим сплайном");
                    break;
                default:
                    anyState().getPlotController().setSeries(functions.get(approximationStrategyComboBox.getSelectionModel().getSelectedItem()));
                    anyState().getPlotController().addSeries(sourceFunctions.get(approximationStrategyComboBox.getSelectionModel().getSelectedItem()));
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
            switch (approximationStrategyComboBox.getValue()) {
                case "Построить полиномы Лагранжа и Ньютона":
                    plotAllAccuracyFunctions(
                            "Интерполяция полиномами Лагранжа",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить полином и линейный сплайн":
                    plotAllAccuracyFunctions(
                            "Интерполяция линейным сплайном",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить полином и параболический сплайн":
                    plotAllAccuracyFunctions(
                            "Интерполяция параболическим сплайном",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить полином и кубический сплайн":
                    plotAllAccuracyFunctions(
                            "Интерполяция кубическим сплайном",
                            "Интерполяция полиномами Ньютона");
                    break;
                case "Построить сплайны":
                    plotAllAccuracyFunctions(
                            "Интерполяция линейным сплайном",
                            "Интерполяция параболическим сплайном",
                            "Интерполяция кубическим сплайном");
                    break;
                default:
                    TabulatedFunction sourceFunction = sourceFunctions.get(approximationStrategyComboBox.getSelectionModel().getSelectedItem());
                    TabulatedFunction approximatedFunction = functions.get(approximationStrategyComboBox.getSelectionModel().getSelectedItem());
                    double h = (sourceFunction.rightBound() - sourceFunction.leftBound()) / (numberOfPointsForApproximation - 1);
                    double[] xPoints = new double[numberOfPointsForApproximation];
                    for (int i = 0; i < xPoints.length; i++) {
                        xPoints[i] = sourceFunction.leftBound() + i * h;
                    }
                    MathFunction[] accuracyFunction = new MathFunction[1];
                    state().accept((TFTabVisitor) tfState -> accuracyFunction[0] = new MathFunction() {
                        private static final long serialVersionUID = -6036766437877741124L;

                        @Override
                        public double apply(double x) {
                            return 42170.4 * Math.abs(Approximation.w(4, x, xPoints)) / factorial(numberOfPointsForApproximation);
                            // 42170.4 is the maximum of the function considered in the laboratory work
                        }


                        @Override
                        public MathFunction differentiate() {
                            return null;
                        }

                        @Override
                        public String getName() {
                            return "Теоретический максимум";
                        }
                    });
                    anyState().getPlotController().setSeries(anyState().getFactory()
                            .create(new AbsFunction().andThen(approximatedFunction.subtract(sourceFunction)), sourceFunction.leftBound(), sourceFunction.rightBound(), sourceFunction.getCount()));
//                    anyState().getPlotController().setSeries(anyState().getFactory()
//                            .create(accuracyFunction[0], sourceFunction.leftBound(), sourceFunction.rightBound(), sourceFunction.getCount()));
                    break;
            }
            anyState().getPlotController().getStage().show();
        } catch (NullPointerException e) {
            AlertWindows.showWarning("Nothing is selected");
        }
    }

    private void plotAllFunctions(String... names) {
        plotAllFunctions(Arrays.stream(names).map(value -> functions.get(value)).collect(Collectors.toList()));
    }

    private void plotAllAccuracyFunctions(String... names) {
        List<TabulatedFunction> accuracyFunctions = new ArrayList<>();
        for (String name : names) {
            TabulatedFunction sourceFunction = sourceFunctions.get(name);
            accuracyFunctions.add(anyState().getFactory()
                    .create(
                            new AbsFunction().andThen(functions.get(name).subtract(sourceFunction)),
                            sourceFunction.leftBound(),
                            sourceFunction.rightBound(),
                            sourceFunction.getCount()));
        }
        boolean isFirst = true;
        for (TabulatedFunction function : accuracyFunctions) {
            if (isFirst) {
                anyState().getPlotController().setSeries(anyState().getFactory()
                        .create(function, function.leftBound(), function.rightBound(), function.getCount()));
                isFirst = false;
            } else {
                anyState().getPlotController().addSeries(anyState().getFactory()
                        .create(function, function.leftBound(), function.rightBound(), function.getCount()));
            }
        }
    }

    private void plotAllFunctions(TabulatedFunction... functions) {
        plotAllFunctions(new ArrayList<>(Arrays.asList(functions)));
    }

    private void plotAllFunctions(List<TabulatedFunction> functions) {
        state().accept((TFTabVisitor) tfState -> anyState().getPlotController().setSeries(tfState.getFunction()));
        functions.forEach(function -> anyState().getPlotController().addSeries(anyState().getFactory()
                .create(function, function.leftBound(), function.rightBound(), function.getCount())));
    }

    @Override
    public void visit(TabController.TFState tfState) {
        approximationStrategies.keySet().forEach(value -> sourceFunctions.put(value, tfState.getFunction().copy()));
        approximationStrategies.keySet()
                .forEach(value -> {
                    startApproximation(value);
                    if ((value.equals("Интерполяция полиномами Лагранжа") || value.equals("Интерполяция полиномами Ньютона")) && isInvalidError(value)) {
                        cropUntilNotProperlyError(value);
                    }
                });
    }

    private void cropUntilNotProperlyError(String value) {
        int i = 0;
        do {
            removeBoundPoints(sourceFunctions.get(value), i++);
            startApproximation(value);
        } while (isInvalidError(value));
    }

    private void removeBoundPoints(TabulatedFunction function, int iterationNumber) {
        if (iterationNumber % 3 == 0) {
            function.remove(0);
        }
        function.remove(function.getCount() - 1);
    }

    private boolean isInvalidError(String value) {
        double max = Double.MIN_VALUE;
        double yMax = 0;
        TabulatedFunction sourceFunction = sourceFunctions.get(value);
        MathFunction function = new AbsFunction().andThen(functions.get(value).subtract(sourceFunction));
        for (Point point : sourceFunction) {
            if (function.apply(point.x) > max) {
                max = function.apply(point.x);
                yMax = sourceFunction.apply(point.x);
            }
        }
        return Math.abs(max / yMax) > 1E-4;
    }

    private void startApproximation(String nameOfApproximationStrategy) {
        functions.remove(nameOfApproximationStrategy);
        TabulatedFunction sourceFunction = sourceFunctions.get(nameOfApproximationStrategy);
        double left = sourceFunction.leftBound();
        double right = sourceFunction.rightBound();
        double h = (right - left) / (numberOfPointsForApproximation - 1);
        double[] yPoints = new double[numberOfPointsForApproximation];
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = sourceFunction.apply(sourceFunction.leftBound() + i * h);
        }
        try {
            MathFunction function = ((MathFunction) approximationStrategies.get(nameOfApproximationStrategy).invoke(classes.get(approximationStrategies.get(nameOfApproximationStrategy))
                    .getDeclaredConstructor(Double.TYPE, Double.TYPE, Integer.TYPE)
                    .newInstance(left, right, numberOfPointsForApproximation), yPoints));
            functions.put(
                    nameOfApproximationStrategy, anyState().getFactory()
                            .create(function, left, right, sourceFunction.getCount()));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int factorial(int n) {
        int result = 1;
        for (int i = 1; i <= n; ++i) {
            result *= i;
        }
        return result;
    }
}
