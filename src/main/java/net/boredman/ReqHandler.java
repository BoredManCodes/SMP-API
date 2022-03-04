package net.boredman;

import express.Express;
import express.ExpressListener;
import net.boredman.routes.DiscordRoute;
import net.boredman.routes.PlayersRoute;
import org.bukkit.plugin.Plugin;

public class ReqHandler {

    public ReqHandler(Express app) {
        int port = API.getPlugin(API.class).getConfig().getInt("port");
        new PlayersRoute(app);
        new DiscordRoute(app);
        app.get("/", (req, res) -> {
            res.send("Error 418: The server refuses to brew coffee because it is, permanently, a teapot.\n" +
                    "For more information: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/418");
        }).listen(port);
    }
}
