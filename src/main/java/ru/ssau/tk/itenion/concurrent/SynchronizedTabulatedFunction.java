package ru.ssau.tk.itenion.concurrent;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.operations.TabulatedFunctionOperationService;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private static final long serialVersionUID = -1376170918005747022L;
    private final TabulatedFunction tabulatedFunction;

    public SynchronizedTabulatedFunction(TabulatedFunction tabulatedFunction) {
        this.tabulatedFunction = tabulatedFunction;
    }

    @NotNull
    @Override
    public Iterator<Point> iterator() {
        synchronized (tabulatedFunction) {
            Point[] copy = TabulatedFunctionOperationService.asPoints(tabulatedFunction);
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
        synchronized (tabulatedFunction) {
            return tabulatedFunction.apply(x);
        }
    }

    @Override
    public int getCount() {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.getCount();
        }
    }

    @Override
    public double getX(int index) {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.getX(index);
        }
    }

    @Override
    public double getY(int index) {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.getY(index);
        }
    }

    @Override
    public void setY(int index, double value) {
        synchronized (tabulatedFunction) {
            tabulatedFunction.setY(index, value);
        }
    }

    @Override
    public void setY(TabulatedFunction function) {
        synchronized (this.tabulatedFunction) {
            this.tabulatedFunction.setY(function);
        }
    }

    @Override
    public int indexOfX(double x) {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.indexOfX(x);
        }
    }

    @Override
    public int indexOfY(double y) {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.indexOfY(y);
        }
    }

    @Override
    public double leftBound() {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.leftBound();
        }
    }

    @Override
    public double rightBound() {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.rightBound();
        }
    }

    @Override
    public TabulatedFunction copy() {
        synchronized (tabulatedFunction) {
            return tabulatedFunction.copy();
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
        return tabulatedFunction;
    }

    @Override
    public TabulatedFunction getInverseOperator() {
        return tabulatedFunction.getInverseOperator();
    }

    @Override
    public MathFunction getMathFunction() {
        return tabulatedFunction.getMathFunction();
    }

    @Override
    public void setMathFunction(MathFunction math) {
        tabulatedFunction.setMathFunction(math);
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
        synchronized (tabulatedFunction) {
            tabulatedFunction.insert(x, y);
        }
    }

    @Override
    public void remove(int index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        synchronized (tabulatedFunction) {
            tabulatedFunction.remove(index);
        }
    }

    @FunctionalInterface
    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }
}
