package one.hyro.spark.smp.deathmessage;

import one.hyro.spark.smp.SparkSmp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DeathMessageManager implements Listener {

    private final JavaPlugin plugin;

    public DeathMessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent damageEvent = player.getLastDamageCause();

        if (damageEvent == null) {
            return; // No damage event means no specific cause
        }

        // Determine the cause of death
        String deathCause = getDeathCause(damageEvent);
        String killerName = getKillerName(player);

        // Format the death message
        String deathMessage = formatDeathMessage(player, killerName, deathCause);

        // Suppress the default death message
        event.setDeathMessage(null);

        // Send the custom death message to the server
        Bukkit.broadcastMessage(ChatColor.RED + deathMessage);

        // Send the custom death message to the SMP class for Discord
        SparkSmp smpPlugin = (SparkSmp) plugin;
        smpPlugin.sendDeathMessageToDiscord(deathMessage);
    }

    private String getDeathCause(EntityDamageEvent damageEvent) {
        switch (damageEvent.getCause()) {
            case ENTITY_ATTACK:
                return "was slain";
            case FALL:
                return "fell";
            case FIRE:
                return "was burned";
            case LAVA:
                return "was melted by lava";
            case DROWNING:
                return "drowned";
            case SUFFOCATION:
                return "was suffocated";
            case STARVATION:
                return "starved";
            case VOID:
                return "fell into the void";
            case LIGHTNING:
                return "was struck by lightning";
            case MAGIC:
                return "was killed by magic";
            default:
                return "died";
        }
    }

    private String getKillerName(Player player) {
        EntityDamageEvent damageEvent = player.getLastDamageCause();

        if (damageEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageEvent = (EntityDamageByEntityEvent) damageEvent;
            Entity damager = entityDamageEvent.getDamager();
            if (damager instanceof Player) {
                return ((Player) damager).getName();
            } else if (damager instanceof LivingEntity) {
                return damager.getType().name().replace("_", " ").toLowerCase();
            }
        }

        return "the environment";
    }

    private String formatDeathMessage(Player player, String killerName, String deathCause) {
        if ("the environment".equals(killerName)) {
            return player.getDisplayName() + " " + deathCause + ".";
        } else {
            return player.getDisplayName() + " " + deathCause + " by " + killerName + ".";
        }
    }
}
