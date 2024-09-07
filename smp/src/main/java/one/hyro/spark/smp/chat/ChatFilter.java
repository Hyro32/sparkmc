package one.hyro.spark.smp.chat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatFilter implements Listener {

    private final JavaPlugin plugin;
    private static Set<String> words = new HashSet<>();
    private static int largestWordLength = 0;

    public ChatFilter(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfigs(); // Load bad words from the words.json file
        plugin.getServer().getPluginManager().registerEvents(this, plugin); // Register the event listener
    }

    public void loadConfigs() {
        File dataFolder = plugin.getDataFolder();
        File file = new File(dataFolder, "words.json");

        if (!file.exists()) {
            System.err.println("words.json file not found in " + dataFolder.getPath());
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> wordList = gson.fromJson(reader, type);

            for (String word : wordList) {
                word = word.replaceAll(" ", "");
                words.add(word);
                if (word.length() > largestWordLength) {
                    largestWordLength = word.length();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> badWordsFound(String input) {
        if (input == null) {
            return new ArrayList<>();
        }

        input = input.replaceAll("1", "i")
                .replaceAll("!", "i")
                .replaceAll("3", "e")
                .replaceAll("4", "a")
                .replaceAll("@", "a")
                .replaceAll("5", "s")
                .replaceAll("7", "t")
                .replaceAll("0", "o")
                .replaceAll("9", "g");

        ArrayList<String> badWords = new ArrayList<>();
        input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

        for (int start = 0; start < input.length(); start++) {
            for (int offset = 1; offset < (input.length() + 1 - start) && offset <= largestWordLength; offset++) {
                String wordToCheck = input.substring(start, start + offset);
                if (words.contains(wordToCheck)) {
                    badWords.add(wordToCheck);
                }
            }
        }

        return badWords;
    }

    private String filterBadWords(String message) {
        ArrayList<String> badWords = badWordsFound(message);

        for (String badWord : badWords) {
            String regex = "\\b" + badWord + "\\b";
            message = message.replaceAll("(?i)" + regex, "❤");
        }

        return message;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        // Filter bad words
        String filteredMessage = filterBadWords(message);

        // Set the filtered message
        event.setMessage(filteredMessage);
    }
}
