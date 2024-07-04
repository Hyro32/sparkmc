package one.hyro.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import one.hyro.proxy.commands.BanCommand;
import one.hyro.proxy.commands.KickCommand;
import one.hyro.proxy.commands.MessageCommand;
import one.hyro.proxy.commands.TempBanCommand;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

@Plugin(
        id = "proxy",
        name = "HyrosProxy",
        version = "1.0-SNAPSHOT",
        authors = {"Hyro"}
)
public class HyrosProxy {
    private final ProxyServer proxy;
    private final Logger logger;

    @Inject
    public HyrosProxy(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = proxy.getCommandManager();

        CommandMeta banCommandMeta = commandManager.metaBuilder("ban")
                .plugin(this)
                .build();

        CommandMeta kickCommandMeta = commandManager.metaBuilder("kick")
                .plugin(this)
                .build();

        CommandMeta messageCommandMeta = commandManager.metaBuilder("message")
                .plugin(this)
                .aliases("msg", "private")
                .build();

        CommandMeta tempBanCommandMeta = commandManager.metaBuilder("tempban")
                .plugin(this)
                .build();

        BrigadierCommand banCommand = BanCommand.createBanCommand(proxy);
        BrigadierCommand kickCommand = KickCommand.createKickCommand(proxy);
        BrigadierCommand messageCommand = MessageCommand.createMessageCommand(proxy);
        BrigadierCommand tempBanCommand = TempBanCommand.createTempBanCommand(proxy);

        commandManager.register(banCommandMeta, banCommand);
        commandManager.register(kickCommandMeta, kickCommand);
        commandManager.register(messageCommandMeta, messageCommand);
        commandManager.register(tempBanCommandMeta, tempBanCommand);

        TranslationRegistry registry = TranslationRegistry.create(Key.key("velocity:i18n"));
        ResourceBundle bundleEN = ResourceBundle.getBundle("translations.en", Locale.ENGLISH, UTF8ResourceBundleControl.get());
        ResourceBundle bundleES = ResourceBundle.getBundle("translations.es", Locale.of("es"), UTF8ResourceBundleControl.get());
        registry.registerAll(Locale.ENGLISH, bundleEN, true);
        registry.registerAll(Locale.forLanguageTag("es"), bundleES, true);
        GlobalTranslator.translator().addSource(registry);

        logger.info("HyrosProxy has been initialized!");
    }
}
