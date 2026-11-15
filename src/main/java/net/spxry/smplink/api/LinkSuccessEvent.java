package net.spxry.smplink.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.UUID;

public final class LinkSuccessEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final UUID minecraftUuid;
    private final String discordId;

    public LinkSuccessEvent(Player player, UUID minecraftUuid, String discordId) {
        this.player = player;
        this.minecraftUuid = minecraftUuid;
        this.discordId = discordId;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getMinecraftUuid() {
        return minecraftUuid;
    }

    public String getDiscordId() {
        return discordId;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
