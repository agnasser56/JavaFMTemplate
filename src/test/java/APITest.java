import apis.ToDos;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import utilities.Common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class APITest {
    @Test
    public void testGetToDo() {
        String currWorkingDirectory = System.getProperty("user.dir");

        //Get application settings(username,passwrod,...etc)
        HashMap<String,Object> appSettings = Common.readAllSettings(currWorkingDirectory + "\\ApplicationSettings\\AppDetails.yaml");

        ToDos toDos = new ToDos();
       Response res= toDos.GetToDo((LinkedHashMap<String, String>) appSettings.get("testCase3"));

        Assertion assertion = new Assertion();
        assertion.assertEquals(res.getStatusCode(),200);
    }

}
