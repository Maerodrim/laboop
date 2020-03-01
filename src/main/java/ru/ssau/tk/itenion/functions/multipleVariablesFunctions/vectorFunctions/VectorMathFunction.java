package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.VectorArgumentMathFunction;

import java.util.ArrayList;

public interface VectorMathFunction {
    Matrix getJacobiMatrix(Matrix x);

    void add(VectorArgumentMathFunction vectorArgumentMathFunction);

    ArrayList<Double> apply(ArrayList<Double> x);

    Matrix apply(Matrix x);

    boolean isCanBeSolved();
}
