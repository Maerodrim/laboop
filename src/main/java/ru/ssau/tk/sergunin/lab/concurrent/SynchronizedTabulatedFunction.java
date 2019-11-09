package ru.ssau.tk.sergunin.lab.concurrent;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

import java.util.Iterator;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private TabulatedFunction func;

    public SynchronizedTabulatedFunction(TabulatedFunction func) {
        this.func = func;
    }

    @NotNull
    @Override
    public Iterator<Point> iterator() {
        return null;
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
   func.setY(index,value);
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
