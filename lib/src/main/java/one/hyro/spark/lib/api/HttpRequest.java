package one.hyro.spark.lib.api;

import okhttp3.*;
import one.hyro.spark.lib.SparkLib;

import java.io.IOException;

public class HttpRequest {
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json");

    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void post(String url, String body) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, body))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            SparkLib.getPlugin().getLogger().info(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String put(String url, String body) {
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(JSON, body))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            SparkLib.getPlugin().getLogger().info(response.body().string());
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String patch(String url, String body) {
        Request request = new Request.Builder()
                .url(url)
                .patch(RequestBody.create(JSON, body))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            SparkLib.getPlugin().getLogger().info(response.body().string());
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void delete(String url) {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            SparkLib.getPlugin().getLogger().info(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
