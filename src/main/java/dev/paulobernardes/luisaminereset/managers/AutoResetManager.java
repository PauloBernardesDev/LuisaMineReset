package dev.paulobernardes.luisaminereset.managers;

import dev.paulobernardes.luisaminereset.LuisaMineReset;
import dev.paulobernardes.luisaminereset.models.Mine;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AutoResetManager {

    private final LuisaMineReset plugin;
    private final MineManager mineManager;
    private final ResetManager resetManager;

    private final Map<String, Integer> elapsedTimes;

    private BukkitTask task;

    public AutoResetManager(
            LuisaMineReset plugin,
            MineManager mineManager,
            ResetManager resetManager
    ) {
        this.plugin = plugin;
        this.mineManager = mineManager;
        this.resetManager = resetManager;
        this.elapsedTimes = new HashMap<>();
    }

    public void start() {

        if (task != null) {
            return;
        }

        task = plugin.getServer()
                .getScheduler()
                .runTaskTimer(
                        plugin,
                        this::checkMines,
                        20L,
                        20L
                );

        plugin.getLogger().info(
                "Automatic mine reset scheduler started."
        );
    }

    public void stop() {

        if (task != null) {
            task.cancel();
            task = null;
        }

        elapsedTimes.clear();
    }

    private void checkMines() {

        for (Mine mine : mineManager.getAllMines()) {

            int resetTime = mine.getResetTime();
            String mineKey = mine.getName().toLowerCase(Locale.ROOT);

            if (resetTime <= 0) {
                elapsedTimes.remove(mineKey);
                continue;
            }

            int elapsedTime = elapsedTimes.getOrDefault(
                    mineKey,
                    0
            );

            elapsedTime++;

            int warningBefore = plugin.getConfig().getInt(
                    "broadcast.warning-before",
                    60
            );

            int timeRemaining = resetTime - elapsedTime;

            if (
                    plugin.getConfig().getBoolean(
                            "broadcast.enabled",
                            true
                    )
                            && warningBefore > 0
                            && resetTime > warningBefore
                            && timeRemaining == warningBefore
            ) {
                broadcastWarning(mine, warningBefore);
            }

            if (elapsedTime >= resetTime) {

                int changedBlocks = resetManager.resetMine(mine);

                if (changedBlocks >= 0) {

                    plugin.getLogger().info(
                            "Mine '" + mine.getName()
                                    + "' was automatically reset. "
                                    + changedBlocks
                                    + " block(s) changed."
                    );

                    if (plugin.getConfig().getBoolean(
                            "broadcast.enabled",
                            true
                    )) {
                        broadcastReset(mine);
                    }

                } else {
                    plugin.getLogger().warning(
                            "Could not automatically reset mine '"
                                    + mine.getName()
                                    + "'. Check its region."
                    );
                }

                elapsedTimes.put(mineKey, 0);

            } else {
                elapsedTimes.put(mineKey, elapsedTime);
            }
        }
    }

    private void broadcastWarning(
            Mine mine,
            int seconds
    ) {
        Bukkit.getOnlinePlayers().forEach(player ->
                plugin.getMessageManager().send(
                        player,
                        "reset-warning",
                        "%mine%", mine.getName(),
                        "%time%", formatTime(seconds)
                )
        );
    }

    private void broadcastReset(Mine mine) {
        Bukkit.getOnlinePlayers().forEach(player ->
                plugin.getMessageManager().send(
                        player,
                        "reset-complete",
                        "%mine%", mine.getName()
                )
        );
    }

    private String formatTime(int seconds) {

        if (seconds % 86400 == 0) {
            int days = seconds / 86400;

            String unit = days == 1
                    ? plugin.getMessageManager().getRawMessage("time-day")
                    : plugin.getMessageManager().getRawMessage("time-days");

            return days + " " + unit;
        }

        if (seconds % 3600 == 0) {
            int hours = seconds / 3600;

            String unit = hours == 1
                    ? plugin.getMessageManager().getRawMessage("time-hour")
                    : plugin.getMessageManager().getRawMessage("time-hours");

            return hours + " " + unit;
        }

        if (seconds % 60 == 0) {
            int minutes = seconds / 60;

            String unit = minutes == 1
                    ? plugin.getMessageManager().getRawMessage("time-minute")
                    : plugin.getMessageManager().getRawMessage("time-minutes");

            return minutes + " " + unit;
        }

        String unit = seconds == 1
                ? plugin.getMessageManager().getRawMessage("time-second")
                : plugin.getMessageManager().getRawMessage("time-seconds");

        return seconds + " " + unit;
    }

    public void restartTimer(Mine mine) {
        elapsedTimes.put(
                mine.getName().toLowerCase(Locale.ROOT),
                0
        );
    }
}