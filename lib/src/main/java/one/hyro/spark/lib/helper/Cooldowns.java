package one.hyro.spark.lib.helper;

import one.hyro.spark.lib.SparkLib;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldowns {
    private BukkitScheduler scheduler;
    private Map<UUID, Integer> cooldowns;
    private final int seconds;

    public Cooldowns(int seconds) {
        this.scheduler = SparkLib.getPlugin().getServer().getScheduler();
        this.cooldowns = new HashMap<>();
        this.seconds = seconds;
    }

    public void addCooldown(UUID uuid) {
        cooldowns.put(uuid, seconds);
        scheduler.runTaskTimer(SparkLib.getPlugin(), task -> {
            int remainingSeconds = cooldowns.get(uuid);
            if (remainingSeconds == 1) {
                cooldowns.remove(uuid);
                task.cancel();
                return;
            }
            cooldowns.put(uuid, remainingSeconds - 1);
        }, 0L, 20L);
    }

    public int getRemainingSeconds(UUID uuid) {
        return cooldowns.get(uuid);
    }

    public boolean hasCooldown(UUID uuid) {
        return cooldowns.containsKey(uuid);
    }
}
