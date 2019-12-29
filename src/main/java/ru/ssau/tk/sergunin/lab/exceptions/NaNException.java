package ru.ssau.tk.sergunin.lab.exceptions;

public class NaNException extends RuntimeException {
    public NaNException() {
    }

    public NaNException(String str) {
        super(str);
    }
}
