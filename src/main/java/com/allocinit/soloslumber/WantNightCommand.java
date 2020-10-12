package com.allocinit.soloslumber;

import com.allocinit.bukkit.SubCommand;
import com.allocinit.bukkit.UsageException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WantNightCommand extends SubCommand {
    public WantNightCommand() {
        super("wantnight");
    }

    @Override
    public void doCommand(CommandSender sender, String[] args) throws Exception {
        if (!SoloSlumber.getPlugin().getConfig().getBoolean("wakerForcesNight")) {
            throw new UsageException();
        }

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

        // First make sure everyone is awake already
        WakeCommand.doWake(waker);

        // Now add the waker
        if (SoloSlumber.getPlugin().getConfig().getBoolean("wakerForcesNight"))
            SoloSlumber.getPlugin().addForcer(waker);

        waker.sendMessage(SoloSlumber.getPlugin().getMessage(waker, "night_requested"));

        // Tell everyone else what happened
        SoloSlumber.getPlugin().tellEveryoneElse(waker, SoloSlumber.getPlugin().getMessage(waker, "wants_night"));
    }

    @Override
    public void writeUsage(CommandSender player) {
        if (SoloSlumber.getPlugin().getConfig().getBoolean("wakerForcesNight")) {
            player.sendMessage(ChatColor.GREEN + "/soloslumber wantnight" + ChatColor.YELLOW
                    + " - Force night for the next night cycle.");
        }
    }
}