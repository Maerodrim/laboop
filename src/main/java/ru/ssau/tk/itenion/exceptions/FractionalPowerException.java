package ru.ssau.tk.itenion.exceptions;

import java.io.Serializable;

public class FractionalPowerException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 7127514080248841481L;

    public FractionalPowerException() {
    }

    public FractionalPowerException(String str) {
        super(str);
    }
}
