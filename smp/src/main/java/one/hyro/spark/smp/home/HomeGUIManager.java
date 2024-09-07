package one.hyro.spark.smp.home;

import com.tserato.smp.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeGUIManager implements Listener {

    private final HomeManager homeManager;
    private final JavaPlugin plugin;
    private final String prefix;
    private final File dbFile;

    public HomeGUIManager(HomeManager homeManager, JavaPlugin plugin) {
        this.homeManager = homeManager;
        this.plugin = plugin;
        this.prefix = ChatUtils.getPrefix();
        plugin.getServer().getPluginManager().registerEvents(this, plugin); // Register the event listener
        this.dbFile = new File(plugin.getDataFolder(), "teams.db");
    }

    private int getMaxHomes(Player player) {
        for (int i = 100; i > 0; i--) {
            if (player.hasPermission("smp.maxhomes." + i)) {
                return i;
            }
        }
        return 2;
    }

    public void openHomeGUI(Player player) {
        int maxHomes = getMaxHomes(player);
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.WHITE + "Your Homes");

        List<String> homeNames = homeManager.getHomeNames(player);
        for (int i = 0; i < homeNames.size() && i < maxHomes; i++) {
            String homeName = homeNames.get(i);
            Location homeLocation = getHomeLocation(player, homeName);
            String creationTime = getHomeCreationTime(player, homeName);

            ItemStack item = new ItemStack(Material.RED_BED);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.AQUA + homeName);
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Location: " + ChatColor.WHITE + homeLocation.getBlockX() + ", " + homeLocation.getBlockY() + ", " + homeLocation.getBlockZ());
                lore.add(ChatColor.GRAY + "Created: " + ChatColor.WHITE + creationTime);
                lore.add(ChatColor.GREEN + "Left Click to teleport.");
                lore.add(ChatColor.RED + "Right Click to delete.");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            gui.setItem(i, item);
        }

        // Handle Team Home slot
        if (hasTeamHome(player)) {
            ItemStack teamHomeItem = new ItemStack(Material.YELLOW_BED);
            ItemMeta teamHomeMeta = teamHomeItem.getItemMeta();
            if (teamHomeMeta != null) {
                teamHomeMeta.setDisplayName(ChatColor.YELLOW + "Team Home");
                teamHomeItem.setItemMeta(teamHomeMeta);
            }
            gui.setItem(gui.getSize() - 2, teamHomeItem);
        } else {
            ItemStack grayGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta glassMeta = grayGlassPane.getItemMeta();
            if (glassMeta != null) {
                glassMeta.setDisplayName(" ");
                grayGlassPane.setItemMeta(glassMeta);
            }
            gui.setItem(gui.getSize() - 2, grayGlassPane);
        }

        ItemStack enchantedBarrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = enchantedBarrier.getItemMeta();
        if (barrierMeta != null) {
            barrierMeta.setDisplayName(ChatColor.RED + "Close");
            barrierMeta.addEnchant(Enchantment.LOYALTY, 1, true);
            barrierMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            enchantedBarrier.setItemMeta(barrierMeta);
        }
        gui.setItem(gui.getSize() - 1, enchantedBarrier);

        ItemStack grayGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = grayGlassPane.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            grayGlassPane.setItemMeta(glassMeta);
        }

        for (int i = homeNames.size(); i < gui.getSize() - 2; i++) {
            gui.setItem(i, grayGlassPane);
        }

        player.openInventory(gui);
    }

    private boolean hasTeamHome(Player player) {
        String teamName = getPlayerTeam(player);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT home_world FROM teams WHERE team_name = ?");
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Location getTeamHomeLocation(Player player) {
        String teamName = getPlayerTeam(player);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT home_world, home_x, home_y, home_z, home_yaw, home_pitch FROM teams WHERE team_name = ?");
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Location(
                        Bukkit.getWorld(rs.getString("home_world")),
                        rs.getDouble("home_x"),
                        rs.getDouble("home_y"),
                        rs.getDouble("home_z"),
                        (float) rs.getDouble("home_yaw"),
                        (float) rs.getDouble("home_pitch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPlayerTeam(Player player) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT team_name FROM team_members WHERE player = ?");
            stmt.setString(1, player.getName().toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("team_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getHomeCreationTime(Player player, String homeName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + homeManager.getDbFile().getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT creation_time FROM homes WHERE player = ? AND name = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("creation_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private Location getHomeLocation(Player player, String homeName) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + homeManager.getDbFile().getAbsolutePath())) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT world, x, y, z, yaw, pitch FROM homes WHERE player = ? AND name = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, homeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Location(
                        Bukkit.getWorld(rs.getString("world")),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getDouble("z"),
                        rs.getFloat("yaw"),
                        rs.getFloat("pitch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (event.getView().getTitle().equals(ChatColor.WHITE + "Your Homes")) {
            event.setCancelled(true);

            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null || !currentItem.hasItemMeta()) {
                return;
            }

            ItemMeta meta = currentItem.getItemMeta();
            if (meta == null) {
                return;
            }

            String displayName = meta.getDisplayName();

            if (currentItem.getType() == Material.BARRIER && displayName.equals(ChatColor.RED + "Close")) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                player.closeInventory();
                return;
            }

            if (currentItem.getType() == Material.RED_BED) {
                String homeName = ChatColor.stripColor(displayName);

                switch (event.getClick()) {
                    case LEFT:
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                        teleportToHomeWithCountdown(player, homeName);
                        break;
                    case RIGHT:
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        openConfirmDeleteGUI(player, homeName);
                        break;
                    default:
                        break;
                }
                return;
            }

            if (currentItem.getType() == Material.YELLOW_BED && displayName.equals(ChatColor.YELLOW + "Team Home")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                teleportToTeamHomeWithCountdown(player);
                return;
            }
        } else if (event.getView().getTitle().equals(ChatColor.RED + "Delete Home?")) {
            event.setCancelled(true);

            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null || !currentItem.hasItemMeta()) {
                return;
            }

            String displayName = currentItem.getItemMeta().getDisplayName();

            if (currentItem.getType() == Material.GREEN_WOOL && displayName.equals(ChatColor.GREEN + "Yes")) {
                String homeName = player.getMetadata("deleteHome").get(0).asString();
                homeManager.deleteHome(player, homeName);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                player.sendMessage(prefix + ChatColor.RED + "Home deleted.");
                player.removeMetadata("deleteHome", plugin);
                player.closeInventory();
            } else if (currentItem.getType() == Material.RED_WOOL && displayName.equals(ChatColor.RED + "No")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                player.closeInventory();
            }
        }
    }


    private void teleportToHomeWithCountdown(Player player, String homeName) {
        Location homeLocation = getHomeLocation(player, homeName);
        if (homeLocation == null) {
            player.sendMessage(ChatColor.RED + "Home not found.");
            return;
        }

        Location initialLocation = player.getLocation().clone();
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            private int countdown = 3;

            @Override
            public void run() {
                if (!player.isOnline() || player.getUniqueId() != playerId) {
                    cancel();
                    return;
                }

                if (!player.getLocation().equals(initialLocation)) {
                    player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    cancel();
                    return;
                }

                if (countdown > 0) {
                    String actionBar = ChatColor.RED + "Teleporting in " + ChatColor.BOLD + ChatColor.AQUA + countdown + ChatColor.RED + " seconds. Do not move!";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    countdown--;
                } else {
                    if (player.getLocation().equals(initialLocation)) {
                        player.teleport(homeLocation);
                        String actionBar = ChatColor.GREEN + "Teleported!";
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void teleportToTeamHomeWithCountdown(Player player) {

        player.closeInventory();

        Location teamHomeLocation = getTeamHomeLocation(player);
        if (teamHomeLocation == null) {
            player.sendMessage(ChatColor.RED + "Team home not found.");
            return;
        }

        Location initialLocation = player.getLocation().clone();
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            private int countdown = 5;

            @Override
            public void run() {
                if (!player.isOnline() || player.getUniqueId() != playerId) {
                    cancel();
                    return;
                }

                if (!player.getLocation().equals(initialLocation)) {
                    player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    cancel();
                    return;
                }

                if (countdown > 0) {
                    String actionBar = ChatColor.RED + "Teleporting to team home in " + ChatColor.BOLD + ChatColor.AQUA + countdown + ChatColor.RED + " seconds. Do not move!";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    countdown--;
                } else {
                    if (player.getLocation().equals(initialLocation)) {
                        player.teleport(teamHomeLocation);
                        String actionBar = ChatColor.GREEN + "Teleported to Team Home!";
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void openConfirmDeleteGUI(Player player, String homeName) {
        Inventory confirmDeleteGUI = Bukkit.createInventory(null, 9, ChatColor.RED + "Delete Home?");

        ItemStack yesItem = new ItemStack(Material.GREEN_WOOL);
        ItemMeta yesMeta = yesItem.getItemMeta();
        if (yesMeta != null) {
            yesMeta.setDisplayName(ChatColor.GREEN + "Yes");
            yesItem.setItemMeta(yesMeta);
        }

        ItemStack noItem = new ItemStack(Material.RED_WOOL);
        ItemMeta noMeta = noItem.getItemMeta();
        if (noMeta != null) {
            noMeta.setDisplayName(ChatColor.RED + "No");
            noItem.setItemMeta(noMeta);
        }

        confirmDeleteGUI.setItem(3, yesItem);
        confirmDeleteGUI.setItem(5, noItem);

        player.openInventory(confirmDeleteGUI);
        player.setMetadata("deleteHome", new FixedMetadataValue(plugin, homeName));
    }
}
