package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.Function;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.VAMF;

import java.util.ArrayList;

public interface VMF extends Function {
    Matrix getJacobiMatrix(Matrix x);

    void add(VAMF VAMF);

    ArrayList<Double> apply(ArrayList<Double> x);

    Matrix apply(Matrix x);

    boolean isCanBeSolved();
}
