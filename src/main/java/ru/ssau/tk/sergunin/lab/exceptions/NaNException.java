package ru.ssau.tk.sergunin.lab.exceptions;

public class NaNException extends RuntimeException {
    private static final long serialVersionUID = 4493009594028149770L;

    public NaNException() {
    }

    public NaNException(String str) {
        super(str);
    }
}
