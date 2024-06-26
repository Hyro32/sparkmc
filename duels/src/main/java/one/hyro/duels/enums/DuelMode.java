package one.hyro.duels.enums;

import lombok.Getter;
import one.hyro.builders.CustomItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public enum DuelMode {
    CLASSIC(
            "name.kits.classic",
            "lore.kits.classic",
            null,
            new CustomItem(Material.DIAMOND_HELMET)
                    .setCustomId("classic-helmet")
                    .setEnchantment(Enchantment.PROTECTION, 2)
                    .build(),
            new CustomItem(Material.DIAMOND_CHESTPLATE)
                    .setCustomId("classic-chestplate")
                    .setEnchantment(Enchantment.PROTECTION, 2)
                    .build(),
            new CustomItem(Material.DIAMOND_LEGGINGS)
                    .setCustomId("classic-leggings")
                    .setEnchantment(Enchantment.PROTECTION, 2)
                    .build(),
            new CustomItem(Material.DIAMOND_BOOTS)
                    .setCustomId("classic-boots")
                    .setEnchantment(Enchantment.PROTECTION, 2)
                    .build(),
            List.of(
                    new CustomItem(Material.DIAMOND_SWORD)
                            .setCustomId("classic-sword")
                            .setEnchantment(Enchantment.SHARPNESS, 1)
                            .build(),
                    new CustomItem(Material.BOW)
                            .setCustomId("classic-bow")
                            .build(),
                    new CustomItem(Material.ARROW)
                            .setCustomId("classic-arrows")
                            .amount(32)
                            .build(),
                    new CustomItem(Material.GOLDEN_APPLE)
                            .setCustomId("classic-gapple")
                            .amount(3)
                            .build(),
                    new CustomItem(Material.OAK_PLANKS)
                            .setCustomId("classic-blocks")
                            .amount(32)
                            .build()
            )
    ),
    BOW(
            "name.kits.bow",
            "lore.kits.bow",
            null,
            new CustomItem(Material.LEATHER_HELMET)
                    .setCustomId("bow-helmet")
                    .build(),
            new CustomItem(Material.LEATHER_CHESTPLATE)
                    .setCustomId("bow-chestplate")
                    .build(),
            new CustomItem(Material.LEATHER_LEGGINGS)
                    .setCustomId("bow-leggings")
                    .build(),
            new CustomItem(Material.LEATHER_BOOTS)
                    .setCustomId("bow-boots")
                    .build(),
            List.of(
                    new CustomItem(Material.BOW)
                            .setCustomId("bow-bow")
                            .setEnchantment(Enchantment.INFINITY, 1)
                            .setEnchantment(Enchantment.POWER, 2)
                            .build(),
                    new CustomItem(Material.ARROW)
                            .setCustomId("bow-arrows")
                            .amount(1)
                            .build(),
                    new CustomItem(Material.OAK_PLANKS)
                            .setCustomId("bow-blocks")
                            .amount(32)
                            .build()
            )

    );

    private final String name;
    private final String lore;
    private final String permission;
    private final CustomItem helmet;
    private final CustomItem chestplate;
    private final CustomItem leggings;
    private final CustomItem boots;
    private final List<CustomItem> items;

    DuelMode(String name, String lore, String permission, CustomItem helmet, CustomItem chestplate, CustomItem leggings, CustomItem boots, List<CustomItem> items) {
        this.name = name;
        this.lore = lore;
        this.permission = permission;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.items = items;
    }

    public void setPlayerInventory(Player player) {
        player.getInventory().setHelmet(helmet.getItem());
        player.getInventory().setChestplate(chestplate.getItem());
        player.getInventory().setLeggings(leggings.getItem());
        player.getInventory().setBoots(boots.getItem());
        for (CustomItem item : items) player.getInventory().addItem(item.getItem());
    }
}
