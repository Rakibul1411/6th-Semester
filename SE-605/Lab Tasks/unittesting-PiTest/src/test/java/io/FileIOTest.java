package io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileIOTest {

    private FileIO fileIO;

    @BeforeEach
    void setUp() {
        fileIO = new FileIO();
    }

    @AfterEach
    void tearDown() {
        fileIO = null;
    }

    private String getPath(String resourceName) {
        try {
            return Paths.get(getClass().getClassLoader().getResource(resourceName).toURI()).toString();
        } catch (URISyntaxException | NullPointerException e) {
            throw new IllegalArgumentException("Resource not found: " + resourceName, e);
        }
    }

    // Existing test case-1 for a file with all valid lines
    @Test
    void testValidFile_AllValidLines() {
        String filePath = getPath("grades_valid.txt");
        int[] expected = {3, 9, 0, 2, 10, 9, 3, 8, 0, 3};
        int[] result = fileIO.readFile(filePath);
        assertArrayEquals(expected, result);
    }

    // Existing test case-2 for a file with mixed valid and invalid lines
    @Test
    void testInvalidFile_MixedValidAndInvalidLines() {
        String filePath = getPath("grades_invalid.txt");
        int[] expected = {3, 9, 2, 10, 8, 0, 3};
        int[] result = fileIO.readFile(filePath);
        assertArrayEquals(expected, result);
    }

    // Existing test case-3 for a file with only invalid lines
    @Test
    void testFileWithOnlyInvalidLines_ThrowsException() {
        String filePath = getPath("only_invalid.txt");
        assertThrows(IllegalArgumentException.class, () -> fileIO.readFile(filePath));
    }

    // Existing test case-4 for an empty file
    @Test
    void testEmptyFile_ThrowsException() {
        String filePath = getPath("empty_file.txt");
        assertThrows(IllegalArgumentException.class, () -> fileIO.readFile(filePath));
    }

    // Existing test case-5 for a file does not exist
    @Test
    void testFileDoesNotExist_ThrowsException_WithMessage() {
        String filePath = "nonexistent.txt";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> fileIO.readFile(filePath));
        assertTrue(exception.getMessage().contains("Input file does not exist"));
    }


    // New test case-1: IOException when trying to read a directory instead of a file
    @Test
    void testIOExceptionWithDirectoryInsteadOfFile() {
        String directoryPath = getPath(""); // Should resolve to /resources or base dir
        Exception exception = assertThrows(RuntimeException.class, () -> fileIO.readFile(directoryPath));
        assertTrue(exception.getMessage().contains("IO Error"));
    }

}