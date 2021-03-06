package ru.ssau.tk.itenion.io;

import ru.ssau.tk.itenion.functions.exponentialFunctions.ExponentialFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.PowFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.LinkedListTabulatedFunction;
import ru.ssau.tk.itenion.ui.AlertWindows;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        ArrayTabulatedFunction arrayTabulatedFunction = new ArrayTabulatedFunction(new PowFunction(2.5), 0, 10, 11);
        LinkedListTabulatedFunction linkedListTabulatedFunction = new LinkedListTabulatedFunction(new ExponentialFunction(1.5), 0, 10, 11);

        try (BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream("output/array_function.bin"))) {
            FunctionsIO.writeTabulatedFunction(fileWriter, arrayTabulatedFunction);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
        BufferedOutputStream fileWriter = null;
        try {
            fileWriter = new BufferedOutputStream(new FileOutputStream("output/binary_function.bin"));
            FunctionsIO.writeTabulatedFunction(fileWriter, linkedListTabulatedFunction);
        } catch (IOException e) {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                AlertWindows.showError(ex);
            }
        }
    }
}
