package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.PowFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.operations.TabulatedDifferentialOperator;

import java.io.*;

import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.deserialize;
import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.serialize;

public class LinkedListTabulatedFunctionSerialization {
    public static void main(String[] args) {
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(new PowFunction(3), 0, 20, 2001);
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
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
            System.out.println(deserialize(inputStream).toString() + '\n' + deserialize(inputStream).toString() + '\n' + deserialize(inputStream).toString());
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
    }
}
