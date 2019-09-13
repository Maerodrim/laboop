package ru.ssau.tk.sergunin.lab.functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction {
    private Node head;
    private Node last;
    private Node buff;

    private class Node {
        public int index;
        public Node next;
        public Node prev;
        public double x;
        public double y;

    }

    void addNode(double x, double y) {
        Node newNode = new Node();
        if (head == null) {
            head = newNode;
            newNode.next = head;
            newNode.prev = head;
            newNode.x = x;
            newNode.y = y;
            last = newNode;
            newNode.index = 0;
        } else {
            newNode.next = head;
            newNode.prev = last;
            head.prev = newNode;
            last.next = newNode;
            newNode.index=last.index+1;
            newNode.x = x;
            newNode.y = y;
            last = newNode;

        }
    }

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        for (int i = 0; i < xValues.length; i++) {
            this.addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {

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

    public int getCount() {
        return last.index;
    }

    public double leftBound() {
        return head.y;
    }

    public double rightBound() {
        return last.y;
    }

    protected Node getNode(int index) {
        if (index > (last.index / 2)) {
            buff = last;
            for (int i = last.index; i > 0; i--) {
                if (buff.index == i) {
                    return buff;
                } else {
                    buff = buff.prev;
                }
            }
        } else {
            buff = head;
            for (int i = 0; i < (index + 1); i++) {
                if (buff.index == i) {
                    return buff;
                } else {
                    buff = buff.next;
                }
            }
        }
        return null;
    }

    public double getX(int index) {
        return getNode(index).x;
    }

    public double getY(int index) {
        return getNode(index).y;
    }

    public void setY(int index, double value) {
        getNode(index).y = value;
    }

    public int indexOfX(double x) {
        buff = head;
        for (int i = 0; i <= getCount(); i++) {
            if (buff.x == x) {
                return buff.index;
            } else {
                buff = buff.next;
            }
        }
        return -1;
    }

    public int indexOfY(double y) {
        buff = head;
        for (int i = 0; i <= getCount(); i++) {
            if (buff.y == y) {
                return buff.index;
            } else {
                buff = buff.next;
            }
        }
        return -1;
    }

    public int floorIndexOfX(double x) {
        buff = head;
        Node floorX = head;
        for (int i = 0; i <= getCount(); i++) {
            if (buff.x == x) {
                return buff.index;
            } else {
                if (x > floorX.x) {
                    floorX = buff;
                } else {
                    return floorX.index;
                }
                buff = buff.next;
            }
        }
        return floorX.index;
    }

    @Override
    double extrapolateLeft(double x) {
        return 0;
    }

    @Override
    double extrapolateRight(double x) {
        return 0;
    }

    @Override
    double interpolate(double x, int floorIndex) {
        return 0;
    }
}