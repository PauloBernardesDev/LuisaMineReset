package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class InfoCommand implements SubCommand {

    private final MineManager mineManager;

    public InfoCommand(MineManager mineManager) {
        this.mineManager = mineManager;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Displays detailed information about a mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr info <mine>";
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

        if (args.length < 2) {
            sender.sendMessage("§cUsage: " + getUsage());
            return;
        }

        Mine mine = mineManager.getMine(args[1]);

        if (mine == null) {
            sender.sendMessage(
                    "§cMine '" + args[1] + "' was not found."
            );
            return;
        }

        boolean regionConfigured =
                mine.getPos1() != null
                        && mine.getPos2() != null;

        boolean spawnConfigured =
                mine.getSpawnLocation() != null;

        boolean blocksConfigured =
                mine.getBlocks() != null
                        && !mine.getBlocks().isEmpty();

        boolean resetConfigured =
                mine.getResetTime() > 0;

        boolean ready =
                regionConfigured
                        && spawnConfigured
                        && blocksConfigured
                        && resetConfigured;

        sender.sendMessage("");
        sender.sendMessage("§8§m----------------------------------------");
        sender.sendMessage("        §b§lMine Information");
        sender.sendMessage("");

        sender.sendMessage(
                "§7Name: §f" + mine.getName()
        );

        sender.sendMessage(
                "§7Status: "
                        + (ready
                        ? "§aReady"
                        : "§cIncomplete")
        );

        sender.sendMessage("");

        sender.sendMessage(
                "§7Region: "
                        + formatConfigured(regionConfigured)
        );

        sendLocation(
                sender,
                "Position 1",
                mine.getPos1()
        );

        sendLocation(
                sender,
                "Position 2",
                mine.getPos2()
        );

        sender.sendMessage("");

        sender.sendMessage(
                "§7Exit Location: "
                        + formatConfigured(spawnConfigured)
        );

        sendLocation(
                sender,
                "Spawn",
                mine.getSpawnLocation()
        );

        sender.sendMessage("");

        sender.sendMessage(
                "§7Automatic Reset: "
                        + formatConfigured(resetConfigured)
        );

        sender.sendMessage(
                "§7Reset Time: §f"
                        + formatTime(mine.getResetTime())
        );

        sender.sendMessage("");
        sender.sendMessage("§b§lBlock Distribution");

        if (!blocksConfigured) {

            sender.sendMessage(
                    "§8▪ §cNo blocks configured."
            );

        } else {

            for (Map.Entry<Material, Double> entry
                    : mine.getBlocks().entrySet()) {

                sender.sendMessage(
                        "§8▪ §f"
                                + formatMaterialName(
                                entry.getKey()
                        )
                                + " §7» §f"
                                + formatPercentage(
                                entry.getValue()
                        )
                                + "%"
                );
            }
        }

        sender.sendMessage("");
        sender.sendMessage("§8§m----------------------------------------");
    }

    private void sendLocation(
            CommandSender sender,
            String name,
            Location location
    ) {

        if (location == null) {
            sender.sendMessage(
                    "§8▪ §7"
                            + name
                            + ": §cNot set"
            );
            return;
        }

        String worldName =
                location.getWorld() == null
                        ? "Unknown"
                        : location.getWorld().getName();

        sender.sendMessage(
                "§8▪ §7"
                        + name
                        + ": §f"
                        + location.getBlockX()
                        + ", "
                        + location.getBlockY()
                        + ", "
                        + location.getBlockZ()
                        + " §8("
                        + worldName
                        + ")"
        );
    }

    private String formatConfigured(boolean configured) {
        return configured
                ? "§aConfigured"
                : "§cNot configured";
    }

    private String formatTime(int seconds) {

        if (seconds <= 0) {
            return "Not configured";
        }

        int days = seconds / 86400;
        int hours = (seconds % 86400) / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append("d ");
        }

        if (hours > 0) {
            result.append(hours).append("h ");
        }

        if (minutes > 0) {
            result.append(minutes).append("m ");
        }

        if (remainingSeconds > 0) {
            result.append(remainingSeconds).append("s");
        }

        return result.toString().trim();
    }

    private String formatMaterialName(Material material) {

        String[] words =
                material.name()
                        .toLowerCase()
                        .split("_");

        StringBuilder formattedName =
                new StringBuilder();

        for (String word : words) {

            if (word.isEmpty()) {
                continue;
            }

            formattedName
                    .append(
                            Character.toUpperCase(
                                    word.charAt(0)
                            )
                    )
                    .append(word.substring(1))
                    .append(" ");
        }

        return formattedName.toString().trim();
    }

    private String formatPercentage(double percentage) {

        if (percentage == Math.floor(percentage)) {
            return String.valueOf((int) percentage);
        }

        return String.valueOf(percentage);
    }
}