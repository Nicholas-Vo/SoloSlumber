package com.allocinit.soloslumber;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BedListener implements Listener {
    private SoloSlumber soloSlumber;

    public BedListener(SoloSlumber soloSlumber) {
        this.soloSlumber = soloSlumber;
    }

    @EventHandler
    public void onBedEnter(final PlayerBedEnterEvent e) {
        if (e.getBedEnterResult() != BedEnterResult.OK)
            return;

        // TODO - Should we schedule this a tick from now to see if the
        // player DOES enter the bed? e.g. bed enter was denied by some
        // other plugin that runs after us.

        final Player player = e.getPlayer();
        final World world = player.getWorld();

        if (!soloSlumber.getConfig().getStringList("worlds").contains(world.getName())) {
            return;
        }

        boolean foundOtherPlayers = false;

        for (Player otherPlayer : world.getPlayers()) {
            if (otherPlayer == player)
                continue;

            if (otherPlayer.getWorld() != world)
                continue;

            foundOtherPlayers = true;

            TextComponent message = new TextComponent(
                    TextComponent.fromLegacyText(getMessage(player, "sleep_warning")));

            TextComponent clickHere = new TextComponent(TextComponent.fromLegacyText(getMessage(player, "click_here")));
            clickHere.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/soloslumber wake"));
            clickHere.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(getMessage(player, "wake_them_up")).create()));

            message.addExtra(clickHere);

            otherPlayer.spigot().sendMessage(message);
        }

        if (!foundOtherPlayers)
            return;

        BukkitTask task = Bukkit.getServer().getScheduler().runTaskLater(soloSlumber, () -> {
            makeDay(world);
        }, soloSlumber.getConfig().getLong("napTime"));

        this.soloSlumber.getSleepers().put(player.getName(), task);
    }

    @EventHandler
    public void onBedLeave(final PlayerBedLeaveEvent e) {
        final Player player = e.getPlayer();

        BukkitTask task = this.soloSlumber.getSleepers().remove(player.getName());
        if (task != null) {
            task.cancel();
        }
    }

    private String getMessage(Player sender, String msgKey) {
        String msg = soloSlumber.getConfig().getString("messages." + msgKey);

        if (sender != null && msg != null)
            msg = msg.replace("%player_name%", sender.getName());

        return msg;
    }

    private void makeDay(World world) {
        if (isNight(world) || world.hasStorm() || world.isThundering()) {
            world.setTime(0L);
            world.setStorm(false);
            world.setThundering(false);

            String msg = getMessage(null, "night_skipped");

            if (msg != null && !msg.isEmpty()) {
                for (Player p : world.getPlayers()) {
                    p.sendMessage(msg);
                }
            }
        }
    }

    private boolean isNight(World world) {
        return world.getTime() >= 12541 && world.getTime() <= 23458;
    }
}