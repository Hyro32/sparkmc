package one.hyro.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequests {
    public static JSONObject sendGetRequest(String url) {
        return sendRequest("GET", url, null);
    }

    public static JSONObject sendPostRequest(String url, JSONObject body) {
        return sendRequest("POST", url, body);
    }

    public static JSONObject sendPutRequest(String url, JSONObject body) {
        return sendRequest("PUT", url, body);
    }

    public static JSONObject sendDeleteRequest(String url) {
        return sendRequest("DELETE", url, null);
    }

    public static JSONObject sendPatchRequest(String url, JSONObject body) {
        return sendRequest("PATCH", url, body);
    }

    private static JSONObject sendRequest(String method, String url, JSONObject body) {
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            if (method.equals("PATCH")) {
                conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                conn.setRequestMethod("POST");
            } else {
                conn.setRequestMethod(method);
            }

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            if (body != null) {
                try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                    dos.writeBytes(body.toString());
                }
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                String responseStr = response.toString();
                if (responseStr.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(responseStr);
                    if (jsonArray.isEmpty()) return null;
                    return jsonArray.getJSONObject(0);
                } else {
                    return new JSONObject(responseStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
