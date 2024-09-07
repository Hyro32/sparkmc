package one.hyro.spark.smp.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {

    private final HomeManager homeManager;

    public HomeTabCompleter(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }

        Player player = (Player) sender;
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("sethome") ||
                command.getName().equalsIgnoreCase("home") ||
                command.getName().equalsIgnoreCase("homes") ||
                command.getName().equalsIgnoreCase("delhome")) {

            if (args.length == 1) {
                // Suggest home names if argument index is 1
                List<String> homeNames = homeManager.getHomeNames(player);
                for (String name : homeNames) {
                    if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                        suggestions.add(name);
                    }
                }
            }
        }
        return suggestions;
    }
}
