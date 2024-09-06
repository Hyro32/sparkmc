package one.hyro.spark.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "spark-proxy",
        name = "SparkProxy",
        version = "0.0.1",
        description = "A simple velocity plugin to manage the sparkmc proxy",
        url = "https://sparkmc.org",
        authors = {"Hyro32"}
)
public class SparkProxy {
    @Inject private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("SparkProxy has been initialized!");
    }
}
