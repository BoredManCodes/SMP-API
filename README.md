<<<<<<< HEAD

# An API to expose player information for Janet (or other discord bots).

### Coding time:
[![wakatime](https://wakatime.com/badge/user/d835e453-8200-4955-8277-80c4e9e3809b/project/2093c49e-2b5c-4aa2-91ad-c86aeef24eff.svg)](https://wakatime.com/badge/user/d835e453-8200-4955-8277-80c4e9e3809b/project/2093c49e-2b5c-4aa2-91ad-c86aeef24eff)

# Support
Prove you aren't a robot and get some help here:
http://invite.boredmandiscord.workers.dev/

## SpigotMC page:
https://www.spigotmc.org/threads/smp-api.549051/



# Available Endpoints
All API calls must include a `secret` header the value of which is set in the config.yml.

The plugin will refuse to load if you don't set a secret to prevent leaking player IP's and base locations.
<br><br><br>
`https://yourserveripchange.me:port/players/BoredManPlays` Lookup some useful player stats:

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
    "online":false,
    "location":"x=192.5,y=58.5625,z=-25.5",
    "time":"8200",
    "deaths":"3",
    "username":"BoredManPlays"
}
```
| key        | definition                                                             |
|------------|------------------------------------------------------------------------|
| kills      | The amount of entities the player has killed                           |
| bed        | Player's bed location                                                  |
| address    | The IP address of the player                                           |
| death      | The time in seconds since the players last death                       |
| level      | The player's XP level                                                  |
| lastjoined | The epoch timestamp from when the player last joined (in milliseconds) |
| health     | Player's health from 0 - 20                                            |
| jumps      | The amount of times the player has jumped                              |
| experience | The players XP points                                                  |
| uuid       | The player's UUID                                                      |
| gamemode   | The player's gamemode                                                  |
| food       | The player's hunger from 0 - 20                                        |
| world      | The world the player is currently in                                   |
| online     | Whether the player is currently online or using cached playerdata      |
| location   | The player's location at the time of the lookup                        |
| time       | The amount of in game time the player has been online                  |
| deaths     | The amount of times the player has died                                |
| username   | The player's username                                                  |

<br><br>
`https://yourserveripchange.me:port/discord/name/BoredManPlays` Lookup linked Minecraft and Discord accounts using Discord username:
**Requires DiscordSRV**
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
<br><br>
`https://yourserveripchange.me:port/discord/id/324504908013240330` Same as above but with the Discord ID instead of username:

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
| key         | definition                                |
|-------------|-------------------------------------------|
| discordid   | The user's Discord ID                     |
| error       | Whether request returned an error         |
| uuid        | The user's Minecraft UUID                 |
| discordTag  | The user's Discord name and discriminator |
| discordName | The user's Discord name                   |
| username    | The user's Minecraft player name          |

<br><br>

API call failed due to invalid secret header:
```json
{
    "error":true,
    "message":"You are not authourised to access this resource"
}
```
Log output on failed authentication:
```log
[SMP-API] A request to access Minecraft info from 206.189.205.251 was rejected 
as they did not pass the correct secret in the header
```

# Configuration
The config is pretty simple. This is the default:
```yml
debug: false
port: 25567
secret: CHANGE THIS!
```
| key    | definition                                                             |
|--------|------------------------------------------------------------------------|
| debug  | Whether debug messages should be logged to console                     |
| port   | The port to host the API on.<br/> **Do not use your Minecraft server port** |
| secret | The "password" that allows access to the API                           |

Note, the plugin will not run unless you change the secret. This is explained above.

# Legal Mumbo Jumbo
The idea and base code for this project came from [TristanSMPAPI](https://github.com/twisttaan/TristanSMPAPI).
It has been extensively added to by myself with much technical help from the developers of [DiscordSRV](https://github.com/DiscordSRV/DiscordSRV/) 
=======
A remake of BoredManCodes' SMP-API to fit my needs.
>>>>>>> 5b19083 (file add)
