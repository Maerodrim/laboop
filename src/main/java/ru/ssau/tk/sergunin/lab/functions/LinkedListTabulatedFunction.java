package ru.ssau.tk.sergunin.lab.functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction {
    private Node head;
    private Node last;
    private class Node {
        public Node next;
        public Node prev;
        public double x;
        public double y;

    };
    void addNode(double x, double y){
        Node newclass=new Node();
        if(head == null){
            head=newclass;
            newclass.next=head;
            newclass.prev=head;
            newclass.x=x;
            newclass.y=y;
            last=newclass;
        }else{
            newclass.next=head;
            newclass.prev=last;
            newclass.x=x;
            newclass.y=y;
            last=newclass;
        };
}
}
