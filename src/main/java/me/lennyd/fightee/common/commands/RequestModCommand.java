package me.lennyd.fightee.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.lennyd.fightee.common.BaseCommand;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class RequestModCommand extends BaseCommand {
    @Override
    public String getName() {
        return "requestmod";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return literal(getName())
                .requires(s -> s.getSender() instanceof Player)
                .requires(s -> s.getSender().hasPermission("fightee.command.requestmod"))
                .executes((ctx) -> {
                    Player sender = (Player) ctx.getSource().getSender();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("fightee.name.mod")) {
                            player.sendMessage(I18n.getComponent("request.mod", sender.getName()));
                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        }
                    }

                    sender.sendMessage(I18n.getComponent("request.success"));

                    return 1;
                });
    }
}
