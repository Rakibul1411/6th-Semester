package org.example.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BoardPage extends BasePage {
    @FindBy(id = "add_new_board")
    private WebElement addNewBoardButton;

    @FindBy(id = "board_name")
    private WebElement boardNameInput;

    @FindBy(linkText = "cancel")
    private WebElement cancelButton;

    @FindBy(css = ".inner")
    private WebElement innerElement;

    public BoardPage(WebDriver driver) {
        super(driver);
    }

    public void createBoard(String boardName) {
        addNewBoardButton.click();
        boardNameInput.sendKeys(boardName);
        innerElement.click();
    }

    public void cancelBoardCreation(String boardName) {
        addNewBoardButton.click();
        boardNameInput.sendKeys(boardName);
        cancelButton.click();
    }
}