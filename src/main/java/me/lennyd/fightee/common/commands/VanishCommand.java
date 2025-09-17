package me.lennyd.fightee.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.lennyd.fightee.common.BaseCommand;
import me.lennyd.fightee.common.Main;
import me.lennyd.fightee.common.util.Chat;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class VanishCommand extends BaseCommand {
    public static final Set<UUID> VANISHED_PLAYERS = new HashSet<>();

    @Override
    public String getName() {
        return "vanish";
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return literal(getName())
                .requires(s -> s.getSender() instanceof Player player)
                .requires(s -> s.getSender().hasPermission("fightee.command.vanish"))
                .executes(this::runCommand);
    }

    private int runCommand(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getSender();

        if (VANISHED_PLAYERS.contains(player.getUniqueId())) {
            // Un-vanish
            VANISHED_PLAYERS.remove(player.getUniqueId());

            for (Player online : Bukkit.getOnlinePlayers()) {
                // Show the player to everyone
                online.showPlayer(Main.PLUGIN, player);

                // Hide all other vanished players from non-vanished players
                if (VANISHED_PLAYERS.contains(online.getUniqueId())) {
                    if (!player.hasPermission("fightee.see.vanished")) {
                        player.hidePlayer(Main.PLUGIN, online);
                    }
                }
            }

            Chat.sendDualMessage(player, I18n.getComponent("vanish.disable"));
        } else {
            // Vanish
            VANISHED_PLAYERS.add(player.getUniqueId());

            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!VANISHED_PLAYERS.contains(online.getUniqueId())) {
                    // Hide the vanished player from all non-vanished players
                    online.hidePlayer(Main.PLUGIN, player);
                } else {
                    // Vanished players see each other
                    player.showPlayer(Main.PLUGIN, online);
                    online.showPlayer(Main.PLUGIN, player);
                }
            }

            Chat.sendDualMessage(player, I18n.getComponent("vanish.enable"));
        }

        return 1;
    }
}
