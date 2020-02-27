package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions;

import ru.ssau.tk.itenion.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.itenion.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.AdditiveVectorArgumentMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.VectorArgumentMathFunction;
import ru.ssau.tk.itenion.matrix.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class VectorMathFunctionSV implements VectorMathFunction {
    private List<AdditiveVectorArgumentMathFunction> functionList;
    private int dim;
    private int requiredDim;
    private StringJoiner joiner;

    public VectorMathFunctionSV() {
        functionList = new ArrayList<>();
        dim = 0;
        joiner = new StringJoiner("; ");
    }

    VectorMathFunctionSV(AdditiveVectorArgumentMathFunction additiveVectorArgumentMathFunction) {
        this();
        functionList.add(additiveVectorArgumentMathFunction);
        requiredDim = additiveVectorArgumentMathFunction.getDim();
        dim++;
        joiner.add(additiveVectorArgumentMathFunction.toString());
    }

    public boolean isCanBeSolved() {
        return dim == requiredDim;
    }

    public Matrix getJacobiMatrix(ArrayList<Double> x) {
        Matrix matrix = new Matrix(dim, dim);
        if (isCanBeSolved()) {
            for (int i = 0; i < dim; i++) {
                for (Variable variable : Variable.values()) {
                    matrix.set(i + 1,
                            variable.ordinal() + 1,
                            functionList.get(i).differentiate(variable).apply(x.get(variable.ordinal()))
                    );
                }
            }
        } else {
            throw new InconsistentFunctionsException();
        }
        return matrix;
    }

    @Override
    public void add(VectorArgumentMathFunction vectorArgumentMathFunction) {
        if (vectorArgumentMathFunction instanceof AdditiveVectorArgumentMathFunction) {
            if (requiredDim == 0 || requiredDim == vectorArgumentMathFunction.getDim()) {
                if (requiredDim == 0) {
                    requiredDim = vectorArgumentMathFunction.getDim();
                }
                functionList.add((AdditiveVectorArgumentMathFunction) vectorArgumentMathFunction);
                joiner.add(vectorArgumentMathFunction.toString());
                dim++;
            } else {
                throw new DifferentLengthOfArraysException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public ArrayList<Double> apply(ArrayList<Double> x) {
        ArrayList<Double> y = new ArrayList<>();
        functionList.forEach(function -> y.add(function.apply(x)));
        return y;
    }

}
