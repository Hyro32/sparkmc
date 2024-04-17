package one.hyro.lib.services;

import com.google.gson.JsonObject;
import one.hyro.lib.enums.Ranks;
import one.hyro.lib.i18n.Locales;
import net.md_5.bungee.api.ChatColor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.UUID;

public class MegatronApiService {
    public static void createPlayer(UUID uuid) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JsonObject json = new JsonObject();
            json.addProperty("uuid", uuid.toString());
            json.addProperty("locale", Locales.ENGLISH.toString());
            json.addProperty("rank", Ranks.DEFAULT.toString());
            json.addProperty("first_joined", new Date().toString());

            System.out.println(json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/players"))
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .header("Content-Type", "application/json")
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(ChatColor.RED + "Error while creating player");
        }
    }

    public static String getPlayer(UUID uuid) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/players?uuid=" + uuid))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    public static void deletePlayer(UUID uuid) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/players?uuid=" + uuid))
                    .DELETE()
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(ChatColor.RED + "Error while deleting player");
        }
    }

    public static Locales getPlayerLocale(UUID uuid) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/players/locale?uuid=" + uuid))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Locales.valueOf(response.body());
        } catch (Exception e) {
            System.out.println(ChatColor.RED + "Error while getting player locale");
            return null;
        }
    }

    public static void setPlayerLocale(UUID uuid, Locales locale) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JsonObject json = new JsonObject();
            json.addProperty("locale", locale.toString());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/players/locale?uuid=" + uuid))
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .header("Content-Type", "application/json")
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(ChatColor.RED + "Error while setting player locale");
        }
    }
}
