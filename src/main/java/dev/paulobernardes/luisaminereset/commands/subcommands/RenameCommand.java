package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.command.CommandSender;

public class RenameCommand implements SubCommand {

    private final LuisaMineReset plugin;
    private final MineManager mineManager;
    private final MineStorage mineStorage;

    public RenameCommand(
            LuisaMineReset plugin,
            MineManager mineManager,
            MineStorage mineStorage
    ) {
        this.plugin = plugin;
        this.mineManager = mineManager;
        this.mineStorage = mineStorage;
    }

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public String getDescription() {
        return "Renames an existing mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr rename <current-name> <new-name>";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.rename";
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
        if (args.length < 3) {
            plugin.getMessageManager().send(
                    sender,
                    "rename-usage"
            );
            return;
        }

        String currentName = args[1];
        String newName = args[2];

        if (!isValidName(newName)) {
            plugin.getMessageManager().send(
                    sender,
                    "invalid-mine-name"
            );
            return;
        }

        if (!mineManager.mineExists(currentName)) {
            plugin.getMessageManager().send(
                    sender,
                    "mine-not-found",
                    "%mine%", currentName
            );
            return;
        }

        if (mineManager.mineExists(newName)) {
            plugin.getMessageManager().send(
                    sender,
                    "mine-already-exists",
                    "%mine%", newName
            );
            return;
        }

        boolean renamed = mineManager.renameMine(
                currentName,
                newName
        );

        if (!renamed) {
            plugin.getMessageManager().send(
                    sender,
                    "rename-failed",
                    "%mine%", currentName
            );
            return;
        }

        mineStorage.saveMines();

        plugin.getMessageManager().send(
                sender,
                "mine-renamed",
                "%old-name%", currentName,
                "%new-name%", newName
        );
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z0-9_-]+");
    }
}