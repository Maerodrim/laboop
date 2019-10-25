package ru.ssau.tk.sergunin.lab.io;

import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
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

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) throws IOException {
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
    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        int count;
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        try {
            count = dataInputStream.readInt();
        } catch (NumberFormatException nfe) {
            throw new IOException(nfe);
        }
        double[] xValues = new double[count];
        double[] yValues = new double[count];
        NumberFormat formatter = NumberFormat.getInstance(Locale.forLanguageTag("ru"));
        for (int i = 0; i < count; i++) {
            xValues[i] = dataInputStream.readDouble();
            yValues[i] = dataInputStream.readDouble();
        }
        return factory.create(xValues, yValues);
    }

    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        ObjectOutputStream ous = new ObjectOutputStream(stream);
        ous.writeObject(function);
        ous.flush();
    }

   public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        return (TabulatedFunction) new ObjectInputStream(stream).readObject();
    }
    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(function.getCount());
        for (Point point : function) {
            dataOutputStream.writeDouble ( point.x );
            dataOutputStream.writeDouble ( point.y );
        }
        try {
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
