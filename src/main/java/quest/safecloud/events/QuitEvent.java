package quest.safecloud.events;

import quest.safecloud.API;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.bukkit.Bukkit.getPlayer;

public class QuitEvent implements Listener {
    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) throws IOException {
        boolean debug = API.getPlugin(API.class).getConfig().getBoolean("debug");
        if (debug) {
            System.out.println(event.getPlayer().getName().toString() + " left the server, saving their player data.");
        }
        File dir = new File(API.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        File plugins = new File(dir.getParentFile().getPath());
        String playerDataFolder = plugins + "\\SMP-API\\playerdata\\";
        if (!Files.exists(Path.of(playerDataFolder))) {
            Files.createDirectory(Path.of(playerDataFolder));
        }
        String filename =  playerDataFolder + event.getPlayer().getName() + ".json";
        String username = event.getPlayer().getName();
        JSONObject obj = new JSONObject();
        Player player = getPlayer(username);
        String bed;
        String[] arrOfBed;
        String location;
        String[] arrOfLocation;
        String address;
        String[] arrOfAddress;
        try {
            obj.put((Object) "username", (Object) player.getName());
            obj.put((Object) "uuid", (Object) player.getUniqueId().toString());
            obj.put((Object) "health", (Object) String.valueOf(player.getHealth()));
            obj.put((Object) "food", (Object) String.valueOf(player.getFoodLevel()));
            obj.put((Object) "world", (Object) player.getWorld().getName());
            obj.put((Object) "experience", (Object) String.valueOf(player.getExp()));
            obj.put((Object) "level", (Object) String.valueOf(player.getLevel()));
            obj.put((Object) "deaths", (Object) String.valueOf(player.getStatistic(Statistic.DEATHS)));
            obj.put((Object) "kills", (Object) String.valueOf(player.getStatistic(Statistic.MOB_KILLS)));
            obj.put((Object) "jumps", (Object) String.valueOf(player.getStatistic(Statistic.JUMP)));
            obj.put((Object) "gamemode", (Object) player.getGameMode().toString());
            if (player.getBedSpawnLocation() != null) {
                bed = player.getBedSpawnLocation().toString();
                arrOfBed = bed.split(",");
                obj.put((Object) "bed", (Object) (arrOfBed[1] + "," + arrOfBed[2] + "," + arrOfBed[3]));
            }
            obj.put((Object) "time", (Object) String.valueOf(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20));
            obj.put((Object) "death", (Object) String.valueOf(player.getStatistic(Statistic.TIME_SINCE_DEATH) / 20));
            address = String.valueOf(player.getAddress()).replace("/", "");
            arrOfAddress = address.split(":");
            obj.put((Object) "address", (Object) arrOfAddress[0]);
            obj.put((Object) "lastJoined", (Object) System.currentTimeMillis());
            obj.put((Object) "online", (Object) false);
            location = player.getLocation().toString();
            arrOfLocation = location.split(",");
            obj.put((Object) "location", (Object) (arrOfLocation[1] + "," + arrOfLocation[2] + "," + arrOfLocation[3]));
            Files.write(Paths.get(filename), obj.toJSONString().getBytes());
            if (debug) {
                API.getPlugin(API.class).getLogger().info("Saved " + event.getPlayer().getName() + "'s player data");
            }
        } catch (Exception e) {
            API.getPlugin(API.class).getLogger().severe("Ran into an error trying to save player data");
            API.getPlugin(API.class).getLogger().severe(String.valueOf(e));
        }
    }
}
