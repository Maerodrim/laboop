package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.enums.SupportedSign;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.LinearCombinationFunction;
import ru.ssau.tk.itenion.functions.MathFunction;

import java.util.ArrayList;
import java.util.Arrays;

@SignAnnotation(supportedSign = SupportedSign.SUBTRACT)
public class SubtractiveVAMFSV extends AbstractVAMF {
    public SubtractiveVAMFSV() {
        super(" - ");
    }

    public SubtractiveVAMFSV(Variable variable, MathFunction mathFunction) {
        super(variable, mathFunction, " - ");
    }

    @Override
    public void put(Variable variable, MathFunction function) {
        MathFunction aFunction = function;
        MathFunction finalAFunction = aFunction;
        functionMap.computeIfPresent(variable, (a, b) -> b.subtract(finalAFunction));
        functionMap.putIfAbsent(variable, aFunction);
        dimension++;
        aFunction = function;
        if (Variable.values()[1].equals(variable) && aFunction instanceof LinearCombinationFunction) {
            aFunction = LinearCombinationFunction.getWithNegateShift((LinearCombinationFunction) aFunction);
        }
        name.add(aFunction.getName(variable));
    }

    @Override
    public double apply(ArrayList<Double> x) {
        return super.apply(x, (y, z) -> y - z);
    }

    @Override
    public double apply(Matrix x) {
        return super.apply(x, (y, z) -> y - z);
    }

}
