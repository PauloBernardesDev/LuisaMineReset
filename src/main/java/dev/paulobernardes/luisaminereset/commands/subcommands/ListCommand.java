package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.command.CommandSender;

public class ListCommand implements SubCommand {

    private final MineManager mineManager;

    public ListCommand(MineManager mineManager) {
        this.mineManager = mineManager;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Displays all registered mines.";
    }

    @Override
    public String getUsage() {
        return "/lmr list";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.list";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (mineManager.getAllMines().isEmpty()) {
            sender.sendMessage("§7There are no registered mines.");
            return;
        }

        sender.sendMessage("");
        sender.sendMessage("§b§lRegistered mines:");

        for (Mine mine : mineManager.getAllMines()) {
            sender.sendMessage("§8- §f" + mine.getName());
        }

        sender.sendMessage("");
        sender.sendMessage(
                "§7Total: §f" + mineManager.getAllMines().size()
        );
    }
}