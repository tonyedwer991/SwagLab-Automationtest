package tests;

import BASE.Basetests;
import PAGES.InventroryPage;
import PAGES.Loginpage;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.DataDriven;

/**
 * Inventorytest
 * -------------
 * Contains test scenarios that verify elements on the Inventory page
 * after a successful login. Credentials come from testData.json via
 * DataDriven.jsonReader() — nothing is hardcoded here.
 *
 * Extends Basetests so each test gets a fresh browser session
 * (setUp/tearDown handled automatically before and after every @Test).
 */
public class Inventorytest extends Basetests {

    /**
     * Scenario 4: Verify Inventory Page Elements After Login
     * Logs in with valid credentials, then checks:
     *  - the URL redirects to /inventory.html
     *  - the page title is "Swag Labs"
     *  - exactly 6 products are displayed
     */
    @Test(groups = "smoke", priority = 1, description = "Verify Inventory Page Elements After Login")
    public void InventoryTest(){
        // Page objects for the pages involved in this flow
        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage = new InventroryPage(driver);

        // Pull valid username/password from testData.json instead of hardcoding
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Log in using the JSON-driven credentials
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();

        // ---- Check 1: URL redirected to the Inventory page ----
        // Capture once, reuse for both print and assertion
        String currentUrl = inventrorypage.getCurrentUrl();
        System.out.println("URL: " + currentUrl);
        Assert.assertEquals(currentUrl, "https://www.saucedemo.com/inventory.html");

        // ---- Check 2: Page title is "Swag Labs" ----
        String pageTitle = inventrorypage.getPageTitle();
        System.out.println("Page title: " + pageTitle);
        Assert.assertEquals(pageTitle, "Swag Labs");

        // ---- Check 3: Exactly 6 products are displayed ----
        int actualCount = inventrorypage.getProductCount();
        System.out.println("number of products found: " + actualCount);
        Assert.assertEquals(actualCount, 6,
                "Expected 6 products on Inventory page but found: " + actualCount);
    }
}