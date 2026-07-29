package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.managers.ResetManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.command.CommandSender;

public class ResetCommand implements SubCommand {

    private final MineManager mineManager;
    private final ResetManager resetManager;

    public ResetCommand(
            MineManager mineManager,
            ResetManager resetManager
    ) {
        this.mineManager = mineManager;
        this.resetManager = resetManager;
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Resets a mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr reset <mine>";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.reset";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§cUsage: " + getUsage());
            return;
        }

        String mineName = args[1];

        Mine mine = mineManager.getMine(mineName);

        if (mine == null) {
            sender.sendMessage(
                    "§cMine §f" + mineName + " §cdoes not exist."
            );
            return;
        }

        sender.sendMessage(
                "§eResetting mine §f" + mine.getName() + "§e..."
        );

        int changedBlocks = resetManager.resetMine(mine);

        if (changedBlocks == -1) {
            sender.sendMessage(
                    "§cThis mine does not have a valid region."
            );
            return;
        }

        sender.sendMessage(
                "§aMine §f" + mine.getName()
                        + " §areset successfully!"
        );

        sender.sendMessage(
                "§7Blocks changed: §f" + changedBlocks
        );
    }
}