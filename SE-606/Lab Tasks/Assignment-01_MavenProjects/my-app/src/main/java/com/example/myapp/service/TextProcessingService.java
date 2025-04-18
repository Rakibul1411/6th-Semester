package com.example.myapp.service;

import com.example.utils.StringUtils;
import com.example.utils.NumberUtils;
import org.springframework.stereotype.Service;

@Service
public class TextProcessingService {
    
    public String processText(String text) {
        // Using our custom library methods
        String titleCaseText = StringUtils.toTitleCase(text);

        
        // Print to console
        System.out.println("Original text: " + text);
        System.out.println("Title case: " + titleCaseText);
        
        return titleCaseText;
    }
    
    public String analyzeNumber(int number) {
        // Using our custom library methods for number analysis
        boolean isPrime = NumberUtils.isPrime(number);

        int digitSum = NumberUtils.sumOfDigits(number);
        
        // Print to console
        System.out.println("Number: " + number);
        System.out.println("Is prime: " + isPrime);

        System.out.println("Sum of digits: " + digitSum);
        
        StringBuilder result = new StringBuilder();
        result.append("Number ").append(number).append(" is ");
        if (isPrime) {
            result.append("a prime number. ");
        } else {
            result.append("not a prime number. ");
        }
        result.append("Sum of its digits is ").append(digitSum).append(".");
        
        return result.toString();
    }
}