package ru.ssau.tk.itenion.enums;

public enum BelongTo {
    VALENTIN("Valentin"),
    STANISLAV("Stanislav");

    private final String text;

    BelongTo(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
