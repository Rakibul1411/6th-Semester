package math;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArithmeticOperationsTest {

    private ArithmeticOperations arithmeticOperations;

    @BeforeEach
    public void setUp() {
        arithmeticOperations = new ArithmeticOperations();
    }

    @AfterEach
    public void tearDown() {
    }

    // _____ For Division Operator ______________________________________________________

    @Test
    void divide_positive_numbers() {
        assertEquals(2.5, arithmeticOperations.divide(10, 4), 1e-2);
    }

    @Test
    void divide_positive_numerator_negative_denominator() {
        assertEquals(-2.5, arithmeticOperations.divide(10, -4), 1e-2);
    }

    @Test
    void divide_negative_numerator_positive_denominator() {
        assertEquals(-2.5, arithmeticOperations.divide(-10, 4), 1e-2);
    }

    @Test
    void divide_both_negative_numbers() {
        assertEquals(2.5, arithmeticOperations.divide(-10, -4), 1e-2);
    }

    @Test
    void divide_numerator_zero() {
        assertEquals(0.0, arithmeticOperations.divide(0, 10), 1e-2);
    }

    @Test
    void divide_by_one() {
        assertEquals(7.25, arithmeticOperations.divide(7.25, 1), 1e-2);
    }

    @Test
    void divide_by_negative_one() {
        assertEquals(-7.25, arithmeticOperations.divide(7.25, -1), 1e-2);
    }

    @Test
    void divide_fractional_numbers() {
        assertEquals(2.5, arithmeticOperations.divide(5.5, 2.2), 1e-2);
    }

    @Test
    void divide_large_numbers() {
        assertEquals(1.0, arithmeticOperations.divide(1e308, 1e308), 1e300);
    }

    @Test
    void divide_small_numbers() {
        assertEquals(1.0, arithmeticOperations.divide(1e-308, 1e-308), 1e-310);
    }

    @Test
    void divideDenominatorZero() {
        assertThrows(
                ArithmeticException.class,
                () -> arithmeticOperations.divide(10, 0)
        );
    }

    // ─── For Multiply ────────────────────────────────────────────────────────────

    @Test
    void multiply_zeroXZeroY() {
        assertEquals(0, arithmeticOperations.multiply(0, 0));
    }

    @Test
    void multiply_positiveX_zeroY() {
        assertEquals(0, arithmeticOperations.multiply(5, 0));
    }

    @Test
    void multiply_zeroX_positiveY() {
        assertEquals(0, arithmeticOperations.multiply(0, 7));
    }


    @Test
    void multiply_smallNumbers() {
        assertEquals(12, arithmeticOperations.multiply(3, 4));
    }

    @Test
    void multiply_oneAndMaxValue() {
        assertEquals(Integer.MAX_VALUE, arithmeticOperations.multiply(1, Integer.MAX_VALUE));
    }

    @Test
    void multiply_boundarySquareNumbers() {
        // 46340 * 46340 = 2 147 395 600 < Integer.MAX_VALUE
        assertEquals(2_147_395_600, arithmeticOperations.multiply(46_340, 46_340));
    }


    @Test
    void multiply_negativeX_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> arithmeticOperations.multiply(-1,  5)
        );
        assertEquals("x & y should be >= 0", exception.getMessage());
    }

    @Test
    void multiply_negativeY_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> arithmeticOperations.multiply(5, -1)
        );
        assertEquals("x & y should be >= 0", exception.getMessage());
    }

    @Test
    void multiply_bothNegative_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> arithmeticOperations.multiply(-3, -7)
        );
        assertEquals("x & y should be >= 0", exception.getMessage());
    }


    @Test
    void multiply_xTooLargeForY_throwsOverflowException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> arithmeticOperations.multiply(2, Integer.MAX_VALUE)
        );
        assertEquals("The product does not fit in an Integer variable", exception.getMessage());
    }

    @Test
    void multiply_yTooLargeForX_throwsOverflowException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> arithmeticOperations.multiply(Integer.MAX_VALUE, 2)
        );
        assertEquals("The product does not fit in an Integer variable", exception.getMessage());
    }
}