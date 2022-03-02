# An API to expose player information for Janet.

# Examples

`https://yourserveripchange.me:port/players/BoredManPlays/stats` (only works when player is online, will fix soon)

```
{
   "kills":40892,
   "world":"world",
   "level":9,
   "health":20,
   "jumps":266945,
   "experience":0.19999996,
   "uuid":"957c3421-5c2c-48aa-810d-2bd895804940",
   "food":20,
   "deaths":647,
   "username":"BoredManPlays"
}
```

`https://yourserveripchange.me:port/players/BoredManPlays/discord`

```
{
	"discordId": "324504908013240330",
	"error": false,
	"uuid": "957c3421-5c2c-48aa-810d-2bd895804940",
	"discordTag": "BoredManPlays#0001",
	"discordName": "BoredManPlays",
	"username": "BoredManPlays"
}
```

`https://yourserveripchange.me:port/discord/users/id/324504908013240330/player`

```
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
