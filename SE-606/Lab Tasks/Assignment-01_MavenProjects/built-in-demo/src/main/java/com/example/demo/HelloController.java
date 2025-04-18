package com.example.demo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @PostMapping("/hello")
    public String hello() {
        String message = "   Hello from commons-lang3   ";
        System.out.println("Trimmed message: " + StringUtils.trim(message));
        return "Response from Spring Boot + commons-lang3!";
    }
}
