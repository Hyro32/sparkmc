package one.hyro.paper.managers;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BossbarsManager {
    private @Nullable BossBar activeBossbar;

    public void showMainBossbar(final @NonNull Audience target) {
        final Component name = Component.text("You are playing on planet.hyro.one", NamedTextColor.YELLOW);
        final BossBar bar = BossBar.bossBar(name, 1.0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);

        target.showBossBar(bar);
        this.activeBossbar = bar;
    }

    public void hideActiveBossbar(final @NonNull Audience target) {
        target.hideBossBar(this.activeBossbar);
        this.activeBossbar = null;
    }
}
