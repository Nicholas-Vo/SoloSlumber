package com.allocinit.soloslumber;

import java.util.HashMap;
import java.util.Map;

import com.allocinit.bukkit.CommandPlugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.scheduler.BukkitTask;

public class SoloSlumber extends CommandPlugin implements CommandExecutor {
   private Map<String, BukkitTask> sleeperVsTask = new HashMap<>();

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
}
