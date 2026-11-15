package net.spxry.smplink;

import net.spxry.smplink.api.SMPLinkAPIImpl;
import net.spxry.smplink.api.SMPLinkProvider;
import net.spxry.smplink.command.DiscordCommand;
import net.spxry.smplink.util.DebugLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPLink extends JavaPlugin {

    public static SMPLink instance;
    public DebugLogger debug;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        debug = new DebugLogger(this);

        SMPLinkProvider.register(new SMPLinkAPIImpl(this));

        var discordCmd = getCommand(getConfig().getString("command-name"));
        if (discordCmd != null) {
            discordCmd.setExecutor(new DiscordCommand(this));
        }

        debug.log("Enabled");
    }

    @Override
    public void onDisable() {
        SMPLinkProvider.unregister();
    }

    public static SMPLink get() {
        return instance;
    }
}
