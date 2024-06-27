package one.hyro.duels.events;

import net.kyori.adventure.text.Component;
import one.hyro.managers.GameManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        GameManager gameManager = GameManager.getInstance();

        if (event.getEntity() instanceof Player entity && gameManager.isInGame(entity)) {
            Entity damager = event.getDamager();

            double health = Math.round(entity.getHealth() * 10.0) / 10.0;
            Component infoHealth = Component.text(entity.getName() + " has " + health + " health left!");

            if (damager instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter) {
                shooter.sendMessage(infoHealth);
            }

            if (damager instanceof Player player) {
                player.sendMessage(infoHealth);
            }
        }
    }
}
