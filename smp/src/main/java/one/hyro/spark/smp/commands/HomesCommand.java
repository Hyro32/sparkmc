package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import one.hyro.spark.smp.managers.HomeGUIManager;
import one.hyro.spark.smp.managers.HomeManager;
import org.bukkit.entity.Player;

public class HomesCommand {

    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand(HomeManager homeManager) {
        LiteralCommandNode<CommandSourceStack> homesNode =  Commands.literal("homes")
                .requires(source -> source.getSender().hasPermission("spark.homes"))
                .executes(context -> {
                    Player player = (Player) context.getSource().getSender();
                    new HomeGUIManager(homeManager).openHomeGUI(player);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        return homesNode;
    }
}
