package ru.ssau.tk.sergunin.lab.functions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Point {
    final public double x;
    final public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String getX(){
        return x + "";
    }

    public String getY(){
        return y + "";
    }
}
