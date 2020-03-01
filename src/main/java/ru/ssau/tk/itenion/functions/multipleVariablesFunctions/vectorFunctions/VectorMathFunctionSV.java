package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.itenion.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.itenion.exceptions.InconsistentMatrixSize;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.AdditiveVectorArgumentMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.VectorArgumentMathFunction;

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
        requiredDim = additiveVectorArgumentMathFunction.getDimension();
        dim++;
        joiner.add(additiveVectorArgumentMathFunction.toString());
    }

    public boolean isCanBeSolved() {
        return dim == requiredDim;
    }

    public Matrix getJacobiMatrix(Matrix x) {
        if (x.getRowDimension() != 1 || x.getColumnDimension() != requiredDim) {
            throw new UnsupportedOperationException();
        }
        Matrix matrix = new Matrix(dim, dim);
        Variable[] variables = Variable.values();
        if (isCanBeSolved()) {
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    matrix.set(i, j, functionList.get(i).differentiate(variables[j]).apply(x.get(0, j)));
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
            if (requiredDim == 0 || requiredDim == vectorArgumentMathFunction.getDimension()) {
                if (requiredDim == 0) {
                    requiredDim = vectorArgumentMathFunction.getDimension();
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

    @Override
    public Matrix apply(Matrix x) {
        if (x.getRowDimension() != 1 || x.getColumnDimension() != requiredDim) {
            throw new InconsistentMatrixSize();
        }
        Matrix y = new Matrix(functionList.size(), 1);
        for (int i = 0; i < functionList.size(); i++) {
            y.set(i, 0, functionList.get(i).apply(x.getMatrix(0, 0, 0, x.getColumnDimension() - 1)));
        }
        return y;
    }

}
