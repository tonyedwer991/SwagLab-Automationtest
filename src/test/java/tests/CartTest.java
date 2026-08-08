package tests;

import BASE.Basetests;
import PAGES.CartPage;
import PAGES.InventroryPage;
import PAGES.Loginpage;
import org.json.JSONObject;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.DataDriven;

import java.time.Duration;
import java.util.List;

/**
 * CartTest
 * --------
 * Covers cart-related scenarios: empty cart validation, adding specific
 * products, removing a product, verifying subtotal price, checkout with
 * an empty cart, and cart state across logout/login.
 */
public class CartTest extends Basetests {

    // Scenario 2: Verify the cart is empty right after login, before
    // anything has been added to it.
    @Test(groups = "smoke", priority = 1, description = "validte cart is empty")
    public void EmptyCartTest() {
        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage = new InventroryPage(driver);
        CartPage cartpage = new CartPage(driver);

        // Pull valid username/password from testData.json instead of hardcoding
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Log in using the JSON-driven credentials
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();
        inventrorypage.clickOnShoppingCart();

        cartpage.sizecartitms();
        // Assert the cart has 0 items
        softAssert.assertEquals(cartpage.sizecartitms(), 0);
        softAssert.assertAll();
        System.out.println("cart itmes are: "+cartpage.sizecartitms());

    }

    // Scenario 3: Add 3 specific products (read from JSON, not hardcoded)
    // and verify they appear in the cart in the same order they were added.
    @Test(groups = "smoke", priority = 2, description = "validte cart has 3 items added")
    public void Add3SpecificProducts() {
        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage = new InventroryPage(driver);
        CartPage cartpage = new CartPage(driver);

        // Pull valid username/password from testData.json instead of hardcoding
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Log in using the JSON-driven credentials
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();

        // Data-driven: product names come from testData.json, not hardcoded here
        List<String> productNames = DataDriven.jsonArrayReader("productName");
        inventrorypage.addProductsToCart(productNames);
        inventrorypage.clickOnShoppingCart();
        wait = new WebDriverWait(driver,Duration.ofSeconds(5));

        // 4. Read what's actually in the cart
        List<String> actualProducts = cartpage.getCartProductNames();
        List<String> expectedProducts = DataDriven.jsonArrayReader("productName");

        // 5. Verify same items, same order
        // assertEquals on two Lists checks both content AND order in one call
        softAssert.assertEquals(actualProducts, expectedProducts,
                "Cart items should match the products added, in the same order");
        softAssert.assertAll();
        System.out.println("cart itmes are: "+cartpage.getCartProductNames());

    }

    // Scenario 4: Remove one specific product from the cart, then confirm
    // its button reverts to "Add to cart" on inventory while the remaining
    // two products still show "Remove".
    @Test(groups = "smoke", priority = 3,description = "validte After Removing Sauce Labs Bolt T-Shirt The rest of the items existe")
    public void  RemoveoneProduct(){

        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage = new InventroryPage(driver);
        CartPage cartpage = new CartPage(driver);

        // Pull valid username/password from testData.json instead of hardcoding
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Log in using the JSON-driven credentials
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();
        List<String> productNames = DataDriven.jsonArrayReader("productName");
        //List<String> reamining = DataDriven.jsonArrayReader("RemaningName");

        // Add all 3 products, then open the cart
        inventrorypage.addProductsToCart(productNames);
        inventrorypage.clickOnShoppingCart();

        // Remove one specific product from the cart
        cartpage.removeProductByName("Sauce Labs Bolt T-Shirt");

        // These two calls just read button text without asserting yet
        // (the actual assertions happen on the next two lines)
        cartpage.removebtntxt("Sauce Labs Backpack");
        cartpage.removebtntxt("Sauce Labs Onesie");

        // Verify the two products NOT removed still show "Remove" in the cart
        softAssert.assertEquals(cartpage.removebtntxt("Sauce Labs Backpack"),"Remove");
        softAssert.assertEquals(cartpage.removebtntxt("Sauce Labs Onesie"),"Remove");
        System.out.println("button text is "+cartpage.removebtntxt("Sauce Labs Onesie"));
        System.out.println("button text is "+cartpage.removebtntxt("Sauce Labs Backpack"));

        // Go back to inventory
        cartpage.clickctnbtn();
        inventrorypage.getProductButton("Sauce Labs Bolt T-Shirt");

        // Verify: removed product's button reverted to "Add to cart"
        softAssert.assertTrue(inventrorypage.getProductButton("Sauce Labs Bolt T-Shirt").getText().equals("Add to cart"));
        softAssert.assertAll();

    }

    // Scenario 5: Verify the checkout subtotal matches the sum of the
    // individual product prices read from the inventory page.
    @Test(groups = "smoke", priority = 4,description = "Verify the subtotal amount of the products ")
    public void  SubtotalProduct(){
        // Create page objects
        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage = new InventroryPage(driver);
        CartPage cartpage = new CartPage(driver);

        // Read valid login credentials from JSON
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Login
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();

        // Read product names from JSON
        List<String> productNames = DataDriven.jsonArrayReader("productName");

        // Add all products to the cart
        inventrorypage.addProductsToCart(productNames);

        // Calculate the expected subtotal by summing each product's price
        // as read from the Inventory page (before checkout)
        double expectedSubtotal = inventrorypage.getProductsTotalPrice(productNames);

// Print the expected subtotal
        System.out.println("Expected Subtotal = $" + expectedSubtotal);

        // Open the cart

        inventrorypage.clickOnShoppingCart();

        // Proceed through checkout steps one and two to reach the order summary page
        cartpage.clickcheckoutbtn();
        cartpage.enterfirstname("jason");
        cartpage.enterlastname("tod");
        cartpage.enterzipcode("gt");
        cartpage.clicksecondctnbtn();

        // Read the subtotal displayed on the checkout summary page
        cartpage.getSubtotalAmount();
        System.out.println("subtotal amount from cart page: "+cartpage.getSubtotalAmount());
        double total = expectedSubtotal;
        double actualSubtotal = cartpage.getSubtotalAmount();

        // Compare the calculated total (from inventory prices) against
        // the actual subtotal shown at checkout
        softAssert.assertEquals(actualSubtotal, total);
        softAssert.assertAll();


    }

    // Scenario 6: Verify checkout is blocked (or behaves correctly) when
    // attempted with an empty cart.
    @Test(groups = "smoke", priority = 5, description = "validating user can not an checkout with empty cart")
    public void checkoutemptyCartTest() {
        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage = new InventroryPage(driver);
        CartPage cartpage = new CartPage(driver);

        // Pull valid username/password from testData.json instead of hardcoding
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Log in using the JSON-driven credentials
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();

        // Cart is empty at this point (nothing was added) — go straight to checkout
        inventrorypage.clickOnShoppingCart();
        cartpage.clickcheckoutbtn();
        cartpage.gettheurl();
        System.out.println("Current url : "+cartpage.gettheurl());

        // Verify checkout did NOT proceed to the normal checkout-step-one page
        softAssert.assertFalse(cartpage.gettheurl().equals("https://www.saucedemo.com/checkout-step-one.html"));
        softAssert.assertAll();
    }

    // Scenario 7: Add products, logout, log back in with the same user,
    // and verify whether the cart items persisted or were cleared.
    @Test(groups = "smoke", priority = 6, description = "Verify cart state after logout and re-login with the same user")
    public void cartStateAfterLogoutLoginTest() {
        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage = new InventroryPage(driver);
        CartPage cartpage = new CartPage(driver);

        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // First login
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();

        // Add Products
        List<String> productNames = DataDriven.jsonArrayReader("productName");
        inventrorypage.addProductsToCart(productNames);

        // Step 2: Logout
        inventrorypage.clickontabs();
        wait =  new WebDriverWait(driver, Duration.ofSeconds(6));
        inventrorypage.clickOnLogout();
        // Handle Chrome's password-breach popup if it appears after this login action
        dismissPasswordBreachPopupIfPresent();
        System.out.println("Current url : "+cartpage.gettheurl());
        dismissPasswordBreachPopupIfPresent();

        // Step 3: Login again with the same user
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();
        // Handle the popup again, since it can appear on this second login too
        dismissPasswordBreachPopupIfPresent();
        inventrorypage.clickOnShoppingCart();
        wait = new WebDriverWait(driver,Duration.ofSeconds(5));

        // 4. Read what's actually in the cart after re-login
        List<String> actualProducts = cartpage.getCartProductNames();
        List<String> expectedProducts = DataDriven.jsonArrayReader("productName");

        // 5. Verify same items, same order (assumes cart persists across logout —
        // adjust this assertion if manual testing shows the cart clears instead)
        softAssert.assertEquals(actualProducts, expectedProducts,
                "Cart items should match the products added, in the same order");
        softAssert.assertAll();
        System.out.println("cart itmes are: "+cartpage.getCartProductNames());

        softAssert.assertAll();
    }
}