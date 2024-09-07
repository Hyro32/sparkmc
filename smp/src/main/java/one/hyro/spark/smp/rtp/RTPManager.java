package one.hyro.spark.smp.rtp;

import com.tserato.smp.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RTPManager implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final String prefix;

    public RTPManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.prefix = ChatUtils.getPrefix();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("rtp")) {
            showFindingMessageAndStartRTP(player);
            return true;
        }

        return false;
    }

    private void showFindingMessageAndStartRTP(Player player) {
        // Display "Finding safe location..." message immediately
        String actionBar = ChatColor.YELLOW + "Finding safe location...";
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));

        // Perform the location finding asynchronously
        new BukkitRunnable() {
            private Location randomLocation;

            @Override
            public void run() {
                randomLocation = getSafeRandomLocation(player.getWorld());

                if (randomLocation == null) {
                    // Send the failure message on the main thread
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage(prefix + ChatColor.RED + "Failed to find a safe location. Teleportation aborted.");
                        }
                    }.runTask(plugin);
                    return;
                }

                // Schedule the countdown and teleportation on the main thread
                new BukkitRunnable() {
                    private int countdown = 3;
                    private final Location initialLocation = player.getLocation().clone(); // Store initial location

                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            cancel();
                            return;
                        }

                        // Check if the player has moved
                        if (!player.getLocation().getBlock().equals(initialLocation.getBlock())) {
                            player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                            cancel();
                            return;
                        }

                        // Countdown logic
                        if (countdown > 0) {
                            String countdownBar = ChatColor.RED + "Teleporting in " + ChatColor.BOLD + ChatColor.AQUA + countdown + ChatColor.RED + " seconds. Do not move!";
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(countdownBar));
                            countdown--;
                        } else {
                            // Perform teleportation if player has not moved
                            if (player.getLocation().getBlock().equals(initialLocation.getBlock())) {
                                player.teleport(randomLocation);
                                String actionBar = ChatColor.GREEN + "Teleported!";
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "Teleportation aborted due to movement!");
                            }
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L); // Check every second (20 ticks)
            }
        }.runTaskAsynchronously(plugin);
    }

    private Location getSafeRandomLocation(World world) {
        int radius = 10000;
        Location location;
        int attempts = 0;
        boolean isSafe;

        do {
            // Generate random coordinates
            int x = random.nextInt(2 * radius) - radius;
            int z = random.nextInt(2 * radius) - radius;
            int y = world.getHighestBlockYAt(x, z);

            // Create location
            location = new Location(world, x, y, z);
            isSafe = isLocationSafe(location);

            attempts++;
        } while (!isSafe && attempts < 10); // Retry up to 10 times

        if (!isSafe) {
            // As a fallback, use the world spawn location if a safe location is not found after retries
            location = world.getSpawnLocation();
        }

        loadChunksAroundLocation(location);
        return location;
    }

    private boolean isLocationSafe(Location location) {
        Material blockType = location.getBlock().getType();
        Material belowBlockType = location.clone().subtract(0, 1, 0).getBlock().getType();

        // Ensure the block is solid and not water, lava, or leaves
        boolean isSolid = blockType.isSolid() && blockType != Material.LAVA && blockType != Material.WATER && !blockType.name().endsWith("_LEAVES");
        boolean isBelowSolid = belowBlockType.isSolid() && belowBlockType != Material.LAVA && belowBlockType != Material.WATER && !belowBlockType.name().endsWith("_LEAVES");

        // Also, ensure the block above is air (to avoid suffocation)
        boolean isAboveAir = location.clone().add(0, 1, 0).getBlock().getType() == Material.AIR;

        return isSolid && isBelowSolid && isAboveAir;
    }

    private void loadChunksAroundLocation(Location location) {
        World world = location.getWorld();
        Chunk chunk = location.getChunk();
        int radius = 1; // Number of chunks to load around the location

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                world.getChunkAt(chunk.getX() + x, chunk.getZ() + z);
            }
        }
    }
}
