package one.hyro.spark.smp.anvil;

import one.hyro.spark.smp.SparkSmp;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class AnvilListener implements Listener {

    private final SparkSmp plugin;
    private final Formatter formatter;

    public AnvilListener(SparkSmp plugin) {
        this.plugin = plugin;
        this.formatter = new Formatter(plugin);
    }

    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent event) {

        List<HumanEntity> viewers = event.getViewers();
        if (viewers.size() != 1) return;
        HumanEntity humanEntity = viewers.get(0);
        if (!(humanEntity instanceof Player)) return;

        Player player = (Player) humanEntity;
        ItemStack item = event.getResult();

        if (item == null) return;
        if (!item.hasItemMeta()) return;
        ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
        if (!meta.hasDisplayName()) return;

        final String originalName = meta.getDisplayName();
        String displayName = originalName;

        RenameResult result = formatter.colorize(player, displayName, plugin.getItalicsMode());
        if (result.getReplacedColorsCount() == 0) return;

        displayName = result.getColoredName();

        if(VersionUtils.hasAnvilRepairCostSupport()) {
            int cost = plugin.getConfig().getInt("level-cost");
            int costMultiplier = plugin.getConfig().getBoolean("cost-per-color") ? result.getReplacedColorsCount() : 1;
            int totalCost = cost * costMultiplier;

        }

        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        event.setResult(item);
    }
}