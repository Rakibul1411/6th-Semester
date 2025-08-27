package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignInPage extends BasePage {
    private final By signInButton = By.cssSelector("button");

    public SignInPage(WebDriver driver) {
        super(driver);
    }

    public void open(String url) {
        driver.get(url);
        pause();
    }

    public void clickSignIn() {
        click(signInButton);
    }
}