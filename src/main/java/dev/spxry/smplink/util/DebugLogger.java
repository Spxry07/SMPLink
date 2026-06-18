package dev.spxry.smplink.util;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public final class DebugLogger {

    private final JavaPlugin plugin;
    private boolean enabled;

    public DebugLogger(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        enabled = plugin.getConfig().getBoolean("debug", false);
    }

    public void log(String msg) {
        if (enabled) plugin.getLogger().info("[DEBUG] " + msg);
    }

    public void log(String msg, Throwable t) {
        if (enabled) plugin.getLogger().log(Level.WARNING, "[DEBUG] " + msg, t);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
