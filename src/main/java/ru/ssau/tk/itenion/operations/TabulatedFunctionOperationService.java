package ru.ssau.tk.itenion.operations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import ru.ssau.tk.itenion.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ConnectableItem(name = "", type = Item.OPERATION)
public class TabulatedFunctionOperationService {

    private static final double ACCURACY = 1E-6;
    private TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionOperationService() {
        factory = new ArrayTabulatedFunctionFactory();
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] points = new Point[tabulatedFunction.getCount()];
        int i = 0;
        for (Point currentPoint : tabulatedFunction) {
            points[i++] = currentPoint;
        }
        return points;
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction, int from, int to) {
        Point[] points = new Point[to - from + 1];
        int i = 0;
        for (Point currentPoint : tabulatedFunction) {
            if (i == to) break;
            if (i >= from) {
                points[i++ - from] = currentPoint;
            } else {
                i++;
            }
        }
        return points;
    }

    static double[][][] asPoints(TabulatedFunction tabulatedFunction, int number) {
        double[][][] points = new double[number][tabulatedFunction.getCount() / number + 1][2];
        int step = tabulatedFunction.getCount() / number;
        int remainder = tabulatedFunction.getCount() % number;
        int i = 0;
        int j = 0;
        for (Point currentPoint : tabulatedFunction) {
            points[i][j][0] = currentPoint.x;
            points[i][j++][1] = currentPoint.y;
            if (j == step + (i >= number - remainder ? 1 : 0)) {
                i++;
                j = 0;
            }
        }
        return points;
    }

    public static void sort(ObservableList<Point> list) {
        list.sort(Comparator.comparingDouble(point -> point.x));
    }

    public static ObservableList<Point> forInverseOperatorObservableList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(new Point(point.y, point.x));
        }
        listPoint.sort(Comparator.comparingDouble(point -> point.y));
        return FXCollections.observableArrayList(listPoint);
    }

    public static ObservableList<Point> asObservableList(TabulatedFunction function) {
        List<Point> listPoint = new ArrayList<>();
        for (Point point : function) {
            listPoint.add(point);
        }
        return FXCollections.observableArrayList(listPoint);
    }

    public static ObservableList<XYChart.Data<Number, Number>> asSeriesData(TabulatedFunction function) {
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        for (Point point : function) {
            data.add(new XYChart.Data<>(point.x, point.y));
        }
        return data;
    }

    public static ObservableList<XYChart.Data<Number, Number>> asInverseSeriesData(TabulatedFunction function) {
        List<Point> points = new ArrayList<>();
        for (Point point : function) {
            points.add(new Point(point.y, -point.x));
        }
        points.sort(Comparator.comparingDouble(point -> point.y));
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        points.forEach(point -> data.add(new XYChart.Data<>(point.x, point.y)));
        return data;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    private TabulatedFunction doOperation(TabulatedFunction a, MathFunction b, BiOperation operation) {
        Point[] aPoints = asPoints(a);
        double[] xValues = new double[a.getCount()];
        double[] yValues = new double[a.getCount()];
        if (b instanceof TabulatedFunction) {
            if (a.getCount() != ((TabulatedFunction) b).getCount()) {
                throw new InconsistentFunctionsException("The number of points in the tabulated functions don't match");
            }
            aPoints = asPoints(a);
            Point[] bPoints = asPoints((TabulatedFunction) b);
            for (int i = 0; i < a.getCount(); i++) {
                if (Math.abs((aPoints[i].x - bPoints[i].x)) > ACCURACY) {
                    throw new InconsistentFunctionsException("The corresponding x values don't match");
                }
                xValues[i] = aPoints[i].x;
                yValues[i] = operation.apply(aPoints[i].y, bPoints[i].y);
            }
        } else {
            xValues = new double[a.getCount()];
            yValues = new double[a.getCount()];
            for (int i = 0; i < a.getCount(); i++) {
                xValues[i] = aPoints[i].x;
                yValues[i] = operation.apply(aPoints[i].y, b.apply(aPoints[i].x));
            }
        }
        return factory.create(xValues, yValues);
    }

    private TabulatedFunction doOperation(TabulatedFunction a, MathFunction b, String operation) {
        TabulatedFunction function = null;
        if (!a.isMathFunctionExist()) {
            switch (operation) {
                case "+": {
                    function = doOperation(a, b, Double::sum);
                    break;
                }
                case "-": {
                    function = doOperation(a, b, (u, v) -> u - v);
                    break;
                }
                case "*": {
                    function = doOperation(a, b, (u, v) -> u * v);
                    break;
                }
                case "/": {
                    function = doOperation(a, b, (u, v) -> u / v);
                    break;
                }
            }
        } else {
            MathFunction mathFunction = a.getMathFunction();
            switch (operation) {
                case "+": {
                    mathFunction = mathFunction.sum(b);
                    break;
                }
                case "-": {
                    mathFunction = mathFunction.subtract(b);
                    break;
                }
                case "*": {
                    mathFunction = mathFunction.multiply(b);
                    break;
                }
                case "/": {
                    mathFunction = mathFunction.divide(b);
                    break;
                }
            }
            function = factory.create(
                    mathFunction,
                    a.leftBound(), a.rightBound(),
                    a.getCount());
            function.setMathFunction(mathFunction);
        }
        return function;
    }

    @ConnectableItem(name = "+", priority = 1, type = Item.OPERATION)
    public TabulatedFunction sum(TabulatedFunction a, MathFunction b) {
        return doOperation(a, b, "+");
    }

    @ConnectableItem(name = "-", priority = 2, type = Item.OPERATION)
    public TabulatedFunction subtract(TabulatedFunction a, MathFunction b) {
        return doOperation(a, b, "-");
    }

    @ConnectableItem(name = "*", priority = 3, type = Item.OPERATION)
    public TabulatedFunction multiplication(TabulatedFunction a, MathFunction b) {
        return doOperation(a, b, "*");
    }

    @ConnectableItem(name = "/", priority = 4, type = Item.OPERATION)
    public TabulatedFunction division(TabulatedFunction a, MathFunction b) {
        return doOperation(a, b, "/");
    }

    @FunctionalInterface
    private interface BiOperation {
        double apply(double u, double v);
    }

}
