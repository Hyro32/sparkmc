package one.hyro.spark.smp.shop;

import one.hyro.spark.smp.economy.Economy;
import one.hyro.spark.smp.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

public class ShopGUIManager implements CommandExecutor, Listener {

    private final JavaPlugin plugin;
    private static Economy economy = null;
    private final String prefix;

    public ShopGUIManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.prefix = ChatUtils.getPrefix();
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public void openMainShop(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);

        Inventory shop = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + "Shop");

        // Armor Keys item
        ItemStack armorKeys = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta armorKeysMeta = armorKeys.getItemMeta();
        if (armorKeysMeta != null) {
            armorKeysMeta.addEnchant(Enchantment.LURE, 1, true);
            armorKeysMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            armorKeysMeta.setDisplayName(ChatColor.GOLD + "Armor Keys");
            armorKeys.setItemMeta(armorKeysMeta);
        }
        shop.setItem(10, armorKeys);

        // Weapons item
        ItemStack weapons = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta weaponsMeta = weapons.getItemMeta();
        if (weaponsMeta != null) {
            weaponsMeta.addEnchant(Enchantment.LURE, 1, true);
            weaponsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            weaponsMeta.setDisplayName(ChatColor.GOLD + "Weapons");
            weapons.setItemMeta(weaponsMeta);
        }
        shop.setItem(13, weapons);

        // Potions item
        ItemStack potions = new ItemStack(Material.POTION);
        ItemMeta potionsMeta = potions.getItemMeta();
        if (potionsMeta != null) {
            potionsMeta.addEnchant(Enchantment.LURE, 1, true);
            potionsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            potions.setItemMeta(potionsMeta);
        }
        shop.setItem(16, potions);

        // Close button
        shop.setItem(26, createCloseButton());

        player.openInventory(shop);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            openMainShop(player);
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }

    public void openArmorKeysShop(Player player) {
        Inventory armorShop = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + "Armor Keys Shop");

        ItemStack ironKey = createKey(ChatColor.WHITE + "Iron Armor Kit Key", 20);

        ItemStack diamondKey = createKey(ChatColor.AQUA + "Diamond Armor Kit Key", 100);

        ItemStack netheriteKey = createKey(ChatColor.DARK_PURPLE + "Netherite Armor Kit Key", 200);

        armorShop.setItem(10, ironKey);
        armorShop.setItem(13, diamondKey);
        armorShop.setItem(16, netheriteKey);

        armorShop.setItem(18, createBackButton());

        armorShop.setItem(26, createCloseButton());

        player.openInventory(armorShop);
    }

    public void openWeaponsShop(Player player) {
        Inventory weaponsShop = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + "Weapons Shop");

        ItemStack diamondSword = createWeapon(ChatColor.AQUA + "Diamond Sword", Material.DIAMOND_SWORD, 100);
        ItemStack diamondAxe = createWeapon(ChatColor.AQUA + "Diamond Axe", Material.DIAMOND_AXE, 90);
        ItemStack bow = createWeapon(ChatColor.YELLOW + "Bow", Material.BOW, 50);
        ItemStack arrows = createWeapon(ChatColor.YELLOW + "32 Arrows", Material.ARROW, 5, 32);

        weaponsShop.setItem(10, diamondSword);
        weaponsShop.setItem(12, diamondAxe);
        weaponsShop.setItem(14, bow);
        weaponsShop.setItem(16, arrows);

        weaponsShop.setItem(18, createBackButton());

        weaponsShop.setItem(26, createCloseButton());

        player.openInventory(weaponsShop);
    }

    private ItemStack createWeapon(String name, Material material, int cost, int amount) {
        ItemStack weapon = new ItemStack(material, amount);
        ItemMeta meta = weapon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);

            if (material == Material.BOW) {
                meta.addEnchant(Enchantment.POWER, 2, true);
                meta.addEnchant(Enchantment.UNBREAKING, 3, true);
                meta.addEnchant(Enchantment.PUNCH, 1, true);
            } else {
                meta.addEnchant(Enchantment.LURE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            meta.setLore(Arrays.asList(ChatColor.YELLOW + "Cost: $" + ChatColor.GOLD + cost));
            weapon.setItemMeta(meta);
        }
        return weapon;
    }

    private ItemStack createWeapon(String name, Material material, int cost) {
        return createWeapon(name, material, cost, 1);
    }

    private ItemStack createKey(String name, int cost) {
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = key.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setCustomModelData(2026);
            meta.setLore(Arrays.asList(ChatColor.YELLOW + "Cost: $" + ChatColor.GOLD + cost));
            key.setItemMeta(meta);
        }
        return key;
    }

    public void openPotionsShop(Player player) {
        Inventory potionsShop = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + "Potions Shop");

        ItemStack healingPotion = createPotion(ChatColor.RED + "Healing Potion", Material.POTION, 25, PotionType.HEALING);
        ItemStack strengthPotion = createPotion(ChatColor.RED + "Strength Potion", Material.POTION, 50, PotionType.STRENGTH);
        ItemStack speedPotion = createPotion(ChatColor.RED + "Speed Potion", Material.POTION, 30, PotionType.SWIFTNESS);

        potionsShop.setItem(10, healingPotion);
        potionsShop.setItem(13, strengthPotion);
        potionsShop.setItem(16, speedPotion);

        potionsShop.setItem(18, createBackButton());
        potionsShop.setItem(26, createCloseButton());

        player.openInventory(potionsShop);
    }

    private ItemStack createPotion(String name, Material material, int cost, PotionType potionType) {
        ItemStack potion = new ItemStack(material);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setBasePotionType(potionType);
            meta.addEnchant(Enchantment.LURE, 1, true); // Optional enchantment
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(Arrays.asList(ChatColor.YELLOW + "Cost: $" + ChatColor.GOLD + cost));
            potion.setItemMeta(meta);
        }
        return potion;
    }



    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryName = event.getView().getTitle();

        if (event.getCurrentItem() != null) {
            ItemStack clickedItem = event.getCurrentItem();
            ItemMeta meta = clickedItem.getItemMeta();

            if (meta != null) {
                String itemName = meta.getDisplayName();

                if (itemName.equals(ChatColor.RED + "Close")) {
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    player.closeInventory();
                    return;
                }

                if (itemName.equals(ChatColor.YELLOW + "Back")) {
                    openMainShop(player);
                    return;
                }

                if (inventoryName.equals(ChatColor.DARK_GREEN + "Shop")) {
                    event.setCancelled(true);

                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                    if (clickedItem.getType() == Material.TRIPWIRE_HOOK) {
                        openArmorKeysShop(player);
                    } else if (clickedItem.getType() == Material.DIAMOND_SWORD) {
                        openWeaponsShop(player);
                    } else if (clickedItem.getType() == Material.POTION) {
                        openPotionsShop(player);
                    }
                } else if (inventoryName.equals(ChatColor.DARK_GREEN + "Armor Keys Shop") ||
                        inventoryName.equals(ChatColor.DARK_GREEN + "Weapons Shop") ||
                        inventoryName.equals(ChatColor.DARK_GREEN + "Potions Shop")) {
                    event.setCancelled(true);

                    int cost = getCostFromItemName(itemName);
                    if (cost > 0 && deductBalance(player, cost)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        if (clickedItem.getItemMeta().getDisplayName().contains("Iron Armor Kit Key")) {
                            ItemStack ironKey = createKey(ChatColor.WHITE + "Iron Armor Kit Key", 20);
                            player.getInventory().addItem(ironKey);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Diamond Armor Kit Key")) {
                            ItemStack diamondKey = createKey(ChatColor.AQUA + "Diamond Armor Kit Key", 100);
                            player.getInventory().addItem(diamondKey);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Netherite Armor Kit Key")) {
                            ItemStack netheriteKey = createKey(ChatColor.DARK_PURPLE + "Netherite Armor Kit Key", 200);
                            player.getInventory().addItem(netheriteKey);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Diamond Sword")) {
                            ItemStack diamondswordItemStack = new ItemStack(Material.DIAMOND_SWORD);
                            ItemMeta diamondswordItemMeta = diamondswordItemStack.getItemMeta();
                            diamondswordItemMeta.addEnchant(Enchantment.SHARPNESS, 3, true);
                            diamondswordItemMeta.addEnchant(Enchantment.UNBREAKING, 2, true);
                            diamondswordItemMeta.addEnchant(Enchantment.MENDING, 1, true);
                            diamondswordItemStack.setItemMeta(diamondswordItemMeta);
                            player.getInventory().addItem(diamondswordItemStack);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Diamond Axe")) {
                            ItemStack diamondaxeItemStack = new ItemStack(Material.DIAMOND_SWORD);
                            ItemMeta diamondaxeItemMeta = diamondaxeItemStack.getItemMeta();
                            diamondaxeItemMeta.addEnchant(Enchantment.SHARPNESS, 3, true);
                            diamondaxeItemMeta.addEnchant(Enchantment.UNBREAKING, 2, true);
                            diamondaxeItemMeta.addEnchant(Enchantment.MENDING, 1, true);
                            diamondaxeItemStack.setItemMeta(diamondaxeItemMeta);
                            player.getInventory().addItem(diamondaxeItemStack);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Bow")) {
                            ItemStack bow = new ItemStack(Material.BOW);
                            ItemMeta bowmeta = bow.getItemMeta();
                            bowmeta.addEnchant(Enchantment.POWER, 3, true);
                            bowmeta.addEnchant(Enchantment.UNBREAKING, 2, true);
                            bowmeta.addEnchant(Enchantment.MENDING, 1, true);
                            bow.setItemMeta(bowmeta);
                            player.getInventory().addItem(bow);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Arrow")) {
                            ItemStack arrow = new ItemStack(Material.ARROW, 32);
                            player.getInventory().addItem(arrow);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Healing Potion")) {
                            ItemStack healingpotion = new ItemStack(Material.POTION, 1);
                            PotionMeta healingmeta = (PotionMeta) healingpotion.getItemMeta();
                            healingmeta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 10, 0), true);
                            healingmeta.setDisplayName(ChatColor.RED + "Healing Potion");
                            healingpotion.setItemMeta(meta);
                            player.getInventory().addItem(healingpotion);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Strength Potion")) {
                            ItemStack strengthpotion = new ItemStack(Material.POTION, 1);
                            PotionMeta strenthmeta = (PotionMeta) strengthpotion.getItemMeta();
                            strenthmeta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, 360, 0), true);
                            strenthmeta.setDisplayName(ChatColor.YELLOW + "Strength Potion");
                            strengthpotion.setItemMeta(meta);
                            player.getInventory().addItem(strengthpotion);
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Speed Potion")) {
                            ItemStack speedpotion = new ItemStack(Material.POTION, 1);
                            PotionMeta speedmeta = (PotionMeta) speedpotion.getItemMeta();
                            speedmeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 360, 0), true);
                            speedmeta.setDisplayName(ChatColor.AQUA + "Speed Potion");
                            speedpotion.setItemMeta(meta);
                            player.getInventory().addItem(speedpotion);
                        }
                        player.sendMessage(prefix + ChatColor.GREEN + "You purchased " + itemName + " for $" + cost + ".");
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        player.sendMessage(prefix + ChatColor.RED + "You don't have enough money to buy " + itemName + ".");
                    }
                }
            }
        }
    }


    private int getCostFromItemName(String itemName) {
        if (itemName.contains("Iron")) return 20;
        if (itemName.contains("Diamond Sword")) return 100;
        if (itemName.contains("Diamond Axe")) return 90;
        if (itemName.contains("Bow")) return 50;
        if (itemName.contains("Arrow")) return 5;
        if (itemName.contains("Diamond")) return 100;
        if (itemName.contains("Netherite")) return 200;
        if (itemName.contains("Healing Potion")) return 25;
        if (itemName.contains("Strength Potion")) return 50;
        if (itemName.contains("Speed Potion")) return 30;
        return 0;
    }


    private boolean deductBalance(Player player, int cost) {
        if (economy != null && economy.has(player, cost)) {
            economy.withdrawPlayer(player, cost);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item != null && item.getType() == Material.TRIPWIRE_HOOK && item.getItemMeta().getCustomModelData() == 2026) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String itemName = meta.getDisplayName();

                if (itemName.contains("Iron Armor Kit Key")) {
                    giveIronArmorKit(player);
                    player.getInventory().removeItem(item);
                } else if (itemName.contains("Diamond Armor Kit Key")) {
                    giveDiamondArmorKit(player);
                    player.getInventory().removeItem(item);
                } else if (itemName.contains("Netherite Armor Kit Key")) {
                    giveNetheriteArmorKit(player);
                    player.getInventory().removeItem(item);
                }
            }
        }
    }

    private void giveIronArmorKit(Player player) {
        player.getInventory().addItem(createEnchantedArmor(Material.IRON_HELMET));
        player.getInventory().addItem(createEnchantedArmor(Material.IRON_CHESTPLATE));
        player.getInventory().addItem(createEnchantedArmor(Material.IRON_LEGGINGS));
        player.getInventory().addItem(createEnchantedArmor(Material.IRON_BOOTS));
        player.sendMessage(prefix + ChatColor.WHITE + "You received an Iron Armor Kit!");
    }

    private void giveDiamondArmorKit(Player player) {
        player.getInventory().addItem(createEnchantedArmor(Material.DIAMOND_HELMET));
        player.getInventory().addItem(createEnchantedArmor(Material.DIAMOND_CHESTPLATE));
        player.getInventory().addItem(createEnchantedArmor(Material.DIAMOND_LEGGINGS));
        player.getInventory().addItem(createEnchantedArmor(Material.DIAMOND_BOOTS));
        player.sendMessage(prefix + ChatColor.AQUA + "You received a Diamond Armor Kit!");
    }

    private void giveNetheriteArmorKit(Player player) {
        player.getInventory().addItem(createEnchantedArmor(Material.NETHERITE_HELMET));
        player.getInventory().addItem(createEnchantedArmor(Material.NETHERITE_CHESTPLATE));
        player.getInventory().addItem(createEnchantedArmor(Material.NETHERITE_LEGGINGS));
        player.getInventory().addItem(createEnchantedArmor(Material.NETHERITE_BOOTS));
        player.sendMessage(prefix + ChatColor.DARK_PURPLE + "You received a Netherite Armor Kit!");
    }

    private ItemStack createEnchantedArmor(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.PROTECTION, 2, true); // Protection II
            meta.addEnchant(Enchantment.UNBREAKING, 3, true); // Unbreaking III
            meta.addEnchant(Enchantment.MENDING, 1, true); // Mending
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createCloseButton() {
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta meta = closeButton.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Close");
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            closeButton.setItemMeta(meta);
        }
        return closeButton;
    }

    private ItemStack createBackButton() {
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta meta = backButton.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Back");
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            backButton.setItemMeta(meta);
        }
        return backButton;
    }
}
