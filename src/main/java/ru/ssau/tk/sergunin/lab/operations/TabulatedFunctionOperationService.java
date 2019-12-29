package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.ui.Item;
import ru.ssau.tk.sergunin.lab.ui.SelectableItem;

@SelectableItem(name = "", type = Item.OPERATION)
public class TabulatedFunctionOperationService {

    private static final double ACCURACY = 1E-6;
    private TabulatedFunctionFactory factory;

    TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    TabulatedFunctionOperationService() {
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

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) {
        if (a.getCount() != b.getCount()) {
            throw new InconsistentFunctionsException("The number of points in the tabulated functions don't match");
        }
        Point[] aPoints = asPoints(a);
        Point[] bPoints = asPoints(b);
        double[] xValues = new double[a.getCount()];
        double[] yValues = new double[a.getCount()];
        for (int i = 0; i < a.getCount(); i++) {
            if (Math.abs((aPoints[i].x - bPoints[i].x)) > ACCURACY) {
                throw new InconsistentFunctionsException("The corresponding x values don't match");
            }
            xValues[i] = aPoints[i].x;
            yValues[i] = operation.apply(aPoints[i].y, bPoints[i].y);
        }
        return factory.create(xValues, yValues);
    }

    @SelectableItem(name = "+", priority = 1, type = Item.OPERATION)
    TabulatedFunction sum(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, Double::sum);
    }

    @SelectableItem(name = "-", priority = 2, type = Item.OPERATION)
    TabulatedFunction subtract(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u - v);
    }

    @SelectableItem(name = "*", priority = 3, type = Item.OPERATION)
    TabulatedFunction multiplication(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u * v);
    }

    @SelectableItem(name = "/", priority = 4, type = Item.OPERATION)
    TabulatedFunction division(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u / v);
    }

    @FunctionalInterface
    private interface BiOperation {
        double apply(double u, double v);
    }

}
