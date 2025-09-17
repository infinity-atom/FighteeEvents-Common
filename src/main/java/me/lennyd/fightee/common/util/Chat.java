package me.lennyd.fightee.common.util;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Chat {
    public static void sendDualMessage(Player player, Component message) {
        player.sendMessage(message);
        player.sendActionBar(message);
    }
}
