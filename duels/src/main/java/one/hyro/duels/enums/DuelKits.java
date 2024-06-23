package one.hyro.duels.enums;

import lombok.Getter;
import one.hyro.builders.CustomItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

@Getter
public enum DuelKits {
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
    );

    private final String name;
    private final String lore;
    private final String permission;
    private final CustomItem helmet;
    private final CustomItem chestplate;
    private final CustomItem leggings;
    private final CustomItem boots;
    private final List<CustomItem> items;

    DuelKits(String name, String lore, String permission, CustomItem helmet, CustomItem chestplate, CustomItem leggings, CustomItem boots, List<CustomItem> items) {
        this.name = name;
        this.lore = lore;
        this.permission = permission;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.items = items;
    }
}
