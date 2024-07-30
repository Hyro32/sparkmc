package one.hyro.common

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

enum class Rank(private val prefix: Component?, private val weight: UByte, val permissions: List<String>) {
    USER(null, 0u, listOf()),
    HELPER(
        Component.text("[", NamedTextColor.DARK_GRAY)
            .append(Component.text("HELPER", NamedTextColor.DARK_GREEN)
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
        1u,
        listOf()
    ),
    MODERATOR(
        Component.text("[", NamedTextColor.DARK_GRAY)
            .append(Component.text("MOD", NamedTextColor.BLUE)
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
        2u,
        listOf()
    ),
    ADMINISTRATOR(
        Component.text("[", NamedTextColor.DARK_GRAY)
            .append(Component.text("ADMIN", NamedTextColor.RED)
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
        3u,
        listOf()
    ),
    OWNER(
        Component.text("[", NamedTextColor.DARK_GRAY)
            .append(Component.text("OWNER", NamedTextColor.GOLD)
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
            ),
        4u,
        listOf()
    );

    fun prefix(): Component = prefix?.appendSpace() ?: Component.empty()
    //fun weightname(): String = weight.toString() + name.lowercase()
}