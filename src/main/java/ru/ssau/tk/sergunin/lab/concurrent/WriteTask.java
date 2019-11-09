package ru.ssau.tk.sergunin.lab.concurrent;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public class WriteTask implements Runnable {
    private TabulatedFunction func;
    private Double value;

    public WriteTask(TabulatedFunction func, Double value) {
        this.func = func;
        this.value = value;
    }

    @Override
    public void run() {
        for (int i = 0; func.getCount() > i; i++) {
            func.setY(i, value);
            System.out.println("Writing for index " + i + " complete");
        }

    }
}
