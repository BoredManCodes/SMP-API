
# An API to expose player information for Janet (or other discord bots).

### Coding time:
[![wakatime](https://wakatime.com/badge/user/d835e453-8200-4955-8277-80c4e9e3809b/project/2093c49e-2b5c-4aa2-91ad-c86aeef24eff.svg)](https://wakatime.com/badge/user/d835e453-8200-4955-8277-80c4e9e3809b/project/2093c49e-2b5c-4aa2-91ad-c86aeef24eff)
# Examples

`https://yourserveripchange.me:port/players/BoredManPlays` Lookup some useful player stats (only works when player is online, will fix soon)

```json
{
  	"kills":"1", # The amount of entities the player has killed
  	"bed":"x=189.5,y=58.5625,z=-25.5", # Player's bed location
  	"address":"127.0.0.1", # The IP address of the player
  	"death":"5801", # The time in seconds since the players last death
  	"level":"0", # The player's XP level
  	"lastJoined":1646231639815, # The epoch timestamp from when the player last joined (in milliseconds)
  	"health":"20.0", # Player's health from 0 - 20
  	"jumps":"167", # The amount of times the player has jumped
  	"experience":"0.0", # The players XP points
  	"uuid":"957c3421-5c2c-48aa-810d-2bd895804940", # The player's UUID
  	"gamemode":"CREATIVE", # The player's gamemode
  	"food":"20", # the player's hunger from 0 - 20
  	"world":"world", # The world the player is currently in
  	"time":"8200", # The amount of time the player has been online
  	"deaths":"3", # The amount of times the player has died
  	"username":"BoredManPlays" # The player's username
}
```

`https://yourserveripchange.me:port/discord/name/BoredManPlays` Lookup linked Minecraft and Discord accounts using Discord username

```json
{
	"discordId": "324504908013240330", # The user's Discord ID
	"error": false, # Whether the request returned an error
	"uuid": "957c3421-5c2c-48aa-810d-2bd895804940", # The user's UUID
	"discordTag": "BoredManPlays#0001", # The user's Discord name and discriminator
	"discordName": "BoredManPlays", # The user's Discord name
	"username": "BoredManPlays" # The user's Minecraft player name
}
```

`https://yourserveripchange.me:port/discord/id/324504908013240330` Same as above but with the Discord ID instead of username

```json
{
	"discordId": "324504908013240330", # The user's Discord ID
	"error": false, # Whether the request returned an error
	"uuid": "957c3421-5c2c-48aa-810d-2bd895804940", # The user's UUID
	"discordTag": "BoredManPlays#0001", # The user's Discord name and discriminator
	"discordName": "BoredManPlays", # The user's Discord name
	"username": "BoredManPlays" # The user's Minecraft player name
}
```

# Legal Mumbo Jumbo
The idea and base code for this project came from [TristanSMP](https://github.com/twisttaan/TristanSMPAPI).
It has been extensively added to by myself with much technical help from the developers of [DiscordSRV](https://github.com/DiscordSRV/DiscordSRV/) 
