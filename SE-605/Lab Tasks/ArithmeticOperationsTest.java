package math;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticOperationsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void divide() {
    }

    @Test
    void multiply() {
    }

    private final ArithmeticOperations arithmeticOperations = new ArithmeticOperations();

    // Tests for divide method

    @Test
    public void testDivideNormalCase() {
        double result = arithmeticOperations.divide(10.0, 2.0);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testDivideWithNegativeNumbers() {
        double result1 = arithmeticOperations.divide(-10.0, 2.0);
        assertEquals(-5.0, result1, 0.0001);

        double result2 = arithmeticOperations.divide(10.0, -2.0);
        assertEquals(-5.0, result2, 0.0001);

        double result3 = arithmeticOperations.divide(-10.0, -2.0);
        assertEquals(5.0, result3, 0.0001);
    }

    @Test
    public void testDivideWithZeroNumerator() {
        double result = arithmeticOperations.divide(0.0, 5.0);
        assertEquals(0.0, result, 0.0001);
    }

//    @Test(expected = ArithmeticException.class)
//    public void testDivideByZero() {
//        arithmeticOperations.divide(10.0, 0.0);
//    }

    @Test
    public void testDivideWithLargeNumbers() {
        double result = arithmeticOperations.divide(Double.MAX_VALUE, 2.0);
        assertEquals(Double.MAX_VALUE / 2.0, result, 0.0001);
    }

    @Test
    public void testDivideWithSmallNumbers() {
        double result = arithmeticOperations.divide(Double.MIN_VALUE, 2.0);
        assertEquals(Double.MIN_VALUE / 2.0, result, 0.0001);
    }

    // Tests for multiply method

    @Test
    public void testMultiplyNormalCase() {
        int result = arithmeticOperations.multiply(5, 6);
        assertEquals(30, result);
    }

    @Test
    public void testMultiplyWithZero() {
        int result1 = arithmeticOperations.multiply(0, 5);
        assertEquals(0, result1);

        int result2 = arithmeticOperations.multiply(5, 0);
        assertEquals(0, result2);
    }

    @Test
    public void testMultiplyWithOne() {
        int result1 = arithmeticOperations.multiply(1, 5);
        assertEquals(5, result1);

        int result2 = arithmeticOperations.multiply(5, 1);
        assertEquals(5, result2);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void testMultiplyWithNegativeFirstArg() {
//        arithmeticOperations.multiply(-5, 6);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testMultiplyWithNegativeSecondArg() {
//        arithmeticOperations.multiply(5, -6);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testMultiplyWithBothNegativeArgs() {
//        arithmeticOperations.multiply(-5, -6);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testMultiplyWithOverflow() {
//        arithmeticOperations.multiply(Integer.MAX_VALUE, 2);
//    }

    @Test
    public void testMultiplyBoundaryWithoutOverflow() {
        int result = arithmeticOperations.multiply(46340, 46340);
        assertEquals(2147395600, result);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void testMultiplyBoundaryWithOverflow() {
//        arithmeticOperations.multiply(46341, 46341);
//    }
}