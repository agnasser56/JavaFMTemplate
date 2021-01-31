import groovy.transform.ASTTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.yaml.snakeyaml.Yaml;
import utilities.Common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TestScenario {


    public static ArrayList<LinkedHashMap<String,Object>> readSettingsAsList(String filePath) {
        ArrayList<LinkedHashMap<String,Object>> appSettings = new ArrayList<>();
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

    public static List<String> readFileInList(String fileName) {

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // do something
            e.printStackTrace();
        }
        return lines;
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
            System.out.println("Error in initializing web driver" + ex.getMessage());
        }
        return driver;
    }


    public static void testCase1()
    {
        System.out.println("test case 1");
    }
    public static void main(String[] args) {

        WebDriver driver = getWebDriver();
        try {
            String currWorkingDirectory = System.getProperty("user.dir");

            //Get application settings(username,passwrod,...etc)
            HashMap<String,Object> appSettings = Common.readAllSettings(currWorkingDirectory + "\\ApplicationSettings\\AppDetails.yaml");


        } catch (Exception ex) {
            System.out.println(ex.getStackTrace() + ex.getMessage());
        } finally {
            driver.quit();
        }
    }


}
