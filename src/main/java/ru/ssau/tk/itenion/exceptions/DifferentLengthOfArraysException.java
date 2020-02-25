package ru.ssau.tk.itenion.exceptions;

public class DifferentLengthOfArraysException extends RuntimeException {
    private static final long serialVersionUID = 5063750975562822369L;

    public DifferentLengthOfArraysException() {
    }

    public DifferentLengthOfArraysException(String str) {
        super(str);
    }
}
