package one.hyro.spark.smp.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String getPrefix() {
        // Create the prefix with color codes
        return ChatColor.GRAY + "[" + ChatColor.RED + ChatColor.BOLD + "SparkMC" + ChatColor.GRAY + "]" + ChatColor.RESET;
    }
}
