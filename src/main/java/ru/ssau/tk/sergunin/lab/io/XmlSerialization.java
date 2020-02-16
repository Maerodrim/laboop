package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.powerFunctions.PowFunction;

import java.io.*;

import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.deserializeXml;
import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.serializeXml;

public class XmlSerialization {

    public static void main(String[] args) {
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(new PowFunction(2.5), 0, 10, 11);
        try (BufferedWriter outputStream = new BufferedWriter(new FileWriter("output/array_function.xml"))) {
            serializeXml(outputStream, function);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader inputStream = new BufferedReader(new FileReader("output/array_function.xml"))) {
            System.out.println(deserializeXml(inputStream, ArrayTabulatedFunction.class).toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
