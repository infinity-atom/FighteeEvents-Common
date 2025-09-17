package me.lennyd.fightee.common.events;

import me.lennyd.fightee.common.Main;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathEvents implements Listener {
    private final Map<UUID, Location> deathPositions = new HashMap<>();

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.deathMessage(I18n.getComponent("chat.death", event.getDeathMessage()));

        // Since keepinv is enabled to save a backup of the inventory
        PlayerInventory inv = event.getPlayer().getInventory();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);

            if (item != null && item.getType() != Material.AIR) {
                event.getPlayer().getWorld().dropItemNaturally(
                        event.getPlayer().getLocation(),
                        item
                );
            }
        }

        Location deathPos = event.getPlayer().getLocation();
        event.getPlayer().getWorld().strikeLightningEffect(deathPos);

        deathPositions.put(event.getPlayer().getUniqueId(), deathPos);
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location location = deathPositions.get(event.getPlayer().getUniqueId());
        if (location != null) {
            Bukkit.getScheduler().runTaskLater(Main.PLUGIN, () -> {
                event.getPlayer().teleport(location);
            }, 1L);
        }
    }
}
