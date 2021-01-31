package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Common;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class LoginPage {
    WebDriver driver ;
    @FindBy(xpath="//input[@placeholder='Enter Company ID']")
    WebElement txtCompanyName;

    @FindBy(xpath="//button[@title='Submit']/parent::div")
    WebElement btnProceedToLogin;

    @FindBy(name="username")
    WebElement txtUserName;

    @FindBy(name="password")
    WebElement txtPassword;

    @FindBy(xpath="//bdi[text()='Log in']/parent::span/parent::span/parent::button")
    WebElement btnLogin;

    @FindBy(xpath="//bdi[text()='Log in']/parent::span/parent::span/parent::button")
    WebElement divWelcome;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void OpenURL(LinkedHashMap<String, String> testData) throws InterruptedException {

        //Setup Environment
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(testData.get("URL"));

    }
    public void Login(LinkedHashMap<String, String> testData)
    {
        try {
            Common comCls = new Common(driver);
            comCls.waitUntilPageLoaded();
            txtUserName.sendKeys(testData.get("UserName"));
            txtPassword.sendKeys(testData.get("Password"));
            btnLogin.click();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Boolean verifyLogin()
    {
        if(divWelcome.isDisplayed())
        {
            return true;
        }
        else
            return false;
    }

}
