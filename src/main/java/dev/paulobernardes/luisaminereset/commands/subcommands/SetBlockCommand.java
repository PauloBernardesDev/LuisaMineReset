package dev.paulobernardes.luisaminereset.commands.subcommands;

import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class SetBlockCommand implements SubCommand {

    private final LuisaMineReset plugin;
    private final MineManager mineManager;
    private final MineStorage mineStorage;

    public SetBlockCommand(
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
        return "setblock";
    }

    @Override
    public String getDescription() {
        return "Sets the block composition of a mine.";
    }

    @Override
    public String getUsage() {
        return "/lmr setblock <mine> <block> <percentage> [block percentage...]";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.setblock";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 4) {
            plugin.getMessageManager().send(
                    sender,
                    "setblock-usage",
                    "%usage%",
                    getUsage()
            );
            return;
        }

        if (args.length % 2 != 0) {
            plugin.getMessageManager().send(
                    sender,
                    "setblock-missing-percentage"
            );

            plugin.getMessageManager().send(
                    sender,
                    "setblock-example"
            );
            return;
        }

        String mineName = args[1];
        Mine mine = mineManager.getMine(mineName);

        if (mine == null) {
            plugin.getMessageManager().send(
                    sender,
                    "mine-not-found",
                    "%mine%",
                    mineName
            );
            return;
        }

        Map<Material, Double> newBlocks =
                new LinkedHashMap<>();

        double totalPercentage = 0.0;

        for (int index = 2; index < args.length; index += 2) {

            String materialText = args[index];

            String percentageText = args[index + 1]
                    .replace("%", "")
                    .replace(",", ".");

            Material material =
                    Material.matchMaterial(materialText);

            if (material == null || !material.isBlock()) {
                plugin.getMessageManager().send(
                        sender,
                        "setblock-invalid-block",
                        "%block%",
                        materialText
                );
                return;
            }

            if (newBlocks.containsKey(material)) {
                plugin.getMessageManager().send(
                        sender,
                        "setblock-duplicate-block",
                        "%block%",
                        material.name()
                                .toLowerCase(Locale.ROOT)
                );
                return;
            }

            double percentage;

            try {
                percentage =
                        Double.parseDouble(percentageText);
            } catch (NumberFormatException exception) {
                plugin.getMessageManager().send(
                        sender,
                        "setblock-invalid-percentage",
                        "%percentage%",
                        args[index + 1]
                );
                return;
            }

            if (!Double.isFinite(percentage)
                    || percentage <= 0
                    || percentage > 100) {

                plugin.getMessageManager().send(
                        sender,
                        "setblock-percentage-range"
                );
                return;
            }

            newBlocks.put(material, percentage);
            totalPercentage += percentage;
        }

        if (Math.abs(totalPercentage - 100.0) > 0.0001) {
            plugin.getMessageManager().send(
                    sender,
                    "setblock-total-invalid",
                    "%total%",
                    formatPercentage(totalPercentage)
            );
            return;
        }

        mine.getBlocks().clear();
        mine.getBlocks().putAll(newBlocks);

        mineStorage.saveMines();

        plugin.getMessageManager().send(
                sender,
                "setblock-saved",
                "%mine%",
                mine.getName()
        );

        newBlocks.forEach((material, percentage) ->
                plugin.getMessageManager().send(
                        sender,
                        "setblock-composition-line",
                        "%block%",
                        material.name()
                                .toLowerCase(Locale.ROOT),
                        "%percentage%",
                        formatPercentage(percentage)
                )
        );
    }

    private String formatPercentage(double percentage) {
        return BigDecimal.valueOf(percentage)
                .stripTrailingZeros()
                .toPlainString();
    }
}