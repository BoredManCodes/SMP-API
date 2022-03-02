package net.boredman.routes;

import express.Express;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;

import java.util.UUID;

import static github.scarsz.discordsrv.DiscordSRV.getPlugin;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class DiscordRoute {
    public DiscordRoute(Express app) {

        // Linked Discord //

        app.get("/discord/name/:username", (req, res) -> {
            final String username = req.getParams().get("username");
            final OfflinePlayer player = getOfflinePlayer(username);
            final String discordId = getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
            final JSONObject obj = new JSONObject();
            if (discordId == null) {
                obj.put("error", true);
                obj.put("message", "Player not linked to discord");
                res.send(obj.toJSONString());
                return;
            } else {
                User user = DiscordUtil.getJda().getUserById(discordId);
                if (user == null) {
                    obj.put("error", true);
                    obj.put("message", "Couldn't find Discord User by ID. Maybe they left the server?");
                    res.send(obj.toJSONString());
                    return;
                } else {
                    obj.put("error", false);
                    obj.put("username", username);
                    obj.put("uuid", player.getUniqueId().toString());
                    obj.put("discordId", discordId);
                    obj.put("discordTag", user.getAsTag());
                    obj.put("discordName", user.getName());
                    res.send(obj.toJSONString());
                    return;
                }
            }
        });

        // Linked Minecraft Account //

        app.get("/discord/id/:discordId", (req, res) -> {
            final String discordId = req.getParams().get("discordId");
            final User user = DiscordUtil.getJda().getUserById(discordId);
            final JSONObject obj = new JSONObject();
            if (user == null) {
                obj.put("error", true);
                obj.put("message", "Couldn't find Discord User by ID. Maybe they left the server?");
                res.send(obj.toJSONString());
            } else {
                final UUID uuid = UUID.fromString(getPlugin().getAccountLinkManager().getUuid(user.getId()).toString());
                final OfflinePlayer player = getOfflinePlayer(uuid);
                if (player == null) {
                    obj.put("error", true);
                    obj.put("message", "Couldn't find player by UUID. Maybe they left the server?");
                    res.send(obj.toJSONString());
                } else {
                    obj.put("error", false);
                    obj.put("uuid", uuid.toString());
                    obj.put("username", player.getName());
                    obj.put("discordId", discordId);
                    obj.put("discordTag", user.getAsTag());
                    obj.put("discordName", user.getName());
                    res.send(obj.toJSONString());
                }
            }
        });
    }
}
