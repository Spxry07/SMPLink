package dev.spxry.smplink.api;

import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface SMPLinkAPI {

    CompletableFuture<LinkResult> link(Player player, String code);

    record LinkResult(boolean ok, String discordId, FailureReason reason) {

        public static LinkResult success(String discordId) {
            return new LinkResult(true, discordId, null);
        }

        public static LinkResult failure(FailureReason reason) {
            return new LinkResult(false, null, reason);
        }
    }

    enum FailureReason {
        INVALID_CODE,
        EXPIRED,
        SERVER_ERROR
    }
}
