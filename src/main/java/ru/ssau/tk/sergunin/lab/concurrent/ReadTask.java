package ru.ssau.tk.sergunin.lab.concurrent;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public class ReadTask implements Runnable {
    private TabulatedFunction func;

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
