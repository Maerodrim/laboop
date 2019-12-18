package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.Point;

public class TabulatedIntegratorTask implements Runnable {
    Point[] points;
    private boolean isCompleted;

    TabulatedIntegratorTask(Point[] points, TabulatedIntegralOperator operator) {
        this.points = points;
    }

    @Override
    public void run() {
        //operator.integrate(points);
        //function.setY(operator.integrate(function, from, to, new double[to - from], new double[to - from], TabulatedFunctionOperationService.asPoints(function, from, to)));
        isCompleted = true;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
