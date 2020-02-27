package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions;

import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;

public class AdditiveVectorArgumentMathFunction implements VectorArgumentMathFunction {
    private Map<Variable, MathFunction> functionMap;
    private StringJoiner name;
    private int dim;

    public AdditiveVectorArgumentMathFunction() {
        functionMap = new HashMap<>();
        dim = 0;
        name = new StringJoiner(" + ");
    }

    AdditiveVectorArgumentMathFunction(Variable variable, MathFunction mathFunction) {
        this();
        functionMap.put(variable, mathFunction);
        dim++;
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

    @Override
    public void put(Variable variable, MathFunction function) {
        functionMap.computeIfPresent(variable, (a, b) -> b.sum(function));
        functionMap.putIfAbsent(variable, function);
        dim++;
        name.add(function.getName(variable));
    }

    @Override
    public double apply(ArrayList<Double> x) {
        AtomicReference<Double> y = new AtomicReference<>(0.);
        functionMap.forEach(((variable, mathFunction) -> y.updateAndGet(
                v -> v + mathFunction.apply(x.get(variable.ordinal()))
        )));
        return y.get();
    }

    public int getDim() {
        return dim;
    }
}
