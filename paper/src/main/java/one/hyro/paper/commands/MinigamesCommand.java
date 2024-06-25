package one.hyro.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.builders.GameMenu;
import one.hyro.builders.CustomItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MinigamesCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (!(stack.getExecutor() instanceof Player player)) return;

        CustomItem survival = new CustomItem(Material.NOTE_BLOCK)
                .setCustomId("survival")
                .setDisplayName(Component.text("Survival", NamedTextColor.GREEN))
                .onClick(clicker -> {
                    clicker.closeInventory();
                    clicker.sendMessage(Component.text("Coming soon!"));
                })
                .build();

        GameMenu minigames = new GameMenu()
                .setCustomId("minigames")
                .setTitle(Component.text("Minigames"))
                .setSize(27)
                .setHolder(player)
                .setItem(13, survival)
                .build();

        player.openInventory(minigames.getInventory());
    }
}
