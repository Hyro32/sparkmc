package dev.mardroide.lib.enums;

import org.bukkit.ChatColor;

public enum Ranks {
    OWNER("owner", ChatColor.GOLD + "Owner", new String[]{}),
    ADMIN("admin", ChatColor.DARK_RED + "Admin", new String[]{}),
    MODERATOR("moderator", ChatColor.BLUE + "Moderator", new String[]{}),
    DEFAULT("default", ChatColor.GRAY + "", new String[]{});

    private final String name;
    private final String prefix;
    private final String[] permissions;

    Ranks(String name, String prefix, String[] permissions) {
        this.name = name;
        this.prefix = prefix;
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
