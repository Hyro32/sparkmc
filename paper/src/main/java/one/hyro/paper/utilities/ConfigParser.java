package one.hyro.paper.utilities;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import one.hyro.paper.HyrosPaper;
import one.hyro.paper.managers.MenusManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigParser {
    public static Map<ItemStack, Integer> parseItems(FileConfiguration configuration, String path, @Nullable Player player) {
        List<Map<?, ?>> items = configuration.getMapList(path);
        Map<ItemStack, Integer> itemStacks = new HashMap<>();

        for (Map<?, ?> item : items) {
            String materialName = (String) item.get("material");
            int slot = (int) item.get("slot");

            if (materialName == null || !item.containsKey("slot")) {
                Bukkit.getLogger().warning("Invalid item parsed in file: " + configuration.getName() + " at item: " + item);
                continue;
            }

            Material material = Material.matchMaterial(materialName);
            ItemStack stack;
            if (materialName.equals("CUSTOM_HEAD")) {
                String texture = (String) item.get("texture");
                if (texture == null || texture.isEmpty()) {
                    Bukkit.getLogger().warning("Texture must be declared for a CUSTOM_HEAD at: " + configuration.getName() + " at item: " + item);
                    continue;
                }
                stack = CustomHeads.getCustomHead(texture);
            } else if (material == Material.PLAYER_HEAD) {
                if (player == null) continue;
                stack = CustomHeads.getPlayerHead(player);
            } else {
                if (material == null) {
                    Bukkit.getLogger().warning("Invalid material item at: " + item);
                    continue;
                }
                stack = new ItemStack(material);
            }

            ItemMeta meta = stack.getItemMeta();

            String name = (String) item.get("name");
            if (name != null) meta.setDisplayName(Chalk.colorizeLegacy(name));

            List<String> lore = (List<String>) item.get("lore");
            if (lore != null) meta.setLore(Chalk.colorizeLegacyLore(lore));

            Boolean enchanted = (Boolean) item.get("enchanted");
            if (enchanted == null) enchanted = false;
            if (enchanted) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DENSITY, 1, false);
            }

            stack.setItemMeta(meta);
            itemStacks.put(stack, slot);
        }

        return itemStacks;
    }

    public static void runItemCommands(FileConfiguration configuration, String path, int slot, Player player) {
        List<Map<?, ?>> itemsList = configuration.getMapList(path);

        for (Map<?, ?> itemMap : itemsList) {
            if ((int) itemMap.get("slot") != slot) continue;
            List<String> commands = (List<String>) itemMap.get("commands");

            for (String command : commands) {
                String label = command.split(" ")[0];
                String args = command.substring(label.length() + 1);

                switch (label) {
                    case "connect":
                        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(args);
                        player.sendPluginMessage(HyrosPaper.getInstance(), "BungeeCord", out.toByteArray());
                        break;
                    case "menu":
                        MenusManager.openMenu(player, args);
                        break;
                    case "message":
                        player.sendMessage(args);
                        break;
                    default:
                        Bukkit.getLogger().warning("Invalid command type: " + label);
                }
            }
        }
    }
}
