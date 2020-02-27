package ru.ssau.tk.itenion.matrix;

public class Row extends Vector{

    public Row(int dim){
        super(dim);
    }

    public Row(double[] numbers){
        super(numbers);
    }

    @Override
    Orientation getOrientation() {
        return Orientation.ROW;
    }

    @Override
    Vector transpose() {
        return new Column(numbers);
    }
}
