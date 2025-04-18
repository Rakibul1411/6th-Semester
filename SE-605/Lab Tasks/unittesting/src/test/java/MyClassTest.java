import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyClassTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @org.junit.jupiter.api.Test
    void div() {
        float expected = 2.0F;
        float actual = (new MyClass().div(10, 5));

        assertEquals(expected, actual, 1e-3);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivByZero() {
        (new MyClass()).div(5, 0);
    }
}