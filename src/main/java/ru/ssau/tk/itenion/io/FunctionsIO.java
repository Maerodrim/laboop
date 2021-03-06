package ru.ssau.tk.itenion.io;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

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
        writer.flush();
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(function.getMathFunction());
        objectOutputStream.writeInt(function.getCount());
        objectOutputStream.writeBoolean(function.isStrict());
        objectOutputStream.writeBoolean(function.isUnmodifiable());
        for (Point point : function) {
            objectOutputStream.writeDouble(point.x);
            objectOutputStream.writeDouble(point.y);
        }
        objectOutputStream.flush();
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

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        MathFunction mathFunction = (MathFunction) objectInputStream.readObject();
        int count = objectInputStream.readInt();
        boolean isStrict = objectInputStream.readBoolean();
        boolean isUnmodifiable = objectInputStream.readBoolean();
        double[] xValues = new double[count];
        double[] yValues = new double[count];
        for (int i = 0; i < count; i++) {
            xValues[i] = objectInputStream.readDouble();
            yValues[i] = objectInputStream.readDouble();
        }
        TabulatedFunction function = factory.create(xValues, yValues);
        function.setMathFunction(mathFunction);
        function.offerStrict(isStrict);
        function.offerUnmodifiable(isUnmodifiable);
        return function;
    }

    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        ObjectOutputStream ous = new ObjectOutputStream(stream);
        ous.writeObject(function);
        ous.flush();
    }

    public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        return (TabulatedFunction) new ObjectInputStream(stream).readObject();
    }

    public static void serializeXml(BufferedWriter writer, TabulatedFunction function) throws IOException {
        XStream stream = new XStream();
        stream.toXML(function, writer);
        writer.flush();
    }

    public static <T extends TabulatedFunction> T deserializeXml(BufferedReader reader, Class<?> clazz) {
        XStream stream = new XStream();
        Object function = stream.fromXML(reader);
        clazz.cast(function);
        return (T) function;
    }

    public static void serializeJson(BufferedWriter writer, TabulatedFunction function) throws IOException {
        ObjectMapper stream = new ObjectMapper();
        try {
            writer.write(stream.writeValueAsString(function));
        } catch (JsonMappingException e) {
            throw new IOException(e);
        }
    }

    public static <T extends TabulatedFunction> T deserializeJson(BufferedReader reader, Class<?> clazz) throws IOException {
        ObjectMapper stream = new ObjectMapper();
        try {
            return stream.readerFor(clazz).readValue(reader);
        } catch (JsonMappingException e) {
            throw new IOException(e);
        }
    }

}
