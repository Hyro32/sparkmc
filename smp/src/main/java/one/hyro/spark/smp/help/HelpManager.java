package one.hyro.spark.smp.help;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpManager implements CommandExecutor {

    private final JavaPlugin plugin;

    public HelpManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("help")) {
            openHelpBook(player);
            return true;
        }

        return false;
    }

    private void openHelpBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        if (meta != null) {
            meta.setTitle(ChatColor.GOLD + "Server Help");
            meta.setAuthor("CoupSquad Team");

            String page1 = ChatColor.DARK_BLUE + "Welcome to CoupSquad SMP!\n\n"
                    + ChatColor.BLACK + "In an SMP (Survival Multiplayer) server, you join a shared world with other players and work together (or compete) in a survival setting. "
                    + "The server provides a rich experience where you can build, explore, and create with others.\n\n"
                    + ChatColor.GREEN + "To get started:\n"
                    + ChatColor.BLACK + "1. Use the Starter Kit Key to receive basic items to help you get started.\n"
                    + "2. Use the /rtp command to teleport to a random location and begin your adventure.\n\n"
                    + ChatColor.YELLOW + "For more commands and information, type /commands in the chat.";

            meta.addPage(page1);
            book.setItemMeta(meta);

            // Open the book for the player
            player.openBook(book);
        }
    }
}
