package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.ExpFunction;
import ru.ssau.tk.sergunin.lab.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.PowFunction;

import java.io.*;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        ArrayTabulatedFunction arrayTabulatedFunction = new ArrayTabulatedFunction(new PowFunction(2.5), 0, 10, 11);
        LinkedListTabulatedFunction linkedListTabulatedFunction = new LinkedListTabulatedFunction(new ExpFunction(1.5), 0, 10, 11);

        try (BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(String.valueOf(new FileWriter("output/array_function.txt"))))) {
            FunctionsIO.writeTabulatedFunction(fileWriter, arrayTabulatedFunction);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedOutputStream fileWriter = null;
        try {
            fileWriter = new BufferedOutputStream(new FileOutputStream(String.valueOf(new FileWriter("output/linked_list_function.txt"))));
            FunctionsIO.writeTabulatedFunction(fileWriter, linkedListTabulatedFunction);
        } catch (IOException e) {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        System.out.println(Math.pow(1, Double.NaN));
    }
}
