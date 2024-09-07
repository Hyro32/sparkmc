package one.hyro.spark.smp.kits;

import net.md_5.bungee.api.ChatColor;
import one.hyro.spark.smp.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StarterKitManager implements Listener, CommandExecutor {

    private final JavaPlugin plugin;
    private final Set<String> firstJoinPlayers = new HashSet<>();
    private final Economy economy;

    public StarterKitManager(JavaPlugin plugin, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("starterkitkey").setExecutor(this);
    }

    public void giveStarterKitKey(Player player) {
        ItemStack starterKitKey = new ItemStack(Material.TRIPWIRE_HOOK); // Using TRIPWIRE_HOOK for the Starter Kit Key
        ItemMeta meta = starterKitKey.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Starter Kit Key");

            // Add hidden enchantments
            meta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            // Set custom ID
            meta.setCustomModelData(2026);

            // Add lore to describe the key
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Right-click to receive a full starter kit:");
            lore.add(ChatColor.GRAY + " - Chainmail Armor (Helmet, Chestplate, Leggings, Boots)");
            lore.add(ChatColor.GRAY + " - Stone Sword");
            lore.add(ChatColor.GRAY + " - Stone Pickaxe");
            lore.add(ChatColor.GRAY + " - Stone Axe");
            lore.add(ChatColor.GRAY + " - Stone Shovel");
            lore.add(ChatColor.GRAY + " - 64 Bread");
            meta.setLore(lore);

            starterKitKey.setItemMeta(meta);

            player.getInventory().setItem(4, starterKitKey);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) {
            return; // Not the first join
        }

        // Give starter kit key
        giveStarterKitKey(player);

        // Set player balance to 500 using Vault API
        if (economy != null) {
            economy.depositPlayer(player, 500);
        }

        // Track first join
        firstJoinPlayers.add(player.getUniqueId().toString());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getItem().getType() != Material.TRIPWIRE_HOOK) {
            return; // Not the Starter Kit Key
        }

        ItemMeta meta = event.getItem().getItemMeta();
        if (meta == null || !meta.getDisplayName().equals(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Starter Kit Key")) {
            return; // Not the correct key
        }

        // Check custom ID
        if (meta.getCustomModelData() != 2026) {
            return; // Invalid key
        }

        // Give starter kit items
        Player player = event.getPlayer();

        // Chainmail armor
        player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));

        // Tools and weapons
        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
        player.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL));
        player.getInventory().addItem(new ItemStack(Material.BREAD, 64)); // One stack of bread

        // Remove the Starter Kit Key
        ItemStack item = event.getItem();
        item.setAmount(item.getAmount() - 1);
        if (item.getAmount() <= 0) {
            player.getInventory().remove(item);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("starterkitkey")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                giveStarterKitKey(player);
                player.sendMessage(ChatColor.GREEN + "You have been given the Starter Kit Key!");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
                return false;
            }
        }
        return false;
    }
}
