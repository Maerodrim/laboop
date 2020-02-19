package ru.ssau.tk.sergunin.lab.functions;

public abstract class AbstractMathFunction implements MathFunction {
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
