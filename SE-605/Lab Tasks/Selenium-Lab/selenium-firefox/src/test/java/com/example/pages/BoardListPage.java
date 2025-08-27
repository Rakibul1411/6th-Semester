package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BoardListPage extends BasePage {
    private final By firstBoardTile = By.cssSelector(".inner");

    public BoardListPage(WebDriver driver) {
        super(driver);
    }

    public void openFirstBoard() {
        click(firstBoardTile);
    }
}