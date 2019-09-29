package ru.ssau.tk.sergunin.lab.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.sergunin.lab.functions.Exp;
import ru.ssau.tk.sergunin.lab.functions.SqrFunction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class MiddleSteppingDifferentialOperatorTest {

    private final double STEP = 1E-6;
    private final double ACCURACY = 1E-3;

    @Test
    public void testDerive() {
        SteppingDifferentialOperator differentialOperator = new MiddleSteppingDifferentialOperator(STEP);
        assertEquals(differentialOperator.derive(new SqrFunction()).apply(1), 2, ACCURACY);
        assertEquals(differentialOperator.derive(new Exp(2)).apply(5), 32 * Math.log(2), ACCURACY);
        differentialOperator.setStep(2 * STEP);
        assertEquals(differentialOperator.getStep(), 2 * STEP);
        assertThrows(IllegalArgumentException.class, () -> differentialOperator.setStep(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new MiddleSteppingDifferentialOperator(Double.POSITIVE_INFINITY));
    }
}