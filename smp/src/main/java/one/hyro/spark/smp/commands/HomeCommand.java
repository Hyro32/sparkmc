package one.hyro.spark.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class HomeCommand {
    public static LiteralCommandNode<CommandSourceStack> createBrigadierCommand() {
        return Commands.literal("home")
                .requires(source -> source.getSender().hasPermission("spark.home"))
                .executes(context -> {
                    context.getSource().getSender().sendMessage("Home command executed");
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("option", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            builder.suggest("add");
                            builder.suggest("remove");
                            builder.suggest("list");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String option = StringArgumentType.getString(context, "option");

                            if (option.equalsIgnoreCase("list")) {
                                context.getSource().getSender().sendMessage("Home list command executed");
                                return Command.SINGLE_SUCCESS;
                            }

                            context.getSource().getSender().sendMessage("Need to provide name");
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("home", StringArgumentType.word())
                                .executes(context -> {
                                    String option = StringArgumentType.getString(context, "option");
                                    String home = StringArgumentType.getString(context, "home");

                                    switch (option.toLowerCase()) {
                                        case "add" -> {
                                            // Check if home name is avaible and max home rank.
                                            context.getSource().getSender().sendMessage("Add home command executed");
                                        }
                                        case "remove" -> {
                                            // Check if user has a home with that name and delete.
                                            context.getSource().getSender().sendMessage("Remove home command executed");
                                        }
                                        default -> context.getSource().getSender().sendMessage("Invalid option");
                                    }

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .build();
    }
}
