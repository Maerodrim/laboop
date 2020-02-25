package ru.ssau.tk.itenion.operations;

import org.testng.annotations.Test;
import ru.ssau.tk.itenion.functions.exponentialFunctions.ExponentialFunction;
import ru.ssau.tk.itenion.functions.powerFunctions.SqrFunction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class RightSteppingDifferentialOperatorTest {

    @Test
    public void testDerive() {
        double STEP = 1E-6;
        SteppingDifferentialOperator differentialOperator = new RightSteppingDifferentialOperator(STEP);
        double ACCURACY = 1E-3;
        assertEquals(differentialOperator.derive(new SqrFunction()).apply(1.), 2, ACCURACY);
        assertEquals(differentialOperator.derive(new ExponentialFunction(2)).apply(5.), 32 * Math.log(2), ACCURACY);
        differentialOperator.setStep(2 * STEP);
        assertEquals(differentialOperator.getStep(), 2 * STEP);
        assertThrows(IllegalArgumentException.class, () -> differentialOperator.setStep(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new RightSteppingDifferentialOperator(Double.NaN));
    }
}