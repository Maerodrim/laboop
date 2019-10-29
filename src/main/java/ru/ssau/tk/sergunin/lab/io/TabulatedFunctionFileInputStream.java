package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.functions.factory.LinkedListTabulatedFunctionFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {
        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream("input/binary_function.bin"))) {
            System.out.println(FunctionsIO.readTabulatedFunction(reader, new LinkedListTabulatedFunctionFactory()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream reader = null;
        try {
            reader = new BufferedInputStream(new FileInputStream("input/binary_function.bin"));
            System.out.println(FunctionsIO.readTabulatedFunction(reader, new ArrayTabulatedFunctionFactory()).toString());
        } catch (IOException e) {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
