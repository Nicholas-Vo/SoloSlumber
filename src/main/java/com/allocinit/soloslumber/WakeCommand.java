package com.allocinit.soloslumber;

import java.util.HashSet;
import java.util.Set;

import com.allocinit.bukkit.SubCommand;
import com.allocinit.bukkit.UsageException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WakeCommand extends SubCommand {
    public WakeCommand() {
        super("wake");
    }

    @Override
    public void doCommand(CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player");
            return;
        }

        if (args.length != 0)
            throw new UsageException();

        Player waker = (Player) sender;

        if (!SoloSlumber.getPlugin().allowedInWorld(waker)) {
            return;
        }

        doWake(waker);
    }

    public static void doWake(Player waker) {
        Set<Player> sleepers = new HashSet<>();

        for (String sleeperName : SoloSlumber.getPlugin().getSleepers().keySet()) {
            Player sleeper = SoloSlumber.getPlugin().getServer().getPlayer(sleeperName);
            if (sleeper != null && sleeper.getWorld() == waker.getWorld()) {
                sleeper.wakeup(true);
                sleeper.sendMessage(SoloSlumber.getPlugin().getMessage(waker, "woken_up"));
                sleepers.add(sleeper);
            }
        }

        if (!sleepers.isEmpty()) {
            waker.sendMessage(SoloSlumber.getPlugin().getMessage(null, "woke_them_up"));

            // Tell everyone else what happened
            for (Player otherPlayer : waker.getWorld().getPlayers()) {
                if (otherPlayer != waker && !sleepers.contains(otherPlayer))
                    otherPlayer.sendMessage(SoloSlumber.getPlugin().getMessage(waker, "woke_up"));
            }

            if (SoloSlumber.getPlugin().getConfig().getBoolean("wakerForcesNight"))
                SoloSlumber.getPlugin().addForcer(waker);
        }
    }

    @Override
    public void writeUsage(CommandSender player) {
        player.sendMessage(
                ChatColor.GREEN + "/soloslumber wake" + ChatColor.YELLOW + " - Wake up sleeping players in this world.");
    }
}