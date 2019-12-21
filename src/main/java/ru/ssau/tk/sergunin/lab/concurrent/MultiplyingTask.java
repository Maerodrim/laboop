package ru.ssau.tk.sergunin.lab.concurrent;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;

public class MultiplyingTask implements Runnable {

    private final TabulatedFunction function;
    private boolean isCompleted;

    MultiplyingTask(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public void run() {
        for (int i = 0; i < function.getCount(); i++) {
            synchronized (function) {
                function.setY(i, 2 * function.getY(i));
            }
        }
        System.out.println("The " + Thread.currentThread().getName() + " has completed execution");
        isCompleted = true;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
