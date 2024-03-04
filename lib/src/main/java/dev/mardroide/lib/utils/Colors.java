package dev.mardroide.lib.utils;

import java.util.ArrayList;
import java.util.List;

public class Colors {
    public static String colorize(String message) {
        return message.replace("&", "§");
    }

    public static List<String> colorizeLore(String ... lines) {
        List<String> lore = new ArrayList<>();

        for (String line : lines) {
            colorize(line);
            lore.add(line);
        }

        return lore;
    }
}
