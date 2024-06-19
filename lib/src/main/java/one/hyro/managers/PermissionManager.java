package one.hyro.managers;

import one.hyro.enums.PlayerRanks;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class PermissionManager {
    private final HashMap<UUID, PermissionAttachment> permissions;
    private final Plugin plugin;

    public PermissionManager(Plugin plugin) {
        this.plugin = plugin;
        this.permissions = new HashMap<>();
    }

    public void addPermission(UUID uuid, String permission) {
        if (plugin == null) return;
        PermissionAttachment attachment = plugin.getServer().getPlayer(uuid).addAttachment(plugin);
        attachment.setPermission(permission, true);
        permissions.put(uuid, attachment);
    }

    public void removePermission(UUID uuid, String permission) {
        if (plugin == null) return;
        PermissionAttachment attachment = permissions.get(uuid);
        if (attachment == null) return;
        attachment.unsetPermission(permission);
    }

    public void removeAllPermissions(UUID uuid) {
        if (plugin == null) return;
        PermissionAttachment attachment = permissions.get(uuid);
        if (attachment == null) return;
        attachment.remove();
        permissions.remove(uuid);
    }

    public void setRankPermissions(UUID uuid, PlayerRanks rank) {
        if (plugin == null) return;
        for (String permission : rank.getPermissions()) {
            addPermission(uuid, permission);
        }
    }

    public void clear() {
        permissions.forEach((uuid, attachment) -> attachment.remove());
        permissions.clear();
    }
}
