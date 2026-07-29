package dev.paulobernardes.luisaminereset.models;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Map;

public class Mine {

    private String name;

    private Location pos1;
    private Location pos2;

    private int resetTime;
    private Location spawnLocation;

    private final Map<Material, Double> blocks;

    public Mine(String name) {
        this.name = name;
        this.blocks = new LinkedHashMap<>();
        this.resetTime = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public int getResetTime() {
        return resetTime;
    }

    public void setResetTime(int resetTime) {
        this.resetTime = resetTime;
    }

    public Map<Material, Double> getBlocks() {
        return blocks;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}