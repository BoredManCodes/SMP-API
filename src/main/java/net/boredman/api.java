package net.boredman;

import com.google.gson.JsonArray;
import express.Express;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class api extends JavaPlugin {
    private static api plugin;
    final FileConfiguration config = getConfig();

    public static Express getApp() {
        return express;
    }

    @Override
    public void onEnable() {
        config.addDefault("port", 25567);
        config.addDefault("secret", "CHANGE THIS!");
        config.options().copyDefaults(true);
        saveConfig();
        start();
        plugin = this;
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
