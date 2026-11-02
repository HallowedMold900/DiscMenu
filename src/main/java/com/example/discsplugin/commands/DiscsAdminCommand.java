package com.example.discsplugin.commands;

import com.example.discsplugin.DiscsPlugin;
import com.example.discsplugin.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscsAdminCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("add", "remove", "list", "reload");

    private final DiscsPlugin plugin;

    public DiscsAdminCommand(DiscsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("discs.admin")) {
            MessageUtil.send(sender, plugin, "no-permission");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.send(sender, plugin, "usage-admin");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                handleAdd(sender);
                return true;
            case "remove":
                handleRemove(sender, args);
                return true;
            case "list":
                handleList(sender);
                return true;
            case "reload":
                handleReload(sender);
                return true;
            default:
                MessageUtil.send(sender, plugin, "usage-admin");
                return true;
        }
    }

    private void handleAdd(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtil.send(sender, plugin, "players-only");
            return;
        }
        Player player = (Player) sender;
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType() == Material.AIR) {
            MessageUtil.send(sender, plugin, "hold-item");
            return;
        }
        plugin.getDiscsManager().addDisc(hand);
        MessageUtil.send(sender, plugin, "added");
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtil.send(sender, plugin, "need-number");
            return;
        }
        int index;
        try {
            index = Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException e) {
            MessageUtil.send(sender, plugin, "invalid-index");
            return;
        }
        if (plugin.getDiscsManager().removeDisc(index)) {
            MessageUtil.send(sender, plugin, "removed", "%index%", args[1]);
        } else {
            MessageUtil.send(sender, plugin, "invalid-index");
        }
    }

    private void handleList(CommandSender sender) {
        List<ItemStack> discs = plugin.getDiscsManager().getDiscs();
        if (discs.isEmpty()) {
            MessageUtil.send(sender, plugin, "list-empty");
            return;
        }
        MessageUtil.send(sender, plugin, "list-header");
        for (int i = 0; i < discs.size(); i++) {
            ItemStack item = discs.get(i);
            String name = item.getType().name();
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                name = meta.getDisplayName();
            }
            sender.sendMessage(MessageUtil.color("&7" + (i + 1) + ". &f" + name));
        }
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getDiscsManager().load();
        MessageUtil.send(sender, plugin, "reload-done");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return SUBCOMMANDS;
        }
        return Collections.emptyList();
    }
}
