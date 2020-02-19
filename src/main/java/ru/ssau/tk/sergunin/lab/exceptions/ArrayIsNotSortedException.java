package ru.ssau.tk.sergunin.lab.exceptions;

public class ArrayIsNotSortedException extends RuntimeException {
    private static final long serialVersionUID = 6749803192172180019L;

    public ArrayIsNotSortedException() {
    }

    public ArrayIsNotSortedException(String str) {
        super(str);
    }
}
