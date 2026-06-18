package dev.spxry.smplink.api;

import dev.spxry.smplink.SMPLink;
import org.bukkit.entity.Player;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class SMPLinkAPIImpl implements SMPLinkAPI {

    private final SMPLink plugin;
    private final HttpClient httpClient;

    public SMPLinkAPIImpl(SMPLink plugin) {
        this.plugin = plugin;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(plugin.getConfig().getInt("http-timeout-seconds")))
                .build();
    }

    @Override
    public CompletableFuture<LinkResult> link(Player player, String code) {
        var uuid = player.getUniqueId().toString().replace("-", "");
        var username = player.getName();
        var botUrl = plugin.getConfig().getString("bot-url");
        var secret = plugin.getConfig().getString("link-secret");

        var normalizedSecret = secret.toLowerCase().replace("_", "-").replace(" ", "-");

        var body = """
                {"secret":"%s","code":"%s","uuid":"%s","username":"%s"}
                """.formatted(normalizedSecret, code.toUpperCase(), uuid, username).strip();

        var request = HttpRequest.newBuilder()
                .uri(URI.create(botUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(plugin.getConfig().getInt("http-timeout-seconds")))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> switch (response.statusCode()) {
                    case 200 -> {
                        var discordId = extractDiscordId(response.body());
                        var event = new LinkSuccessEvent(player, player.getUniqueId(), discordId);
                        plugin.getServer().getPluginManager().callEvent(event);
                        yield LinkResult.success(discordId);
                    }
                    case 404 -> LinkResult.failure(SMPLinkAPI.FailureReason.INVALID_CODE);
                    case 410 -> LinkResult.failure(SMPLinkAPI.FailureReason.EXPIRED);
                    default -> LinkResult.failure(SMPLinkAPI.FailureReason.SERVER_ERROR);
                })
                .exceptionally(ex -> {
                    plugin.debug.log("HTTP error during link", ex);
                    return LinkResult.failure(SMPLinkAPI.FailureReason.SERVER_ERROR);
                });
    }

    private String extractDiscordId(String json) {
        var marker = "\"discord_id\":\"";
        var start = json.indexOf(marker);
        if (start == -1) return "";
        start += marker.length();
        var end = json.indexOf('"', start);
        return end == -1 ? "" : json.substring(start, end);
    }
}
