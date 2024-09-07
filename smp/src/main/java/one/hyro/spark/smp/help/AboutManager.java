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

public class AboutManager implements CommandExecutor {

    private final JavaPlugin plugin;

    public AboutManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("about")) {
            openAboutBook(player);
            return true;
        }

        return false;
    }

    private void openAboutBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        if (meta != null) {
            meta.setTitle(ChatColor.GOLD + "About CoupSquad");
            meta.setAuthor("CoupSquad Team");

            String page1 = ChatColor.DARK_BLUE + "Welcome to the CoupSquad server!\n\n"
                    + ChatColor.BLACK + "CoupSquad was created by two passionate gamers, TSERATO and Gotthfryd.\n\n"
                    + "Our mission is to create a fair and enjoyable gaming experience, free from the pay-to-win mechanics that plague many servers. "
                    + "We started by playing on various SMP servers to gather a community of like-minded players who share our vision. \n\n"
                    + "Thanks to your support and dedication, what started as a simple idea has now grown into a vibrant and expansive community of players.\n\n"
                    + "We are grateful for every player who has joined us on this journey and helped make CoupSquad a fantastic place to play.";

            meta.addPage(page1);
            book.setItemMeta(meta);

            // Open the book for the player
            player.openBook(book);
        }
    }
}
