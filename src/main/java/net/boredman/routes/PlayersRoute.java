package net.boredman.routes;

import express.Express;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.UUID;

import static github.scarsz.discordsrv.DiscordSRV.getPlugin;
import static org.bukkit.Bukkit.*;

public class PlayersRoute {
    public PlayersRoute(Express app) {

        // Online Player Stats //

        app.get("/players/:username", (req, res) -> {
            String username = req.getParams().get("username");
            JSONObject obj = new JSONObject();
            Player player = getPlayer(username);
            String bed;
            String[] arrOfBed;
            String address;
            String[] arrOfAddress;
            try {
                obj.put((Object)"username", (Object)player.getName());
                obj.put((Object)"uuid", (Object)player.getUniqueId().toString());
                obj.put((Object)"health", (Object)String.valueOf(player.getHealth()));
                obj.put((Object)"food", (Object)String.valueOf(player.getFoodLevel()));
                obj.put((Object)"world", (Object)player.getWorld().getName());
                obj.put((Object)"experience", (Object)String.valueOf(player.getExp()));
                obj.put((Object)"level", (Object)String.valueOf(player.getLevel()));
                obj.put((Object)"deaths", (Object)String.valueOf(player.getStatistic(Statistic.DEATHS)));
                obj.put((Object)"kills", (Object)String.valueOf(player.getStatistic(Statistic.MOB_KILLS)));
                obj.put((Object)"jumps", (Object)String.valueOf(player.getStatistic(Statistic.JUMP)));
                obj.put((Object)"gamemode", (Object)player.getGameMode().toString());
                if (player.getBedSpawnLocation() != null) {
                    bed = player.getBedSpawnLocation().toString();
                    arrOfBed = bed.split(",");
                    obj.put((Object)"bed", (Object)(arrOfBed[1] + "," + arrOfBed[2] + "," + arrOfBed[3]));
                }
                obj.put((Object)"time", (Object)String.valueOf(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20));
                obj.put((Object)"death", (Object)String.valueOf(player.getStatistic(Statistic.TIME_SINCE_DEATH) / 20));
                address = String.valueOf(player.getAddress()).replace("/", "");
                arrOfAddress = address.split(":");
                obj.put((Object)"address", (Object)arrOfAddress[0]);
                obj.put((Object)"lastJoined", (Object)player.getLastPlayed());

                res.send(obj.toJSONString());
            } catch (Exception e) {
                if (e.getMessage().contains("Cannot invoke \"org.bukkit.entity.Player.getName()\" because \"player\" is null")) {
                    obj.put("error", true);
                    obj.put("message", "Player not online, or not found");
                    res.send(obj.toJSONString());

                } else {
                    getLogger().info("Error: " + e.getMessage());
                    res.send("Error: " + e.getMessage());
                }
            }
        });
    }
}
