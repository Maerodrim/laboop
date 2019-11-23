package ru.ssau.tk.sergunin.lab.concurrent;

import ru.ssau.tk.sergunin.lab.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.UnitFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiplyingTaskExecutor {
    private static final int NUMBER_OF_THREADS = 15;

    public static void main(String[] args) {
        TabulatedFunction function = new LinkedListTabulatedFunction(new UnitFunction(), 1, 1000, 1001);
        List<Thread> threads = new ArrayList<>();
        Collection<MultiplyingTask> collection = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            MultiplyingTask task = new MultiplyingTask(function);
            collection.add(task);
        }
        for (MultiplyingTask task : collection) {
            Thread thread = new Thread(task);
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        while (!collection.isEmpty()) {
            Thread.onSpinWait();
            collection.removeIf(MultiplyingTask::isCompleted);
        }
        System.out.println(function);
    }
}
