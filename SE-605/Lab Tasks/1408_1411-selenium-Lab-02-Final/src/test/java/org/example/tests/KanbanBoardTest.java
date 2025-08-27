package org.example.tests;


import org.example.pages.BoardPage;
import org.example.pages.CardPage;
import org.example.pages.ListPage;
import org.example.pages.SignInPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class KanbanBoardTest {
    private WebDriver driver;
    private SignInPage signInPage;
    private BoardPage boardPage;
    private ListPage listPage;
    private CardPage cardPage;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        signInPage = new SignInPage(driver);
        boardPage = new BoardPage(driver);
        listPage = new ListPage(driver);
        cardPage = new CardPage(driver);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testKanbanBoardWorkflow() {
        // 1. Sign in
        signInPage.navigateToSignInPage();
        signInPage.clickSignIn();

        // 2. Board operations
        boardPage.cancelBoardCreation("board");
        boardPage.createBoard("board");

        // 3. List operations
        listPage.cancelListCreation("list");
        listPage.createList("list");

        // 4. Card operations
        cardPage.cancelCardCreation("card");
        cardPage.createCard("card");

        // 5. Comment operations
        cardPage.addComment("hello");

        // 6. Edit card
        cardPage.editCard("card a", "des");

        // 7. Edit list
        listPage.editListName("list a");
    }
}