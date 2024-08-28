package one.hyro

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import org.slf4j.Logger

@Plugin(
    id = "proxy",
    name = "HyroProxy",
    version = "1.0.0-SNAPSHOT"
)
class Proxy @Inject constructor(val logger: Logger) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        logger.info("HyroProxy has been initialized!")
    }
}
