package ru.ssau.tk.itenion.exceptions;

public class InconsistentMatrixSize extends RuntimeException {
    private static final long serialVersionUID = -3492746716348048280L;

    public InconsistentMatrixSize() {
    }

    public InconsistentMatrixSize(String str) {
        super(str);
    }

    public InconsistentMatrixSize(String str, RuntimeException cause) {
        super(str, cause);
    }
}
