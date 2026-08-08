package PAGES;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Loginpage
 * ---------
 * Page Object for the Swag Labs login page (https://www.saucedemo.com/).
 * Holds all locators for the login form and exposes methods for
 * interacting with it. Test classes call these methods — they never
 * touch locators or WebElements directly.
 */
public class Loginpage {

    // ---------- Locators ----------
    By username = By.id("user-name");
    By password = By.id("password");
    By loginbtn = By.id("login-button");
    //public By vaildusername = By.xpath("//*[@id=\"login_credentials\"]/text()[1]");
    //public By vaildpassword = By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div/div[2]/text()");
    public By errormsg = By.xpath("//*[@id=\"login_button_container\"]/div/form/div[3]/h3");

    public WebDriver driver;
    public WebDriverWait wait;

    // ---------- Constructor ----------
    public Loginpage(WebDriver driver) {
        this.driver = driver;
        // Explicit wait, max 5 seconds, used for elements that may not be
        // immediately present/visible (e.g. the error message after login)
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ---------- Private element getters ----------
    // These locate and return the raw WebElement; kept package-private
    // since only the public action methods below should use them directly.

    WebElement getusername() {
        return driver.findElement(username);
    }

    WebElement getpassword() {
        return driver.findElement(password);
    }

    WebElement getloginbutton() {
        return driver.findElement(loginbtn);
    }

    // Uses an explicit wait since the error banner only appears after
    // a failed login attempt — it isn't present on initial page load
    WebElement errormsgelement() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errormsg));
    }

    // ---------- Public actions (used by test classes) ----------

    /**
     * Types the given username into the username field.
     */
    public void enterusername(String username) {
        getusername().sendKeys(username);
    }

    /**
     * Types the given password into the password field.
     */
    public void enterpassword(String password) {
        getpassword().sendKeys(password);
    }

    /**
     * Clicks the Login button.
     */
    public void clickloginbutton() {
        getloginbutton().click();
    }

    /**
     * Returns the text of the error message shown after a failed login
     * attempt (e.g. invalid credentials, missing password).
     */
    public String geterrormsg() {
        return errormsgelement().getText();
    }

    public String geturl() {
        return driver.getCurrentUrl();
    }
}