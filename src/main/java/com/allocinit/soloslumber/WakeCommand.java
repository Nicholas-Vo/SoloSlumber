package com.allocinit.soloslumber;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.allocinit.bukkit.SubCommand;
import com.allocinit.bukkit.UsageException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WakeCommand extends SubCommand<SoloSlumber> {
    public WakeCommand(SoloSlumber plugin) {
        super(plugin, "wake");
    }

    @Override
    public void doCommand(CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player");
            return;
        }

        Player waker = (Player) sender;

        if (args.length != 0)
            throw new UsageException();

        Set<Player> wakers = new HashSet<>();

        for (String playerName : this.plugin.getSleepers().keySet()) {
            Player player = this.plugin.getServer().getPlayer(playerName);
            if (player != null && player.getWorld() == waker.getWorld()) {
                player.wakeup(true);
                player.sendMessage(this.plugin.getMessage(sender, "woken_up"));
                wakers.add(player);
            }
        }

        if (!wakers.isEmpty()) {
            sender.sendMessage(this.plugin.getMessage(null, "woke_them_up"));

            // Tell everyone else what happened
            for (Player otherPlayer : waker.getWorld().getPlayers()) {
                if (otherPlayer != waker && !wakers.contains(otherPlayer))
                    otherPlayer.sendMessage(this.plugin.getMessage(waker, "woke_up"));
            }

            if (this.plugin.getConfig().getBoolean("wakerForcesNight"))
                this.plugin.getNightForcers().put(waker.getWorld().getUID(), new NightForcer(waker));
        }
    }

    @Override
    public void writeUsage(CommandSender player) {
        player.sendMessage(
                ChatColor.GREEN + "/soloslumber wake" + ChatColor.YELLOW + "- Wake up sleeping players in this world.");
    }
}