package net.boredman;

import express.Express;

public class ReqHandler {

    public ReqHandler(Express app) {

        new net.boredman.routes.PlayersRoute(app);
        new net.boredman.routes.DiscordRoute(app);

        app.get("/", (req, res) -> {
            res.redirect("https://boredman.net");
        }).listen(25567);
    }
}
