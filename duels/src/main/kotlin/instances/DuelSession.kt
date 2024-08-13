package one.hyro.instances

import one.hyro.kits.Kit
import one.hyro.minigame.Minigame
import one.hyro.minigame.Session

class DuelSession(
    minigame: Minigame,
    min: Int,
    max: Int,
    val kit: Kit
) : Session(minigame, min, max) {
    fun getKit(): Kit = kit
}