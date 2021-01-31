package apis;

import io.restassured.response.Response;
import utilities.Common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ToDos {
    public Response GetToDo(LinkedHashMap<String, String> apiSpecs)
    {
        return Common.APIRequest(apiSpecs);
    }

}
