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

public class DonateManager implements CommandExecutor {

    private final JavaPlugin plugin;

    public DonateManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("donate")) {
            openDonateBook(player);
            return true;
        }

        return false;
    }

    private void openDonateBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        if (meta != null) {
            meta.setTitle(ChatColor.GOLD + "Support Our Server");
            meta.setAuthor("Server Team");

            String page1 = ChatColor.DARK_BLUE + "Thank you for considering a donation to support our server!\n\n"
                    + ChatColor.BLACK + "To donate, please visit the following link:\n\n"
                    + ChatColor.BLUE + ChatColor.UNDERLINE + "https://example.com/donate" + ChatColor.RESET + "\n\n"
                    + ChatColor.BLACK + "Your support helps us keep the server running and improve it!";

            meta.addPage(page1);
            book.setItemMeta(meta);

            // Open the book for the player
            player.openBook(book);
        }
    }
}
