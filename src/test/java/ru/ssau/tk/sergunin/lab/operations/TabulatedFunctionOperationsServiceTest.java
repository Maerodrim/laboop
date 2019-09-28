package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.Point;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;

import static org.testng.Assert.*;

public class TabulatedFunctionOperationsServiceTest {

    @Test
    public void testAsPoints() {
        TabulatedFunction list = (new ArrayTabulatedFunctionFactory()).create(new double[]{1,2,3}, new double[]{2,4,6});
        Point[] points = TabulatedFunctionOperationsService.asPoints(list);
        int i = 0;
        for(Point point : points) {
            assertEquals(point.x, list.getX(i));
            assertEquals(point.y, list.getY(i++));
        }
    }
}