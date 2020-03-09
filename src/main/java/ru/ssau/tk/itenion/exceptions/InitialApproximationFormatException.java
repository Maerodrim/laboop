package ru.ssau.tk.itenion.exceptions;

public class InitialApproximationFormatException extends RuntimeException {
    private static final long serialVersionUID = 8426179237997995104L;

    public InitialApproximationFormatException() {
    }

    public InitialApproximationFormatException(String str) {
        super(str);
    }

    public InitialApproximationFormatException(String str, RuntimeException cause) {
        super(str, cause);
    }
}
