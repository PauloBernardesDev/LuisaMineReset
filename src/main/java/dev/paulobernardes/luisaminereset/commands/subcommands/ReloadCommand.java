package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.LuisaMineReset;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SubCommand {

    private final LuisaMineReset plugin;

    public ReloadCommand(LuisaMineReset plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin configuration.";
    }

    @Override
    public String getUsage() {
        return "/lmr reload";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.admin";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        plugin.getMessageManager().reloadMessages();

        plugin.getMessageManager().send(sender, "reload-success");
    }
}