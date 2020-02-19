package ru.ssau.tk.sergunin.lab.concurrent;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.sergunin.lab.functions.MathFunction;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.operations.TabulatedFunctionOperationService;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private static final long serialVersionUID = -1376170918005747022L;
    private final TabulatedFunction function;

    public SynchronizedTabulatedFunction(TabulatedFunction function) {
        this.function = function;
    }

    @NotNull
    @Override
    public Iterator<Point> iterator() {
        synchronized (function) {
            Point[] copy = TabulatedFunctionOperationService.asPoints(function);
            return new Iterator<>() {
                int i = 0;

                @Override
                public boolean hasNext() {
                    return i != copy.length;
                }

                @Override
                public Point next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    } else {
                        return new Point(copy[i].x, copy[i++].y);
                    }
                }
            };
        }
    }

    @Override
    public double apply(double x) {
        synchronized (function) {
            return function.apply(x);
        }
    }

    @Override
    public int getCount() {
        synchronized (function) {
            return function.getCount();
        }
    }

    @Override
    public double getX(int index) {
        synchronized (function) {
            return function.getX(index);
        }
    }

    @Override
    public double getY(int index) {
        synchronized (function) {
            return function.getY(index);
        }
    }

    @Override
    public void setY(int index, double value) {
        synchronized (function) {
            function.setY(index, value);
        }
    }

    @Override
    public void setY(TabulatedFunction function) {
        synchronized (this.function) {
            this.function.setY(function);
        }
    }

    @Override
    public int indexOfX(double x) {
        synchronized (function) {
            return function.indexOfX(x);
        }
    }

    @Override
    public int indexOfY(double y) {
        synchronized (function) {
            return function.indexOfY(y);
        }
    }

    @Override
    public double leftBound() {
        synchronized (function) {
            return function.leftBound();
        }
    }

    @Override
    public double rightBound() {
        synchronized (function) {
            return function.rightBound();
        }
    }

    @Override
    public TabulatedFunction copy() {
        synchronized (function) {
            return function.copy();
        }
    }

    @Override
    public boolean isStrict() {
        return false;
    }

    @Override
    public boolean isUnmodifiable() {
        return false;
    }

    @Override
    public TabulatedFunction unwrap() {
        return function;
    }

    @Override
    public MathFunction getMathFunction() {
        return function.getMathFunction();
    }

    @Override
    public void setMathFunction(MathFunction math) {
        function.setMathFunction(math);
    }

    @Override
    public void offerStrict(boolean isStrict) {
    }

    @Override
    public void offerUnmodifiable(boolean isUnmodifiable) {
    }

    public synchronized <T> T doSynchronously(Operation<? extends T> operation) {
        return operation.apply(this);
    }

    @Override
    public void insert(double x, double y) {
        synchronized (function) {
            function.insert(x, y);
        }
    }

    @Override
    public void remove(int index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        synchronized (function) {
            function.remove(index);
        }
    }

    @FunctionalInterface
    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }
}
