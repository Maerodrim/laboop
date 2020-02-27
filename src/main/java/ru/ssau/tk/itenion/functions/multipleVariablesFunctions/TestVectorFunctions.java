package ru.ssau.tk.itenion.functions.multipleVariablesFunctions;

import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.AdditiveVectorArgumentMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.VectorArgumentMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VectorMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VectorMathFunctionSV;
import ru.ssau.tk.itenion.functions.powerFunctions.PowFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.SqrFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.CosFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.SinFunction;
import ru.ssau.tk.itenion.matrix.Matrix;

import java.util.ArrayList;

public class TestVectorFunctions {
    public static void main(String[] args) {
        VectorMathFunction vectorMathFunction = new VectorMathFunctionSV();
        VectorArgumentMathFunction vectorArgumentMathFunction1 = new AdditiveVectorArgumentMathFunction();
        vectorArgumentMathFunction1.put(Variable.x, new CosFunction());
        vectorArgumentMathFunction1.put(Variable.y, new SqrFunction());
        VectorArgumentMathFunction vectorArgumentMathFunction2 = new AdditiveVectorArgumentMathFunction();
        vectorArgumentMathFunction2.put(Variable.x, new PowFunction(3));
        vectorArgumentMathFunction2.put(Variable.y, new SinFunction());
        vectorMathFunction.add(vectorArgumentMathFunction1);
        vectorMathFunction.add(vectorArgumentMathFunction2);
        ArrayList<Double> testList = new ArrayList<>();
        testList.add(1.);
        testList.add(2.);
        Matrix jacobi = vectorMathFunction.getJacobiMatrix(testList);
        int n = 5;
    }
}
