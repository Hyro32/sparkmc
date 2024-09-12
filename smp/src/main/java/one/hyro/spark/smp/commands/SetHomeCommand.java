package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import one.hyro.spark.smp.managers.HomeManager;
import org.bukkit.entity.Player;

public class SetHomeCommand {

    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand(HomeManager homeManager) {
        LiteralCommandNode<CommandSourceStack> setHomeNode =  Commands.literal("sethome")
                .requires(source -> source.getSender().hasPermission("spark.sethome"))
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(context -> {
                            Player player = (Player) context.getSource().getSender();
                            String homeName = StringArgumentType.getString(context, "name");
                            homeManager.setHome(player, homeName);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                // No argument, default to "home"
                .executes(context -> {
                    Player player = (Player) context.getSource().getSender();
                    homeManager.setHome(player, "home");
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        return setHomeNode;
    }
}
