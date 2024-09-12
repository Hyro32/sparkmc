package one.hyro.spark.smp.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.spark.lib.builder.SparkItem;
import one.hyro.spark.lib.builder.SparkMenu;
import one.hyro.spark.smp.SparkSmp;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import java.util.List;

public class HomeGUIManager implements Listener {

    private final HomeManager homeManager;

    public HomeGUIManager(HomeManager homeManager) {
        this.homeManager = homeManager;
        SparkSmp.getInstance().getServer().getPluginManager().registerEvents(this, SparkSmp.getInstance());
    }

    public void openHomeGUI(Player player) {
        SparkMenu homeMenu = new SparkMenu()
                .setRows(3)
                .setTitle(Component.text("Homes", NamedTextColor.YELLOW))
                .build();

        // Fetch homes from the database
        List<String> homeNames = homeManager.getHomeNames(player);
        int slot = 0;
        for (String homeName : homeNames) {
            SparkItem homeItem = new SparkItem(Material.RED_BED)
                    .setDisplayName(Component.text(homeName, NamedTextColor.AQUA))
                    .setLore(Component.text(""), Component.text("Left-Click to teleport", NamedTextColor.GREEN), Component.text("Right-Click to delete", NamedTextColor.RED))
                    .onRightClick(p -> openConfirmationGUI(p, homeName))
                    .onLeftClick(p -> {
                            homeManager.teleportToHome(p, homeName);
                            player.closeInventory();
                    })
                    .build();

            homeMenu.setItem(slot, homeItem);

            // Ensure the slots are not exceeding the inventory size
            if (++slot >= 27) {
                break;
            }
        }

        player.openInventory(homeMenu.getInventory());
    }

    private void openConfirmationGUI(Player player, String homeName) {
        SparkMenu confirmationMenu = new SparkMenu()
                .setRows(1)
                .setTitle(Component.text("Confirm Delete", NamedTextColor.RED))
                .build();

        SparkItem confirmDelete = new SparkItem(Material.GREEN_CONCRETE)
                .setDisplayName(Component.text("Confirm", NamedTextColor.GREEN))
                .setLore(Component.text("Click to delete the home", NamedTextColor.YELLOW))
                .onLeftClick(p -> {
                    homeManager.deleteHome(player, homeName);
                    player.closeInventory();
                })
                .onRightClick(p -> {
                    homeManager.deleteHome(player, homeName);
                    player.closeInventory();
                })
                .build();

        SparkItem cancel = new SparkItem(Material.RED_CONCRETE)
                .setDisplayName(Component.text("Cancel", NamedTextColor.RED))
                .setLore(Component.text("Click to cancel", NamedTextColor.YELLOW))
                .onLeftClick(p -> player.closeInventory())
                .onRightClick(p -> player.closeInventory())
                .build();

        confirmationMenu.setItem(11, confirmDelete);
        confirmationMenu.setItem(15, cancel);

        player.openInventory(confirmationMenu.getInventory());
    }
}
