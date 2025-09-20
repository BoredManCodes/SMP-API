package net.boredman;

import express.Express;
import net.boredman.routes.DiscordRoute;
import net.boredman.routes.PlayersRoute;

public class ReqHandler {

    public ReqHandler(Express app) {
        int port = api.getPlugin(api.class).getConfig().getInt("port");
        new PlayersRoute(app);
        new DiscordRoute(app);
        app.get("/", (req, res) -> {
            res.send("Error 418: The server refuses to brew coffee because it is, permanently, a teapot.\n" +
                    "For more information: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/418");
        }).listen(port);
    }
}
