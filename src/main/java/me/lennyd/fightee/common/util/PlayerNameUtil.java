package me.lennyd.fightee.common.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerNameUtil {
    public static void onUpdate() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Check player permissions
            if (player.hasPermission("fightee.name.owner") || player.getName().equalsIgnoreCase("F1ghtee")) {
                setName(player, I18n.getComponent("names.owner", player.getName()));
                continue;
            }

            if (player.hasPermission("fightee.name.dev")) {
                setName(player, I18n.getComponent("names.dev", player.getName()));
                continue;
            }

            if (player.hasPermission("fightee.name.host")) {
                setName(player, I18n.getComponent("names.host", player.getName()));
                continue;
            }

            if (player.hasPermission("fightee.name.mod")) {
                setName(player, I18n.getComponent("names.mod", player.getName()));
            }
        }
    }

    private static void setName(Player player, Component name) {
        player.displayName(name);
        player.customName(name);
        player.playerListName(name);
        player.setCustomNameVisible(true);
    }
}
