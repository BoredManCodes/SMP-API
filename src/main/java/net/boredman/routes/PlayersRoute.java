package net.boredman.routes;

import express.Express;
import net.boredman.api;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPlayer;
import java.sql.*;


public class PlayersRoute {
    public PlayersRoute(Express app) {
        boolean debug = api.getPlugin(api.class).getConfig().getBoolean("debug");
        String secret = api.getPlugin(api.class).getConfig().getString("secret");


        app.get("/players/:username", (req, res) -> {
            if (debug) {
                api.getPlugin(api.class).getLogger().info("A request was made to access " +
                        req.getParams().get("username") + "'s player data");
            }

            if (secret.equals(req.getHeader("secret").get(0))) {
                // ✅ Use the plugin's data folder instead of guessing paths
                File dataFolder = new File(api.getPlugin(api.class).getDataFolder(), "playerdata");
                if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                    api.getPlugin(api.class).getLogger().severe("Could not create playerdata folder at " + dataFolder.getAbsolutePath());
                }
                String playerDataFolder = dataFolder.getAbsolutePath();

                String username = req.getParams().get("username");
                JSONObject obj = new JSONObject();
                Player player = getPlayer(username);

                try {
                    obj.put("username", player.getName());
                    obj.put("uuid", player.getUniqueId().toString());
                    obj.put("health", String.valueOf(player.getHealth()));
                    obj.put("food", String.valueOf(player.getFoodLevel()));
                    obj.put("world", player.getWorld().getName());
                    obj.put("experience", String.valueOf(player.getExp()));
                    obj.put("level", String.valueOf(player.getLevel()));
                    obj.put("deaths", String.valueOf(player.getStatistic(Statistic.DEATHS)));
                    obj.put("kills", String.valueOf(player.getStatistic(Statistic.MOB_KILLS)));
                    obj.put("jumps", String.valueOf(player.getStatistic(Statistic.JUMP)));
                    obj.put("gamemode", player.getGameMode().toString());

                    if (player.getBedSpawnLocation() != null) {
                        String[] arrOfBed = player.getBedSpawnLocation().toString().split(",");
                        obj.put("bed", arrOfBed[1] + "," + arrOfBed[2] + "," + arrOfBed[3]);
                    }

                    obj.put("time", String.valueOf(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20));
                    obj.put("death", String.valueOf(player.getStatistic(Statistic.TIME_SINCE_DEATH) / 20));

                    String[] arrOfAddress = String.valueOf(player.getAddress())
                            .replace("/", "")
                            .split(":");
                    obj.put("address", arrOfAddress[0]);
                    obj.put("lastJoined", player.getLastPlayed());
                    obj.put("online", true);

                    String[] arrOfLocation = player.getLocation().toString().split(",");
                    obj.put("location", arrOfLocation[1] + "," + arrOfLocation[2] + "," + arrOfLocation[3]);

                    res.send(obj.toJSONString());
                } catch (Exception e) {
                    // Player is offline → serve cached file if available
                    if (e.getMessage() != null && e.getMessage().contains("player\" is null")) {
                        if (debug) {
                            api.getPlugin(api.class).getLogger().info("Player offline, attempting to serve cached player data");
                        }
                        Path filename = Path.of(playerDataFolder, username + ".json");
                        if (Files.exists(filename)) {
                            try {
                                String content = Files.readString(filename);
                                res.send(content);

                                BasicFileAttributes attr = Files.readAttributes(filename, BasicFileAttributes.class);
                                if (debug) {
                                    api.getPlugin(api.class).getLogger().info("Served cached data from " + attr.lastModifiedTime());
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                res.send("Error reading cached player data");
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
                api.getPlugin(api.class).getLogger().warning("A request to access Minecraft info from " + req.getIp() +
                        " was rejected as they did not pass the correct secret in the header");
            }
        });

        // ========================
        //  New /db route
        // ========================
        app.post("/db", (req, res) -> {
            // Check auth
            if (!secret.equals(req.getHeader("secret").get(0))) {
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                obj.put("message", "You are not authorised to access this resource");
                res.send(obj.toJSONString());
                api.getPlugin(api.class).getLogger().warning("Rejected DB request from " + req.getIp());
                return;
            }

            // Get query string from body or query param
            String sql = req.getQuery("sql");
            if (sql == null || sql.isEmpty()) {
                res.send("{\"error\":true,\"message\":\"No SQL query provided\"}");
                return;
            }
            sql = URLDecoder.decode(sql, StandardCharsets.UTF_8);
            File dbFile = new File(api.getPlugin(api.class).getDataFolder().getParentFile(),
                    "CMI/cmi.sqlite.db");
            if (!dbFile.exists()) {
                res.send("{\"error\":true,\"message\":\"Database file not found at " + dbFile.getAbsolutePath() + "\"}");
                return;
            }

            JSONArray results = new JSONArray();

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
                 Statement stmt = conn.createStatement()) {

                boolean hasResult = stmt.execute(sql);

                if (hasResult) {
                    try (ResultSet rs = stmt.getResultSet()) {
                        ResultSetMetaData meta = rs.getMetaData();
                        int colCount = meta.getColumnCount();

                        while (rs.next()) {
                            JSONObject row = new JSONObject();
                            for (int i = 1; i <= colCount; i++) {
                                row.put(meta.getColumnLabel(i), rs.getObject(i));
                            }
                            results.add(row);
                        }
                    }
                } else {
                    JSONObject update = new JSONObject();
                    update.put("updated", stmt.getUpdateCount());
                    results.add(update);
                }

                res.send(results.toJSONString());

            } catch (SQLException e) {
                getLogger().severe("SQLite error: " + e.getMessage());
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                obj.put("message", e.getMessage());
                res.send(obj.toJSONString());
            }
        });
    }
}
