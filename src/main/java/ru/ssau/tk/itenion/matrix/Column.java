package ru.ssau.tk.itenion.matrix;

public class Column extends Vector{
    public Column(int dim){
        super(dim);
    }

    public Column(double[] numbers){
        super(numbers);
    }

    @Override
    Orientation getOrientation() {
        return Orientation.COLUMN;
    }

    @Override
    Vector transpose() {
        return new Row(numbers);
    }
}
