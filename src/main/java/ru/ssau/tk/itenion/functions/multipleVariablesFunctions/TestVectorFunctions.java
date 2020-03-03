package ru.ssau.tk.itenion.functions.multipleVariablesFunctions;

import Jama.Matrix;
import ru.ssau.tk.itenion.functions.Variable;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.AdditiveVAMFSV;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.MultiplicativeVAMFSV;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions.VAMF;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMFSV;
import ru.ssau.tk.itenion.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.PolynomialFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.CosFunction;
import ru.ssau.tk.itenion.functions.trigonometricFunctions.SinFunction;
import ru.ssau.tk.itenion.numericalMethods.NumericalMethods;

import java.text.NumberFormat;
import java.util.Locale;

public class TestVectorFunctions {
    public static void main(String[] args) {
        VMF VMF = new VMFSV();
        VAMF vamf1 = new AdditiveVAMFSV();
        vamf1.put(Variable.x, new SinFunction());
        vamf1.put(Variable.y, new PolynomialFunction("2x - 2"));
        vamf1.put(Variable.z, new PolynomialFunction("-x^2"));
        VAMF vamf2 = new AdditiveVAMFSV();
        vamf2.put(Variable.x, new PolynomialFunction("2x"));
        vamf2.put(Variable.y, new CosFunction().andThen(new PolynomialFunction("x - 1")).subtract(new ConstantFunction(0.7)));
        vamf2.put(Variable.z, new PolynomialFunction("x^3"));
        VAMF vamf3 = new MultiplicativeVAMFSV();
        vamf3.put(Variable.x, new CosFunction().sum(new ConstantFunction(1.4)));
        vamf3.put(Variable.y, new CosFunction().negate());
        vamf3.put(Variable.z, new PolynomialFunction("x - 1"));
        VMF.add(vamf1);
        VMF.add(vamf2);
        VMF.add(vamf3);
        Matrix testList = new Matrix(new double[][]{{0.}, {0.}, {0.}});
        Matrix result = NumericalMethods.solveNonlinearSystem(VMF, testList, false);
        System.out.println(VMF);
        result.print(NumberFormat.getInstance(Locale.forLanguageTag("ru")), 5);
    }
}
