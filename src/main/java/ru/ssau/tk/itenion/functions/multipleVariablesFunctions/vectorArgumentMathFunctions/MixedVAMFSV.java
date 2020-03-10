package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Variable;

import java.util.ArrayList;

/**
 * TODO
 * It's expected to work as a general case of the {@link AdditiveVAMFSV} and {@link MultiplicativeVAMFSV}
 */

public class MixedVAMFSV extends AbstractVAMF {

    public MixedVAMFSV(String delimiter) {
        super(delimiter);
    }

    @Override
    public void put(Variable variable, MathFunction function) {

    }

    @Override
    public double apply(ArrayList<Double> x) {
        return 0;
    }

    @Override
    public double apply(Matrix x) {
        return 0;
    }
}
