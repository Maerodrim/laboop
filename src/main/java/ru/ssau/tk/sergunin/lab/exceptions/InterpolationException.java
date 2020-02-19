package ru.ssau.tk.sergunin.lab.exceptions;

public class InterpolationException extends RuntimeException {
    private static final long serialVersionUID = -7408640052321111946L;

    public InterpolationException() {
    }

    public InterpolationException(String str) {
        super(str);
    }
}
