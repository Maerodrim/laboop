package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions;

import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.VectorArgumentMathFunction;
import ru.ssau.tk.itenion.matrix.Matrix;

import java.util.ArrayList;

public interface VectorMathFunction {
    Matrix getJacobiMatrix(ArrayList<Double> x);

    void add(VectorArgumentMathFunction vectorArgumentMathFunction);

    ArrayList<Double> apply(ArrayList<Double> x);

    boolean isCanBeSolved();
}
