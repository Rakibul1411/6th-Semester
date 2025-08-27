package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;

public class BoardPage extends BasePage {
    private final By addNewBoardBtn = By.id("add_new_board");
    private final By boardNameInput = By.id("board_name");

    public BoardPage(WebDriver driver) {
        super(driver);
    }

    public void clickAddNewBoard() {
        click(addNewBoardBtn);
    }

    public void createBoard(String name) {
        type(boardNameInput, name);
        find(boardNameInput).sendKeys(Keys.ENTER);
        pause();
    }
}
