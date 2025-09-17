package me.lennyd.fightee.common.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lennyd.fightee.common.util.I18n;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatEvents implements Listener {
    @EventHandler
    public void onPlayerChatAsync(AsyncChatEvent event) {
        event.setCancelled(true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(event.getPlayer().displayName()
                    .append(I18n.getComponent("chat.chat", PlainTextComponentSerializer.plainText().serialize(event.message()))));
        }
    }
}
