package dev.paulobernardes.luisaminereset.managers;

import dev.paulobernardes.luisaminereset.LuisaMineReset;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class MessageManager {

    private final LuisaMineReset plugin;
    private FileConfiguration messages;

    public MessageManager(LuisaMineReset plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    public void loadMessages() {
        File file = new File(plugin.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(file);
    }

    public void reloadMessages() {
        loadMessages();
    }

    public String getMessage(String path, String... replacements) {
        String message = messages.getString(path);

        if (message == null) {
            return color("&cMessage not found: " + path);
        }

        String prefix = messages.getString("prefix", "");

        for (int i = 0; i < replacements.length - 1; i += 2) {
            message = message.replace(replacements[i], replacements[i + 1]);
        }

        return color(prefix + message);
    }

    public String getRawMessage(
            String path,
            String... replacements
    ) {
        String message = messages.getString(path);

        if (message == null) {
            return color("&cMessage not found: " + path);
        }

        for (int i = 0; i < replacements.length - 1; i += 2) {
            message = message.replace(
                    replacements[i],
                    replacements[i + 1]
            );
        }

        return color(message);
    }

    public void send(CommandSender sender, String path, String... replacements) {
        sender.sendMessage(getMessage(path, replacements));
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}