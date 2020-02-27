package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.MultipleVariableDifferentiable;

import java.util.ArrayList;

public interface VectorArgumentMathFunction extends MultipleVariableDifferentiable {
    //MultipleVariableMathFunction sum(MultipleVariableMathFunction afterFunction);
    //MultipleVariableMathFunction subtract(MultipleVariableMathFunction afterFunction);
    //MultipleVariableMathFunction multiply(MultipleVariableMathFunction afterFunction);
    //MultipleVariableMathFunction divide(MultipleVariableMathFunction afterFunction);
    MathFunction get(Variable variable);

    void put(Variable variable, MathFunction function);

    double apply(ArrayList<Double> x);

    int getDim();
}
