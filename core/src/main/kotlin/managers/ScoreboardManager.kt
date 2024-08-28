package one.hyro.managers

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import one.hyro.HyroCore
import one.hyro.common.Rank
import one.hyro.scheduler.Schedule
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scoreboard.*

object ScoreboardManager {
    private val configuration: FileConfiguration = HyroCore.instance?.config!!
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val lobbyWorlds: List<String> = configuration.getStringList("lobby.worlds")

    private val sidebar: Objective = scoreboard.registerNewObjective(
        "sidebar",
        Criteria.DUMMY,
        Component.text("ʜʏʀᴏ᾽ѕ ᴘʟᴀɴᴇᴛ", NamedTextColor.DARK_GREEN)
    )

    private val health: Objective = scoreboard.registerNewObjective(
        "health",
        Criteria.HEALTH,
        Component.text(configuration.getString("health.symbol")?: "❤", NamedTextColor.RED)
    )

    @OptIn(ExperimentalStdlibApi::class)
    fun registerRoleTeams() {
        for (rank: Rank in Rank.entries) {
            val team: Team = scoreboard.getTeam(rank.name)
                ?: scoreboard.registerNewTeam(rank.name)
            team.prefix(rank.prefix())
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun unregisterRoleTeams() {
        for (rank: Rank in Rank.entries) {
            val team: Team = scoreboard.getTeam(rank.name)?: continue
            team.unregister()
        }
    }

    fun setRole(player: Player) {
        // Get role from database
        val team: Team = scoreboard.getTeam(Rank.OWNER.name)?: return
        team.addEntry(player.name)
    }

    private fun setCustomScoreboard(configuration: FileConfiguration, player: Player) {
        if (!configuration.getBoolean("scoreboard.enabled")) return
        sidebar.displaySlot = DisplaySlot.SIDEBAR

        val lines: List<Component> = listOf(
            Component.text("1.0.0-SNAPSHOT", NamedTextColor.GRAY),
            Component.empty(),
            Component.text(player.name + ":", NamedTextColor.GRAY),
            Component.text("★", NamedTextColor.RED)
                .appendSpace()
                .append(Component.text("Rank:", NamedTextColor.WHITE))
                .appendSpace()
                .append(Rank.OWNER.prefix()),
            Component.text("⛃", NamedTextColor.LIGHT_PURPLE)
                .appendSpace()
                .append(Component.text("Coins:", NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text("0", NamedTextColor.LIGHT_PURPLE)),
            Component.text("✔", NamedTextColor.AQUA)
                .appendSpace()
                .append(Component.text("Online:", NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text(Bukkit.getOnlinePlayers().size.toString(), NamedTextColor.AQUA)),
            Component.empty(),
            Component.text("planet.hyro.one", NamedTextColor.GRAY)
        )

        for ((index, element) in lines.asReversed().withIndex()) {
            val score: Score = sidebar.getScore(index.toString())
            score.score = index
            score.customName(element)
        }

        player.scoreboard = scoreboard
    }

    private fun setMainTab(player: Player) {
        player.sendPlayerListHeaderAndFooter(
            Component.text("ʜʏʀᴏ᾽ѕ ᴘʟᴀɴᴇᴛ", NamedTextColor.DARK_GREEN),
            Component.text("ᴘʟᴀɴᴇᴛ.ʜʏʀᴏ.ᴏɴᴇ", NamedTextColor.GRAY)
        )
    }

    private fun setHealthBelowName(configuration: FileConfiguration, player: Player) {
        if (player.world.name in lobbyWorlds || !configuration.getBoolean("health.enabled")) return
        health.displaySlot = DisplaySlot.BELOW_NAME
        player.scoreboard = scoreboard
    }

    fun updateScoreboard(plugin: Plugin) {
        Schedule.runTaskTimer {
            for (player: Player in Bukkit.getOnlinePlayers()) {
                setMainTab(player)
                setCustomScoreboard(plugin.config, player)
                setHealthBelowName(plugin.config, player)
            }
        }
    }
}