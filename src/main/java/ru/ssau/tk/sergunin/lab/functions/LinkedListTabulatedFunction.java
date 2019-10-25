package ru.ssau.tk.sergunin.lab.functions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ssau.tk.sergunin.lab.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.sergunin.lab.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.sergunin.lab.exceptions.InterpolationException;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Serializable {
    private static final long serialVersionUID = -8102232408974120402L;
    private Node head;
    private int count;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) throws IllegalArgumentException, DifferentLengthOfArraysException, ArrayIsNotSortedException {
        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);
        if (xValues.length < 2) {
            throw new IllegalArgumentException("Length of array less than minimum length");
        }
        this.count = xValues.length;
        for (int i = 0; i < count; i++) {
            this.addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) throws IllegalArgumentException, DifferentLengthOfArraysException, ArrayIsNotSortedException {
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
            this.addNode(buff, source.apply(buff));
            buff += step;
        }
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

    @Nullable
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
        return null;
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
        } else if (nodeOfX(x) != null) {
            return nodeOfX(x).y;
        } else {
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

    @Nullable
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
        return null;
    }

    @Override
    public void insert(double x, double y) {
        if (indexOfX(x) != -1) {
            setY(indexOfX(x), y);
        } else {
            int index;
            try {
                index = floorIndexOfX(x);
            } catch (IllegalArgumentException e) {
                index = 0;
            }
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

    private static class Node {
        Node next;
        Node prev;
        double x;
        double y;
    }
}