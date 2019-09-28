package ru.ssau.tk.sergunin.lab.exceptions;

public class InconsistentFunctionsException extends RuntimeException {
    public InconsistentFunctionsException() {
    }

    public InconsistentFunctionsException(String str) {
        super(str);
    }

    public InconsistentFunctionsException(String str, RuntimeException cause) {
        super(str, cause);
    }
}
