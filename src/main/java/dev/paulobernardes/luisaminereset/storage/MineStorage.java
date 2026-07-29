package dev.paulobernardes.luisaminereset.storage;

import org.bukkit.configuration.file.FileConfiguration;
import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MineStorage {

    private final LuisaMineReset plugin;
    private final MineManager mineManager;

    private final File minesFile;
    private YamlConfiguration minesConfig;

    public MineStorage(
            LuisaMineReset plugin,
            MineManager mineManager
    ) {
        this.plugin = plugin;
        this.mineManager = mineManager;

        this.minesFile = new File(plugin.getDataFolder(), "mines.yml");
        this.minesConfig = YamlConfiguration.loadConfiguration(minesFile);
    }

    public void loadMines() {
        minesConfig = YamlConfiguration.loadConfiguration(minesFile);

        ConfigurationSection minesSection =
                minesConfig.getConfigurationSection("mines");

        if (minesSection == null) {
            plugin.getLogger().info("No saved mines were found.");
            return;
        }

        int loadedMines = 0;

        for (String mineKey : minesSection.getKeys(false)) {
            String path = "mines." + mineKey;

            String mineName = minesConfig.getString(
                    path + ".name",
                    mineKey
            );

            boolean created = mineManager.createMine(mineName);

            if (!created) {
                plugin.getLogger().warning(
                        "Could not load mine '" + mineName
                                + "' because it already exists."
                );
                continue;
            }

            Mine mine = mineManager.getMine(mineName);

            if (mine == null) {
                continue;
            }

            mine.setResetTime(
                    minesConfig.getInt(path + ".reset-time", 0)
            );

            mine.setSpawnLocation(
                    loadLocation(
                            minesConfig,
                            path + ".spawn"
                    )
            );

            mine.setPos1(loadLocation(path + ".pos1"));
            mine.setPos2(loadLocation(path + ".pos2"));

            ConfigurationSection blocksSection =
                    minesConfig.getConfigurationSection(
                            path + ".blocks"
                    );

            if (blocksSection != null) {
                for (String materialName : blocksSection.getKeys(false)) {
                    Material material = Material.matchMaterial(materialName);

                    if (material == null || !material.isBlock()) {
                        plugin.getLogger().warning(
                                "Invalid material '" + materialName
                                        + "' in mine '" + mineName + "'."
                        );
                        continue;
                    }

                    double percentage =
                            blocksSection.getDouble(materialName);

                    mine.getBlocks().put(material, percentage);
                }
            }

            loadedMines++;
        }

        plugin.getLogger().info(
                loadedMines + " mine(s) loaded successfully."
        );
    }

    public void saveMines() {
        YamlConfiguration newConfig = new YamlConfiguration();

        for (Mine mine : mineManager.getAllMines()) {
            String path = "mines." + mine.getName().toLowerCase();

            newConfig.set(path + ".name", mine.getName());
            newConfig.set(path + ".reset-time", mine.getResetTime());
            saveExactLocation(
                    newConfig,
                    path + ".spawn",
                    mine.getSpawnLocation()
            );

            saveBlockLocation(
                    newConfig,
                    path + ".pos1",
                    mine.getPos1()
            );

            saveBlockLocation(
                    newConfig,
                    path + ".pos2",
                    mine.getPos2()
            );

            for (Map.Entry<Material, Double> entry
                    : mine.getBlocks().entrySet()) {

                newConfig.set(
                        path + ".blocks." + entry.getKey().name(),
                        entry.getValue()
                );
            }
        }

        try {
            if (!plugin.getDataFolder().exists()) {
                boolean created = plugin.getDataFolder().mkdirs();

                if (!created) {
                    plugin.getLogger().warning(
                            "Could not create the plugin data folder."
                    );
                }
            }

            newConfig.save(minesFile);
            minesConfig = newConfig;

        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "Could not save mines.yml."
            );

            exception.printStackTrace();
        }
    }

    private void saveBlockLocation(
            YamlConfiguration config,
            String path,
            Location location
    ) {
        if (location == null || location.getWorld() == null) {
            config.set(path, null);
            return;
        }

        config.set(
                path + ".world",
                location.getWorld().getName()
        );

        config.set(path + ".x", location.getBlockX());
        config.set(path + ".y", location.getBlockY());
        config.set(path + ".z", location.getBlockZ());
    }

    private Location loadLocation(String path) {
        if (!minesConfig.contains(path + ".world")) {
            return null;
        }

        String worldName =
                minesConfig.getString(path + ".world");

        if (worldName == null) {
            return null;
        }

        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            plugin.getLogger().warning(
                    "World '" + worldName
                            + "' was not found while loading a mine."
            );

            return null;
        }

        int x = minesConfig.getInt(path + ".x");
        int y = minesConfig.getInt(path + ".y");
        int z = minesConfig.getInt(path + ".z");

        return new Location(world, x, y, z);
    }

    private void saveExactLocation(
            FileConfiguration config,
            String path,
            Location location
    ) {
        if (location == null || location.getWorld() == null) {
            config.set(path, null);
            return;
        }

        config.set(
                path + ".world",
                location.getWorld().getName()
        );

        config.set(
                path + ".x",
                location.getX()
        );

        config.set(
                path + ".y",
                location.getY()
        );

        config.set(
                path + ".z",
                location.getZ()
        );

        config.set(
                path + ".yaw",
                location.getYaw()
        );

        config.set(
                path + ".pitch",
                location.getPitch()
        );
    }

    private Location loadLocation(
            FileConfiguration config,
            String path
    ) {
        String worldName = config.getString(
                path + ".world"
        );

        if (worldName == null) {
            return null;
        }

        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            plugin.getLogger().warning(
                    "Could not load world '" + worldName
                            + "' for location '" + path + "'."
            );

            return null;
        }

        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");

        float yaw = (float) config.getDouble(
                path + ".yaw"
        );

        float pitch = (float) config.getDouble(
                path + ".pitch"
        );

        return new Location(
                world,
                x,
                y,
                z,
                yaw,
                pitch
        );
    }
}