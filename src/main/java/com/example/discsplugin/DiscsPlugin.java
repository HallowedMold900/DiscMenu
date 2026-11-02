package com.example.discsplugin;

import com.example.discsplugin.commands.DiscsAdminCommand;
import com.example.discsplugin.commands.DiscsMenuCommand;
import com.example.discsplugin.data.CooldownManager;
import com.example.discsplugin.data.DiscsManager;
import com.example.discsplugin.menu.MenuBuilder;
import com.example.discsplugin.menu.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscsPlugin extends JavaPlugin {

    private DiscsManager discsManager;
    private CooldownManager cooldownManager;
    private MenuBuilder menuBuilder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getDataFolder().mkdirs();

        this.discsManager = new DiscsManager(this);
        this.cooldownManager = new CooldownManager(this);
        this.menuBuilder = new MenuBuilder(this);

        DiscsMenuCommand menuCommand = new DiscsMenuCommand(this);
        getCommand("discsmenu").setExecutor(menuCommand);

        DiscsAdminCommand adminCommand = new DiscsAdminCommand(this);
        getCommand("discsadmin").setExecutor(adminCommand);
        getCommand("discsadmin").setTabCompleter(adminCommand);

        getServer().getPluginManager().registerEvents(new MenuListener(this), this);

        getLogger().info("DiscMenu enabled. Discs in the database: " + discsManager.getDiscs().size());
    }

    @Override
    public void onDisable() {
        if (discsManager != null) {
            discsManager.save();
        }
        if (cooldownManager != null) {
            cooldownManager.save();
        }
        getLogger().info("DiscMenu disabled.");
    }

    public DiscsManager getDiscsManager() {
        return discsManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public MenuBuilder getMenuBuilder() {
        return menuBuilder;
    }
}
