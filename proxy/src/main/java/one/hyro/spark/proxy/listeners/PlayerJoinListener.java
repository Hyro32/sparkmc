package one.hyro.spark.proxy.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.spark.proxy.BanInfo;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;

public class PlayerJoinListener {

    private final ProxyServer proxyServer;
    private final Logger logger;

    public PlayerJoinListener(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        logger.info("Player " + player.getUsername() + " is joining the server.");

        // Step 1: Check if the player is banned (replace with actual database query)
        Optional<BanInfo> banInfo = checkIfPlayerIsBanned(playerUuid);

        if (banInfo.isPresent()) {
            BanInfo info = banInfo.get();

            // Step 2: If banned, deny login and show a ban message
            event.setResult(LoginEvent.ComponentResult.denied(
                    Component.text("You are banned from this server!\n" +
                                    "Reason: " + info.getReason() + "\n" +
                                    "Expires: " + (info.isPermanent() ? "Never" : info.getExpiryDate()),
                            NamedTextColor.RED)
            ));
            logger.info("Player " + player.getUsername() + " was denied access due to a ban.");
        } else {
            logger.info("Player " + player.getUsername() + " is not banned.");
        }
    }

    // This method should query the database to check if the player is banned
    private Optional<BanInfo> checkIfPlayerIsBanned(UUID playerUuid) {
        // Replace this comment with actual database interaction to check if the player is banned
        // Example:
        // 1. Query the database with the player's UUID.
        // 2. If a ban record is found, return an Optional<BanInfo> with ban details.
        // 3. If no ban is found, return Optional.empty().

        // Simulating a banned player (example)
        if (playerUuid.toString().equals("example-banned-uuid")) {
            // Example of a temporary ban
            return Optional.of(new BanInfo("Griefing", false, "2024-12-31"));
        }

        // No ban found
        return Optional.empty();
    }
}
