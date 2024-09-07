package one.hyro.spark.smp;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import one.hyro.spark.smp.anvil.AnvilListener;
import one.hyro.spark.smp.anvil.ItalicsMode;
import one.hyro.spark.smp.chat.ChatFilter;
import one.hyro.spark.smp.chat.Formatter;
import one.hyro.spark.smp.chat.ItemReplacementListener;
import one.hyro.spark.smp.deathmessage.DeathMessageManager;
import one.hyro.spark.smp.economy.Economy;
import one.hyro.spark.smp.help.AboutManager;
import one.hyro.spark.smp.help.DonateManager;
import one.hyro.spark.smp.help.HelpManager;
import one.hyro.spark.smp.home.HomeManager;
import one.hyro.spark.smp.home.HomeTabCompleter;
import one.hyro.spark.smp.kits.StarterKitManager;
import one.hyro.spark.smp.rtp.RTPManager;
import one.hyro.spark.smp.scoreboard.CustomScoreboard;
import one.hyro.spark.smp.shop.ShopGUIManager;
import one.hyro.spark.smp.spawn.SpawnManager;
import one.hyro.spark.smp.statistics.PlayerEvents;
import one.hyro.spark.smp.statistics.PlayerStatisticsManager;
import one.hyro.spark.smp.statistics.StatisticsCommand;
import one.hyro.spark.smp.team.TeamManager;
import one.hyro.spark.smp.team.TeamTabCompleter;
import one.hyro.spark.smp.tpa.TPARequestManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SparkSmp extends JavaPlugin implements Listener {

    private JDA jda;
    private TextChannel textChannel;
    private TextChannel openTeamChannel;
    private final String textChannelID = ""; // Updated channel ID
    private final String openTeamChannelID = "";
    private final CustomScoreboard customScoreboard = new CustomScoreboard(this);
    private PlayerStatisticsManager playerStatisticsManager;
    private Economy economy;
    private Formatter formatter;
    private LuckPerms luckPerms;
    private File messageIdFile;

    @Override
    public void onEnable() {
        // Initialize JDA
        try {
            String BOT_TOKEN = ""; // Use your bot token
            jda = JDABuilder.createDefault(BOT_TOKEN)
                    .addEventListeners(new ListenerAdapter() {
                        @Override
                        public void onReady(@NotNull ReadyEvent event) {
                            textChannel = event.getJDA().getTextChannelById(textChannelID);
                            if (textChannel == null) {
                                getLogger().severe("Failed to find the Discord text channel with ID: " + textChannelID);
                            }
                            openTeamChannel  = event.getJDA().getTextChannelById(openTeamChannelID);
                            if (openTeamChannel == null) {
                                getLogger().severe("Failed to find the Discord text channel with ID: " + openTeamChannelID);
                            }
                        }
                    })
                    .build();
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        // Create the "SMP" directory and necessary files if they don't exist
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File playerDataFile = new File(dataFolder, "playerdata.db");
        File teamDataFile = new File(dataFolder, "teams.db");
        File statisticsDataFile = new File(dataFolder, "statistics.db");
        this.messageIdFile = new File(this.getDataFolder(), "lastMessageId.txt");

        try {
            if (!playerDataFile.exists()) {
                playerDataFile.createNewFile();
            }
            if (!teamDataFile.exists()) {
                teamDataFile.createNewFile();
            }
            if (!statisticsDataFile.exists()) {
                statisticsDataFile.createNewFile();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        // Initialize managers
        HomeManager homeManager = new HomeManager(playerDataFile);
        TeamManager teamManager = new TeamManager(this);
        DeathMessageManager deathMessageManager = new DeathMessageManager(this);
        playerStatisticsManager = new PlayerStatisticsManager(this);
        PlayerStatisticsManager statisticsManager = new PlayerStatisticsManager(this);
        new ItemReplacementListener(this);
        new ChatFilter(this);
        new StarterKitManager(this, economy);
        formatter = new Formatter(this);
        ShopGUIManager shopGUIManager = new ShopGUIManager(this);
        new DonateManager(this);
        new AboutManager(this);
        new HelpManager(this);

        luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        // Ensure databases are initialized
        homeManager.initializeDatabase(); // Initialize the player database
        teamManager.initializeDatabase(); // Initialize the team database

        // Register commands and tab completers
        Objects.requireNonNull(getCommand("home")).setExecutor(homeManager);
        Objects.requireNonNull(getCommand("home")).setTabCompleter(new HomeTabCompleter(homeManager));
        Objects.requireNonNull(getCommand("homes")).setExecutor(homeManager);
        Objects.requireNonNull(getCommand("homes")).setTabCompleter(new HomeTabCompleter(homeManager));
        Objects.requireNonNull(getCommand("sethome")).setExecutor(homeManager);
        Objects.requireNonNull(getCommand("sethome")).setTabCompleter(new HomeTabCompleter(homeManager));
        Objects.requireNonNull(getCommand("delhome")).setExecutor(homeManager);
        Objects.requireNonNull(getCommand("delhome")).setTabCompleter(new HomeTabCompleter(homeManager));
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnManager());
        Objects.requireNonNull(getCommand("team")).setExecutor(teamManager);
        Objects.requireNonNull(getCommand("team")).setTabCompleter(new TeamTabCompleter(teamDataFile));
        Objects.requireNonNull(getCommand("statistics")).setExecutor(new StatisticsCommand(statisticsManager));
        Objects.requireNonNull(getCommand("rtp")).setExecutor(new RTPManager(this));
        Objects.requireNonNull(getCommand("shop")).setExecutor(new ShopGUIManager(this));
        Objects.requireNonNull(getCommand("tpa")).setExecutor(new TPARequestManager(this));
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPARequestManager(this));
        Objects.requireNonNull(getCommand("tpadeny")).setExecutor(new TPARequestManager(this));
        Objects.requireNonNull(getCommand("donate")).setExecutor(new DonateManager(this));
        Objects.requireNonNull(getCommand("about")).setExecutor(new AboutManager(this));
        Objects.requireNonNull(getCommand("help")).setExecutor(new HelpManager(this));


        // Register events
        Bukkit.getPluginManager().registerEvents(deathMessageManager, this);
        Bukkit.getPluginManager().registerEvents(shopGUIManager, this);
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(playerStatisticsManager), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
        getServer().getPluginManager().registerEvents(this, this); // Register this class as a listener

        // Load and setup scoreboard for all players on server startup
        for (Player player : Bukkit.getOnlinePlayers()) {
            customScoreboard.createScoreboard(player);
        }

        saveDefaultConfig();
    }

    public ItalicsMode getItalicsMode() {
        switch (getConfig().getString("italics", "force").toLowerCase()) {
            case "force":
                return ItalicsMode.NONE;
            case "remove":
                return ItalicsMode.NONE;
            default:
                return ItalicsMode.NONE;
        }
    }

    @Override
    public void onDisable() {
        if (jda != null) {
            jda.shutdown();
        }
        playerStatisticsManager.close();
    }

    public TextChannel getOpenTeamChannel() {
        return openTeamChannel;
    }

    public void sendDeathMessageToDiscord(String deathMessage) {
        if (textChannel != null) {
            textChannel.sendMessage("**" + deathMessage + "**").queue();
        }
    }

    public PlayerStatisticsManager getPlayerStatisticsManager() {
        return playerStatisticsManager;
    }

    public static boolean isPlaceholderAPIAvailable() {
        boolean placeholderAPIAvailable = false;
        return placeholderAPIAvailable;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Create scoreboard for the player when they join
        event.setJoinMessage(null);
        customScoreboard.createScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onAchievement(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        formatter.handleChatEvent(event);

        Player player = event.getPlayer();
        String message = event.getMessage();

        // Remove Minecraft color codes and formatting codes from the message
        String cleanedMessage = message.replaceAll("§[0-9A-FK-ORa-fk-or]", "");

        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        // If the user is not found, return
        if (user == null) {
            return;
        }

        String group = user.getPrimaryGroup().toUpperCase();

        // Combine the prefix and player's name for the Discord message
        String discordMessage = "**" + group + "** " + player.getName() + " >> " + cleanedMessage;

        if (textChannel != null) {
            textChannel.sendMessage(discordMessage).queue();
        }
    }

    public void notifyDiscordChannel(File dbFile) {
        Bukkit.getScheduler().runTask(this, () -> {
            try {
                // Fetch the open teams
                List<String> openTeams = new ArrayList<>();
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
                    PreparedStatement stmt = connection.prepareStatement("SELECT team_name FROM teams WHERE is_open = TRUE");
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        openTeams.add(rs.getString("team_name"));
                    }
                }

                // Build the embed message
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Open Teams");
                eb.setColor(Color.BLUE);

                // Append open teams to the description
                StringBuilder descriptionBuilder = new StringBuilder();
                for (String team : openTeams) {
                    descriptionBuilder.append("- ").append(team).append("\n");
                }
                eb.setDescription(descriptionBuilder.toString());

                if (openTeamChannel != null) {
                    // Implement getLastMessageId and setLastMessageId as needed
                    String lastMessageId = getLastMessageId();
                    if (lastMessageId != null) {
                        openTeamChannel.retrieveMessageById(lastMessageId).queue(
                                message -> message.delete().queue(),
                                throwable -> {} // Handle any potential errors
                        );
                    }

                    openTeamChannel.sendMessageEmbeds(eb.build()).queue(message -> setLastMessageId(message.getId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String getLastMessageId() {
        if (messageIdFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(messageIdFile))) {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void setLastMessageId(String messageId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(messageIdFile))) {
            writer.write(messageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the player right-clicked with a fireball in hand
        if (player.getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE) {
            // Shoot a fireball in the direction the player is looking
            Fireball fireball = player.launchProjectile(Fireball.class);
            Vector direction = player.getEyeLocation().getDirection().multiply(2);
            fireball.setVelocity(direction);

            // Increase the explosion power of the fireball
            fireball.setYield(10.0f);  // Default is 1.0f, increase this for a bigger explosion
            fireball.setIsIncendiary(true);  // Set to true if you want the explosion to cause fire

            // Keep the fireball item in the player's inventory (do nothing to decrease it)
        }
    }
}
