package dev.paulobernardes.luisaminereset.messages;

import org.bukkit.command.CommandSender;

public final class Messages {

    private static final String PREFIX =
            "§8[§bLuisaMineReset§8] §r";

    private Messages() {
    }

    public static void send(
            CommandSender sender,
            String message
    ) {
        sender.sendMessage(PREFIX + message);
    }

    public static void unknownCommand(CommandSender sender) {
        send(
                sender,
                "§cUnknown subcommand. "
                        + "§7Use §f/lmr help§7."
        );
    }

    public static void noPermission(CommandSender sender) {
        send(
                sender,
                "§cYou don't have permission "
                        + "to execute this command."
        );
    }

    public static void playerOnly(CommandSender sender) {
        send(
                sender,
                "§cOnly players can execute this command."
        );
    }

    public static void usage(
            CommandSender sender,
            String usage
    ) {
        send(sender, "§cUsage: §f" + usage);
    }

    public static void mineCreated(
            CommandSender sender,
            String mineName
    ) {
        send(
                sender,
                "§aMine §f" + mineName
                        + " §acreated successfully!"
        );
    }

    public static void mineAlreadyExists(
            CommandSender sender,
            String mineName
    ) {
        send(
                sender,
                "§cA mine named §f" + mineName
                        + " §calready exists."
        );
    }

    public static void invalidMineNameLength(
            CommandSender sender
    ) {
        send(
                sender,
                "§cThe mine name cannot exceed "
                        + "32 characters."
        );
    }

    public static void mineSaved(CommandSender sender) {
        send(
                sender,
                "§7The mine has been saved to mines.yml."
        );
    }
}