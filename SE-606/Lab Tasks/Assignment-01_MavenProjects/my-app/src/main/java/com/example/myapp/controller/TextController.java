package com.example.myapp.controller;

import org.apache.commons.lang3.StringUtils;
import com.example.myapp.service.TextProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TextController {

    private final TextProcessingService textProcessingService;

    @Autowired
    public TextController(TextProcessingService textProcessingService) {
        this.textProcessingService = textProcessingService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/hello")
    @ResponseBody
    public String hello() {
        String message = "   Hello from commons-lang3   ";
        System.out.println("Trimmed message: " + StringUtils.trim(message));
        return "Response from Spring Boot + commons-lang3!";
    }

    @PostMapping("/process-text")
    @ResponseBody
    public String processText(@RequestParam String text) {
        return textProcessingService.processText(text);
    }
    
    @PostMapping("/analyze-number")
    @ResponseBody
    public String analyzeNumber(@RequestParam int number) {
        return textProcessingService.analyzeNumber(number);
    }
}