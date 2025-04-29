package math;

import io.FileIO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ArrayOperationsTest {

    private ArrayOperations arrayOperations;
    private FileIO fileIO;
//    private MyMath myMath;

    @BeforeEach
    void setUp() {
        arrayOperations = new ArrayOperations();
        fileIO = new FileIO();
//        myMath = new MyMath();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findPrimesInFile() {
        String filePath = "/Users/md.rakibulislam/IIT/6th Semester/SE-605/Lab Tasks/unittesting/src/test/resources/grades_valid.txt";

        MyMath safeMyMath = new MyMath() {
            @Override
            public boolean isPrime(int n) {
                try {
                    return super.isPrime(n);
                } catch (IllegalArgumentException e) {
                    // any n < 2 → just “not prime”
                    return false;
                }
            }
        };

        int[] expectedPrimes = {3, 2, 3, 3};
        int[] actualPrimes   = arrayOperations.findPrimesInFile(fileIO, filePath, safeMyMath);

        assertArrayEquals(expectedPrimes, actualPrimes);
    }
}