package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.managers.AutoResetManager;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class SetTimeCommand implements SubCommand {

    private final LuisaMineReset plugin;
    private final MineManager mineManager;
    private final MineStorage mineStorage;
    private final AutoResetManager autoResetManager;

    public SetTimeCommand(
            LuisaMineReset plugin,
            MineManager mineManager,
            MineStorage mineStorage,
            AutoResetManager autoResetManager
    ) {
        this.plugin = plugin;
        this.mineManager = mineManager;
        this.mineStorage = mineStorage;
        this.autoResetManager = autoResetManager;
    }

    @Override
    public String getName() {
        return "settime";
    }

    @Override
    public String getDescription() {
        return "Sets the automatic reset time of a mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr settime <mine> <time>";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.settime";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length != 3) {
            plugin.getMessageManager().send(
                    sender,
                    "settime-usage"
            );

            plugin.getMessageManager().send(
                    sender,
                    "settime-example"
            );

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

        int seconds = parseTime(args[2]);

        if (seconds <= 0) {
            plugin.getMessageManager().send(
                    sender,
                    "settime-invalid-time"
            );

            plugin.getMessageManager().send(
                    sender,
                    "settime-example"
            );

            return;
        }

        mine.setResetTime(seconds);
        autoResetManager.restartTimer(mine);
        mineStorage.saveMines();

        plugin.getMessageManager().send(
                sender,
                "settime-success",
                "%mine%", mine.getName(),
                "%time%", formatTime(seconds)
        );
    }

    private int parseTime(String text) {

        if (text == null || text.length() < 2) {
            return -1;
        }

        text = text.toLowerCase(Locale.ROOT);

        char unit = text.charAt(text.length() - 1);
        String numberText = text.substring(0, text.length() - 1);

        int value;

        try {
            value = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            return -1;
        }

        if (value <= 0) {
            return -1;
        }

        try {
            return switch (unit) {
                case 's' -> value;
                case 'm' -> Math.multiplyExact(value, 60);
                case 'h' -> Math.multiplyExact(value, 3600);
                case 'd' -> Math.multiplyExact(value, 86400);
                default -> -1;
            };
        } catch (ArithmeticException exception) {
            return -1;
        }
    }

    private String formatTime(int seconds) {

        if (seconds % 86400 == 0) {
            return (seconds / 86400) + "d";
        }

        if (seconds % 3600 == 0) {
            return (seconds / 3600) + "h";
        }

        if (seconds % 60 == 0) {
            return (seconds / 60) + "m";
        }

        return seconds + "s";
    }
}