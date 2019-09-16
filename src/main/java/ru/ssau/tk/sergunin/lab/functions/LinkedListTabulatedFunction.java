package ru.ssau.tk.sergunin.lab.functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
    private Node head;
    private Node last;
    private Node buff;
    private int count;

    private static class Node {
        Node next;
        Node prev;
        double x;
        double y;

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
        } else {
            newNode.next = head;
            newNode.prev = last;
            head.prev = newNode;
            last.next = newNode;
            newNode.x = x;
            newNode.y = y;
            last = newNode;
        }
    }

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        this.count = xValues.length;
        for (int i = 0; i < count; i++) {
            this.addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
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

    public int getCount() {
        return count;
    }

    public double leftBound() {
        return head.x;
    }

    public double rightBound() {
        return last.x;
    }

    protected Node getNode(int index) {
        if (index > (count / 2)) {
            buff = last;
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

    public int floorIndexOfX(double x) {
        if (x < head.x) {
            return 0;
        }
        buff = head;
        for (int i = 0; i < count; i++) {
            if (buff.x < x) {
                buff = buff.next;
            } else {
                return i - 1;
            }
        }
        return getCount();
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count == 1) {
            return x;
        }
        return head.y + (head.next.y - head.y) * (x - head.x) / (head.next.x - head.x);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count == 1) {
            return x;
        }
        return last.prev.y + (last.y - last.prev.y) * (x - last.prev.x) / (last.x - last.prev.x);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count == 1) {
            return x;
        }
        Node left = getNode(floorIndex);
        Node right = left.next;
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

    protected Node floorNodeOfX(double x) {
        if (x < head.x) {
            return head;
        }
        buff = head;
        for (int i = 0; i < count; i++) {
            if (buff.x < x) {
                buff = buff.next;
            } else {
                return buff.prev;
            }
        }
        return last;
    }

    protected Node nodeOfX(double x) {
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
            int index = floorIndexOfX(x);
            Node newNode = new Node();
            if (index == 0) {
                newNode.next = head;
                newNode.prev = last;
                newNode.x = x;
                newNode.y = y;
                head.prev = newNode;
                last.next = newNode;
                head = newNode;
                count++;
            } else if (index == count) {
                newNode.next = head;
                newNode.prev = last;
                newNode.x = x;
                newNode.y = y;
                head.prev = newNode;
                last.next = newNode;
                last = newNode;
                count++;
            } else {
                Node previous = getNode(index);
                Node further = getNode(index + 1);
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
    public void remove(int index) {
        buff = getNode(index);
        Node previous = buff.prev;
        Node further = buff.next;
        previous.next = further;
        further.prev = previous;
        count--;
    }
}