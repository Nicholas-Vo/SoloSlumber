package com.allocinit.soloslumber;

import org.bukkit.entity.Player;

public class NightForcer {
    public long nextAllowedWake;
    public Player waker;

    public NightForcer(Player waker) {
        this.waker = waker;
        this.nextAllowedWake = (waker.getWorld().getFullTime() / 24000) * 24000 + 24000;
    }
}