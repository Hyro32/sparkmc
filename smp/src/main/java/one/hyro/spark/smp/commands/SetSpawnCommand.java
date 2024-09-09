package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetSpawnCommand {

    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("setspawn")
                .requires(source -> source.getSender().hasPermission("spark.setspawn"))
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) return Command.SINGLE_SUCCESS;

                    Location currentLocation = player.getLocation();
                    SpawnCommand.setSpawnLocation(currentLocation);

                    Component setSpawnMessage = Component.translatable("context.success.setSpawn")
                            .color(NamedTextColor.GOLD);
                    player.sendMessage(setSpawnMessage);

                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
