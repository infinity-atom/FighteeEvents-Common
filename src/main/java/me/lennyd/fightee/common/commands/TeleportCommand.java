package me.lennyd.fightee.common.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.lennyd.fightee.common.BaseCommand;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class TeleportCommand extends BaseCommand {
    @Override
    public String getName() {
        return "tp";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return literal(getName())
                .requires(s -> s.getSender() instanceof Player)
                .requires(s -> s.getSender().hasPermission("fightee.command.teleport"))
                .then(
                    argument("target", StringArgumentType.word())
                            .suggests((s, b) -> {
                                String input = b.getRemaining().toLowerCase();
                                Bukkit.getOnlinePlayers().stream()
                                        .map(Player::getName)
                                        .filter(name -> name.toLowerCase().startsWith(input))
                                        .forEach(b::suggest);

                                return b.buildFuture();
                            })
                            .executes(ctx -> {
                                Player sender = (Player) ctx.getSource().getSender();
                                Player target = Bukkit.getPlayerExact(StringArgumentType.getString(ctx, "target"));

                                if (target == null) {
                                    ctx.getSource().getSender().sendMessage(I18n.getComponent("generic.unknown_player"));
                                    return 1;
                                }

                                sender.teleport(target);

                                return 1;
                            })
                );
    }
}
