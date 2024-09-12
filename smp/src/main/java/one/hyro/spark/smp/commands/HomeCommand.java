package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import one.hyro.spark.smp.managers.HomeGUIManager;
import one.hyro.spark.smp.managers.HomeManager;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand {

    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand(HomeManager homeManager) {
        LiteralCommandNode<CommandSourceStack> homeNode = Commands.literal("home")
                .requires(source -> source.getSender().hasPermission("spark.home"))
                .then(Commands.argument("name", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            Player player = (Player) context.getSource().getSender();
                            List<String> homeNames = homeManager.getHomeNames(player);
                            for (String homeName : homeNames) {
                                builder.suggest(homeName);
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            Player player = (Player) context.getSource().getSender();
                            String homeName = StringArgumentType.getString(context, "name");
                            homeManager.teleportToHome(player, homeName);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                // No argument, open GUI
                .executes(context -> {
                    Player player = (Player) context.getSource().getSender();
                    new HomeGUIManager(homeManager).openHomeGUI(player);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        return homeNode;
    }
}
