package ru.ssau.tk.sergunin.lab.concurrent;

import ru.ssau.tk.sergunin.lab.functions.powerFunctions.ConstantFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.LinkedListTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        TabulatedFunction listTabulatedFunction = new LinkedListTabulatedFunction(new ConstantFunction(-3), 1, 500, 500);
        Thread readThread = new Thread(new ReadTask(listTabulatedFunction));
        Thread writeThread = new Thread(new WriteTask(listTabulatedFunction, 4));
        writeThread.start();
        readThread.start();
    }
}

