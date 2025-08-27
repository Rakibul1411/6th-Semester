package math;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyMathTest {

    private MyMath myMath;

    @BeforeEach
    void setUp() {
        myMath = new MyMath();
    }

    @AfterEach
    void tearDown() {
        myMath = null;
    }

    // ✅ Existing test case-1 for valid factorial
    @Test
    void factorialWithinRange() {
        int expectedValue = 720;
        int actualValue = myMath.factorial(6);
        assertEquals(expectedValue, actualValue);
    }

    // ✅ Existing test case-2 for upper-bound violation
    @Test
    void factorialOutsideRange() {
        assertThrows(IllegalArgumentException.class,
                () -> myMath.factorial(13));
    }

    // ✅ Existing test case-3 for valid prime
    @Test
    void isPrime() {
        boolean expectedValue = true;
        boolean actualValue = myMath.isPrime(7);
        assertEquals(expectedValue, actualValue);
    }

    // ✅ Existing test case-4 for invalid prime input
    @Test
    void isPrimeLessThanTwo() {
        assertThrows(IllegalArgumentException.class,
                () -> myMath.isPrime(-2));
    }

    // 🔼 Additional tests for factorial edge cases
    // Additional test case-1 for factorial of zero
    @Test
    void factorialOfZero() {
        assertEquals(1, myMath.factorial(0));
    }

    // Additional test case-2 for factorial of one
    @Test
    void factorialOfOne() {
        assertEquals(1, myMath.factorial(1));
    }

    // Additional test case-3 for factorial of twelve (upper boundary)
    @Test
    void factorialUpperBoundary() {
        assertEquals(479001600, myMath.factorial(12));
    }

    // Additional test case-4 for factorial of negative number (lower boundary)
    @Test
    void factorialLowerBoundary() {
        assertThrows(IllegalArgumentException.class,
                () -> myMath.factorial(-1));
    }

    // Additional test case-5 for factorial of two
    @Test
    void factorialOfTwo() {
        assertEquals(2, myMath.factorial(2));
    }

    // 🔼 Additional tests for isPrime edge cases
    // Additional test case-1 for isPrime with negative number
    @Test
    void testIsPrimeWithTwo() {
        assertTrue(myMath.isPrime(2));
    }

    // Additional test case-2 for isPrime with one
    @Test
    void testIsPrimeWithFour() {
        assertFalse(myMath.isPrime(4));
    }

    // Additional test case-3 for isPrime with zero
    @Test
    void testIsPrimeWithNine() {
        assertFalse(myMath.isPrime(9));
    }

    // Additional test case-4 for isPrime with negative number
    @Test
    void testIsPrimeWithEleven() {
        assertTrue(myMath.isPrime(11));
    }
}
