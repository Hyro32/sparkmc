package one.hyro.paper.utilities;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class CustomHeads {
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");

    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        URL urlObject;

        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }

        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    public static ItemStack getCustomHead(String url) {
        PlayerProfile profile = getProfile("https://textures.minecraft.net/texture/" + url);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getPlayerHead(Player player) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta playerHeadMeta = playerHead.getItemMeta();
        SkullMeta skullMeta = (SkullMeta) playerHeadMeta;
        playerHeadMeta.displayName(Component.text("Your profile", NamedTextColor.GREEN));
        skullMeta.setOwningPlayer(player);
        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }
}
