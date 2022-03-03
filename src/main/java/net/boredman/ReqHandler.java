package net.boredman;

import express.Express;
import express.ExpressListener;
import org.bukkit.plugin.Plugin;

public class ReqHandler {

    public ReqHandler(Express app) {
        int port = api.getConfig().getInt("port");
        new net.boredman.routes.PlayersRoute(app);
        new net.boredman.routes.DiscordRoute(app);
        app.get("/", (req, res) -> {
            res.redirect("https://boredman.net");
        }).listen(port);
    }
}
