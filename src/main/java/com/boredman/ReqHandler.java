package com.boredman;

import express.Express;

public class ReqHandler {

    public ReqHandler(Express app) {

        new com.boredman.routes.PlayersRoute(app);
        new com.boredman.routes.DiscordRoute(app);

        app.get("/", (req, res) -> {
            res.redirect("https://github.com/twisttaan/TristanSMPAPI");
        }).listen(25567);
    }
}
