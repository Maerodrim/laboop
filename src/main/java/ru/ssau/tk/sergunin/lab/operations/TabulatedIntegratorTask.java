package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public class TabulatedIntegratorTask implements Runnable {
    TabulatedFunction function;
    private TabulatedIntegralOperator operator;
    private int from;
    private int to;
    private boolean isCompleted;

    TabulatedIntegratorTask(TabulatedFunction function, int from, int to, TabulatedIntegralOperator operator) {
        this.function = function;
        this.from = from;
        this.to = to;
        this.operator = operator;
    }

    @Override
    public void run() {
        function = operator.integrate(function, from, to, new double[to - from], new double[to - from], TabulatedFunctionOperationService.asPoints(function, from, to));
        isCompleted = true;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
