package ru.ssau.tk.itenion.matrix;

public abstract class Vector {
    double[] numbers;
    int dim;

    public Vector(double[] numbers){
        this.numbers = numbers;
        dim = numbers.length;
    }

    public Vector(int dim) {
        this.dim = dim;
        numbers = new double[dim];
    }

    abstract Orientation getOrientation();

    abstract Vector transpose();
}
