package ru.ssau.tk.itenion.ui;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IndexAnnotated
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface ConnectableItem {
    String name();

    int priority() default 0;

    Item type();

    boolean hasParameter() default false;

    boolean parameterInstanceOfDouble() default true;

    //boolean methodOnlyForPolynomialFunction() default false;
    boolean numericalOperator() default true;

    String pathFXML() default "";
}