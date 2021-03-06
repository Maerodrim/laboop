package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions;

import Jama.Matrix;
import javafx.util.Pair;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.itenion.enums.SupportedSign;
import ru.ssau.tk.itenion.enums.Variable;
import ru.ssau.tk.itenion.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.itenion.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.itenion.exceptions.InconsistentMatrixSize;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.AbstractVAMF;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.SignAnnotation;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.SubtractiveVAMFSV;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.VAMF;

import java.util.*;

public class VMFSV implements VMF {
    private List<AbstractVAMF> functionList;
    private int dim;
    private int requiredDim;
    private StringJoiner joiner;

    public VMFSV() {
        functionList = new ArrayList<>();
        dim = 0;
        joiner = new StringJoiner(";\n");
    }

    public VMFSV(VMFSV vmfsv) {
        functionList = new ArrayList<>(vmfsv.functionList);
        dim = vmfsv.dim;
        joiner = new StringJoiner(";\n");
        joiner.merge(vmfsv.joiner);
    }

    VMFSV(AbstractVAMF abstractVAMF) {
        this();
        functionList.add(abstractVAMF);
        requiredDim = abstractVAMF.getDimension();
        dim++;
        joiner.add(abstractVAMF.toString());
    }

    private VMFSV(Map<Pair<Variable, Integer>, MathFunction> baseOfVMF, List<SupportedSign> signs, boolean withConvert) {
        this();
        requiredDim = Variable.values().length;
        if (baseOfVMF.values().size() != signs.size() * signs.size()) {
            throw new UnsupportedOperationException();
        }
        signs.forEach(supportedSign -> functionList.add(supportedSign.getVAMF()));
        baseOfVMF.forEach((variableIntegerPair, function) -> {
            functionList.get(variableIntegerPair.getValue() - 1).put(variableIntegerPair.getKey(), function);
            if (variableIntegerPair.getKey() == Variable.values()[Variable.values().length - 1]) {
                joiner.add(functionList.get(variableIntegerPair.getValue() - 1).toString());
                dim++;
            }
        });
    }

    public VMFSV(Map<Pair<Variable, Integer>, MathFunction> baseOfVMF, List<SupportedSign> signs) {
        this(baseOfVMF, signs, false);
    }

    public boolean isCanBeSolved() {
        return dim == requiredDim;
    }

    @Override
    public boolean isCanBePlotted() {
        boolean result = requiredDim == 2;
        if (!result) {
            return false;
        }
        for (int i = 0; i < 2; i++) {
            result &= functionList.get(i).isExistIdentityFunction();
        }
        return result;
    }

    @Override
    public Optional<Matrix> getIndexForPlot() {
        if (requiredDim != 2 || !isCanBeSolved()) {
            return Optional.empty();
        }
        Matrix indexes = new Matrix(requiredDim, 1);
        for (int i = 0; i < requiredDim; i++) {
            indexes.set(i, 0, functionList.get(i).getIndexForPlot());
        }
        return Optional.of(indexes);
    }

    @Override
    public MathFunction getMathFunction(Variable variable, int index) {
        return functionList.get(index).getMathFunction(variable);
    }

    @Override
    public List<SupportedSign> getGeneratedSigns(int j) {
        return List.of(functionList.get(j).getClass().getAnnotation(SignAnnotation.class).supportedSign());
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
                    MathFunction function = functionList.get(i).differentiate(variables[j]);
                    if (j != 0 && functionList.get(i).getClass().getAnnotation(SignAnnotation.class).supportedSign().equals(SupportedSign.SUBTRACT)) {
                        function = function.negate();
                    }
                    matrix.set(i, j, function.apply(x.get(0, j)));
                }
            }
        } else {
            throw new InconsistentFunctionsException();
        }
        return matrix;
    }

    @Override
    public void add(VAMF vamf) {
        if (vamf instanceof AbstractVAMF) {
            if (requiredDim == 0 || requiredDim == vamf.getDimension()) {
                if (requiredDim == 0) {
                    requiredDim = vamf.getDimension();
                }
                functionList.add((AbstractVAMF) vamf);
                joiner.add(vamf.toString());
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

    @Override
    public String toString() {
        return joiner.toString();
    }

    @Override
    public String getName() {
        return joiner.toString();
    }

    @Override
    public double[] getDimensionArray() {
        return new double[]{functionList.size(), requiredDim};
    }
}
