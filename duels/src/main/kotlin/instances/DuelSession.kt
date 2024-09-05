package one.hyro.instances

import one.hyro.common.Kit
import one.hyro.minigame.Minigame
import one.hyro.minigame.Session
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DuelSession(
    minigame: Minigame,
    minPlayers: Int,
    maxPlayers: Int,
    val kit: Kit
) : Session(minigame, minPlayers, maxPlayers)