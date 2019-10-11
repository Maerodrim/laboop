package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException();
    }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) {
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.println(function.getCount());
        for (Point point : function) {
            printWriter.printf("%f %f\n", point.x, point.y);
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        int count;
        try {
            count = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException nfe) {
            throw new IOException(nfe);
        }
        double[] xValues = new double[count];
        double[] yValues = new double[count];
        NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("ru"));
        String tempString;
        for (int i = 0; i < count; i++) {
            tempString = reader.readLine();
            try {
                xValues[i] = formatter.parse(tempString.split(" ")[0]).doubleValue();
                yValues[i] = formatter.parse(tempString.split(" ")[1]).doubleValue();
            } catch (ParseException pe) {
                throw new IOException(pe);
            }
        }
        return factory.create(xValues, yValues);
    }

    static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        ObjectOutputStream ous = new ObjectOutputStream(stream);
        ous.writeObject(function);
        ous.flush();
    }

    static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        return (TabulatedFunction) new ObjectInputStream(stream).readObject();
    }
}
