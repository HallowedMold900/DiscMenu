package com.example.discsplugin.commands;

import com.example.discsplugin.DiscsPlugin;
import com.example.discsplugin.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscsMenuCommand implements CommandExecutor {

    private final DiscsPlugin plugin;

    public DiscsMenuCommand(DiscsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.send(sender, plugin, "players-only");
            return true;
        }
        if (!sender.hasPermission("discs.use")) {
            MessageUtil.send(sender, plugin, "no-permission");
            return true;
        }

        Player player = (Player) sender;
        plugin.getMenuBuilder().openMenu(player, 0);
        return true;
    }
}
