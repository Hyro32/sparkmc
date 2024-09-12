package one.hyro.spark.smp.enums;

import lombok.Getter;
import one.hyro.spark.smp.instances.ShopItem;
import org.bukkit.Material;

@Getter
public enum ShopCategory {
    ARMORY(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    DECORATION(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    BUILDING(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    CRAFTING(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    ENGINEERING(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    RESOURCES(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    TOOLS(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    CHEMISTRY(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    DROPS(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    KNOWLEDGE(
            new ShopItem(Material.OAK_LOG, 100, 50)
    ),

    FOOD(
            new ShopItem(Material.OAK_LOG, 100, 50)
    );

    private final ShopItem[] stacks;

    ShopCategory(ShopItem... stacks) {
        this.stacks = stacks;
    }
}
