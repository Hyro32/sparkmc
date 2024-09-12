package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import one.hyro.spark.smp.managers.HomeManager;
import org.bukkit.entity.Player;

import java.util.List;

public class DelHomeCommand {

    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand(HomeManager homeManager) {
        LiteralCommandNode<CommandSourceStack> delHomeNode =  Commands.literal("delhome")
                .requires(source -> source.getSender().hasPermission("spark.delhome"))
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
                            homeManager.deleteHome(player, homeName);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
        return delHomeNode;
    }
}
