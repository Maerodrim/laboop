package ru.ssau.tk.sergunin.lab.alt_ui;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IndexAnnotated
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface SelectableFunction {
    String name() default "function";

    int priority() default 0;

    boolean parameter() default false;
}
