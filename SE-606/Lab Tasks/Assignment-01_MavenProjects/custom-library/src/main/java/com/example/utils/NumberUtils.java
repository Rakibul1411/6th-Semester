package com.example.utils;

public class NumberUtils {

    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }

        int i = 5;
        while (i * i <= number) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
            i += 6;
        }
        return true;
    }

    public static int sumOfDigits(int number) {
        int sum = 0;
        int absNumber = Math.abs(number);

        while (absNumber > 0) {
            sum += absNumber % 10;
            absNumber /= 10;
        }

        return sum;
    }
}