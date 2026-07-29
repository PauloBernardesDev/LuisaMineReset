package dev.paulobernardes.luisaminereset.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class ResetManager {

    private final RandomBlockProvider randomBlockProvider;

    public ResetManager(RandomBlockProvider randomBlockProvider) {
        this.randomBlockProvider = randomBlockProvider;
    }

    public int resetMine(Mine mine) {

        Location pos1 = mine.getPos1();
        Location pos2 = mine.getPos2();

        if (pos1 == null || pos2 == null) {
            return -1;
        }

        teleportPlayersOutsideMine(mine);

        World world = pos1.getWorld();

        if (world == null) {
            return -1;
        }

        if (pos2.getWorld() == null
                || !world.getUID().equals(pos2.getWorld().getUID())) {
            return -1;
        }

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int changedBlocks = 0;

        for (int x = minX; x <= maxX; x++) {

            for (int y = minY; y <= maxY; y++) {

                for (int z = minZ; z <= maxZ; z++) {

                    Material material =
                            randomBlockProvider.getRandomMaterial(mine);

                    world.getBlockAt(x, y, z)
                            .setType(material, false);

                    changedBlocks++;
                }
            }
        }

        return changedBlocks;
    }

    private void teleportPlayersOutsideMine(Mine mine) {

        Location spawnLocation = mine.getSpawnLocation();

        if (spawnLocation == null || spawnLocation.getWorld() == null) {
            return;
        }

        Location pos1 = mine.getPos1();
        Location pos2 = mine.getPos2();

        if (pos1 == null || pos2 == null) {
            return;
        }

        if (pos1.getWorld() == null || pos2.getWorld() == null) {
            return;
        }

        if (!pos1.getWorld().equals(pos2.getWorld())) {
            return;
        }

        for (Player player : pos1.getWorld().getPlayers()) {

            if (isInsideMine(player.getLocation(), pos1, pos2)) {
                player.teleport(spawnLocation);
                player.sendMessage(
                        "§eYou were teleported outside because the mine was reset."
                );
            }
        }
    }

    private boolean isInsideMine(
            Location location,
            Location pos1,
            Location pos2
    ) {
        if (location.getWorld() == null
                || pos1.getWorld() == null
                || !location.getWorld().equals(pos1.getWorld())) {
            return false;
        }

        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());

        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());

        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        double playerX = location.getX();
        double playerY = location.getY();
        double playerZ = location.getZ();

        return playerX >= minX
                && playerX <= maxX + 1
                && playerY >= minY
                && playerY <= maxY + 1
                && playerZ >= minZ
                && playerZ <= maxZ + 1;
    }
}