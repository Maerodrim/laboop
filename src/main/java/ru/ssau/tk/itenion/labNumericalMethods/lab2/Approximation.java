package ru.ssau.tk.itenion.labNumericalMethods.lab2;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ConnectableItem(name = "", type = Item.APPROXIMATION_NUMERICAL_METHOD)
public class Approximation {
    private static final int LINEAR = 1;
    private static final int PARABOLIC = 2;
    private static final int CUBIC = 3;
    private Map<Integer, String> names;
    double left;
    double right;
    int numberOfPoints;
    double[] xPoints;
    double h;
    TabulatedFunctionFactory factory;

    public Approximation(double left, double right, int numberOfPoints, TabulatedFunctionFactory factory) {
        this.left = left;
        this.right = right;
        this.numberOfPoints = numberOfPoints;
        this.factory = factory;
        xPoints = new double[numberOfPoints];
        h = (right - left) / (numberOfPoints - 1);
        for (int i = 0; i < numberOfPoints; i++) {
            xPoints[i] = left + i * h;
        }
        names = new HashMap<>();
        names.put(LINEAR, "Линейный сплайн");
        names.put(PARABOLIC, "Параболический сплайн");
        names.put(CUBIC, "Кубический сплайн");
    }

    @ConnectableItem(name = "Интерполяция полиномами Лагранжа", type = Item.APPROXIMATION_NUMERICAL_METHOD, priority = 1)
    public TabulatedFunction lagrangeTabulate(double[] yPoints) {
        return tabulate(new MathFunction() {
            private static final long serialVersionUID = -7502011778115269174L;

            @Override
            public double apply(double x) {
                return lagrange(yPoints, x);
            }

            @Override
            public MathFunction differentiate() {
                return null;
            }

            @Override
            public String getName() {
                return "Полином Лагранжа";
            }
        });
    }

    @ConnectableItem(name = "Интерполяция полиномами Ньютона", type = Item.APPROXIMATION_NUMERICAL_METHOD, priority = 2)
    public TabulatedFunction newtonTabulate(double[] yPoints) {
        return tabulate(new MathFunction() {
            @Override
            public double apply(double x) {
                double y = yPoints[0];
                for (int i = 1; i < numberOfPoints; i++) {
                    y += getDividedDifference(Arrays.copyOfRange(yPoints, 0, i + 1), Arrays.copyOfRange(xPoints, 0, i + 1)) * w(i, x, xPoints);
                }
                return y;
            }

            @Override
            public MathFunction differentiate() {
                return null;
            }

            @Override
            public String getName() {
                return "Полином Ньютона";
            }
        });
    }

    @ConnectableItem(name = "Интерполяция линейным сплайном", type = Item.APPROXIMATION_NUMERICAL_METHOD, priority = 3, isForSplineApproximation = true)
    public TabulatedFunction linearSpline(double[] yPoints) {
        Matrix A = new Matrix(new double[][]{
                {1, 0, 0, 0, 0, 0},
                {1, h, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0},
                {0, 0, 1, h, 0, 0},
                {0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1, h}}
        );
        Matrix B = new Matrix(new double[][]{{yPoints[0]}, {yPoints[1]}, {yPoints[1]}, {yPoints[2]}, {yPoints[2]}, {yPoints[3]}});
        return spline(LINEAR, A.solve(B));
    }

    @ConnectableItem(name = "Интерполяция параболическим сплайном", type = Item.APPROXIMATION_NUMERICAL_METHOD, priority = 4, isForSplineApproximation = true)
    public TabulatedFunction parabolicSpline(double[] yPoints) {
        Matrix A = new Matrix(new double[][]{
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, h, h * h, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, h, h * h, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, h, h * h},
                {1, h, h*h, -1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, h, h*h, -1, 0, 0},
                {0, 1, 2 * h, 0, -1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 2 * h, 0, -1, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0}}
        );
        Matrix B = new Matrix(new double[][]{{yPoints[0]}, {yPoints[1]}, {yPoints[2]}, {yPoints[3]}, {0.}, {0.}, {0.}, {0.}, {0.}});
        return spline(PARABOLIC, A.solve(B));
    }

    @ConnectableItem(name = "Интерполяция кубическим сплайном", type = Item.APPROXIMATION_NUMERICAL_METHOD, priority = 5, isForSplineApproximation = true)
    public TabulatedFunction cubicSpline(double[] yPoints) {
        Matrix A = new Matrix(new double[][]{
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, h, h * h, h * h * h, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, h, h * h, h * h * h, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, h, h * h, h * h * h},
                {0, 1, 2 * h, 3 * h * h, 0, -1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 2 * h, 3 * h * h, 0, -1, 0, 0},
                {0, 0, 2, 6 * h, 0, 0, -2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 6 * h, 0, 0, -2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 6 * h},
                {0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0}}
        );
        Matrix B = new Matrix(new double[][]{{yPoints[0]}, {yPoints[1]}, {yPoints[1]}, {yPoints[2]}, {yPoints[2]}, {yPoints[3]}, {0.}, {0.}, {0.}, {0.}, {0.}, {0.}});
        return spline(CUBIC, A.solve(B));
    }

    private TabulatedFunction tabulate(MathFunction function) {
        return factory.create(function, left, right, 1000);
    }

    private double l(int n, double x) {
        if (n < 0 || n >= numberOfPoints) {
            throw new UnsupportedOperationException();
        }
        double L = 1;
        for (int i = 0; i < numberOfPoints; i++) {
            if (i == n) {
                continue;
            }
            L *= (x - xPoints[i]) / (xPoints[n] - xPoints[i]);
        }
        return L;
    }

    private double lagrange(double[] yPoints, double x) {
        double y = 0;
        for (int i = 0; i < numberOfPoints; i++) {
            y += yPoints[i] * l(i, x);
        }
        return y;
    }

    private double getDividedDifference(double[] yPoints, double... x) {
        if (x.length == 2) {
            return (yPoints[1] - yPoints[0]) / (x[1] - x[0]);
        } else {
            return (getDividedDifference(Arrays.copyOfRange(yPoints, 1, x.length), Arrays.copyOfRange(x, 1, x.length)) -
                    getDividedDifference(Arrays.copyOfRange(yPoints, 0, x.length - 1), Arrays.copyOfRange(x, 0, x.length - 1)))
                    / (x[x.length - 1] - x[0]);
        }
    }

    public static double w(int n, double x, double[] xPoints) {
        double W = 1;
        for (int i = 0; i < n; i++) {
            W *= (x - xPoints[i]);
        }
        return W;
    }

    private TabulatedFunction spline(int orderOfSpline, Matrix coefficients) {
        return tabulate(new MathFunction() {
            private static final long serialVersionUID = 8977791784179429156L;

            @Override
            public double apply(double x) {
                if (x < left || x > right) {
                    return 0;
                } else {
                    double y = 0;
                    for (int i = 0; i <= orderOfSpline; i++) {
                        y += splineBit(i + shiftForSpline(orderOfSpline, x), x, orderOfSpline, coefficients);
                    }
                    return y;
                }
            }

            @Override
            public MathFunction differentiate() {
                return null;
            }

            @Override
            public String getName() {
                return names.get(orderOfSpline);
            }
        });
    }

    private double splineBit(int n, double x, int orderOfSpline, Matrix coefficients) {
        return coefficients.get(n, 0) * Math.pow(x - xPoints[n / (orderOfSpline + 1)], n % (orderOfSpline + 1));
    }

    private int shiftForSpline(int orderOfSpline, double x) {
        if (x >= xPoints[0] && x < xPoints[1]) {
            return 0;
        } else if (x >= xPoints[1] && x < xPoints[2]) {
            return orderOfSpline + 1;
        } else if (x >= xPoints[2] && x < xPoints[3]) {
            return 2 * (orderOfSpline + 1);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
