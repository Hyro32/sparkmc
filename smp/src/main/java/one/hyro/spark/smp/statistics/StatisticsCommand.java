package one.hyro.spark.smp.statistics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class StatisticsCommand implements CommandExecutor {

    private final PlayerStatisticsManager statsManager;

    public StatisticsCommand(PlayerStatisticsManager statsManager) {
        this.statsManager = statsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String targetName = args[0];
                Player target = Bukkit.getPlayer(targetName);

                if (target != null) {
                    Inventory statsInventory = createStatsGUI(target);
                    player.openInventory(statsInventory);
                } else {
                    player.sendMessage("Player not found.");
                }
            } else {
                sender.sendMessage("Usage: /statistics <player>");
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }
        return true;
    }

    private Inventory createStatsGUI(Player target) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Player Statistics");

        // Create the player head
        ItemStack playerHead = new ItemStack(org.bukkit.Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(target);
            skullMeta.setDisplayName(target.getName());

            // Retrieve and format statistics
            String stats = statsManager.getStatistics(target);
            String[] statsArray = stats.split("\n"); // Split stats into lines for lore

            // Set lore
            skullMeta.setLore(java.util.Arrays.asList(statsArray));
            playerHead.setItemMeta(skullMeta);
        }

        // Add player head to the center slot (slot 13 in a 27-slot inventory)
        inventory.setItem(13, playerHead);

        return inventory;
    }
}
