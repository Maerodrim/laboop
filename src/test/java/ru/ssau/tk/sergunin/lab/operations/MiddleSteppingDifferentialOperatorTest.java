package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.ExpFunction;
import ru.ssau.tk.sergunin.lab.functions.SqrFunction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class MiddleSteppingDifferentialOperatorTest {

    @Test
    public void testDerive() {
        double STEP = 1E-6;
        SteppingDifferentialOperator differentialOperator = new MiddleSteppingDifferentialOperator(STEP);
        double ACCURACY = 1E-3;
        assertEquals(differentialOperator.derive(new SqrFunction()).apply(1), 2, ACCURACY);
        assertEquals(differentialOperator.derive(new ExpFunction(2)).apply(5), 32 * Math.log(2), ACCURACY);
        differentialOperator.setStep(2 * STEP);
        assertEquals(differentialOperator.getStep(), 2 * STEP);
        assertThrows(IllegalArgumentException.class, () -> differentialOperator.setStep(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new MiddleSteppingDifferentialOperator(Double.POSITIVE_INFINITY));
    }
}