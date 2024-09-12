package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import one.hyro.spark.lib.builder.SparkItem;
import one.hyro.spark.lib.builder.SparkMenu;
import one.hyro.spark.smp.enums.ShopCategory;
import one.hyro.spark.smp.instances.ShopItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopCommand {
    private static int buyingAmount = 1;
    private static int currentPage = 0;

    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("shop")
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;

                    SparkItem buildingCategory = new SparkItem(Material.STRIPPED_OAK_LOG)
                            .setDisplayName(Component.text("Building"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.BUILDING);
                            })
                            .build();

                    SparkItem armorCategory = new SparkItem(Material.DIAMOND_CHESTPLATE)
                            .setDisplayName(Component.text("Armory"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.ARMORY);
                            })
                            .build();

                    SparkItem decorationCategory = new SparkItem(Material.CHERRY_LEAVES)
                            .setDisplayName(Component.text("Decoration"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.DECORATION);
                            })
                            .build();

                    SparkItem foodCategory = new SparkItem(Material.COOKED_PORKCHOP)
                            .setDisplayName(Component.text("Food"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.FOOD);
                            })
                            .build();

                    SparkItem resourcesCategory = new SparkItem(Material.AMETHYST_SHARD)
                            .setDisplayName(Component.text("Resources"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.RESOURCES);
                            })
                            .build();

                    SparkItem engineeringCategory = new SparkItem(Material.REPEATER)
                            .setDisplayName(Component.text("Engineering"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.ENGINEERING);
                            })
                            .build();

                    SparkItem craftingCategory = new SparkItem(Material.FURNACE)
                            .setDisplayName(Component.text("Crafting"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.CRAFTING);
                            })
                            .build();

                    SparkItem knowledgeCategory = new SparkItem(Material.BOOK)
                            .setDisplayName(Component.text("Knowledge"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.KNOWLEDGE);
                            })
                            .build();

                    SparkItem chemistryCategory = new SparkItem(Material.CAULDRON)
                            .setDisplayName(Component.text("Chemistry"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.CHEMISTRY);
                            })
                            .build();

                    SparkItem toolsCategory = new SparkItem(Material.BRUSH)
                            .setDisplayName(Component.text("Tools"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.TOOLS);
                            })
                            .build();

                    SparkItem dropsCategory = new SparkItem(Material.SLIME_BALL)
                            .setDisplayName(Component.text("Drops"))
                            .onClick(p -> {
                                currentPage = 0;
                                buyingAmount = 1;
                                showCategoryMenu(p, ShopCategory.DROPS);
                            })
                            .build();

                    SparkMenu shopMenu = new SparkMenu()
                            .setTitle(Component.text("Shop"))
                            .setRows(5)
                            .setItem(12, resourcesCategory)
                            .setItem(13, armorCategory)
                            .setItem(14, foodCategory)
                            .setItem(20, decorationCategory)
                            .setItem(21, engineeringCategory)
                            .setItem(22, buildingCategory)
                            .setItem(23, craftingCategory)
                            .setItem(24, knowledgeCategory)
                            .setItem(30, chemistryCategory)
                            .setItem(31, toolsCategory)
                            .setItem(32, dropsCategory)
                            .fillColumn(0, Material.GRAY_STAINED_GLASS_PANE)
                            .fillColumn(8, Material.GRAY_STAINED_GLASS_PANE)
                            .build();

                    player.openInventory(shopMenu.getInventory());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static void showCategoryMenu(Player player, ShopCategory category) {
        int itemsPerPage = 36;
        int totalItems = category.getStacks().length;
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        SparkItem exit = new SparkItem(Material.BARRIER)
                .setDisplayName(Component.text("Back"))
                .onClick(p -> p.performCommand("shop"))
                .build();

        SparkMenu categoryMenu = new SparkMenu()
                .setTitle(Component.text("Shop"))
                .setRows(6)
                .setItem(49, exit)
                .fillRow(4, Material.GRAY_STAINED_GLASS_PANE)
                .build();

        if (currentPage > 0) {
            SparkItem previousPage = new SparkItem(Material.ARROW)
                    .setDisplayName(Component.text("Previous page"))
                    .onClick(p -> {
                        currentPage--;
                        showCategoryMenu(p, category);
                    })
                    .build();
            categoryMenu.setItem(48, previousPage);
        }

        if (currentPage < totalPages - 1) {
            SparkItem nextPage = new SparkItem(Material.ARROW)
                    .setDisplayName(Component.text("Next page"))
                    .onClick(p -> {
                        currentPage++;
                        showCategoryMenu(p, category);
                    })
                    .build();
            categoryMenu.setItem(50, nextPage);
        }

        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        for (int i = startIndex; i < endIndex; i++) {
            ShopItem shopItem = category.getStacks()[i];
            SparkItem item = new SparkItem(shopItem.material())
                    .setLore(
                            Component.text("Price: " + shopItem.buyPrice()),
                            Component.text("Sell price: " + shopItem.sellPrice())
                    )
                    .onClick(p -> showBuyMenu(p, shopItem.material()))
                    .build();

            categoryMenu.setItem(i - startIndex, item);
        }

        player.openInventory(categoryMenu.getInventory());
    }

    private static void showBuyMenu(Player player, Material material) {
        SparkItem buyItem = new SparkItem(material)
                .setAmount(buyingAmount)
                .build();

        SparkItem add1 = new SparkItem(Material.LIME_STAINED_GLASS_PANE)
                .setDisplayName(Component.text("Add 1"))
                .onClick(p -> {
                    buyingAmount = Math.min(buyingAmount + 1, 64);
                    buyItem.setAmount(buyingAmount);
                })
                .build();

        SparkItem add5 = new SparkItem(Material.LIME_STAINED_GLASS_PANE)
                .setDisplayName(Component.text("Add 5"))
                .onClick(p -> {
                    buyingAmount = Math.min(buyingAmount + 5, 64);
                    buyItem.setAmount(buyingAmount);
                })
                .build();

        SparkItem buyStack = new SparkItem(Material.ORANGE_STAINED_GLASS_PANE)
                .setDisplayName(Component.text("Buy the full stack"))
                .onClick(p -> {
                    buyingAmount = 64;
                    buyItem.setAmount(buyingAmount);
                })
                .build();

        SparkItem remove1 = new SparkItem(Material.RED_STAINED_GLASS_PANE)
                .setDisplayName(Component.text("Remove 1"))
                .onClick(p -> {
                    buyingAmount = Math.max(buyingAmount - 1, 1);
                    buyItem.setAmount(buyingAmount);
                })
                .build();

        SparkItem remove5 = new SparkItem(Material.RED_STAINED_GLASS_PANE)
                .setDisplayName(Component.text("Remove 5"))
                .onClick(p -> {
                    buyingAmount = Math.max(buyingAmount - 5, 1);
                    buyItem.setAmount(buyingAmount);
                })
                .build();

        SparkItem resetStack = new SparkItem(Material.YELLOW_STAINED_GLASS_PANE)
                .setDisplayName(Component.text("Reset stack"))
                .onClick(p -> {
                    buyingAmount = 1;
                    buyItem.setAmount(buyingAmount);
                })
                .build();

        SparkItem confirmPurchase = new SparkItem(Material.MINECART)
                .setDisplayName(Component.text("Confirm purchase"))
                .onClick(p -> {
                    p.getInventory().addItem(buyItem.getStack());
                    p.closeInventory();
                })
                .build();

        SparkItem cancelPurchase = new SparkItem(Material.BARRIER)
                .setDisplayName(Component.text("Cancel purchase"))
                .onClick(p -> {
                    p.closeInventory();
                    p.performCommand("shop");
                })
                .build();

        SparkMenu buyMenu = new SparkMenu()
                .setTitle(Component.text("Confirm"))
                .setRows(5)
                .setItem(11, remove5)
                .setItem(12, remove1)
                .setItem(13, buyItem)
                .setItem(14, add1)
                .setItem(15, add5)
                .setItem(20, resetStack)
                .setItem(22, confirmPurchase)
                .setItem(24, buyStack)
                .setItem(31, cancelPurchase)
                .fillColumn(0, Material.GRAY_STAINED_GLASS_PANE)
                .fillColumn(8, Material.GRAY_STAINED_GLASS_PANE)
                .build();

        player.openInventory(buyMenu.getInventory());
    }
}