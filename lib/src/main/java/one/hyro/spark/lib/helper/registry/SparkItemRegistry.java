package one.hyro.spark.lib.helper.registry;

import one.hyro.spark.lib.builder.SparkItem;
import one.hyro.spark.lib.interfaces.Registry;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SparkItemRegistry implements Registry<SparkItem> {
    private List<SparkItem> items = new ArrayList<>();
    private static SparkItemRegistry instance;

    public SparkItemRegistry() {
        instance = this;
    }

    @Override
    public void register(SparkItem record) {
        items.add(record);
    }

    @Override
    public void unregister(SparkItem record) {
        items.remove(record);
    }

    public SparkItem getSparkItem(ItemStack stack) {
        return items.stream()
                .filter(item -> item.getStack().equals(stack))
                .findFirst()
                .orElse(null);
    }

    public static SparkItemRegistry getInstance() {
        if (instance == null) instance = new SparkItemRegistry();
        return instance;
    }
}
