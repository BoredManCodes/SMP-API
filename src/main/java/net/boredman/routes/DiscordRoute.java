package net.boredman.routes;

import express.Express;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.boredman.api;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.UUID;

import static github.scarsz.discordsrv.DiscordSRV.getPlugin;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class DiscordRoute {
    public DiscordRoute(Express app) {

        // Read config
        boolean debug = api.getPlugin(api.class).getConfig().getBoolean("debug");
        String secret = api.getPlugin(api.class).getConfig().getString("secret");

        // Lookup Discord via username
        app.get("/discord/name/:username", (req, res) -> {
            final String username = req.getParams().get("username");
            final OfflinePlayer player = getOfflinePlayer(username);
            final JSONObject obj = new JSONObject();
            if (debug) {
                api.getPlugin(api.class).getLogger().info("A request was made to access " + username + "'s Discord data");
            }

            if (!hasValidSecret(req.getHeader("secret"), secret)) {
                rejectUnauthorized(req, res, obj);
                return;
            }

            if (player.getName() == null && !player.hasPlayedBefore()) {
                obj.put("error", true);
                obj.put("message", "Player not found");
                res.send(obj.toJSONString());
                return;
            }

            respondWithDiscordData(res, obj, player, username);
        });

        // Lookup Discord via UUID
        app.get("/discord/id/:id", (req, res) -> {
            final String id = req.getParams().get("id");
            final JSONObject obj = new JSONObject();
            if (debug) {
                api.getPlugin(api.class).getLogger().info("A request was made to access " + id + "'s Discord data");
            }

            if (!hasValidSecret(req.getHeader("secret"), secret)) {
                rejectUnauthorized(req, res, obj);
                return;
            }

            final UUID uuid;
            try {
                uuid = UUID.fromString(id);
            } catch (IllegalArgumentException ex) {
                obj.put("error", true);
                obj.put("message", "Invalid UUID");
                res.send(obj.toJSONString());
                return;
            }

            final OfflinePlayer player = getOfflinePlayer(uuid);
            if (player.getName() == null && !player.hasPlayedBefore()) {
                obj.put("error", true);
                obj.put("message", "Player not found");
                res.send(obj.toJSONString());
                return;
            }

            respondWithDiscordData(res, obj, player, player.getName() != null ? player.getName() : id);
        });

    }

    private void respondWithDiscordData(Object res, JSONObject obj, OfflinePlayer player, String username) {
        final String discordId = getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
        if (discordId == null) {
            obj.put("error", true);
            obj.put("message", "Player not linked to discord");
            invokeSend(res, obj.toJSONString());
            return;
        }

        User user = DiscordUtil.getJda().getUserById(discordId);
        if (user == null) {
            obj.put("error", true);
            obj.put("message", "Couldn't find Discord User by ID. Maybe they left the server?");
            invokeSend(res, obj.toJSONString());
            return;
        }

        obj.put("error", false);
        obj.put("username", username);
        obj.put("uuid", player.getUniqueId().toString());
        obj.put("discordId", discordId);
        obj.put("discordTag", user.getAsTag());
        obj.put("discordName", user.getName());
        invokeSend(res, obj.toJSONString());
    }

    private boolean hasValidSecret(List<String> secretHeader, String secret) {
        return secretHeader != null && !secretHeader.isEmpty() && secret.equals(secretHeader.get(0));
    }

    private void rejectUnauthorized(Object req, Object res, JSONObject obj) {
        obj.put("error", true);
        obj.put("message", "You are not authorised to access this resource");
        invokeSend(res, obj.toJSONString());
        api.getPlugin(api.class).getLogger().warning("A request to access Discord info was rejected as the correct secret was not passed in the header");
    }

    private void invokeSend(Object res, String body) {
        try {
            res.getClass().getMethod("send", String.class).invoke(res, body);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send response", e);
        }
    }
}
