package BASE;

import Utilities.MyListenere;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class Basetests {

    public WebDriver driver;
    public WebDriverWait wait;
    public SoftAssert softAssert;

    public By links = By.tagName("a");
    public By inputlink = By.linkText("Inputs");

    public String URL = "https://www.saucedemo.com/";

    @BeforeMethod(groups = {"smoke","regression"})
    public void beforeTest() {
        System.out.println("==== Starting Test ====");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.setExperimentalOption("prefs", java.util.Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false
        ));

        WebDriver rawDriver = new EdgeDriver();
        driver = new EventFiringDecorator<>(new MyListenere()).decorate(rawDriver);

        driver.manage().window().maximize();
        softAssert = new SoftAssert();
        driver.get(URL);
    }

    public void dismissPasswordBreachPopupIfPresent() {
        try {
            driver.switchTo().alert().accept();
            System.out.println("Native alert dismissed.");
        } catch (NoAlertPresentException e) {
            System.out.println("No native alert present.");
        }
    }

    @AfterMethod(groups = {"smoke", "regression"})
    public void afterTest(ITestResult result) {
        try {
            File screenshotDir = new File("target/Screenshots");
            screenshotDir.mkdirs();

            int randomNum = (int) (Math.random() * 9000) + 1000;
            File destination = new File(screenshotDir, result.getName() + "_" + randomNum + ".png");

            Files.move(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath(),
                    destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot: " + destination.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
        driver.quit();
    }
}