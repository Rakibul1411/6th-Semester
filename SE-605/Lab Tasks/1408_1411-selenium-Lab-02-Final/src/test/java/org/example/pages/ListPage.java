package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ListPage extends BasePage {
    @FindBy(id = "list_name")
    private WebElement listNameInput;

    @FindBy(css = "button")
    private WebElement submitButton;

    @FindBy(linkText = "cancel")
    private WebElement cancelButton;

    public ListPage(WebDriver driver) {
        super(driver);
    }

    public void createList(String listName) {
        listNameInput.sendKeys(listName);
        submitButton.click();
    }

    public void cancelListCreation(String listName) {
        listNameInput.sendKeys(listName);
        cancelButton.click();
    }

    public void editListName(String newListName) {
        listNameInput.click();
        listNameInput.clear();
        listNameInput.sendKeys(newListName);
        submitButton.click();
    }
}