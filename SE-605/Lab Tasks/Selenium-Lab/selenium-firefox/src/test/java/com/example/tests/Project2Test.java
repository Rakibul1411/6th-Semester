// Project2Test.java
package com.example.tests;

import com.example.pages.SignInPage;
import com.example.pages.BoardPage;
import com.example.pages.BoardListPage;
import com.example.pages.HeaderPage;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;

public class Project2Test {
    private WebDriver driver;
    private SignInPage signInPage;
    private BoardPage boardPage;
    private BoardListPage boardListPage;
    private HeaderPage headerPage;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(856, 627));
        signInPage = new SignInPage(driver);
        boardPage = new BoardPage(driver);
        boardListPage = new BoardListPage(driver);
        headerPage = new HeaderPage(driver);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void project2() {
        signInPage.open("http://localhost:4000/sign_in");
        signInPage.clickSignIn();
        boardPage.clickAddNewBoard();
        boardPage.createBoard("ki jani");
        boardListPage.openFirstBoard();
        headerPage.signOut();
    }
}
