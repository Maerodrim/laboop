package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TabulatedIntegralOperator extends TabulatedOperator implements IntegralOperator<TabulatedFunction> {
    public TabulatedIntegralOperator() {
        super();
    }

    public TabulatedIntegralOperator(TabulatedFunctionFactory factory) {
        super(factory);
    }

    protected TabulatedFunctionFactory getFactory() {
        return super.getFactory();
    }

    protected void setFactory(TabulatedFunctionFactory factory) {
        super.setFactory(factory);
    }

    @Override
    public TabulatedFunction integrate(TabulatedFunction function) {
        return new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory()).subtract(super.integrate(function, 0, function.getCount(), new double[function.getCount()], new double[function.getCount()], TabulatedFunctionOperationService.asPoints(function)), shift);
    }

    public TabulatedFunction integrateParallel(TabulatedFunction function) {
        return integrateParallel(function.copy(), 16);
    }

    public TabulatedFunction integrateParallel(TabulatedFunction function, final int NUMBER_OF_THREADS) {
        //TabulatedFunction result = function.copy(); // создаём функцию в которую будем записывать результат
        boolean isParalledCompleted = false;
        int step = function.getCount() / NUMBER_OF_THREADS;
        List<Thread> threads = new ArrayList<>();
        Collection<TabulatedIntegratorTask> collection = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            //TabulatedIntegratorTask task = new TabulatedIntegratorTask(function, step * i, step * (i + 1) + (i == NUMBER_OF_THREADS - 1 && function.getCount() % 2 != 0 ? 1 : 0), this);
            //collection.add(task);
            //threads.add(new Thread(task));
        }
        for (Thread thread : threads) {
            thread.start();
            //Thread.onSpinWait();
        }
        /*try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        while (!isParalledCompleted) {
            Thread.onSpinWait();
            isParalledCompleted = collection.stream().allMatch(TabulatedIntegratorTask::isCompleted);
        }
        return function;
        //return new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory()).subtract(result, shift);
    }

}
