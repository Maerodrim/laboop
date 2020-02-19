package ru.ssau.tk.sergunin.lab.exceptions;

public class InconsistentFunctionsException extends RuntimeException {
    private static final long serialVersionUID = -8438376385724329798L;

    public InconsistentFunctionsException() {
    }

    public InconsistentFunctionsException(String str) {
        super(str);
    }

    public InconsistentFunctionsException(String str, RuntimeException cause) {
        super(str, cause);
    }
}
