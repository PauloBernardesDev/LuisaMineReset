package dev.paulobernardes.luisaminereset.commands;

import dev.paulobernardes.luisaminereset.commands.subcommands.RenameCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.ReloadCommand;
import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.commands.subcommands.InfoCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.SetSpawnCommand;
import dev.paulobernardes.luisaminereset.managers.AutoResetManager;
import dev.paulobernardes.luisaminereset.commands.subcommands.SetTimeCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.SetBlockCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.CreateCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.DeleteCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.HelpCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.ListCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.ResetCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.SetRegionCommand;
import dev.paulobernardes.luisaminereset.commands.subcommands.SubCommand;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.managers.ResetManager;
import dev.paulobernardes.luisaminereset.storage.MineStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager {

    private final Map<String, SubCommand> commands =
            new LinkedHashMap<>();

    public CommandManager(
            LuisaMineReset plugin,
            MineManager mineManager,
            MineStorage mineStorage,
            ResetManager resetManager,
            AutoResetManager autoResetManager
    ) {
        register(new CreateCommand(mineManager, mineStorage));
        register(new DeleteCommand(mineManager, mineStorage));
        register(new RenameCommand(
                plugin,
                mineManager,
                mineStorage
        ));
        register(new ListCommand(mineManager));
        register(new SetRegionCommand(mineManager, mineStorage));
        register(new SetBlockCommand(
                plugin,
                mineManager,
                mineStorage
        ));
        register(new SetTimeCommand(
                plugin,
                mineManager,
                mineStorage,
                autoResetManager
        ));
        register(new SetSpawnCommand(
                mineManager,
                mineStorage
        ));
        register(new InfoCommand(
                mineManager
        ));
        register(new ReloadCommand(plugin));
        register(new ResetCommand(mineManager, resetManager));
        register(new HelpCommand(plugin, this));
    }

    private void register(SubCommand command) {
        commands.put(
                command.getName().toLowerCase(),
                command
        );
    }

    public SubCommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    public Collection<SubCommand> getCommands() {
        return commands.values();
    }
}