package one.hyro.paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.hyro.paper.HyrosPaper;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        try {
            HyrosPaper.getInstance().reloadConfig();
            TextComponent message = Component.text("Config reloaded successfully!").color(NamedTextColor.GREEN);
            stack.getExecutor().sendMessage(message);
        } catch (Exception e) {
            TextComponent message = Component.text("Error while reloading config!").color(NamedTextColor.RED);
            stack.getExecutor().sendMessage(message);
            e.printStackTrace();
        }
    }
}
