package ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorArgumentMathFunctions;

import org.atteo.classindex.IndexAnnotated;
import ru.ssau.tk.itenion.enums.SupportedSign;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface SignAnnotation {
    SupportedSign supportedSign();
}
