package com.allocinit.soloslumber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.allocinit.bukkit.CommandPlugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

public class SoloSlumber extends CommandPlugin implements CommandExecutor {
   private Map<String, BukkitTask> sleeperVsTask = new HashMap<>();
   private Map<UUID, NightForcer> worldVsNightForcer = new HashMap<>();

   public SoloSlumber() {
      super("soloslumber");

      registerSubCommand(new WakeCommand(this));
      registerSubCommand(new ReloadCommand(this));
   }

   @Override
   public void onEnable() {
      super.onEnable();

      this.getServer().getPluginManager().registerEvents(new BedListener(this), this);
   }

   public Map<String, BukkitTask> getSleepers() {
      return sleeperVsTask;
   }

   public Map<UUID, NightForcer> getNightForcers() {
      return this.worldVsNightForcer;
   }

   public String getMessage(CommandSender sender, String msgKey) {
      String msg = this.getConfig().getString("messages." + msgKey);

      if (sender != null && msg != null)
         msg = msg.replace("%player_name%", sender.getName());

      return msg;
   }
}
