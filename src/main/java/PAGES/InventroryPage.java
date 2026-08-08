package PAGES;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DataDriven;

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
    By products = By.className("inventory_item");// each product card on the page
    By cartpage = By.className("shopping_cart_link");
    By linkedinlocator = new By.ByLinkText("LinkedIn");
    By facebooklocator = new By.ByLinkText("Facebook");
    By twitterlocator = new By.ByLinkText("Twitter");
    By tabslocator = By.id("react-burger-menu-btn");      // hamburger menu icon (top-left) that reveals the sidebar
    By logoutlocator = By.id("logout_sidebar_link");       // "Logout" link inside the sidebar menu


    // Loaded once when this Page Object is constructed — currently unused
    // elsewhere in this class (every method takes its own productName
    // parameter instead), kept here in case a future method needs it.
    List<String> productName = DataDriven.jsonArrayReader("productName");

    // Builds a locator for a product's Add-to-cart/Remove button by
    // matching the product's name text, then searching within that same
    // product card for its button.
    public By getButtonLocatorByName(String productName) {
        return By.xpath(
                "//div[@class='inventory_item']" +
                        "[.//div[contains(@class,'inventory_item_name') and contains(text(),'" + productName + "')]]" +
                        "//button"
        );
    }

    // Builds a locator for a product's price element by matching the
    // product's exact name text, then searching within that same product
    // card for its price div.
    public By getPriceLocatorByName(String productName) {
        return By.xpath(
                "//div[@class='inventory_item']" +
                        "[.//div[contains(@class,'inventory_item_name') and text()='" + productName + "']]" +
                        "//div[@class='inventory_item_price']"
        );
    }


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
    public WebElement getLinkedin() {
        return driver.findElement(linkedinlocator);
    }
    public WebElement getFacebook() {
        return driver.findElement(facebooklocator);
    }
    public WebElement getTwitter() {
        return driver.findElement(twitterlocator);
    }
    public WebElement getShoppingCart() {
        return driver.findElement(cartpage);
    }
    // Returns the hamburger menu icon element (opens the sidebar).
    public WebElement tabselement(){
        return driver.findElement(tabslocator);
    }
    // Returns the "Logout" link element inside the sidebar menu.
    public WebElement logoutelement(){
        return driver.findElement(logoutlocator);
    }



    // Resolves the button locator above into an actual WebElement
    // for the given product name.
    public WebElement getProductButton(String productName) {
        return driver.findElement(getButtonLocatorByName(productName));
    }




    // Element: resolve that locator into an actual WebElement
    public WebElement getPriceElement(String productName) {
        return driver.findElement(getPriceLocatorByName(productName));
    }

    // Clicks the Add-to-cart button for a single named product.
    public void addProductToCart(String productName) {
        getProductButton(productName).click();
    }

    // Adds every product in the given list to the cart, one at a time,
    // in the order they appear in the list.
    public void addProductsToCart(List<String> productNames) {
        for (String name : productNames) {
            addProductToCart(name);
        }
    }



    // Returns the number of product cards currently displayed on the page.
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
    public  void clickOnLinkedin() {
        driver.findElement(linkedinlocator).click();
    }
    public  void clickOnFacebook() {
        driver.findElement(facebooklocator).click();
    }
    public  void clickOnTwitter() {
        driver.findElement(twitterlocator).click();
    }
    public  void clickOnShoppingCart() {
        driver.findElement(cartpage).click();
    }
    // Clicks the hamburger menu icon to open the sidebar (needed before
    // the Logout link becomes visible/clickable).
    public void clickontabs(){
        driver.findElement(tabslocator).click();
    }
    // Waits for the Logout link to become visible (the sidebar animation
    // can take a moment) before clicking it.
    public void clickOnLogout() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutlocator));
        driver.findElement(logoutlocator).click();
    }


    //read a button's text by product name
    public String getButtonTextByName(String productName) {
        return getProductButton(productName).getText();
    }

    // Function: read the price text, strip "$", return as a double
    public double getProductPrice(String productName) {
        String rawText = getPriceElement(productName).getText(); // e.g. "$29.99"
        String cleaned = rawText.replace("$", "");               // "29.99"

        double value = Double.parseDouble(cleaned);

        return value;
    }
    // Calculate the total price of the selected products
    public double getProductsTotalPrice(List<String> productNames) {

        double total = 0.0;

        for (String product : productNames) {
            total += getProductPrice(product);
        }

        return total;
    }
}