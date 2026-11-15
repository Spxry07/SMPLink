package net.spxry.smplink.command;

import net.spxry.smplink.SMPLink;
import net.spxry.smplink.api.SMPLinkAPI;
import net.spxry.smplink.api.SMPLinkProvider;
import net.spxry.smplink.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class DiscordCommand implements CommandExecutor {

    private final SMPLink plugin;

    public DiscordCommand(SMPLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtil.colorize(plugin.getConfig().getString("messages.player-only")));
            return true;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase(plugin.getConfig().getString("subcommand-link"))) {
            player.sendMessage(ColorUtil.colorize(plugin.getConfig().getString("messages.usage")));
            return true;
        }

        var code = args[1];

        plugin.getServer().getAsyncScheduler().runNow(plugin, task ->
                SMPLinkProvider.get().link(player, code).whenComplete((result, error) -> {
                    String message;
                    if (error != null || result == null) {
                        message = plugin.getConfig().getString("messages.error");
                    } else if (result.ok()) {
                        message = plugin.getConfig().getString("messages.success");
                    } else {
                        message = switch (result.reason()) {
                            case INVALID_CODE -> plugin.getConfig().getString("messages.invalid-code");
                            case EXPIRED -> plugin.getConfig().getString("messages.expired");
                            case SERVER_ERROR -> plugin.getConfig().getString("messages.error");
                        };
                    }
                    var finalMessage = ColorUtil.colorize(message);
                    player.getScheduler().run(plugin, scheduledTask -> player.sendMessage(finalMessage), null);
                })
        );

        return true;
    }
}
