package ru.ssau.tk.itenion.functions.multipleVariablesFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.AdditiveVectorArgumentMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentFunctions.VectorArgumentMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VectorMathFunction;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VectorMathFunctionSV;
import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.PolynomialFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.CosFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.SinFunction;
import ru.ssau.tk.itenion.numericalMethods.NumericalMethods;

import java.text.NumberFormat;
import java.util.Locale;

public class TestVectorFunctions {
    public static void main(String[] args) {
        VectorMathFunction vectorMathFunction = new VectorMathFunctionSV();
        VectorArgumentMathFunction vectorArgumentMathFunction1 = new AdditiveVectorArgumentMathFunction();
        vectorArgumentMathFunction1.put(Variable.x, new SinFunction());
        vectorArgumentMathFunction1.put(Variable.y, new PolynomialFunction("2x - 2"));
        VectorArgumentMathFunction vectorArgumentMathFunction2 = new AdditiveVectorArgumentMathFunction();
        vectorArgumentMathFunction2.put(Variable.x, new PolynomialFunction("2x"));
        vectorArgumentMathFunction2.put(Variable.y, new CosFunction().andThen(new PolynomialFunction("x - 1")).subtract(new ConstantFunction(0.7)));
        vectorMathFunction.add(vectorArgumentMathFunction1);
        vectorMathFunction.add(vectorArgumentMathFunction2);
        Matrix testList = new Matrix(new double[][]{{0.}, {0.}});
        Matrix result = NumericalMethods.solveNonlinearSystem(vectorMathFunction, testList, true);
        result.print(NumberFormat.getInstance(Locale.forLanguageTag("ru")), 5);
    }
}
