package ru.ssau.tk.sergunin.lab.functions;

import java.io.*;

public interface MathFunction extends Serializable {

    double apply(double x);

    default CompositeFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }

    default MathFunction sum(MathFunction afterFunction) {
        return x -> this.apply(x) + afterFunction.apply(x);
    }

    default MathFunction subtract(MathFunction afterFunction) {
        return x -> this.apply(x) - afterFunction.apply(x);
    }

    default MathFunction multiply(double number) {
        return x -> number * this.apply(x);
    }

    default MathFunction multiply(MathFunction afterFunction) {
        return x -> this.apply(x) * afterFunction.apply(x);
    }

    default MathFunction divide(MathFunction afterFunction) {
        if (afterFunction instanceof ZeroFunction) {
            throw new IllegalArgumentException();
        }
        return x -> this.apply(x) / afterFunction.apply(x);
    }

    default MathFunction copy() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous;
            ous = new ObjectOutputStream(baos);
            ous.writeObject(this);
            ous.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (MathFunction) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
