package dev.paulobernardes.luisaminereset.commands.subcommands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.LocalSession;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.models.Mine;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class SetRegionCommand implements SubCommand {

    private final MineManager mineManager;
    private final MineStorage mineStorage;

    public SetRegionCommand(MineManager mineManager, MineStorage mineStorage) {
        this.mineManager = mineManager;
        this.mineStorage = mineStorage;
    }

    @Override
    public String getName() {
        return "setregion";
    }

    @Override
    public String getDescription() {
        return "Sets a mine region using your WorldEdit selection.";
    }

    @Override
    public String getUsage() {
        return "/lmr setregion <mine>";
    }

    @Override
    public String getPermission() {
        return "luisaminereset.setregion";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        org.bukkit.entity.Player bukkitPlayer =
                (org.bukkit.entity.Player) sender;

        if (args.length < 2) {
            sender.sendMessage("§cUsage: " + getUsage());
            return;
        }

        String mineName = args[1];

        Mine mine = mineManager.getMine(mineName);

        if (mine == null) {
            sender.sendMessage("§cMine §f" + mineName + " §cdoes not exist.");
            return;
        }

        Player worldEditPlayer = BukkitAdapter.adapt(bukkitPlayer);

        LocalSession session = WorldEdit.getInstance()
                .getSessionManager()
                .get(worldEditPlayer);

        try {
            Region selection = session.getSelection(worldEditPlayer.getWorld());

            if (!(selection instanceof CuboidRegion cuboidRegion)) {
                sender.sendMessage("§cYou must create a cuboid WorldEdit selection.");
                return;
            }

            BlockVector3 minimumPoint = cuboidRegion.getMinimumPoint();
            BlockVector3 maximumPoint = cuboidRegion.getMaximumPoint();

            World world = BukkitAdapter.adapt(cuboidRegion.getWorld());

            Location pos1 = new Location(
                    world,
                    minimumPoint.x(),
                    minimumPoint.y(),
                    minimumPoint.z()
            );

            Location pos2 = new Location(
                    world,
                    maximumPoint.x(),
                    maximumPoint.y(),
                    maximumPoint.z()
            );

            mine.setPos1(pos1);
            mine.setPos2(pos2);

            mineStorage.saveMines();

            sender.sendMessage(
                    "§aRegion successfully set for mine §f"
                            + mine.getName()
                            + "§a."
            );

            sender.sendMessage(
                    "§7Minimum: §f"
                            + minimumPoint.x() + ", "
                            + minimumPoint.y() + ", "
                            + minimumPoint.z()
            );

            sender.sendMessage(
                    "§7Maximum: §f"
                            + maximumPoint.x() + ", "
                            + maximumPoint.y() + ", "
                            + maximumPoint.z()
            );

        } catch (IncompleteRegionException exception) {
            sender.sendMessage("§cYour WorldEdit selection is incomplete.");
            sender.sendMessage("§7Select two positions using §f//wand§7.");
        }
    }
}