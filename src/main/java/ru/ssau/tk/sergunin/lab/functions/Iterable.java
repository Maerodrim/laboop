package ru.ssau.tk.sergunin.lab.functions;

import java.util.Iterator;

public interface Iterable<Point> extends java.lang.Iterable {
    Iterator<Point> iterator();
}
