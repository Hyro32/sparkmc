package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.spark.lib.helper.Cooldowns;
import one.hyro.spark.smp.SparkSmp;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class RtpCommand  {
    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("rtp")
                .requires(source -> source.getSender().hasPermission("spark.rtp"))
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                    Cooldowns cooldowns = new Cooldowns(15);

                    if (cooldowns.hasCooldown(player.getUniqueId())) {
                        Component cooldownMessage = Component.translatable(
                                "context.error.randomTeleportCooldown",
                                Component.text(cooldowns.getRemainingSeconds(player.getUniqueId()), NamedTextColor.GOLD)
                        ).color(NamedTextColor.RED);

                        player.sendMessage(cooldownMessage);
                        return Command.SINGLE_SUCCESS;
                    }

                    randomTeleportPlayer(player);
                    cooldowns.addCooldown(player.getUniqueId());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static void randomTeleportPlayer(Player player) {
        Component findingMessage = Component.translatable("context.success.randomTeleportFinding").color(NamedTextColor.YELLOW);
        player.sendActionBar(findingMessage);

        BukkitScheduler scheduler = SparkSmp.getInstance().getServer().getScheduler();
        scheduler.runTaskAsynchronously(SparkSmp.getInstance(), () -> {
            World world = Bukkit.getWorld("world"); // TODO: Make this configurable
            Location location = getSafeLocation(world);

            if (location == null) {
                Component nullLocationMessage = Component.translatable("context.error.randomTeleportNoSafeLocation").color(NamedTextColor.RED);
                player.sendMessage(nullLocationMessage);
                return;
            }

            // Schedule chunk loading on the main thread
            scheduler.runTask(SparkSmp.getInstance(), () -> {
                new BukkitRunnable() {
                    final Location initialLocation = player.getLocation();
                    int seconds = 5;

                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            cancel();
                            return;
                        }

                        if (!initialLocation.getBlock().equals(player.getLocation().getBlock())) {
                            Component movedMessage = Component.translatable("context.error.randomTeleportMoved").color(NamedTextColor.RED);
                            player.sendMessage(movedMessage);
                            cancel();
                            return;
                        }

                        if (seconds == 0) {
                            player.teleportAsync(location).thenAccept(success -> {
                                if (!success) {
                                    Component teleportFailed = Component.translatable("context.error.randomTeleportFailed").color(NamedTextColor.RED);
                                    player.sendMessage(teleportFailed);
                                    cancel();
                                    return;
                                }

                                Component teleportSuccess = Component.translatable("context.success.randomTeleportSuccess").color(NamedTextColor.GOLD);
                                player.sendActionBar(teleportSuccess);
                                cancel();
                            });
                            return;
                        }

                        Component teleporting = Component.translatable(
                                "context.success.randomTeleportTeleporting",
                                Component.text(seconds, NamedTextColor.DARK_GREEN)
                        ).color(NamedTextColor.GREEN);

                        player.sendActionBar(teleporting);
                        seconds--;
                    }
                }.runTaskTimer(SparkSmp.getInstance(), 0L, 20L);
            });
        });
    }

    private static Location getSafeLocation(World world) {
        Location location = null;
        boolean isLocationSafe = false;
        int attempts = 0;
        int radius = 1000;

        while (!isLocationSafe && attempts < 10) {
            int x = (int) (Math.random() * radius * 2) - radius;
            int z = (int) (Math.random() * radius * 2) - radius;
            int y = world.getHighestBlockYAt(x, z);

            location = new Location(world, x, y, z);
            isLocationSafe = isLocationSafe(location);
            attempts++;
        }

        if (!isLocationSafe) {
            location = world.getSpawnLocation();
        } else {
            loadChucksAroundLocation(location);
        }

        return location;
    }

    private static boolean isLocationSafe(Location location) {
        Material blockType = location.getBlock().getType();
        Material belowBlockType = location.clone().subtract(0, 1, 0).getBlock().getType();

        boolean isSolid = blockType.isSolid();
        boolean isBelowSolid = belowBlockType.isSolid();
        boolean isAboveAir = location.clone().add(0, 1, 0).getBlock().getType().isAir();

        return isSolid && isBelowSolid && isAboveAir;
    }

    private static void loadChucksAroundLocation(Location location) {
        World world = location.getWorld();
        Chunk chunk = location.getChunk();
        int radius = 1;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                world.getChunkAt(chunk.getX() + x, chunk.getZ() + z);
            }
        }
    }
}
