# SoloSlumber - A Minecraft pluging for single-player sleep.

### SoloSlumber allows any player to initiate the skip-night feature of Minecraft but gives the other players a chance to wakeup the sleeper if they prefer not to skip night.  SoloSlumber only notifies players that are in the same world as the sleeper.

## Config
```yml
# Number of ticks to sleep a player sleep before making it day
napTime: 100

# The worlds that this plugin should listen for sleepers.
# Note that each world is handled separately.  Sleeping off
# night in a particular world will not affect other worlds
# listed here.
worlds:
  - world
```
## Commands
* `/soloslumber wake <player>` - Wake the sleepers
* `/soloslumber reload` - Reload the configuration.  Requires the `soloslumber.reload` permission.
