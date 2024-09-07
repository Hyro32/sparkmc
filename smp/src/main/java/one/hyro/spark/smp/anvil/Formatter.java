package one.hyro.spark.smp.anvil;

import one.hyro.spark.smp.anvil.Color;
import one.hyro.spark.smp.anvil.ItalicsMode;
import one.hyro.spark.smp.anvil.RenameResult;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    private final Plugin plugin;

    private static final Pattern HEX_PATTERN = Pattern.compile("#([0-9a-fA-F]{6})");

    public Formatter(Plugin plugin) {
        this.plugin = plugin;
    }

    public RenameResult colorize(Permissible permissible, String input, ItalicsMode italicsMode) {

        int colors = 0;

        if(VersionUtils.hasHexColorSupport() && hasPermission(permissible,"anvilcolors.color.hex")) {
            RenameResult result = replaceHexColors(input);
            input = result.getColoredName();
            colors += result.getReplacedColorsCount();
        }

        for(Color color : Color.list()) {
            if(hasPermission(permissible, color.getPermission())) {
                RenameResult result = color.transform(input, italicsMode == ItalicsMode.FORCE);
                input = result.getColoredName();
                colors += result.getReplacedColorsCount();
            }
        }
        if(italicsMode == ItalicsMode.REMOVE) {
            input = ChatColor.RESET + input;
        }

        return new RenameResult(input, colors);
    }

    private boolean hasPermission(Permissible permissible, String permission) {
        return !plugin.getConfig().getBoolean("require-permissions") || permissible == null || permissible.hasPermission(permission);
    }

    public static RenameResult replaceHexColors(String input) {
        int lastIndex = 0;
        StringBuilder output = new StringBuilder();
        Matcher matcher = HEX_PATTERN.matcher(input);
        int colors = 0;
        while (matcher.find()) {
            colors++;
            output.append(input, lastIndex, matcher.start())
                    .append(ChatColor.of("#" + matcher.group(1)));

            lastIndex = matcher.end();
        }
        if (lastIndex < input.length()) {
            output.append(input, lastIndex, input.length());
        }
        return new RenameResult(output.toString(), colors);
    }

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}