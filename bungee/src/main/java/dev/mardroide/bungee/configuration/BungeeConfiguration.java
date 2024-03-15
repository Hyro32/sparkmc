package dev.mardroide.bungee.configuration;

import dev.mardroide.bungee.MegatronBungee;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BungeeConfiguration {
    @Getter
    private static Configuration configuration;

    public static void loadConfiguration() {
        if (!MegatronBungee.getInstance().getDataFolder().exists()) {
            MegatronBungee.getInstance().getDataFolder().mkdir();
        }

        File file = new File(MegatronBungee.getInstance().getDataFolder(), "config.yml");

        try {
            if (!file.exists()) {
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream in = MegatronBungee.getInstance().getResourceAsStream("config.yml");
                in.transferTo(outputStream);
            }

            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            ProxyServer.getInstance().getConsole().sendMessage( ChatColor.RED + "Error loading configuration file.");
        }
    }
}
