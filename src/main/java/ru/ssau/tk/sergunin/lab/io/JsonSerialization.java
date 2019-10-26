package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.PowFunction;

import java.io.*;

import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.deserializeJson;
import static ru.ssau.tk.sergunin.lab.io.FunctionsIO.serializeJson;

public class JsonSerialization {

    public static void main(String[] args) {
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(new PowFunction(2.5), 0, 10, 11);
        try (BufferedWriter outputStream = new BufferedWriter(new FileWriter("output/array_function.json"))) {
            serializeJson(outputStream, function);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader inputStream = new BufferedReader(new FileReader("output/array_function.json"))) {
            System.out.println(deserializeJson(inputStream).toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
