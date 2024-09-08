package one.hyro.spark.lib.builder;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import one.hyro.spark.lib.helper.registry.SparkItemRegistry;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
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
    private Consumer<Player> clickConsumer;
    private Consumer<Player> interactConsumer;

    public SparkItem(Material material) {
        this.material = material;
        this.stack = new ItemStack(material);
        this.meta = stack.getItemMeta();
        build();
    }

    public void setAmount(int amount) {
        if (amount <= 0 || amount > 64) stack.setAmount(1);
        else stack.setAmount(amount);
    }

    public void setDisplayName(Component component) {
        meta.displayName(component.decoration(TextDecoration.ITALIC, false));
    }

    public void setLore(Component... components) {
        List<Component> componentList = Arrays.stream(components)
                .map(component -> component.decoration(TextDecoration.ITALIC, false))
                .toList();

        meta.lore(componentList);
    }

    public void setEnchantment(Enchantment enchantment, int level) {
        stack.addEnchantment(enchantment, level);
    }

    public void hideEnchantments() {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public void build() {
        stack.setItemMeta(meta);
        SparkItemRegistry.getInstance().register(this);
    }
}
