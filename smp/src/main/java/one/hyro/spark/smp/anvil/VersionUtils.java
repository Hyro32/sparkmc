package one.hyro.spark.smp.anvil;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.AnvilInventory;

public class VersionUtils {

    private static Boolean hasHexColorSupport = null;
    private static Boolean hasAnvilRepairCostSupport = null;
    private static Boolean hasPdcSupport = null;

    public static boolean hasHexColorSupport() {
        if(hasHexColorSupport != null) {
            return hasHexColorSupport;
        }
        try {
            ChatColor.class.getDeclaredMethod("of", String.class);
            return hasHexColorSupport = true;
        } catch (NoSuchMethodException e) {
            return hasHexColorSupport = false;
        }
    }

    public static boolean hasAnvilRepairCostSupport() {
        if(hasAnvilRepairCostSupport != null) {
            return hasAnvilRepairCostSupport;
        }
        try {
            AnvilInventory.class.getDeclaredMethod("setRepairCost", int.class);
            return hasAnvilRepairCostSupport = true;
        } catch (NoSuchMethodException e) {
            return hasAnvilRepairCostSupport = false;
        }
    }



}