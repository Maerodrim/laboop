package ru.ssau.tk.sergunin.lab.functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction {
    private Node head;
    private Node last;

    private class Node {
        public int index;
        public Node next;
        public Node prev;
        public double x;
        public double y;

    }

    void addNode(double x, double y) {
        Node newclass = new Node();
        if (head == null) {
            head = newclass;
            newclass.next = head;
            newclass.prev = head;
            newclass.x = x;
            newclass.y = y;
            last = newclass;
            newclass.index = 0;
        } else {
            newclass.next = head;
            newclass.prev = last;
            head.prev = newclass;
            newclass.x = x;
            newclass.y = y;
            last = newclass;
            newclass.index++;
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
        double buff = xFrom;
        for (int i = 0; i < (count + 1); i++) {
            this.addNode(buff, source.apply(buff));
            buff += (xTo - xFrom) / count;
        }
        ;
    }

    int getCount() {
        return last.index;
    }

    Node leftBound() {
        return head;
    }

    Node rightBound() {
        return last;
    }

    Node getNode(int index) {
        if(index>(getCount()/2)){
            Node buff = last;
            for (int i = getCount(); i > 0; i--) {
                if (buff.index == i) {
                    return buff;
                } else {
                    buff = buff.prev;
                }
            }
        }
        else{
            Node buff = head;
            for (int i = 0; i < (index+1); i++) {
                if (buff.index == i) {
                    return buff;
                } else {
                    buff = buff.next;
                }
            }
        }
        return null;
    }
//    getX(){};
//   getY(){};
//    setY(){};

}
