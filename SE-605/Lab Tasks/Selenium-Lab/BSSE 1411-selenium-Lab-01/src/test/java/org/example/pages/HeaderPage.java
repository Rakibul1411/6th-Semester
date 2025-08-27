package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends BasePage {
    private final By signOutBtn = By.cssSelector("#crawler-sign-out > span");

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public void signOut() {
        click(signOutBtn);
    }
}