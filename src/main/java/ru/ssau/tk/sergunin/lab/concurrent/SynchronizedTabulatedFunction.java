package ru.ssau.tk.sergunin.lab.concurrent;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.operations.TabulatedFunctionOperationService;

import java.sql.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private TabulatedFunction func;

    public SynchronizedTabulatedFunction(TabulatedFunction func) {
        this.func = func;
    }

    @NotNull
    @Override
    public synchronized Iterator<Point> iterator() {
       Point[] copy = TabulatedFunctionOperationService.asPoints(func);
        return new Iterator<>() {
            int i = 0;

            public boolean hasNext() {
                return i != copy.length;
            }

            public Point next() {
                if (i == copy.length) {
                    throw new NoSuchElementException();
                }
                return new Point(copy[i].x, copy[i++].y);
            }
        };
    }

    @Override
    public synchronized double apply(double x) {
        return func.apply(x);
    }

    @Override
    public synchronized int getCount() {
        return func.getCount();
    }

    @Override
    public synchronized double getX(int index) {
        return func.getX(index);
    }

    @Override
    public synchronized double getY(int index) {
        return func.getY(index);
    }

    @Override
    public synchronized void setY(int index, double value) {
        func.setY(index, value);
    }

    @Override
    public synchronized int indexOfX(double x) {
        return func.indexOfX(x);
    }

    @Override
    public synchronized int indexOfY(double y) {
        return func.indexOfY(y);
    }

    @Override
    public synchronized double leftBound() {
        return func.leftBound();
    }

    @Override
    public synchronized double rightBound() {
        return func.rightBound();
    }
}
