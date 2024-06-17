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
import one.hyro.proxy.commands.KickCommand;
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

    @Inject
    private Logger logger;

    @Inject
    public HyrosProxy(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = proxy.getCommandManager();

        CommandMeta kickCommandMeta = commandManager.metaBuilder("kick")
                .plugin(this)
                .build();

        BrigadierCommand kickCommand = KickCommand.createKickCommand(proxy);

        commandManager.register(kickCommandMeta, kickCommand);

        TranslationRegistry registry = TranslationRegistry.create(Key.key("velocity:i18n"));
        ResourceBundle bundleEN = ResourceBundle.getBundle("translations.en", Locale.ENGLISH, UTF8ResourceBundleControl.get());
        ResourceBundle bundleES = ResourceBundle.getBundle("translations.es", Locale.of("es"), UTF8ResourceBundleControl.get());
        registry.registerAll(Locale.ENGLISH, bundleEN, true);
        registry.registerAll(Locale.forLanguageTag("es"), bundleES, true);
        GlobalTranslator.translator().addSource(registry);

        logger.info("HyrosProxy has been initialized!");
    }
}
