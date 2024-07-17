package one.hyro.managers

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.common.Role
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

class ScoreboardManager {
    companion object {
        fun setMainTab(player: Player) {
            player.sendPlayerListHeaderAndFooter(
                Component.text("ʜʏʀᴏ᾽ѕ ᴘʟᴀɴᴇᴛ", NamedTextColor.GOLD),
                Component.text("ᴘʟᴀɴᴇᴛ.ʜʏʀᴏ.ᴏɴᴇ", NamedTextColor.GRAY)
            )
        }

        fun registerRoleTeams() {
            for (role: Role in Role.entries) {
                val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
                val team: Team = scoreboard.getTeam(role.name)
                    ?: scoreboard.registerNewTeam(role.name)
                team.prefix(role.prefix())
            }
        }

        fun unregisterRoleTeams() {
            for (role: Role in Role.entries) {
                val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
                val team: Team = scoreboard.getTeam(role.name)?: continue
                team.unregister()
            }
        }

        fun setRole(player: Player) {
            // Get role from database
            val scoreboard: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard
            val team: Team = scoreboard.getTeam(Role.OWNER.name)?: return
            team.addEntry(player.name)
        }

        fun updateScoreboard(plugin: Plugin) {
            val scheduler: BukkitScheduler = Bukkit.getScheduler()
            scheduler.scheduleSyncRepeatingTask(plugin, {
                for (player: Player in Bukkit.getOnlinePlayers()) {
                    setMainTab(player)
                }
            }, 0L, 20L) // Each 20 ticks or 1 second
        }
    }
}