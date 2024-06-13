package one.hyro.paper.utilities;

import java.util.List;

public class Chalk {
    public static String colorizeLegacy(String text) {
        return text.replace('&', '§');
    }

    public static List<String> colorizeLegacyLore(List<String> lore) {
        for (String line : lore)
            lore.set(lore.indexOf(line), colorizeLegacy(line));
        return lore;
    }
}
