package ru.ssau.tk.itenion.io;

import ru.ssau.tk.itenion.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.powerFunctions.PowFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.LinkedListTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.itenion.ui.AlertWindows;

import java.io.*;

public class LinkedListTabulatedFunctionSerialization {
    private static final int MAX_NUMBER = 853; // the maximum number of values in a function that is allowed for serialization

    public static void main(String[] args) {
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(new PowFunction(3.), 0, 20, MAX_NUMBER);
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction firstDerivative = differentialOperator.derive(function);
        TabulatedFunction secondDerivative = differentialOperator.derive(firstDerivative);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("output/serialized_array_functions.bin"))) {
            FunctionsIO.serialize(outputStream, function);
            FunctionsIO.serialize(outputStream, firstDerivative);
            FunctionsIO.serialize(outputStream, secondDerivative);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("output/serialized_array_functions.bin"))) {
            System.out.println(FunctionsIO.deserialize(inputStream).toString() + '\n' + FunctionsIO.deserialize(inputStream).toString() + '\n' + FunctionsIO.deserialize(inputStream).toString());
        } catch (IOException | ClassNotFoundException ioe) {
            AlertWindows.showError(ioe);
        }
    }
}
