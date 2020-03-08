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
    /**
     * @return general parameters
     */
    String name();

    int priority() default 0;

    Item type();

    /**
     * @return function parameters
     */
    boolean hasParameter() default false;

    Class<?> parameterInstance() default Double.class;

    boolean isAdjacentConstant() default false;

    /**
     * @return operators parameters
     */
    boolean numericalOperator() default true;

    /**
     * @return controller parameters
     */
    String pathFXML() default "";

    /**
     * @return VMF
     */
    boolean forVMF() default false;
}