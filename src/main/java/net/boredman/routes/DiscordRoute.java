package net.boredman.routes;

import express.Express;
import express.utils.Status;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.boredman.API;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;

import java.util.UUID;

import static github.scarsz.discordsrv.DiscordSRV.getPlugin;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class DiscordRoute {
    public DiscordRoute(Express app) {

        // Read config
        boolean debug = API.getPlugin(API.class).getConfig().getBoolean("debug");
        String secret = API.getPlugin(API.class).getConfig().getString("secret");

        // Lookup Discord via username
        app.get("/discord/name/:username", (req, res) -> {
            final String username = req.getParams().get("username");
            final OfflinePlayer player = getOfflinePlayer(username);
            final String discordId = getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
            final JSONObject obj = new JSONObject();
            if (debug) {
                API.getPlugin(API.class).getLogger().info("A request was made to access " +
                        req.getParams().get("username") + "'s Discord data");
            }
            if (secret!= null &&!secret.equals(req.getHeader("secret").get(0))) {
                obj.put("error", true);
                obj.put("message", "You are not authorised to access this resource");
                res.send(obj.toJSONString());
                API.getPlugin(API.class).getLogger().warning("A request to access Discord info from " + req.getIp() +
                        " was rejected as they did not pass the correct secret in the header");
            } else {
                if (discordId == null) {
                    obj.put("error", true);
                    obj.put("message", "Player not linked to discord");
                    res.send(obj.toJSONString());
                } else {
                    User user = DiscordUtil.getJda().getUserById(discordId);
                    if (user == null) {
                        obj.put("error", true);
                        obj.put("message", "Couldn't find Discord User by ID. Maybe they left the server?");
                        res.send(obj.toJSONString());
                    } else {
                        obj.put("error", false);
                        obj.put("username", username);
                        obj.put("uuid", player.getUniqueId().toString());
                        obj.put("discordId", discordId);
                        obj.put("discordTag", user.getAsTag());
                        obj.put("discordName", user.getName());
                        res.send(obj.toJSONString());
                    }
                }
            }
        });

        // Lookup Discord via ID
        app.get("/discord/id/:id", (req, res) -> {
            final String username = req.getParams().get("id");
            final OfflinePlayer player = getOfflinePlayer(username);
            final String discordId = getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
            final JSONObject obj = new JSONObject();
            if (debug) {
                API.getPlugin(API.class).getLogger().info("A request was made to access " +
                        req.getParams().get("id") + "'s Discord data");
            }
            if (secret!= null &&!secret.equals(req.getHeader("secret").get(0))) {
                obj.put("error", true);
                obj.put("message", "You are not authorised to access this resource");
                res.send(obj.toJSONString());
                API.getPlugin(API.class).getLogger().warning("A request to access Discord info from " + req.getIp() +
                        " was rejected as they did not pass the correct secret in the header");
            } else {
                if (discordId == null) {
                    obj.put("error", true);
                    obj.put("message", "Player not linked to discord");
                    res.send(obj.toJSONString());
                } else {
                    User user = DiscordUtil.getJda().getUserById(discordId);
                    if (user == null) {
                        obj.put("error", true);
                        obj.put("message", "Couldn't find Discord User by ID. Maybe they left the server?");
                        res.send(obj.toJSONString());
                    } else {
                        obj.put("error", false);
                        obj.put("username", username);
                        obj.put("uuid", player.getUniqueId().toString());
                        obj.put("discordId", discordId);
                        obj.put("discordTag", user.getAsTag());
                        obj.put("discordName", user.getName());
                        res.send(obj.toJSONString());
                    }
                }
            }
        });

    }
}
