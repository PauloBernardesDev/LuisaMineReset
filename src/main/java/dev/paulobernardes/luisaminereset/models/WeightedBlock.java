package dev.paulobernardes.luisaminereset.models;

import org.bukkit.Material;

public class WeightedBlock {

    private final Material material;
    private double chance;

    public WeightedBlock(Material material, double chance) {
        this.material = material;
        this.chance = chance;
    }

    public Material getMaterial() {
        return material;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}