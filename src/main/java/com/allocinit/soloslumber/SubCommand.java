package com.allocinit.soloslumber;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    protected SoloSlumber soloSlumber;
    private String cmd;

    public SubCommand(SoloSlumber soloSlumber, String cmd) {
        this.soloSlumber = soloSlumber;
        this.cmd = cmd;
    }

    public String getCommandName() {
        return this.cmd;
    }

    public abstract void doCommand(CommandSender sender, String[] args) throws Exception;

    public abstract void writeUsage(CommandSender player);

    protected void checkPerm(CommandSender player, String perm) {
        if (!player.hasPermission(perm))
            throw new PermissionDeniedException();
    }
}
