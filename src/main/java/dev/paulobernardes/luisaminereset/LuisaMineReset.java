package dev.paulobernardes.luisaminereset;

import dev.paulobernardes.luisaminereset.managers.MessageManager;
import dev.paulobernardes.luisaminereset.managers.AutoResetManager;
import dev.paulobernardes.luisaminereset.managers.RandomBlockProvider;
import dev.paulobernardes.luisaminereset.managers.ResetManager;
import dev.paulobernardes.luisaminereset.commands.MineCommand;
import dev.paulobernardes.luisaminereset.managers.MineManager;
import dev.paulobernardes.luisaminereset.storage.MineStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class LuisaMineReset extends JavaPlugin {

    private MessageManager messageManager;
    private MineManager mineManager;
    private MineStorage mineStorage;
    private ResetManager resetManager;
    private RandomBlockProvider randomBlockProvider;
    private AutoResetManager autoResetManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        saveResource("messages.yml", false);

        messageManager = new MessageManager(this);

        mineManager = new MineManager();
        randomBlockProvider = new RandomBlockProvider();
        resetManager = new ResetManager(randomBlockProvider);
        mineStorage = new MineStorage(this, mineManager);
        mineStorage.loadMines();
        autoResetManager = new AutoResetManager(
                this,
                mineManager,
                resetManager
        );
        autoResetManager.start();

        if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            Bukkit.getConsoleSender().sendMessage(
                    "§8[§bLuisaMineReset§8] "
                            + "§aWorldEdit integration enabled!"
            );
        } else {
            Bukkit.getConsoleSender().sendMessage(
                    "§8[§bLuisaMineReset§8] "
                            + "§eWorldEdit was not found. "
                            + "Region selection commands will be unavailable."
            );
        }

        PluginCommand command = getCommand("luisaminereset");

        if (command == null) {
            Bukkit.getConsoleSender().sendMessage(
                    "§8[§bLuisaMineReset§8] "
                            + "§cCommand registration failed!"
            );

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MineCommand mineCommand = new MineCommand(
                this,
                mineManager,
                mineStorage,
                resetManager,
                autoResetManager
        );

        if (getCommand("lmr") != null) {
            getCommand("lmr").setExecutor(mineCommand);
            getCommand("lmr").setTabCompleter(mineCommand);
        }

        if (getCommand("luisaminereset") != null) {
            getCommand("luisaminereset").setExecutor(mineCommand);
        } else {
            getLogger().severe(
                    "Command 'luisaminereset' was not found in plugin.yml!"
            );
        }

        Bukkit.getConsoleSender().sendMessage(
                "§8[§bLuisaMineReset§8] "
                        + "§aPlugin enabled successfully!"
        );

        Bukkit.getConsoleSender().sendMessage(
                "§8[§bLuisaMineReset§8] "
                        + "§aCommand /lmr registered successfully!"
        );
    }

    @Override
    public void onDisable() {

        if (autoResetManager != null) {
            autoResetManager.stop();
        }

        if (mineStorage != null) {
            mineStorage.saveMines();
        }

        getLogger().info("LuisaMineReset disabled.");
    }

    public MineManager getMineManager() {
        return mineManager;
    }

    public MineStorage getMineStorage() {
        return mineStorage;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}