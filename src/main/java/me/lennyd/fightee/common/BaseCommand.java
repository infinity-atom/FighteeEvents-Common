package me.lennyd.fightee.common;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand {
    private static final List<BaseCommand> commands = new ArrayList<>();

    public abstract String getName();
    public abstract LiteralArgumentBuilder<CommandSourceStack> build();
    public void register(Commands registrar) { registrar.register(build().build()); }

    public static void registerCommand(BaseCommand command) {
        commands.add(command);
    }
    public static List<BaseCommand> getAllRegisteredCommands() {
        return commands;
    }
}
