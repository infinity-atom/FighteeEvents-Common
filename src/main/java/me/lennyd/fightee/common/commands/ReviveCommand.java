package me.lennyd.fightee.common.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.lennyd.fightee.common.BaseCommand;
import me.lennyd.fightee.common.util.Chat;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class ReviveCommand extends BaseCommand {
    @Override
    public String getName() {
        return "revive";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return literal(getName())
                .requires(s -> s.getSender() instanceof Player)
                .requires(s -> s.getSender().hasPermission("fightee.command.revive"))
                .then(argument("player", StringArgumentType.word())
                        .suggests((s, b) -> {
                            String input = b.getRemaining().toLowerCase();
                            Bukkit.getOnlinePlayers().stream()
                                    .map(Player::getName)
                                    .filter(name -> name.toLowerCase().startsWith(input))
                                    .forEach(b::suggest);

                            return b.buildFuture();
                        })
                        .executes(this::revive));
    }

    private int revive(CommandContext<CommandSourceStack> ctx) {
        Player sender = (Player) ctx.getSource().getSender();
        Player revivedPlayer = Bukkit.getPlayerExact(StringArgumentType.getString(ctx, "player"));

        if (revivedPlayer == null) {
            ctx.getSource().getSender().sendMessage(I18n.getComponent("generic.unknown_player"));
            return 1;
        }

        revivedPlayer.teleport(new Location(
                sender.getWorld(),
                sender.getLocation().getX(),
                sender.getLocation().getY(),
                sender.getLocation().getZ()
        ));

        revivedPlayer.setGameMode(GameMode.SURVIVAL);

        Chat.sendDualMessage(sender, I18n.getComponent("revive.success", revivedPlayer.getName()));
        Bukkit.broadcast(I18n.getComponent("chat.announcement",
                I18n.get("revive.announcement", revivedPlayer.getName())));

        return 1;
    }
}
