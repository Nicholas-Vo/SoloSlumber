package com.allocinit.soloslumber;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public class SoloSlumber extends JavaPlugin implements CommandExecutor {
   private Map<String, BukkitTask> sleeperVsTask = new HashMap<>();
   private Map<String, SubCommand> commands = new HashMap<>();
   private FileConfiguration config;

   public SoloSlumber() {
      registerSubCommand(new WakeCommand(this));
      registerSubCommand(new ReloadCommand(this));
   }

   public void onEnable() {
      this.saveDefaultConfig();

      this.getServer().getPluginManager().registerEvents(new BedListener(this), this);
      this.getCommand("soloslumber").setExecutor(this);
   }

   private void registerSubCommand(SubCommand cmd) {
      commands.put(cmd.getCommandName(), cmd);
   }

   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      try {
         if (args.length == 0)
            throw new UsageException();

         SubCommand cmd = commands.get(args[0]);

         if (cmd == null)
            throw new UsageException();

         cmd.doCommand(sender, Arrays.copyOfRange(args, 1, args.length));
      } catch (UsageException e) {
         this.writeUsage(sender);
      } catch (PermissionDeniedException e) {
         sender.sendMessage(ChatColor.RED + "Permission Denied");
      } catch (ErrorException e) {
         sender.sendMessage(ChatColor.RED + e.getMessage());
      } catch (Exception e) {
         sender.sendMessage("Uncaught exception: " + e.getMessage());
      }

      return true;
   }

   public void writeUsage(CommandSender player) {
      player.sendMessage("[" + ChatColor.GOLD + "SoloSlumber" + ChatColor.WHITE + "]");
      for (SubCommand cmd : commands.values())
         cmd.writeUsage(player);
   }

   public Map<String, BukkitTask> getSleepers() {
      return sleeperVsTask;
   }
}
