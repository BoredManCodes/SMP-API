package net.boredman;

import express.Express;
import net.boredman.events.QuitEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class api extends JavaPlugin implements Listener {
    private static api plugin;
    final FileConfiguration config = getConfig();

    public static Express getApp() {
        return express;
    }

    @Override
    public void onEnable() {
        config.addDefault("port", 25567);
        config.addDefault("secret", "CHANGE THIS!");
        config.addDefault("debug", false);
        config.options().copyDefaults(true);
        saveConfig();
        start();
        plugin = this;
        if (config.getString("secret").equals("CHANGE THIS!")) {
            getLogger().warning("--------------------------------------------");
            getLogger().severe("You MUST change the secret in the config.yml for this plugin to work. " +
                    "This prevents exposing player IP addresses to the world");
            getLogger().warning("--------------------------------------------");
            this.getPluginLoader().disablePlugin(this);
        }
        this.getServer().getPluginManager().registerEvents(new QuitEvent(), this);
    }

    @Override
    public void onDisable() {
    }

    public void start() {
        getLogger().info("Starting API on port " + config.getInt("port"));
        new ReqHandler(express);
        getLogger().info("API Started");
    }

    public static Express express = new Express();


}
