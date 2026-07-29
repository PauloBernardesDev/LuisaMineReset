package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.commands.CommandManager;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {

    private final LuisaMineReset plugin;
    private final CommandManager commandManager;

    public HelpCommand(
            LuisaMineReset plugin,
            CommandManager commandManager
    ) {
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Displays the available commands.";
    }

    @Override
    public String getUsage() {
        return "/lmr help";
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        plugin.getMessageManager().send(sender, "help-header");
        sender.sendMessage("");

        for (SubCommand subCommand : commandManager.getCommands()) {

            String permission = subCommand.getPermission();

            if (permission != null
                    && !permission.isBlank()
                    && !sender.hasPermission(permission)) {
                continue;
            }

            plugin.getMessageManager().send(
                    sender,
                    "help-command-format",
                    "%usage%", subCommand.getUsage()
            );

            plugin.getMessageManager().send(
                    sender,
                    "help-description-" + subCommand.getName().toLowerCase()
            );

            sender.sendMessage("");
        }
    }
}