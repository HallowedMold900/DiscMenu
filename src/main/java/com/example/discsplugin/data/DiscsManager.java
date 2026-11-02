package com.example.discsplugin.data;

import com.example.discsplugin.DiscsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Хранит список кастомных пластинок, доступных в меню /discsmenu.
 * Данные сериализуются в discs.yml как список ItemStack.
 */
public class DiscsManager {

    private final DiscsPlugin plugin;
    private final File file;
    private FileConfiguration config;
    private final List<ItemStack> discs = new ArrayList<>();

    public DiscsManager(DiscsPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "discs.yml");
        load();
    }

    public void load() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать discs.yml: " + e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        discs.clear();
        List<?> rawList = config.getList("discs");
        if (rawList != null) {
            for (Object obj : rawList) {
                if (obj instanceof ItemStack) {
                    discs.add((ItemStack) obj);
                }
            }
        }
    }

    public void save() {
        config.set("discs", discs);
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить discs.yml: " + e.getMessage());
        }
    }

    /**
     * Неизменяемый список для отображения — изменения делаются через addDisc/removeDisc.
     */
    public List<ItemStack> getDiscs() {
        return Collections.unmodifiableList(discs);
    }

    public void addDisc(ItemStack item) {
        discs.add(item.clone());
        save();
    }

    /**
     * @param index индекс с 0
     * @return true если пластинка была удалена
     */
    public boolean removeDisc(int index) {
        if (index < 0 || index >= discs.size()) {
            return false;
        }
        discs.remove(index);
        save();
        return true;
    }
}
