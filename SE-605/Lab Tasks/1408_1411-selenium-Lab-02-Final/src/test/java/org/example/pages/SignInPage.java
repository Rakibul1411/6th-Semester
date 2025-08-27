// SignInPage.java
package org.example.pages;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignInPage extends BasePage {
    @FindBy(css = "button")
    private WebElement signInButton;

    public SignInPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToSignInPage() {
        driver.get("http://localhost:4000/sign_in");
        driver.manage().window().setSize(new Dimension(995, 1071));
    }

    public void clickSignIn() {
        signInButton.click();
    }
}
