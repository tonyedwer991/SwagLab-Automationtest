package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataDriven {

    private static final String JSON_FILE_PATH =
            System.getProperty("user.dir") + "/testData/testData.json";

    public static JSONObject jsonReader(String key) {
        try (InputStream inputStream = new FileInputStream(JSON_FILE_PATH)) {
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject rootObject = new JSONObject(tokener);
            return rootObject.getJSONObject(key);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data JSON file at path: " + JSON_FILE_PATH, e);
        }
    }

    public static List<String> jsonArrayReader(String key) {
        List<String> list = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(JSON_FILE_PATH)) {
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject rootObject = new JSONObject(tokener);
            JSONArray array = rootObject.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data JSON file at path: " + JSON_FILE_PATH, e);
        }
        return list;
    }
}