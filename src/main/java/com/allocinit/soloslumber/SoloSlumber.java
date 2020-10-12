package com.allocinit.soloslumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.allocinit.bukkit.CommandPlugin;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class SoloSlumber extends CommandPlugin {
   private static SoloSlumber instance;
   private Map<String, BukkitTask> sleeperVsTask = new HashMap<>();
   private Map<UUID, List<Player>> worldVsNightForcers = new HashMap<>();
   private Map<UUID, Long> worldVsNextDay = new HashMap<>();

   public SoloSlumber() {
      super("soloslumber");

      SoloSlumber.instance = this;

      registerSubCommand(new WakeCommand());
      registerSubCommand(new ReloadCommand());
      registerSubCommand(new WantNightCommand());
   }

   public static SoloSlumber getPlugin() {
      return instance;
   }

   @Override
   public void onEnable() {
      super.onEnable();

      this.getServer().getPluginManager().registerEvents(new BedListener(this), this);
   }

   public boolean allowedInWorld(Player player) {
      World world = player.getWorld();
      return SoloSlumber.getPlugin().getConfig().getStringList("worlds").contains(world.getName());
   }

   public Map<String, BukkitTask> getSleepers() {
      return sleeperVsTask;
   }

   public void addForcer(Player player) {
      World world = player.getWorld();

      // When someone forces night, we compute the time of the next day
      // in that world. We'll use that time to know if a sleeper is allowed
      // to skip night. We use this strategy to avoid having to watch the
      // clock on every tick.
      Long nextAllowedWake = (world.getFullTime() / 24000) * 24000 + 24000;

      // If the nextAllowedWake is greater than the previous nextAllowedWake,
      // then this is the first waker on the next day, so clear out the
      // stale forcers
      Long last = worldVsNextDay.get(world.getUID());
      if (last != null && nextAllowedWake > last) {
         worldVsNextDay.clear();
      }

      worldVsNextDay.put(world.getUID(), nextAllowedWake);

      // Now add them to the list of forcers
      List<Player> forcers = worldVsNightForcers.get(world.getUID());
      if (forcers == null) {
         forcers = new ArrayList<>();
         worldVsNightForcers.put(world.getUID(), forcers);
      }

      if (!forcers.contains(player)) {
         forcers.add(player);
      }
   }

   public void tellEveryoneElse(Player player, String message) {
      for (Player otherPlayer : player.getWorld().getPlayers()) {
         if (otherPlayer != player)
            otherPlayer.sendMessage(message);
      }
   }

   public Map<UUID, Long> getWorldVsNextDay() {
      return worldVsNextDay;
   }

   public Map<UUID, List<Player>> getNightForcers() {
      return this.worldVsNightForcers;
   }

   public String getMessage(CommandSender sender, String msgKey) {
      String msg = this.getConfig().getString("messages." + msgKey);

      if (sender != null && msg != null)
         msg = msg.replace("%player_name%", sender.getName());

      return msg;
   }
}
