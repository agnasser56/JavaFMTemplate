import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import pages.LoginPage;
import utilities.Common;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class UITests {

    @Test
    public void testLoginUI(){
        WebDriver driver = Common.getWebDriver();
        String currWorkingDirectory = System.getProperty("user.dir");

        //Get application settings(username,passwrod,...etc)
        HashMap<String,Object> appSettings = Common.readAllSettings(currWorkingDirectory + "\\ApplicationSettings\\AppDetails.yaml");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.Login((LinkedHashMap<String, String>) appSettings.get("URL"));
        Boolean res  = loginPage.verifyLogin();
        Assertion assertion = new Assertion();
        assertion.assertEquals(java.util.Optional.ofNullable(res),true,"");
    }
}
