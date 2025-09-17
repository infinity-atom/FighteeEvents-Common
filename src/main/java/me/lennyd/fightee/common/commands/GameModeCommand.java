package me.lennyd.fightee.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.lennyd.fightee.common.BaseCommand;
import me.lennyd.fightee.common.util.Chat;
import me.lennyd.fightee.common.util.I18n;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class GameModeCommand extends BaseCommand {
    @Override
    public String getName() {
        return "gamemode";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return literal(getName())
                .requires(s -> s.getSender() instanceof Player)
                .requires(s -> s.getSender().hasPermission("fightee.command.gamemode"))
                .then(literal("creative").executes(this::creative))
                .then(literal("spectator").executes(this::spectator))
                .then(literal("c").executes(this::creative))
                .then(literal("s").executes(this::spectator));
    }

    private int spectator(CommandContext<CommandSourceStack> ctx) {
        Player sender = (Player) ctx.getSource().getSender();
        sender.setGameMode(GameMode.SPECTATOR);
        Chat.sendDualMessage(sender, I18n.getComponent("gamemode.spectator"));
        return 1;
    }

    private int creative(CommandContext<CommandSourceStack> ctx) {
        Player sender = (Player) ctx.getSource().getSender();
        sender.setGameMode(GameMode.CREATIVE);
        Chat.sendDualMessage(sender, I18n.getComponent("gamemode.creative"));
        return 1;
    }
}
