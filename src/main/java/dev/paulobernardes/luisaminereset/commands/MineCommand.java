package dev.paulobernardes.luisaminereset.commands;

import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.commands.subcommands.SubCommand;
import dev.paulobernardes.luisaminereset.managers.AutoResetManager;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.managers.ResetManager;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MineCommand implements CommandExecutor, TabCompleter {

    private final LuisaMineReset plugin;
    private final CommandManager commandManager;
    private final MineManager mineManager;

    public MineCommand(
            LuisaMineReset plugin,
            MineManager mineManager,
            MineStorage mineStorage,
            ResetManager resetManager,
            AutoResetManager autoResetManager
    ) {
        this.plugin = plugin;
        this.mineManager = mineManager;

        this.commandManager = new CommandManager(
                plugin,
                mineManager,
                mineStorage,
                resetManager,
                autoResetManager
        );
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {

        if (args.length == 0) {
            SubCommand helpCommand =
                    commandManager.getCommand("help");

            if (helpCommand != null) {
                helpCommand.execute(sender, args);
            }

            return true;
        }

        SubCommand subCommand =
                commandManager.getCommand(args[0]);

        if (subCommand == null) {
            plugin.getMessageManager().send(
                    sender,
                    "unknown-command"
            );
            return true;
        }

        String permission = subCommand.getPermission();

        if (permission != null
                && !permission.isBlank()
                && !sender.hasPermission(permission)) {

            plugin.getMessageManager().send(
                    sender,
                    "no-permission"
            );
            return true;
        }

        if (subCommand.isPlayerOnly()
                && !(sender instanceof Player)) {

            plugin.getMessageManager().send(
                    sender,
                    "player-only"
            );
            return true;
        }

        subCommand.execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            String typed = args[0].toLowerCase(Locale.ROOT);

            for (SubCommand subCommand : commandManager.getCommands()) {
                String permission = subCommand.getPermission();

                boolean hasPermission =
                        permission == null
                                || permission.isBlank()
                                || sender.hasPermission(permission);

                if (!hasPermission) {
                    continue;
                }

                String commandName = subCommand.getName();

                if (commandName.toLowerCase(Locale.ROOT)
                        .startsWith(typed)) {

                    suggestions.add(commandName);
                }
            }

            return suggestions;
        }

        if (args.length == 2) {
            String subCommandName =
                    args[0].toLowerCase(Locale.ROOT);

            if (usesMineName(subCommandName)) {
                String typedMineName =
                        args[1].toLowerCase(Locale.ROOT);

                for (Mine mine : mineManager.getAllMines()) {

                    String mineName = mine.getName();

                    if (mineName.toLowerCase(Locale.ROOT)
                            .startsWith(typedMineName)) {

                        suggestions.add(mineName);
                    }
                }
            }
        }

        return suggestions;
    }

    private boolean usesMineName(String commandName) {
        return commandName.equals("delete")
                || commandName.equals("rename")
                || commandName.equals("setregion")
                || commandName.equals("setblock")
                || commandName.equals("settime")
                || commandName.equals("setspawn")
                || commandName.equals("reset")
                || commandName.equals("info");
    }
}