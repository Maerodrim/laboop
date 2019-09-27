package ru.ssau.tk.sergunin.lab.exceptions;

public class ArrayIsNotSortedException extends RuntimeException {
    public ArrayIsNotSortedException() {
    }

    public ArrayIsNotSortedException(String str) {
        super(str);
    }
}
