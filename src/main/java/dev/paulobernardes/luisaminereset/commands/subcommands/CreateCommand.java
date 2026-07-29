package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.messages.Messages;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.command.CommandSender;

public class CreateCommand implements SubCommand {

    private final MineManager mineManager;
    private final MineStorage mineStorage;

    public CreateCommand(
            MineManager mineManager,
            MineStorage mineStorage
    ) {
        this.mineManager = mineManager;
        this.mineStorage = mineStorage;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates a new mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr create <name>";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.create";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(
            CommandSender sender,
            String[] args
    ) {
        if (args.length < 2) {
            Messages.usage(sender, getUsage());
            return;
        }

        String mineName = args[1];

        if (mineName.length() > 32) {
            Messages.invalidMineNameLength(sender);
            return;
        }

        if (!mineManager.createMine(mineName)) {
            Messages.mineAlreadyExists(
                    sender,
                    mineName
            );

            return;
        }

        mineStorage.saveMines();

        Messages.mineCreated(sender, mineName);
        Messages.mineSaved(sender);
    }
}