import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxAutomationBrowser {
    public static void main(String[] args) {
        // 1) Point to your geckodriver executable:
        System.setProperty("webdriver.gecko.driver",
                "/Users/md.rakibulislam/Downloads/geckodriver");

        // 2) Instantiate the FirefoxDriver
        WebDriver driver = new FirefoxDriver();

        // 3) Drive the browser
        driver.get("https://www.google.com");

        // … your test steps …

        // 4) Clean up
        //driver.quit();
    }
}
