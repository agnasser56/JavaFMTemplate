package utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class Common {
    WebDriver driver;
    @FindBy(xpath = "//div[@role='progressbar']")
    WebElement divLoad;

    int defTimeOut;

    public Common(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        defTimeOut = 20000;
    }

    public static boolean Exists(WebDriver browser, WebElement element, int timeOutInSeconds) {
        boolean isExist = false;
        final WebElement elemnt = element;
        try
        {
            browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(browser, 1);
            wait.ignoring(StaleElementReferenceException.class,org.openqa.selenium.NoSuchElementException.class);
            isExist = wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    try{return elemnt.isDisplayed();}catch(Exception e){return false;}
                }
            });

            return isExist;
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            browser.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS);
        }
    }
    public static boolean ExistsInDOM(WebDriver browser, WebElement element) {
        boolean isExist = false;
        final WebElement el = element;

        // check that element is not null;
        if (el == null)
        {
            return false;
        }
        //check the element existance.
        try
        {
            browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(browser, 2);
            wait.ignoring(TimeoutException.class);
            isExist = wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    boolean returnVal = false;
                    try{returnVal = el.isDisplayed(); returnVal = true;}
                    catch(Exception EX)
                    {
                        if (EX.getClass().equals(org.openqa.selenium.NoSuchElementException.class)) returnVal = false;
                        else if (EX.getClass().equals(org.openqa.selenium.StaleElementReferenceException.class)) returnVal = false;
                        else if (EX.getMessage().toLowerCase().contains("could not find")) returnVal = false;
                    }
                    return returnVal;
                }
            });

            return isExist;
        }
        catch (Exception ex)
        {
            return false;
        }
        finally
        {
            browser.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }
    }


    public static boolean WaitUntilHidden(WebDriver browser, WebElement el) {
        boolean isExist = false;
        final WebElement elemnt = el;

        try
        {
            browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(browser, 1);
            wait.ignoring(StaleElementReferenceException.class,org.openqa.selenium.NoSuchElementException.class);
            isExist = wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    try{return ! elemnt.isDisplayed();}catch(Exception e){return false;}
                }
            });

            return isExist;
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            browser.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }
    }

    public void waitUntilPageLoaded() {
        String divLoadInDom = "";


        String pageLoadStatus = "";
        try {

            if(ExistsInDOM(driver,divLoad))
                WaitUntilHidden(driver,divLoad);
            }
            catch (Exception ex)
            {

            }

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            Integer tryCount=0;
            while (!js.executeScript("return document.readyState;").toString().equalsIgnoreCase("complete")) {
                Thread.sleep(1000);
                tryCount++;
                if(tryCount==15)
                    break;
            }

        } catch (Exception ex) {
            System.out.println("Waiting function:"+ex.getMessage());

        }
    }


    // this method is to get the status of the payment request from the DB
    public List<String> getStatusFromDB(String query) {
        List<String> result = new ArrayList<>();
        try {
            String url = "jdbc:sqlserver://10.14.9.160";
            Connection conn = DriverManager.getConnection(url, "sa", "Aa123456789");
            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery(query);


            while (rs.next()) {
                result.add(rs.getString("status"));
            }

            conn.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public static Response APIRequest(HashMap<String, String> TestData) {
        try {
            //forming the request body and filling it from the excel sheet
            //setting the request headers
            RequestSpecBuilder builder = new RequestSpecBuilder();

            builder.addHeader("Content-Type", TestData.get("ContentType"));
            builder.setBody(TestData.get("Body")).setBaseUri(TestData.get("URL"));

            //setting the request HTTPS certificate settings (only if the webservice
            // uses HTTPS certificates)
            builder.setConfig(RestAssuredConfig.newConfig().sslConfig(SSLConfig.sslConfig().allowAllHostnames()));

            //running the request and (you can choose the HTTP method type ie. post , get,..etc )
            RequestSpecification requestSpec = builder.build();
            Response response = null;
            if(TestData.get("Method").equalsIgnoreCase("Post"))
                response = given().spec(requestSpec).when().post(TestData.get("URLPath"));
            else if(TestData.get("Method").equalsIgnoreCase("Put"))
                response = given().spec(requestSpec).when().put(TestData.get("URLPath"));
            else if(TestData.get("Method").equalsIgnoreCase("Patch"))
                response = given().spec(requestSpec).when().patch(TestData.get("URLPath"));
            else if(TestData.get("Method").equalsIgnoreCase("Get"))
                response = given().spec(requestSpec).when().get(TestData.get("URLPath"));
            else response = null;

            //logging the server response in console and returning
            // the server response to be asserted in the test scenarios
            response.then().log().all();

            // returning the response
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void ExecuteFunction(String sFunctionNames) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        try {

            Class testScenarios = Class.forName("test.application.scenarios.TestScenarios");
            Method method = testScenarios.getMethod("testCase1");
            method.invoke(testScenarios);
        } catch (Exception ex) {

        }
    }

    public static HashMap<String, String> readSettings(String filePath) {
        HashMap<String, String> appSettings = new HashMap<>();
        try {
            String yamlFileContent = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            Yaml yaml = new Yaml();
            appSettings = yaml.load(yamlFileContent);
            System.out.println(appSettings);

        } catch (Exception ex) {
            System.out.println("Error in reading settings" + ex.getMessage());
        }
        return appSettings;
    }

    public static HashMap<String, Object> readAllSettings(String filePath) {
        HashMap<String, Object> appSettings = new HashMap<>();
        try {
            String yamlFileContent = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            Yaml yaml = new Yaml();
            appSettings = yaml.load(yamlFileContent);
            System.out.println(appSettings);

        } catch (Exception ex) {
            System.out.println("Error in reading settings" + ex.getMessage());
        }
        return appSettings;
    }

    public static WebDriver getWebDriver() {
        WebDriver driver = null;
        try {

            String currWorkingDirectory = System.getProperty("user.dir");

            //System.setProperty("wdm.cachePath", currWorkingDirectory + "\\ApplicationSettings\\chromedriver.exe");

            //WebDriverManager.getInstance(CHROME).setup();

            System.setProperty("webdriver.chrome.driver", currWorkingDirectory + "\\ApplicationSettings\\chromedriver.exe");
            driver = new ChromeDriver();
            return driver;

        } catch (Exception ex) {
            System.out.println("Error in initializing web driver"+ex.getMessage());
        }
        return driver;
    }


}