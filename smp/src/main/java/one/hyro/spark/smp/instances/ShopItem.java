package one.hyro.spark.smp.instances;

import org.bukkit.Material;

public record ShopItem(
    Material material,
    double buyPrice,
    double sellPrice
) {}
