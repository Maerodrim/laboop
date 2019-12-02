package ru.ssau.tk.sergunin.lab.concurrent;

import ru.ssau.tk.sergunin.lab.functions.ConstantFunction;
import ru.ssau.tk.sergunin.lab.functions.LinkedListTabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(new ConstantFunction(-1), 0, 20, 2001);
        Thread taskReadThread = new Thread(new ReadTask(function));
        Thread taskWriteThread = new Thread(new WriteTask(function, 2.));
        synchronized (function) {
            taskReadThread.start();
            taskWriteThread.start();
        }
    }
}

