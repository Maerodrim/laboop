package ru.ssau.tk.itenion.functions;

import java.io.Serializable;

public abstract class AbstractMathFunction implements MathFunction, Serializable {
    private static final long serialVersionUID = -3394964029296749196L;
    protected String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
