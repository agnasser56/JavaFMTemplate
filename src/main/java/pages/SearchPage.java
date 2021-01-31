package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchPage {
    WebDriver driver ;
    @FindBy(xpath="//input[@type='search']")
    WebElement txtSearchEmployee;

    @FindBy(xpath="//div[@id='globalLogoPositionRevealer']/following-sibling::div")
    WebElement divEmployeePanel;

    @FindBy(name="username")
    WebElement txtUserName;

    @FindBy(name="password")
    WebElement txtPassword;

    @FindBy(xpath="//bdi[text()='Log in']/parent::span/parent::span/parent::button")
    WebElement btnLogin;


    public SearchPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateToEmployeeProfilePage(String empID,String accessToken)
    {
        driver.get("https://performancemanager.successfactors.eu/xi/ui/pages/empfile/liveprofile.xhtml?selected_user="+empID+"&_s.crb="+accessToken);
        System.out.println("search ended");
    }

    public void Search(String empID)
    {
        txtSearchEmployee.sendKeys(empID);
        try {
            Thread.sleep(4000);
            divEmployeePanel.click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("search ended");
    }
}
