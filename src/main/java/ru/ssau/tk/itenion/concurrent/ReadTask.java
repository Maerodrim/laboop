package ru.ssau.tk.itenion.concurrent;

import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;

public class ReadTask implements Runnable {
    private final TabulatedFunction func;

    public ReadTask(TabulatedFunction func) {
        this.func = func;
    }

    @Override
    public void run() {
        for (int i = 0; func.getCount() > i; i++) {
            synchronized (func) {
                System.out.println("After read:i =" + i + " x =" + func.getX(i) + " y =" + func.getY(i));
            }
        }
    }
}
