package one.hyro.spark.lib.builder;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import one.hyro.spark.lib.helper.registry.SparkItemRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
public class SparkItem {
    private final Material material;
    private final ItemStack stack;
    private ItemMeta meta;
    private Consumer<Player> leftClickConsumer;
    private Consumer<Player> rightClickConsumer;
    private Consumer<Player> interactConsumer;

    public SparkItem(Material material) {
        this.material = material;
        this.stack = new ItemStack(material);
        this.meta = stack.getItemMeta();
    }

    public SparkItem setAmount(int amount) {
        if (amount <= 0 || amount > 64) stack.setAmount(1);
        else stack.setAmount(amount);
        return this;
    }

    public SparkItem setDisplayName(Component component) {
        meta.displayName(component.decoration(TextDecoration.ITALIC, false));
        return this;
    }

    public SparkItem setLore(Component... components) {
        List<Component> componentList = Arrays.stream(components)
                .map(component -> component.decoration(TextDecoration.ITALIC, false))
                .toList();

        meta.lore(componentList);
        return this;
    }

    public SparkItem onLeftClick(Consumer<Player> consumer) {
        this.leftClickConsumer = consumer;
        return this;
    }

    public SparkItem onRightClick(Consumer<Player> consumer) {
        this.rightClickConsumer = consumer;
        return this;
    }

    public SparkItem onInteract(Consumer<Player> consumer) {
        this.interactConsumer = consumer;
        return this;
    }

    public SparkItem build() {
        stack.setItemMeta(meta);
        SparkItemRegistry.getInstance().register(this);
        return this;
    }
}
