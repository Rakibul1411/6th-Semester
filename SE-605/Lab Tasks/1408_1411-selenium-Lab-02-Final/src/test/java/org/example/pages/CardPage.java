package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.interactions.Actions;

public class CardPage extends BasePage {
    @FindBy(linkText = "Add a new card...")
    private WebElement addNewCardLink;

    @FindBy(id = "card_name")
    private WebElement cardNameInput;

    @FindBy(css = ".card-content")
    private WebElement cardContent;

    @FindBy(css = "textarea")
    private WebElement commentTextarea;

    @FindBy(css = ".fa-close")
    private WebElement closeButton;

    @FindBy(linkText = "Edit")
    private WebElement editLink;

    @FindBy(css = "input")
    private WebElement cardTitleInput;

    @FindBy(css = "textarea:nth-child(2)")
    private WebElement cardDescriptionInput;

    public CardPage(WebDriver driver) {
        super(driver);
    }

    public void createCard(String cardName) {
        addNewCardLink.click();
        cardNameInput.sendKeys(cardName);
        submitButton.click();
    }

    public void cancelCardCreation(String cardName) {
        addNewCardLink.click();
        cardNameInput.sendKeys(cardName);
        cancelButton.click();
    }

    public void addComment(String comment) {
        cardContent.click();
        commentTextarea.sendKeys(comment);
        submitButton.click();
    }

    public void editCard(String newTitle, String newDescription) {
        editLink.click();
        cardTitleInput.clear();
        cardTitleInput.sendKeys(newTitle);
        cardDescriptionInput.clear();
        cardDescriptionInput.sendKeys(newDescription);
        submitButton.click();
    }
}