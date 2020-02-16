package ru.ssau.tk.sergunin.lab.concurrent;

import ru.ssau.tk.sergunin.lab.functions.tabulatedFunctions.TabulatedFunction;

public class WriteTask implements Runnable {
    private final TabulatedFunction tabulatedFunction;
    private double value;

    WriteTask(TabulatedFunction tabulatedFunction, double value) {
        this.value = value;
        this.tabulatedFunction = tabulatedFunction;
    }

    @Override
    public void run() {
        for (int i = 0; i < tabulatedFunction.getCount(); i++) {
            synchronized (tabulatedFunction) {
                tabulatedFunction.setY(i, value);
                System.out.printf("Writing for index %d complete\n", i);
            }
        }
    }
}
