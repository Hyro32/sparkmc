package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.spark.lib.helper.Cooldowns;
import one.hyro.spark.smp.SparkSmp;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnCommand {
    private static Location spawnLocation = SparkSmp.getInstance().getServer().getWorld("world").getSpawnLocation();

    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("spawn")
                .requires(source -> source.getSender().hasPermission("spark.spawn"))
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;
                    Cooldowns cooldowns = new Cooldowns(15);

                    if (cooldowns.hasCooldown(player.getUniqueId())) {
                        Component cooldownMessage = Component.translatable(
                                "context.error.spawnCooldown",
                                Component.text(cooldowns.getRemainingSeconds(player.getUniqueId()), NamedTextColor.GOLD)
                        ).color(NamedTextColor.RED);
                        player.sendMessage(cooldownMessage);
                        return Command.SINGLE_SUCCESS;
                    }

                    teleportToSpawn(player);
                    cooldowns.addCooldown(player.getUniqueId());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }

    private static void teleportToSpawn(Player player) {
        Component findingMessage = Component.translatable("context.success.spawnFinding").color(NamedTextColor.YELLOW);
        player.sendActionBar(findingMessage);

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
                    Component movedMessage = Component.translatable("context.error.spawnMoved").color(NamedTextColor.RED);
                    player.sendMessage(movedMessage);
                    cancel();
                    return;
                }

                if (seconds == 0) {
                    player.teleportAsync(spawnLocation).thenAccept(success -> {
                        if (!success) {
                            Component teleportFailed = Component.translatable("context.error.spawnTeleportFailed").color(NamedTextColor.RED);
                            player.sendMessage(teleportFailed);
                            cancel();
                            return;
                        }

                        Component teleportSuccess = Component.translatable("context.success.spawnSuccess").color(NamedTextColor.GOLD);
                        player.sendActionBar(teleportSuccess);
                        cancel();
                    });
                    return;
                }

                Component teleporting = Component.translatable(
                        "context.success.spawnTeleporting",
                        Component.text(seconds, NamedTextColor.DARK_GREEN)
                ).color(NamedTextColor.GREEN);

                player.sendActionBar(teleporting);
                seconds--;
            }
        }.runTaskTimer(SparkSmp.getInstance(), 0L, 20L);
    }

    public static void setSpawnLocation(Location location) {
        spawnLocation = location;
    }
}
