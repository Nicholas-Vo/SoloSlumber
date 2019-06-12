package com.allocinit.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandPlugin extends JavaPlugin implements CommandExecutor {
   private String cmdName;
   private Map<String, SubCommand<?>> commands = new HashMap<>();

   public CommandPlugin(String cmdName) {
      this.cmdName = cmdName;
   }

   public void onEnable() {
      initConfig();

      this.getCommand(cmdName).setExecutor(this);
   }

   private void initConfig() {
      File dataFolder = getDataFolder();
      File configFile = new File(dataFolder, "config.yml");

      try {
         if (!configFile.exists()) {
            saveResource("config.yml", false);
            return;
         }
      } catch (Exception e) {
         return;
      }

      checkForMigrations();
   }

   private void checkForMigrations() {
      File dataFolder = getDataFolder();
      File configFile = new File(dataFolder, "config.yml");

      // Load up the old config.yml
      YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);

      // Load up the new config.yml
      YamlConfiguration newConfig = YamlConfiguration
            .loadConfiguration(new InputStreamReader(getResource("config.yml")));

      // If the new config has keys that the old config doesn't have, we'll kick off
      // the migration progress
      if (needsMigration(newConfig.getRoot(), oldConfig.getRoot())) {
         migrateSection(oldConfig.getRoot(), oldConfig.getRoot());
         configFile.renameTo(new File(dataFolder, "config.yml.prev"));
         try {
            newConfig.save(configFile);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   private void migrateSection(ConfigurationSection newConfig, ConfigurationSection oldConfig) {
      for (String aKey : oldConfig.getKeys(false)) {
         Object val = oldConfig.get(aKey);
         if (val instanceof ConfigurationSection) {
            ConfigurationSection newSubConfig = newConfig.getConfigurationSection(aKey);
            migrateSection(newSubConfig, (ConfigurationSection) val);
         } else {
            newConfig.set(aKey, val);
         }
      }
   }

   private boolean needsMigration(ConfigurationSection newConfig, ConfigurationSection oldConfig) {
      for (String aKey : newConfig.getKeys(false)) {
         Object val = oldConfig.get(aKey);
         if (val == null)
            return true;
         if (val instanceof ConfigurationSection) {
            ConfigurationSection newSubConfig = newConfig.getConfigurationSection(aKey);
            if (needsMigration(newSubConfig, (ConfigurationSection) val))
               return true;
         }
      }
      return false;
   }

   protected void registerSubCommand(SubCommand<?> cmd) {
      commands.put(cmd.getCommandName(), cmd);
   }

   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      try {
         if (args.length == 0)
            throw new UsageException();

         SubCommand<?> cmd = commands.get(args[0]);

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
      player.sendMessage("[" + ChatColor.GOLD + cmdName + ChatColor.WHITE + " usage]");
      for (SubCommand<?> cmd : commands.values())
         cmd.writeUsage(player);
   }
}
