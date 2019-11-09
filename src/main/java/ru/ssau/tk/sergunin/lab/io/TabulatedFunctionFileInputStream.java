package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.operations.TabulatedDifferentialOperator;

import java.io.*;

import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.readTabulatedFunction;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {
        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream("output/array_function.bin"))) {
            System.out.println(FunctionsIO.readTabulatedFunction(reader, new LinkedListTabulatedFunctionFactory()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream readerStream = null;
        try {
            readerStream = new BufferedInputStream(new FileInputStream("input/binary_function.bin"));
            System.out.println(FunctionsIO.readTabulatedFunction(readerStream, new ArrayTabulatedFunctionFactory()).toString());
        } catch (IOException e) {
            try {
                readerStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            System.out.println("Введите размер и значения функции:");
            System.out.println(new TabulatedDifferentialOperator().derive(readTabulatedFunction(new BufferedReader(new InputStreamReader(System.in)), new LinkedListTabulatedFunctionFactory())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
