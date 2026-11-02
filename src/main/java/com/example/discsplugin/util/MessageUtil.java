package com.example.discsplugin.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.example.discsplugin.DiscsPlugin;

public class MessageUtil {

    private MessageUtil() {
    }

    public static String color(String input) {
        if (input == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void send(CommandSender sender, DiscsPlugin plugin, String path) {
        String raw = plugin.getConfig().getString("messages." + path, path);
        sender.sendMessage(color(raw));
    }

    public static void send(CommandSender sender, DiscsPlugin plugin, String path, String placeholder, String value) {
        String raw = plugin.getConfig().getString("messages." + path, path);
        raw = raw.replace(placeholder, value);
        sender.sendMessage(color(raw));
    }
}
