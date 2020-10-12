package com.allocinit.soloslumber;

import com.allocinit.bukkit.SubCommand;
import com.allocinit.bukkit.UsageException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    public ReloadCommand() {
        super("reload");
    }

    @Override
    public void doCommand(CommandSender sender, String[] args) throws Exception {
        checkPerm(sender, "soloslumber.reload");

        if (args.length != 0)
            throw new UsageException();

        SoloSlumber.getPlugin().reloadConfig();

        sender.sendMessage("" + ChatColor.GREEN + "Configuration reloaded.");
    }

    @Override
    public void writeUsage(CommandSender player) {
        player.sendMessage(ChatColor.GREEN + "/soloslumber reload" + ChatColor.YELLOW + " - Reload the configuration.");
    }
}
