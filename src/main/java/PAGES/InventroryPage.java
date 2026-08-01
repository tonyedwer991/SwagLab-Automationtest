package PAGES;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * InventroryPage
 * --------------
 * Page Object for the Swag Labs Inventory (Products) page,
 * shown after a successful login. Holds locators for the page
 * and exposes methods for reading its elements — test classes
 * call these methods instead of touching locators directly.
 */
public class InventroryPage {

    WebDriver driver;

    // ---------- Locators ----------
    By carticon = By.xpath("//*[@id=\"shopping_cart_container\"]/a"); // cart icon top-right
    By products = By.className("inventory_item");                     // each product card on the page

    public WebDriverWait wait;

    // ---------- Constructor ----------
    public InventroryPage(WebDriver driver) {
        this.driver = driver;
        // Explicit wait, max 5 seconds, used where the page needs time
        // to finish rendering (e.g. waiting for the correct page title)
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ---------- Element getters / actions ----------

    /**
     * Returns the cart icon WebElement. Can be used to check visibility
     * or click into the cart.
     */
    public WebElement getcarticon() {
        return driver.findElement(carticon);
    }

    /**
     * Returns the full list of product elements currently displayed
     * on the Inventory page. Uses findElements (plural) so it returns
     * ALL matches, not just the first one.
     */
    public List<WebElement> getProducts() {
        return driver.findElements(products);
    }

    /**
     * Returns how many products are currently displayed, based on
     * the size of the product list above.
     */
    public int getProductCount() {
        return getProducts().size();
    }

    /**
     * Returns the current page URL — useful for verifying redirection
     * after login (e.g. checking it contains "/inventory.html").
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Returns the browser tab title (e.g. "Swag Labs").
     * Waits explicitly until the title matches before reading it,
     * to avoid reading a stale/blank title before the page finishes loading.
     */
    public String getPageTitle() {
        wait.until(ExpectedConditions.titleIs("Swag Labs"));
        return driver.getTitle();
    }
}