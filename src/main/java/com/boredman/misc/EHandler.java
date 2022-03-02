package com.boredman.misc;

import com.earth2me.essentials.Essentials;
import net.ess3.api.events.MuteStatusChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EHandler implements Listener {
    static Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    @EventHandler
    public void onMute(MuteStatusChangeEvent event){
        if(event.getAffected() instanceof Player){
            Player player = (Player) event.getAffected();
            if(ess.getUser(player).isMuted()){
                player.sendMessage("§cYou have been muted!");
            } else {
                player.sendMessage("§aYou have been unmuted!");
            }
        }
    }


}
