package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.enums.Variable;

import java.util.ArrayList;

public class AdditiveVAMFSV extends AbstractVAMF {

    public AdditiveVAMFSV() {
        super(" + ");
    }

    public AdditiveVAMFSV(Variable variable, MathFunction mathFunction) {
        super(variable, mathFunction, " + ");
    }

    @Override
    public void put(Variable variable, MathFunction function) {
        super.put(variable, function, MathFunction::sum);
    }

    @Override
    public double apply(ArrayList<Double> x) {
        return super.apply(x, Double::sum);
    }

    @Override
    public double apply(Matrix x) {
        return super.apply(x, Double::sum);
    }
}
