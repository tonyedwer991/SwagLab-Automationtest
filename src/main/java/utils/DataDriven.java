package utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads test data from the standalone testData/testData.json file
 * located at the project root (outside src).
 */
public class DataDriven {

    // Project root (user.dir) + testData folder + file name
    private static final String JSON_FILE_PATH =
            System.getProperty("user.dir") + "/testData/testData.json";

    public static JSONObject jsonReader(String key) {
        JSONObject jsonObject;

        try (InputStream inputStream = new FileInputStream(JSON_FILE_PATH)) {
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject rootObject = new JSONObject(tokener);
            jsonObject = rootObject.getJSONObject(key);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data JSON file at path: " + JSON_FILE_PATH, e);
        }

        return jsonObject;
    }
}