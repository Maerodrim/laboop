package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import java.util.HashMap;
import java.util.Map;

public enum SupportedSign {
    SUM("+"),
    MULTIPLY("*");

    private static final Map<String, SupportedSign> lookup = new HashMap<>();

    static {
        for (SupportedSign env : SupportedSign.values()) {
            lookup.put(env.toString(), env);
        }
    }

    private final String text;

    SupportedSign(final String text) {
        this.text = text;
    }

    public static SupportedSign get(String url) {
        return lookup.get(url);
    }

    @Override
    public String toString() {
        return text;
    }

    public AbstractVAMF getVAMF() {
        switch (this) {
            case MULTIPLY: {
                return new MultiplicativeVAMFSV();
            }
            case SUM: {
                return new AdditiveVAMFSV();
            }
        }
        return null;
    }
}
