package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.messages.Messages;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.command.CommandSender;

public class DeleteCommand implements SubCommand {

    private final MineManager mineManager;
    private final MineStorage mineStorage;

    public DeleteCommand(
            MineManager mineManager,
            MineStorage mineStorage
    ) {
        this.mineManager = mineManager;
        this.mineStorage = mineStorage;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Deletes an existing mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr delete <name>";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.delete";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Messages.usage(sender, getUsage());
            return;
        }

        String mineName = args[1];

        if (!mineManager.deleteMine(mineName)) {
            sender.sendMessage(
                    "§cMine §f" + mineName + " §cwas not found."
            );
            return;
        }

        mineStorage.saveMines();

        sender.sendMessage(
                "§aMine §f" + mineName + " §adeleted successfully!"
        );
    }
}