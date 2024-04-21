package quest.safecloud.routes;

import express.Express;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import quest.safecloud.API;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

import static github.scarsz.discordsrv.DiscordSRV.getPlugin;
import static org.bukkit.Bukkit.*;

public class PlayersRoute {
    public PlayersRoute(Express app) {

        // Online Player Stats //
        boolean debug = API.getPlugin(API.class).getConfig().getBoolean("debug");
        String secret = API.getPlugin(API.class).getConfig().getString("secret");
        app.get("/players/:username", (req, res) -> {
            if (debug) {
                API.getPlugin(API.class).getLogger().info("A request was made to access " +
                        req.getParams().get("username") + "'s player data");
            }
            if (secret.equals(req.getHeader("secret").get(0))) {
                File dir = new File(API.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
                File plugins = new File(dir.getParentFile().getPath());
                String playerDataFolder = plugins + "\\SMP-API\\playerdata\\";
                if (!Files.exists(Path.of(playerDataFolder))) {
                    try {
                        Files.createDirectory(Path.of(playerDataFolder));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String username = req.getParams().get("username");
                JSONObject obj = new JSONObject();
                Player player = getPlayer(username);
                String bed;
                String[] arrOfBed;
                String location;
                String[] arrOfLocation;
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
                    obj.put((Object)"lastJoined", (Object)player.getLastPlayed());
                    obj.put((Object) "online", (Object) true);
                    location = player.getLocation().toString();
                    arrOfLocation = location.split(",");
                    obj.put((Object) "location", (Object) (arrOfLocation[1] + "," + arrOfLocation[2] + "," + arrOfLocation[3]));
                    res.send(obj.toJSONString());
                } catch (Exception e) {
                    if (e.getMessage().contains("Cannot invoke \"org.bukkit.entity.Player.getName()\" because \"player\" is null")) {
                        if (debug) {
                            API.getPlugin(API.class).getLogger().info("Player offline, attempting to serve cached player data");
                        }
                        String filename =  playerDataFolder + username + ".json";
                        if (Files.exists(Path.of(filename))) {
                            String content = "blank";
                            try {
                                content = Files.readString(Path.of(filename));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            res.send(content);
                            BasicFileAttributes attr = null;
                            try {
                                attr = Files.readAttributes(Path.of(filename), BasicFileAttributes.class);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            if (debug) {
                                if (attr != null) {
                                    API.getPlugin(API.class).getLogger().info("Served cached data from " + attr.lastModifiedTime());
                                } else {
                                    API.getPlugin(API.class).getLogger().info("Served cached data");
                                }
                            }
                            return;
                        }
                        obj.put("error", true);
                        obj.put("message", "Player not online, or not found");
                        res.send(obj.toJSONString());
                    } else {
                        getLogger().info("Error: " + e.getMessage());
                        res.send("Error: " + e.getMessage());
                    }
                }
        } else {
                final JSONObject obj = new JSONObject();
                obj.put("error", true);
                obj.put("message", "You are not authorised to access this resource");
                res.send(obj.toJSONString());
                API.getPlugin(API.class).getLogger().warning("A request to access Minecraft info from " + req.getIp() +
                        " was rejected as they did not pass the correct secret in the header");
            }
            });

    }
}
