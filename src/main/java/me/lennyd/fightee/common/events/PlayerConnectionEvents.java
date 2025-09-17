package me.lennyd.fightee.common.events;

import me.lennyd.fightee.common.Main;
import me.lennyd.fightee.common.commands.VanishCommand;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerConnectionEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();

        if (!VanishCommand.VANISHED_PLAYERS.contains(joined.getUniqueId())) {
            for (UUID uuid : VanishCommand.VANISHED_PLAYERS) {
                Player vanished = Bukkit.getPlayer(uuid);
                if (vanished != null) {
                    joined.hidePlayer(Main.PLUGIN, vanished);
                }
            }
        }

        if (VanishCommand.VANISHED_PLAYERS.contains(joined.getUniqueId())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!VanishCommand.VANISHED_PLAYERS.contains(online.getUniqueId())) {
                    online.hidePlayer(Main.PLUGIN, joined);
                }
            }
        }

        event.joinMessage(I18n.getComponent("chat.join", event.getPlayer().getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(I18n.getComponent("chat.quit", event.getPlayer().getName()));
    }
}
