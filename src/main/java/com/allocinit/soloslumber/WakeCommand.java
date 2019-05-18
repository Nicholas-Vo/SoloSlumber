package com.allocinit.soloslumber;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WakeCommand extends SubCommand {
    public WakeCommand(SoloSlumber plugin) {
        super(plugin, "wake");
    }

    @Override
    public void doCommand(CommandSender sender, String[] args) throws Exception {
        checkPerm(sender, "soloslumber.reload");
        
        if (args.length != 0)
            throw new UsageException();

        for (String playerName : this.soloSlumber.getSleepers().keySet()) {
            Player player = this.soloSlumber.getServer().getPlayer(playerName);
            if (player != null) {
                player.wakeup(true);
                player.sendMessage("" + ChatColor.AQUA + sender.getName() + " woke you up!");
            }
        }
    }

    @Override
    public void writeUsage(CommandSender player) {
        player.sendMessage(
                ChatColor.GREEN + "/soloslumber wake" + ChatColor.YELLOW + "- Wake up sleeping players in this world.");
    }
}
