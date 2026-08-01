package BASE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

/**
 * Basetests
 * ---------
 * Base class for all test classes. Handles common setup/teardown logic
 * (browser launch, navigation, cleanup) so individual test classes
 * (Logintest, Inventorytest, etc.) only need to focus on test logic.
 *
 * All test classes should extend this class to automatically get
 * a fresh browser session before every @Test method.
 */
public class Basetests {

    // Shared across all test classes that extend this one
    public WebDriver driver;
    public WebDriverWait wait;
    public SoftAssert softAssert; // allows collecting multiple assertion failures per test instead of stopping at the first one

    // Example/reusable locators — not tied to any specific page
    public By links = By.tagName("a");
    public By inputlink = By.linkText("Inputs");

    // Base URL every test starts from
    public String URL = "https://www.saucedemo.com/";

    /**
     * Runs before every @Test method (alwaysRun ensures it executes
     * even if a previous test in the class failed).
     * Launches Chrome, maximizes the window, and navigates to the
     * Swag Labs login page.
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {

        System.out.println("==== Starting Test ====");

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Explicit wait instance, max 5 seconds, available to any test/page object that needs it
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        softAssert = new SoftAssert();

        driver.get(URL);
    }

    /**
     * Runs after every @Test method (alwaysRun ensures cleanup happens
     * even if the test failed or threw an exception).
     * Quits the browser to free up resources between tests.
     */
    @AfterMethod(alwaysRun = true)
    public void afterTest() {

        if (driver != null) {
            driver.quit();
        }

        System.out.println("==== Test Finished ====");
    }
}