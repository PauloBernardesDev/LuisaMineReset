package dev.paulobernardes.luisaminereset.managers;

import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.Material;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBlockProvider {

    public Material getRandomMaterial(Mine mine) {

        Map<Material, Double> blocks = mine.getBlocks();

        if (blocks == null || blocks.isEmpty()) {
            return Material.STONE;
        }

        double totalPercentage = 0.0;

        for (double percentage : blocks.values()) {
            if (percentage > 0) {
                totalPercentage += percentage;
            }
        }

        if (totalPercentage <= 0) {
            return Material.STONE;
        }

        double randomValue = ThreadLocalRandom.current()
                .nextDouble(totalPercentage);

        double accumulatedPercentage = 0.0;

        for (Map.Entry<Material, Double> entry : blocks.entrySet()) {

            double percentage = entry.getValue();

            if (percentage <= 0) {
                continue;
            }

            accumulatedPercentage += percentage;

            if (randomValue < accumulatedPercentage) {
                return entry.getKey();
            }
        }

        return blocks.keySet().iterator().next();
    }
}