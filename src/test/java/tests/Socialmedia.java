package tests;

import BASE.Basetests;
import PAGES.InventroryPage;
import PAGES.Loginpage;
import org.json.JSONObject;
import org.testng.annotations.Test;
import utils.DataDriven;

/**
 * Socialmedia
 * -----------
 * Verifies that each footer social media icon on the Inventory page
 * navigates to the correct external URL after a successful login.
 */
public class Socialmedia extends Basetests {
    @Test(groups = "regression",priority = 1,description = "validte social media links")
    public void validteSocialmedialinks()
    {
        Loginpage loginpage=new Loginpage(driver);
        InventroryPage inventrorypage=new InventroryPage(driver);

        // Pull valid username/password from testData.json instead of hardcoding
        JSONObject validLogin = DataDriven.jsonReader("validLogin");

        // Log in using the JSON-driven credentials
        loginpage.enterusername(validLogin.getString("username"));
        loginpage.enterpassword(validLogin.getString("password"));
        loginpage.clickloginbutton();

        // Click the LinkedIn icon and verify the browser navigated to Sauce Labs' LinkedIn page
        inventrorypage.clickOnLinkedin();
        String linkurl = inventrorypage.getCurrentUrl();
        softAssert.assertEquals(linkurl, "https://www.linkedin.com/company/sauce-labs/");
        System.out.println("Link title: " + linkurl);

        // Click the Facebook icon and verify the browser navigated to Sauce Labs' Facebook page
        inventrorypage.clickOnFacebook();
        String faceUrl = inventrorypage.getCurrentUrl();
        System.out.println("URL: " + faceUrl);
        softAssert.assertEquals(faceUrl, "https://www.facebook.com/saucelabs");

        // Click the X (Twitter) icon and verify the browser navigated to Sauce Labs' X page
        inventrorypage.clickOnTwitter();
        String twitterurl = inventrorypage.getCurrentUrl();
        softAssert.assertEquals(twitterurl, "https://x.com/saucelabs");
        System.out.println("Link title: " + twitterurl);
    }
}