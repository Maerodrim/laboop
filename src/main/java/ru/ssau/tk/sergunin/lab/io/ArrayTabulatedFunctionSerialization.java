package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.PowFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.operations.TabulatedDifferentialOperator;

import java.io.*;

import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.deserialize;
import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.serialize;

public class ArrayTabulatedFunctionSerialization {
    public static void main(String[] args) {
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(new PowFunction(3), 0, 20, 2001);
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());
        TabulatedFunction firstDerivative = differentialOperator.derive(function);
        TabulatedFunction secondDerivative = differentialOperator.derive(firstDerivative);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("output/serialized_array_functions.bin"))) {
            serialize(outputStream, function);
            serialize(outputStream, firstDerivative);
            serialize(outputStream, secondDerivative);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("output/serialized_array_functions.bin"))) {
            System.out.println(deserialize(inputStream).toString() + '\n' + deserialize(inputStream).toString() + '\n' + deserialize(inputStream));
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
    }

}
