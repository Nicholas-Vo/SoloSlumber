package com.allocinit.bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand<T extends CommandPlugin> {
    protected T plugin;
    private String cmd;

    public SubCommand(T plugin, String cmd) {
        this.plugin = plugin;
        this.cmd = cmd;
    }

    public String getCommandName() {
        return this.cmd;
    }

    public abstract void doCommand(CommandSender sender, String[] args) throws Exception;

    public abstract void writeUsage(CommandSender player);

    protected void checkPerm(CommandSender sender, String perm) {
        if (sender instanceof Player) {
            if (!((Player) sender).hasPermission(perm))
                throw new PermissionDeniedException();
        }
    }
}
