
# An API to expose player information for Janet (or other discord bots).

### Coding time:
[![wakatime](https://wakatime.com/badge/user/d835e453-8200-4955-8277-80c4e9e3809b/project/2093c49e-2b5c-4aa2-91ad-c86aeef24eff.svg)](https://wakatime.com/badge/user/d835e453-8200-4955-8277-80c4e9e3809b/project/2093c49e-2b5c-4aa2-91ad-c86aeef24eff)
# Examples

`https://yourserveripchange.me:port/players/BoredManPlays` (only works when player is online, will fix soon)

```json
{
  "kills":"1",
  "bed":"x=189.5,y=58.5625,z=-25.5",
  "address":"127.0.0.1",
  "death":"5801",
  "level":"0",
  "lastJoined":1646231639815,
  "health":"20.0",
  "jumps":"167",
  "experience":"0.0",
  "uuid":"957c3421-5c2c-48aa-810d-2bd895804940",
  "gamemode":"CREATIVE",
  "food":"20",
  "world":"world",
  "time":"8200",
  "deaths":"3",
  "username":"BoredManPlays"
}
```

`https://yourserveripchange.me:port/discord/name/BoredManPlays`

```json
{
	"discordId": "324504908013240330",
	"error": false,
	"uuid": "957c3421-5c2c-48aa-810d-2bd895804940",
	"discordTag": "BoredManPlays#0001",
	"discordName": "BoredManPlays",
	"username": "BoredManPlays"
}
```

`https://yourserveripchange.me:port/discord/id/324504908013240330`

```json
{
	"discordId": "324504908013240330",
	"error": false,
	"uuid": "957c3421-5c2c-48aa-810d-2bd895804940",
	"discordTag": "BoredManPlays#0001",
	"discordName": "BoredManPlays",
	"username": "BoredManPlays"
}
```

# Legal Mumbo Jumbo
The idea and base code for this project came from [TristanSMP](https://github.com/twisttaan/TristanSMPAPI).
It has been extensively added to by myself with much technical help from the developers of [DiscordSRV](https://github.com/DiscordSRV/DiscordSRV/) 
