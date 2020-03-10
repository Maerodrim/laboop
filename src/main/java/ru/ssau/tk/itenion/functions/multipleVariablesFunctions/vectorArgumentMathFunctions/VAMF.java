package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.MultipleVariableDifferentiable;

import java.util.ArrayList;

public interface VAMF extends MultipleVariableDifferentiable {
    MathFunction get(Variable variable);

    void put(Variable variable, MathFunction function);

    double apply(ArrayList<Double> x);

    double apply(Matrix x);

    VAMF getMathFunction(Variable variable);

    int getDimension();
}
