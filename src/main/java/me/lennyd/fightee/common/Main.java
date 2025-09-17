package me.lennyd.fightee.common;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.lennyd.fightee.common.commands.*;
import me.lennyd.fightee.common.events.PlayerChatEvents;
import me.lennyd.fightee.common.events.PlayerConnectionEvents;
import me.lennyd.fightee.common.events.PlayerDeathEvents;
import me.lennyd.fightee.common.util.I18n;
import me.lennyd.fightee.common.util.PlayerNameUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    public static Logger LOGGER;
    public static Main PLUGIN;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PLUGIN = this;
        LOGGER = getLogger();
        I18n.setup();

        // Commands
        BaseCommand.registerCommand(new VanishCommand());
        BaseCommand.registerCommand(new GameModeCommand());
        BaseCommand.registerCommand(new ReviveCommand());
        BaseCommand.registerCommand(new RequestModCommand());
        BaseCommand.registerCommand(new RequestHostCommand());
        BaseCommand.registerCommand(new TeleportCommand());
        BaseCommand.registerCommand(new WhisperCommand());

        // Register Commands
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, e -> {
            for (BaseCommand command : BaseCommand.getAllRegisteredCommands()) {
                command.register(e.registrar());
            }
        });

        // Gamerules
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
        }

        // Events
        getServer().getPluginManager().registerEvents(new PlayerConnectionEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatEvents(), this);

        // Schedules
        Bukkit.getScheduler().runTaskTimer(this, PlayerNameUtil::onUpdate, 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendPlayerListHeaderAndFooter(
                        I18n.getComponent("tablist.header"),
                        I18n.getComponent("tablist.footer", p.getPing(), p.getStatistic(Statistic.PLAYER_KILLS))
                );
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
