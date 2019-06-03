package com.allocinit.soloslumber;

import com.allocinit.bukkit.SubCommand;
import com.allocinit.bukkit.UsageException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand<SoloSlumber> {
    public ReloadCommand(SoloSlumber plugin) {
        super(plugin, "reload");
    }

    @Override
    public void doCommand(CommandSender sender, String[] args) throws Exception {
        if (args.length != 0)
            throw new UsageException();

        this.plugin.reloadConfig();

        sender.sendMessage("" + ChatColor.GREEN + "Configuration reloaded.");
    }

    @Override
    public void writeUsage(CommandSender player) {
        player.sendMessage(ChatColor.GREEN + "/soloslumber reload" + ChatColor.YELLOW + "- Reload the configuration.");
    }
}
