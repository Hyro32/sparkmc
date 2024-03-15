package dev.mardroide.lib.enums;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
public enum Ranks {
    OWNER(ChatColor.GOLD + "Owner", new String[]{}),
    ADMIN(ChatColor.DARK_RED + "Admin", new String[]{}),
    MODERATOR(ChatColor.BLUE + "Moderator", new String[]{}),
    DEFAULT(ChatColor.GRAY + "", new String[]{});

    private final String prefix;
    private final String[] permissions;

    Ranks(String prefix, String[] permissions) {
        this.prefix = prefix;
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
