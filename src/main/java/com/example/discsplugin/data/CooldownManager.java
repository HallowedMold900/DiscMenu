package com.example.discsplugin.data;

import com.example.discsplugin.DiscsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Хранит время последнего получения бесплатной пластинки для каждого игрока.
 */
public class CooldownManager {

    private final DiscsPlugin plugin;
    private final File file;
    private FileConfiguration config;
    private final Map<UUID, Long> lastClaim = new HashMap<>();

    public CooldownManager(DiscsPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "cooldowns.yml");
        load();
    }

    public void load() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать cooldowns.yml: " + e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        lastClaim.clear();
        ConfigurationSection section = config.getConfigurationSection("cooldowns");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    long time = section.getLong(key);
                    lastClaim.put(uuid, time);
                } catch (IllegalArgumentException ignored) {
                    // некорректный UUID в файле — пропускаем
                }
            }
        }
    }

    public void save() {
        config.set("cooldowns", null);
        for (Map.Entry<UUID, Long> entry : lastClaim.entrySet()) {
            config.set("cooldowns." + entry.getKey(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить cooldowns.yml: " + e.getMessage());
        }
    }

    /**
     * @return сколько миллисекунд осталось ждать (0 если можно брать сейчас)
     */
    public long getRemainingMillis(UUID uuid, long cooldownMillis) {
        long last = lastClaim.getOrDefault(uuid, 0L);
        long elapsed = System.currentTimeMillis() - last;
        long remaining = cooldownMillis - elapsed;
        return Math.max(0, remaining);
    }

    public void markUsed(UUID uuid) {
        lastClaim.put(uuid, System.currentTimeMillis());
        save();
    }
}
