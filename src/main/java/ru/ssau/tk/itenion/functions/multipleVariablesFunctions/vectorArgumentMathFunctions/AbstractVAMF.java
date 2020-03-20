package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.functions.LinearCombinationFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.IdentityFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;

public abstract class AbstractVAMF implements VAMF {
    protected Map<Variable, MathFunction> functionMap;
    protected StringJoiner name;
    protected int dimension;

    public AbstractVAMF(String delimiter) {
        functionMap = new HashMap<>();
        dimension = 0;
        name = new StringJoiner(delimiter);
    }

    AbstractVAMF(Variable variable, MathFunction mathFunction, String delimiter) {
        this(delimiter);
        functionMap.put(variable, mathFunction);
        dimension++;
        name.add(mathFunction.getName(variable));
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public MathFunction differentiate(Variable variable) {
        return functionMap.get(variable).differentiate();
    }

    @Override
    public MathFunction get(Variable variable) {
        return functionMap.get(variable);
    }

    public void put(Variable variable, MathFunction function, BinaryOperator<MathFunction> operator) {
        functionMap.computeIfPresent(variable, (a, b) -> operator.apply(b, function));
        functionMap.putIfAbsent(variable, function);
        dimension++;
        name.add(function.getName(variable));
    }

    public double apply(ArrayList<Double> x, DoubleBinaryOperator operator) {
        AtomicReference<Double> y = new AtomicReference<>(0.);
        functionMap.forEach(((variable, mathFunction) -> y.updateAndGet(
                v -> operator.applyAsDouble(v, mathFunction.apply(x.get(variable.ordinal())))
        )));
        return y.get();
    }

    public double apply(Matrix x, DoubleBinaryOperator operator) {
        double y = 0;
        Variable[] variables = Variable.values();
        for (int i = 0; i < functionMap.size(); i++) {
            y = operator.applyAsDouble(y, functionMap.get(variables[i]).apply(x.get(0, i)));
        }
        return y;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean isExistIdentityFunction() {
        AtomicReference<Boolean> isExist = new AtomicReference<>(false);
        functionMap.forEach((variable, function) -> {
            if (LinearCombinationFunction.isValidForSimplePlot(function)) {
                isExist.set(true);
            }
        });
        return isExist.get();
    }

    public double getIndexForPlot() {
        if (dimension != 2) {
            return -1;
        }
        if (LinearCombinationFunction.isValidForSimplePlot(functionMap.get(Variable.values()[0]))) {
            return 1;
        } else return 0;
    }

    @Override
    public MathFunction getMathFunction(Variable variable) {
        return functionMap.get(variable);
    }
}

