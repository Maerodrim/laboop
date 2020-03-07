package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Variable;

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
}

