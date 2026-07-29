package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements SubCommand {

    private final MineManager mineManager;
    private final MineStorage mineStorage;

    public SetSpawnCommand(
            MineManager mineManager,
            MineStorage mineStorage
    ) {
        this.mineManager = mineManager;
        this.mineStorage = mineStorage;
    }

    @Override
    public String getName() {
        return "setspawn";
    }

    @Override
    public String getDescription() {
        return "Sets the exit location of a mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr setspawn <mine>";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.admin";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(
            CommandSender sender,
            String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    "§cThis command can only be used by a player."
            );
            return;
        }

        if (args.length < 2) {
            player.sendMessage(
                    "§cUsage: " + getUsage()
            );
            return;
        }

        String mineName = args[1];

        Mine mine = mineManager.getMine(mineName);

        if (mine == null) {
            player.sendMessage(
                    "§cMine '" + mineName + "' was not found."
            );
            return;
        }

        mine.setSpawnLocation(
                player.getLocation().clone()
        );

        mineStorage.saveMines();

        player.sendMessage(
                "§aThe exit location for mine '"
                        + mine.getName()
                        + "' has been set."
        );
    }
}