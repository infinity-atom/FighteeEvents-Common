package me.lennyd.fightee.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.lennyd.fightee.common.BaseCommand;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class RequestHostCommand extends BaseCommand {
    @Override
    public String getName() {
        return "requesthost";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return literal(getName())
                .requires(s -> s.getSender() instanceof Player)
                .requires(s -> s.getSender().hasPermission("fightee.command.requesthost"))
                .executes((ctx) -> {
                    Player sender = (Player) ctx.getSource().getSender();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("fightee.name.host")) {
                            player.sendMessage(I18n.getComponent("request.host", sender.getName()));
                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        }
                    }

                    sender.sendMessage(I18n.getComponent("request.success"));

                    return 1;
                });
    }
}
