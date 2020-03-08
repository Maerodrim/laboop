package ru.ssau.tk.itenion.functions.tabulatedFunctions;

import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.itenion.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.itenion.exceptions.InterpolationException;
import ru.ssau.tk.itenion.exceptions.NaNException;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.operations.TabulatedFunctionOperationService;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Serializable {
    private static final long serialVersionUID = -8102232408974120402L;
    private Node head;
    private int count;
    private boolean isStrict;
    private boolean isUnmodifiable;
    private MathFunction mathFunction;

    private LinkedListTabulatedFunction() {
        head = null;
    }

    private LinkedListTabulatedFunction(LinkedListTabulatedFunction function) {
        Point[] points = TabulatedFunctionOperationService.asPoints(function);
        this.count = points.length;
        this.isStrict = function.isStrict();
        this.isUnmodifiable = function.isUnmodifiable();
        this.mathFunction = function.getMathFunction();
        for (int i = 0; i < count; i++) {
            this.addNode(points[i].x, points[i].y);
        }
    }

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);
        if (xValues.length < 2) {
            throw new IllegalArgumentException("Length of array less than minimum length");
        }
        for (double yValue : yValues) {
            if (yValue != yValue) {
                throw new NaNException();
            }
        }
        this.count = xValues.length;
        for (int i = 0; i < count; i++) {
            this.addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("The count of points is less than the minimum count (2)");
        }
        this.count = count;
        if (xFrom > xTo) {
            xFrom = xFrom - xTo;
            xTo = xFrom + xTo;
            xFrom = -xFrom + xTo;
        }
        double step = (xTo - xFrom) / (count - 1);
        double buff = xFrom;
        for (int i = 0; i < count; i++) {
            double temp = source.apply(buff);
            if (temp != temp) {
                throw new NaNException();
            }
            this.addNode(buff, temp);
            buff += step;
        }
        mathFunction = source;
    }

    public static TabulatedFunction getIdentity() {
        return new LinkedListTabulatedFunction();
    }

    public MathFunction getMathFunction() {
        return mathFunction;
    }

    @Override
    public void setMathFunction(MathFunction mathFunction) {
        this.mathFunction = mathFunction;
    }

    void addNode(double x, double y) {
        Node newNode = new Node();
        if (head == null) {
            head = newNode;
            newNode.next = head;
            newNode.prev = head;
            newNode.x = x;
            newNode.y = y;
        } else {
            newNode.next = head;
            newNode.prev = head.prev;
            newNode.x = x;
            newNode.y = y;
            head.prev.next = newNode;
            head.prev = newNode;
        }
    }

    public int getCount() {
        return count;
    }

    public double leftBound() {
        return head.x;
    }

    public double rightBound() {
        return head.prev.x;
    }

    @Override
    public LinkedListTabulatedFunction copy() {
        return new LinkedListTabulatedFunction(this);
    }

    @Override
    public boolean isStrict() {
        return false;
    }

    @Override
    public boolean isUnmodifiable() {
        return false;
    }

    public void offerStrict(boolean strict) {
        isStrict = strict;
    }

    public void offerUnmodifiable(boolean unmodifiable) {
        isUnmodifiable = unmodifiable;
    }

    @Override
    public TabulatedFunction unwrap() {
        return this;
    }

    private Node getNode(int index) {
        Node buff;
        if (index > (count / 2)) {
            buff = head.prev;
            for (int i = count - 1; i > 0; i--) {
                if (i == index) {
                    return buff;
                } else {
                    buff = buff.prev;
                }
            }
        } else {
            buff = head;
            for (int i = 0; i < count; i++) {
                if (index == i) {
                    return buff;
                } else {
                    buff = buff.next;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public double getX(int index) throws IllegalArgumentException {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
        return getNode(index).x;
    }

    public double getY(int index) throws IllegalArgumentException {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
        return getNode(index).y;
    }

    public void setY(int index, double value) throws IllegalArgumentException {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
        getNode(index).y = value;
    }

    @Override
    public void setY(TabulatedFunction function) {
        if (function.getCount() > count || leftBound() > function.leftBound() || rightBound() < function.rightBound())
            throw new InconsistentFunctionsException();
        Node node = nodeOfTheBeginningOfTheProjectionOccurrence(function);
        if (Objects.equals(null, node)) throw new InconsistentFunctionsException();
        double shift = node == head ? 0 : node.prev.y;
        for (Point point : function) {
            node.y = point.y + shift;
            node = node.next;
        }
    }

    private Node nodeOfTheBeginningOfTheProjectionOccurrence(TabulatedFunction function) {
        Node node = head;
        while (true) {
            if (node.x == function.leftBound()) {
                break;
            }
            if (node == head.prev) {
                return null;
            }
            node = node.next;
        }
        Node result = node;
        for (Point point : function) {
            if (point.x != node.x) return null;
            node = node.next;
        }
        return result;
    }

    public int indexOfX(double x) {
        Node buff;
        buff = head;
        for (int i = 0; i < count; i++) {
            if (buff.x == x) {
                return i;
            } else {
                buff = buff.next;
            }
        }
        return -1;
    }

    public int indexOfY(double y) {
        Node buff;
        buff = head;
        for (int i = 0; i < count; i++) {
            if (buff.y == y) {
                return i;
            } else {
                buff = buff.next;
            }
        }
        return -1;
    }

    public int floorIndexOfX(double x) throws IllegalArgumentException {
        Node buff;
        if (x < head.x) {
            throw new IllegalArgumentException("Argument x less than minimal x in tabulated function");
        }
        buff = head;
        for (int i = 0; i < count; i++)
            if (buff.x < x) {
                buff = buff.next;
            } else {
                return i - 1;
            }
        return getCount();
    }

    @Override
    protected double extrapolateLeft(double x) {
        return head.y + (head.next.y - head.y) * (x - head.x) / (head.next.x - head.x);
    }

    @Override
    protected double extrapolateRight(double x) {
        return head.prev.prev.y + (head.prev.y - head.prev.prev.y) * (x - head.prev.prev.x) / (head.prev.x - head.prev.prev.x);
    }

    @Override
    protected double interpolate(double x, int floorIndex) throws InterpolationException {
        Node left = getNode(floorIndex);
        Node right = left.next;
        if (x < left.x || right.x < x) {
            throw new InterpolationException();
        }
        return left.y + (right.y - left.y) * (x - left.x) / (right.x - left.x);
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else try {
            return nodeOfX(x).y;
        } catch (UnsupportedOperationException e) {
            Node left = floorNodeOfX(x);
            Node right = left.next;
            return left.y + (right.y - left.y) * (x - left.x) / (right.x - left.x);
        }
    }

    private Node floorNodeOfX(double x) throws IllegalArgumentException {
        Node buff;
        if (x < head.x) {
            throw new IllegalArgumentException("Argument x less than minimal x in tabulated function");
        }
        buff = head;
        for (int i = 0; i < count; i++) {
            if (buff.x < x) {
                buff = buff.next;
            } else {
                return buff.prev;
            }
        }
        return head.prev;
    }

    private Node nodeOfX(double x) {
        Node buff;
        buff = head;
        for (int i = 0; i < count; i++) {
            if (buff.x == x) {
                return buff;
            } else {
                buff = buff.next;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(double x, double y) {
        if (indexOfX(x) != -1) {
            setY(indexOfX(x), y);
        } else {
            int index = x < head.x ? 0 : floorIndexOfX(x);
            Node newNode = new Node();
            if (index == 0 || index == count) {
                newNode.next = head;
                newNode.prev = head.prev;
                newNode.x = x;
                newNode.y = y;
                head.prev.next = newNode;
                head.prev = newNode;
                if (index == 0) {
                    head = newNode;
                }
                count++;
            } else {
                Node previous = getNode(index);
                Node further = previous.next;
                newNode.next = further;
                newNode.prev = previous;
                newNode.x = x;
                newNode.y = y;
                previous.next = newNode;
                further.prev = newNode;
                count++;
            }
        }
    }

    @Override
    public void remove(int index) throws IllegalArgumentException {
        Node buff;
        buff = getNode(index);
        if (buff == null) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
        Node previous = buff.prev;
        Node further = buff.next;
        previous.next = further;
        further.prev = previous;
        count--;
    }

    @Override
    @NotNull
    public Iterator<Point> iterator() {
        return new Iterator<>() {
            private Node node = head;

            public boolean hasNext() {
                return (node != null);
            }

            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Point point = new Point(node.x, node.y);
                node = node != head.prev ? node.next : null;
                return point;
            }
        };
    }

    private static class Node implements Serializable {
        private static final long serialVersionUID = -1467915199737523461L;
        Node next;
        Node prev;
        double x;
        double y;
    }
}