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
    }

    @Test
    void factorialWithinRange() {
        int expectedValue = 720;
        int actualValue = myMath.factorial(6);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void factorialOutsideRange() {
        assertThrows(IllegalArgumentException.class,
                () -> myMath.factorial(13)
        );
    }

    @Test
    void isPrime() {
        boolean expectedValue = true;
        boolean actualValue = myMath.isPrime(7);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void isPrimeLessThanTwo() {
        assertThrows(IllegalArgumentException.class,
                () -> myMath.isPrime(-2)
        );
    }
}