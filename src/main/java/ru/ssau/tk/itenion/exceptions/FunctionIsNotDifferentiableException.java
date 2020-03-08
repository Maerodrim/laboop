package ru.ssau.tk.itenion.exceptions;

import java.io.Serializable;

public class FunctionIsNotDifferentiableException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 7127514080248841481L;

    public FunctionIsNotDifferentiableException() {
    }

    public FunctionIsNotDifferentiableException(String str) {
        super(str);
    }
}
