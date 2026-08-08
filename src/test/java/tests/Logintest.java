package tests;

import BASE.Basetests;
import PAGES.InventroryPage;
import PAGES.Loginpage;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.DataDriven;

/**
 * Logintest
 * ---------
 * Contains all test scenarios related to the Login functionality
 * of the Swag Labs website. All credentials are pulled from
 * testData.json via DataDriven.jsonReader() — no hardcoded values here.
 *
 * Extends Basetests so each test gets a fresh browser session
 * (setUp/tearDown handled automatically before and after every @Test).
 */
//@Listeners(Listenere.Executionlistenere.class)
public class Logintest extends Basetests {

    /**
     * Scenario 1: Successful Login
     * Logs in with valid credentials and verifies the user
     * lands on the Inventory page.
     */
    @Test(groups = "regression", priority = 1, description = "successful login by valid credintials")
    public void validlogintest(){
        // Page objects for the pages involved in this flow
        Loginpage loginpage1 = new Loginpage(driver);
        InventroryPage inventrorypage1 = new InventroryPage(driver);

        // Pull valid username/password from testData.json instead of hardcoding
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Perform login using the JSON-driven credentials
        loginpage1.enterusername(validLogin.getString("username"));
        loginpage1.enterpassword(validLogin.getString("password"));
        loginpage1.clickloginbutton();

        // Capture the URL once after login, reuse it for both printing and asserting
        String currentUrl = inventrorypage1.getCurrentUrl();
        System.out.println(currentUrl);

        // Verify redirection to the Inventory page
        softAssert.assertTrue(currentUrl.contains("/inventory.html"),
                "Expected URL to contain '/inventory.html' but was: " + currentUrl);
    }

    /**
     * Scenario 2: Invalid Login
     * Attempts login with an invalid username/password combo and
     * verifies the correct error message is displayed.
     */
    @Test(groups = "regression", priority = 2, description = "Invalid login by an Invalid username and invalid password")
    public void invalidlogintest(){
        Loginpage loginpage2 = new Loginpage(driver);

        // Pull invalid credentials from testData.json
        JSONObject invalidLogin = DataDriven.jsonReader("invalidLogin");

        loginpage2.enterusername(invalidLogin.getString("username"));
        loginpage2.enterpassword(invalidLogin.getString("password"));
        loginpage2.clickloginbutton();

        // Capture the error message once, reuse for print + assertion
        // (avoids calling geterrormsg() multiple times and re-running its wait each time)
        String actualError = loginpage2.geterrormsg();
        System.out.println(actualError);

        // Verify the exact error text shown by Swag Labs for mismatched credentials
        softAssert.assertEquals(actualError, "Epic sadface: Username and password do not match any user in this service");
    }

    /**
     * Scenario 3: Login Without Password
     * Enters a valid username but leaves the password field blank,
     * and verifies the "Password is required" error message appears.
     */
    @Test(groups = "regression", priority = 3, description = "login by an valid username and no password")
    public void logintestwithoutpassword(){
        Loginpage loginpage2 = new Loginpage(driver);

        // Pull username-only data (no password expected/used for this scenario)
        JSONObject noPasswordLogin = DataDriven.jsonReader("noPasswordLogin");

        loginpage2.enterusername(noPasswordLogin.getString("username"));
        // Note: password field is intentionally left empty — no enterpassword() call here
        loginpage2.clickloginbutton();

        // Capture error once, reuse for print + assertion
        String actualError = loginpage2.geterrormsg();
        System.out.println(actualError);

        softAssert.assertEquals(actualError, "Epic sadface: Password is required");
    }
}