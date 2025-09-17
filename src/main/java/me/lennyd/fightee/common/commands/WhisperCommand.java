package me.lennyd.fightee.common.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.lennyd.fightee.common.BaseCommand;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class WhisperCommand extends BaseCommand {
    @Override
    public String getName() {
        return "message";
    }

    private LiteralArgumentBuilder<CommandSourceStack> root(String cmd) {
        return literal(cmd)
                .requires(s -> s.getSender() instanceof Player)
                .requires(s -> s.getSender().hasPermission("fightee.command.message"))
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
                                .then(
                                        argument("message", StringArgumentType.greedyString())
                                                .executes(ctx -> {
                                                    Player sender = (Player) ctx.getSource().getSender();
                                                    Player target = Bukkit.getPlayerExact(StringArgumentType.getString(ctx, "target"));
                                                    String message = StringArgumentType.getString(ctx, "message");

                                                    if (target == null) {
                                                        sender.sendMessage(I18n.getComponent("generic.unknown_player"));
                                                        return 1;
                                                    }

                                                    sender.sendMessage(I18n.getComponent("chat.whisper_outbound", target.getName(), message));
                                                    target.sendMessage(I18n.getComponent("chat.whisper_inbound", sender.getName(), message));

                                                    return 1;
                                                })
                                )
                );
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return root(getName());
    }

    @Override
    public void register(Commands registrar) {
        registrar.register(root(getName()).build());
        registrar.register(root("msg").build());
        registrar.register(root("tell").build());
        registrar.register(root("w").build());
        registrar.register(root("whisper").build());
        registrar.register(root("dm").build());
        registrar.register(root("pm").build());
    }
}
