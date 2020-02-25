package ru.ssau.tk.itenion.io;

import ru.ssau.tk.itenion.functions.powerFunctions.PowFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.ArrayTabulatedFunction;
import ru.ssau.tk.itenion.ui.AlertWindows;

import java.io.*;

public class XmlSerialization {

    public static void main(String[] args) {
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(new PowFunction(2.5), 0, 10, 11);
        try (BufferedWriter outputStream = new BufferedWriter(new FileWriter("output/array_function.xml"))) {
            FunctionsIO.serializeXml(outputStream, function);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
        try (BufferedReader inputStream = new BufferedReader(new FileReader("output/array_function.xml"))) {
            System.out.println(FunctionsIO.deserializeXml(inputStream, ArrayTabulatedFunction.class).toString());
        } catch (IOException ioe) {
            AlertWindows.showError(ioe);
        }
    }
}
