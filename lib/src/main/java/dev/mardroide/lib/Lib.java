package dev.mardroide.lib;

import dev.mardroide.lib.i18n.I18n;

public class Lib {
    public static void main(String[] args) {
        System.out.println("Hello from Lib");
        System.out.println(I18n.getTranslation("en", "moderation.kick.message"));
    }
}
