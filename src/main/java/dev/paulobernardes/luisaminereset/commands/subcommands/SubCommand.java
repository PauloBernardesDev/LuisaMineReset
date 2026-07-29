package dev.paulobernardes.luisaminereset.commands.subcommands;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    String getName();

    String getDescription();

    String getUsage();

    String getPermission();

    boolean isPlayerOnly();

    void execute(CommandSender sender, String[] args);

}