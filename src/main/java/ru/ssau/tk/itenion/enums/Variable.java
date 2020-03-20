package ru.ssau.tk.itenion.enums;

public enum Variable {
    x,
    y;

    public Variable getAnotherVariable(){
        if (Variable.values().length == 2) {
            if (this.equals(Variable.x)) {
                return Variable.y;
            } else {
                return Variable.x;
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
