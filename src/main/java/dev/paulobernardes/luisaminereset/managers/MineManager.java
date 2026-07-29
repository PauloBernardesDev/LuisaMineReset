package dev.paulobernardes.luisaminereset.managers;

import dev.paulobernardes.luisaminereset.models.Mine;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MineManager {

    private final Map<String, Mine> mines;

    public MineManager() {
        this.mines = new LinkedHashMap<>();
    }

    public boolean createMine(String name) {
        String key = normalizeName(name);

        if (mines.containsKey(key)) {
            return false;
        }

        mines.put(key, new Mine(name));
        return true;
    }

    public Mine getMine(String name) {
        return mines.get(normalizeName(name));
    }

    public boolean mineExists(String name) {
        return mines.containsKey(normalizeName(name));
    }

    public boolean deleteMine(String name) {
        return mines.remove(normalizeName(name)) != null;
    }

    public Collection<Mine> getAllMines() {
        return mines.values();
    }

    public boolean renameMine(
            String currentName,
            String newName
    ) {
        String currentKey = normalizeName(currentName);
        String newKey = normalizeName(newName);

        Mine mine = mines.get(currentKey);

        if (mine == null) {
            return false;
        }

        if (mines.containsKey(newKey)) {
            return false;
        }

        mines.remove(currentKey);

        mine.setName(newName);
        mines.put(newKey, mine);

        return true;
    }

    private String normalizeName(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}