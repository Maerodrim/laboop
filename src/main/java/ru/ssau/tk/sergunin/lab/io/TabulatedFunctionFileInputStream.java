package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.sergunin.lab.ui.AlertWindows;

import java.io.*;

import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.readTabulatedFunction;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {
        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream("output/array_function.bin"))) {
            System.out.println(FunctionsIO.readTabulatedFunction(reader, new LinkedListTabulatedFunctionFactory()).toString());
        } catch (IOException | ClassNotFoundException e) {
            AlertWindows.showError(e);
        }
        BufferedInputStream readerStream = null;
        try {
            readerStream = new BufferedInputStream(new FileInputStream("input/binary_function.bin"));
            System.out.println(FunctionsIO.readTabulatedFunction(readerStream, new ArrayTabulatedFunctionFactory()).toString());
        } catch (IOException | ClassNotFoundException e) {
            try {
                readerStream.close();
            } catch (IOException ex) {
                AlertWindows.showError(ex);
            }
        }
        try {
            System.out.println("Введите размер и значения функции:");
            System.out.println(new TabulatedDifferentialOperator().derive(readTabulatedFunction(new BufferedReader(new InputStreamReader(System.in)), new LinkedListTabulatedFunctionFactory())));
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
    }
}
