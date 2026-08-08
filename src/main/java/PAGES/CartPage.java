package PAGES;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * CartPage
 * --------
 * Page Object for the Cart page and the checkout flow (steps one and two).
 * Holds locators for cart items, checkout form fields, and the subtotal,
 * and exposes methods for reading/interacting with them.
 */
public class CartPage {
    WebDriver driver;

    // ---------- Locators ----------
    By cartitems = By.className("cart_item");            // each product row in the cart
    By ctnbtn  = By.id("continue-shopping");              // "Continue Shopping" button on the cart page
    By checkoutlocator= By.id("checkout");                // "Checkout" button on the cart page
    By firstnamelocator = By.id("first-name");            // checkout step-one: first name field
    By lastnamelocator = By.id("last-name");              // checkout step-one: last name field
    By zipcodelocator = By.id("postal-code");             // checkout step-one: zip/postal code field
    By secondctnlocator = new By.ById("continue");        // checkout step-one: "Continue" button (moves to step two)
    By subtotallocator = new By.ByClassName("summary_subtotal_label"); // checkout step-two: item subtotal label


    // Builds a locator for the Remove button of a specific product,
    // by matching the product's name text and walking up to its cart_item
    // ancestor, then searching inside for the button.
    public By getRemoveButtonLocatorByName(String productName) {
        return By.xpath(
                "//div[contains(@class,'inventory_item_name') and contains(text(),'" + productName + "')]" +
                        "/ancestor::div[@class='cart_item']//button"
        );
    }



    // ---------- Constructor ----------
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }



    // ---------- Element getters ----------

    public WebElement secondctnelement(){
        return driver.findElement(secondctnlocator);
    }

    public WebElement firstnameelement() {
        return driver.findElement(firstnamelocator);
    }
    public WebElement lastnameelement() {
        return driver.findElement(lastnamelocator);
    }
    public WebElement zipcodeelement() {
        return driver.findElement(zipcodelocator);
    }

    public WebElement subtotalelement() {
        return driver.findElement(subtotallocator);
    }

    public WebElement checkoutButton() {
        return driver.findElement(checkoutlocator);
    }
    public WebElement getctnbtn() {
        return driver.findElement(ctnbtn);
    }

    // Resolves the Remove-button locator above into an actual WebElement
    // for the given product name.
    public WebElement getRemoveButton(String productName) {
        return driver.findElement(getRemoveButtonLocatorByName(productName));
    }

    // Returns every product row currently in the cart. Package-private
    // (no access modifier) since it's only used internally by other
    // methods in this class.
    List<WebElement> getcartitems() {
        return driver.findElements(cartitems);
    }

    // Returns how many items are currently in the cart.
    public int sizecartitms(){
        return getcartitems().size();
    }

    // Returns the names of all products currently in the cart, in DOM
    // order (which matches the order they were added) — used to verify
    // cart contents and ordering.
    public List<String> getCartProductNames() {

        List<String> productNames = new ArrayList<>();

        for (WebElement item : getcartitems()) {
            productNames.add(
                    item.findElement(By.className("inventory_item_name")).getText()
            );
        }

        return productNames;
    }



    // Finds the cart row matching the given product name and clicks its
    // Remove button. Throws if no matching product is found in the cart,
    // so a typo or missing product fails loudly instead of silently.
    public void removeProductByName(String productName) {
        for (WebElement item : getcartitems()) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equalsIgnoreCase(productName)) {
                item.findElement(By.className("cart_button")).click();
                return;
            }
        }
        throw new RuntimeException("Product not found in cart: " + productName);
    }

    // Reads the current text of a product's Remove/Add-to-cart button
    // (e.g. to confirm it still says "Remove" after removing a different item).
    public String removebtntxt(String remain) {
        return getRemoveButton(remain).getText();
    }

    // Clicks "Continue Shopping" to go back from the cart page to inventory.
    public void clickctnbtn(){
        driver.findElement(ctnbtn).click();
    }




    // Clicks "Checkout" on the cart page to begin the checkout flow.
    public void clickcheckoutbtn(){
        driver.findElement(checkoutlocator).click();
    }

    // ---------- Checkout step-one form actions ----------
    public void enterfirstname(String firstname) {
        firstnameelement().sendKeys(firstname);
    }
    public void enterlastname(String lastname) {
        lastnameelement().sendKeys(lastname);
    }
    public void enterzipcode(String zipcode) {
        zipcodeelement().sendKeys(zipcode);
    }
    // Clicks "Continue" on checkout step one, moving to step two (order summary).
    public void clicksecondctnbtn(){
        driver.findElement(secondctnlocator).click();
    }

    // Reads the subtotal text on checkout step two (e.g. "Item total: $53.97"),
    // strips everything except digits and the decimal point, and returns
    // it as a double so it can be compared numerically against a calculated total.
    public double getSubtotalAmount() {
        String rawText = subtotalelement().getText(); // e.g. "Item total: $53.97"
        String numericPart = rawText.replaceAll("[^0-9.]", ""); // keep only digits and decimal point
        return Double.parseDouble(numericPart);
    }

    // Returns the current page URL — useful for verifying navigation
    // (e.g. confirming checkout was blocked with an empty cart).
    public String gettheurl() {
        return driver.getCurrentUrl();
    }

}