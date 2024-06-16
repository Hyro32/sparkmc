package one.hyro.managers;

import one.hyro.builders.GameMenu;
import one.hyro.builders.CustomItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {
    private static MenuManager instance;
    private final Map<String, GameMenu> menus;
    private final Map<String, CustomItem> items;

    private MenuManager() {
        this.menus = new HashMap<>();
        this.items = new HashMap<>();
    }

    public void registerMenu(GameMenu menu) {
        menus.put(menu.getCustomId(), menu);
    }

    public void registerItem(CustomItem item) {
        items.put(item.getCustomId(), item);
    }

    public GameMenu getMenu(String customId) {
        return menus.get(customId);
    }

    public GameMenu getMenu(Inventory inventory) {
        for (GameMenu menu : menus.values())
            if (menu.getInventory().equals(inventory)) return menu;
        return null;
    }

    public CustomItem getItem(String customId) {
        return items.get(customId);
    }

    public CustomItem getItem(ItemStack itemStack) {
        for (CustomItem item : items.values())
            if (item.getItem().equals(itemStack)) return item;
        return null;
    }

    public static MenuManager getInstance() {
        if (instance == null) instance = new MenuManager();
        return instance;
    }

}