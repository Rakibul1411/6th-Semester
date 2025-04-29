package io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileIOTest {

    private FileIO fileIO;

    @BeforeEach
    void setUp() {
        fileIO = new FileIO();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readEmptyFile() {
        String filePath = "/Users/md.rakibulislam/IIT/6th Semester/SE-605/Lab Tasks/unittesting/src/test/resources/empty_file.txt";
        assertThrows(IllegalArgumentException.class,
                () -> fileIO.readFile(filePath)
        );
    }

    @Test
    void nonExistentFile() {
        String filePath = "/Users/md.rakibulislam/IIT/6th Semester/SE-605/Lab Tasks/unittesting/src/test/resources/non_existent_file.txt";
        assertThrows(IllegalArgumentException.class,
                () -> fileIO.readFile(filePath)
        );
    }

//    @Test
//    void checkInvalidEntries() {
//        String filePath = "/Volumes/T7 Shield/IIT Folder/Semesters/6th Semester/SE-605/Lab Tasks/unittesting/src/test/resources/grades_invalid.txt";
//        assertThrows(NumberFormatException.class,
//                () -> fileIO.readFile(filePath)
//        );
//    }

    @Test
    void checkInvalidEntriesAreSkipped() {
        String filePath = "/Users/md.rakibulislam/IIT/6th Semester/SE-605/Lab Tasks/unittesting/src/test/resources/grades_invalid.txt";

        // 1) No exception should be thrown
        int[] actual = assertDoesNotThrow(
                () -> fileIO.readFile(filePath),
                "readFile should skip invalid lines rather than throw"
        );

        // 2) Only the 7 valid integers should remain, in file order:
        int[] expected = { 3, 9, 2, 10, 8, 0, 3 };
        assertArrayEquals(
                expected,
                actual,
                "Invalid lines (a, 9.42, b) should be skipped"
        );
    }

    @Test
    void checkValidEntries() {
        String filePath = "/Users/md.rakibulislam/IIT/6th Semester/SE-605/Lab Tasks/unittesting/src/test/resources/grades_valid.txt";
        int[] expectedArrayValues = {3, 9, 0, 2, 10, 9, 3, 8, 0, 3};
        int[] actualArrayValues = fileIO.readFile(filePath);

        assertArrayEquals(expectedArrayValues, actualArrayValues);
    }
}