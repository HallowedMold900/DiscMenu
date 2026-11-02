package com.example.discsplugin.menu;

import com.example.discsplugin.DiscsPlugin;
import com.example.discsplugin.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MenuBuilder {

    public static final int PAGE_SIZE = 45; // слоты 0-44 под пластинки
    public static final int PREV_SLOT = 45;
    public static final int INFO_SLOT = 49;
    public static final int NEXT_SLOT = 53;

    private final DiscsPlugin plugin;

    public MenuBuilder(DiscsPlugin plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player, int page) {
        List<ItemStack> discs = plugin.getDiscsManager().getDiscs();
        int maxPage = discs.isEmpty() ? 0 : (discs.size() - 1) / PAGE_SIZE;

        if (page < 0) {
            page = 0;
        }
        if (page > maxPage) {
            page = maxPage;
        }

        String rawTitle = plugin.getConfig().getString("menu-title", "&8Discs");
        String title = MessageUtil.color(rawTitle) + " (" + (page + 1) + "/" + (maxPage + 1) + ")";

        DiscsMenuHolder holder = new DiscsMenuHolder(page);
        Inventory inventory = Bukkit.createInventory(holder, 54, title);
        holder.setInventory(inventory);

        // Заполняем нижний ряд декоративными панелями
        ItemStack filler = namedItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int slot = 45; slot < 54; slot++) {
            inventory.setItem(slot, filler);
        }

        int start = page * PAGE_SIZE;
        int end = Math.min(discs.size(), start + PAGE_SIZE);
        for (int i = start; i < end; i++) {
            inventory.setItem(i - start, discs.get(i).clone());
        }

        if (discs.isEmpty()) {
            inventory.setItem(INFO_SLOT, namedItem(Material.BARRIER, "&cThere are no discs yet."));
        } else {
            inventory.setItem(INFO_SLOT, namedItem(Material.BOOK, "&fPage " + (page + 1) + " of " + (maxPage + 1)));
        }

        if (page > 0) {
            inventory.setItem(PREV_SLOT, namedItem(Material.ARROW, "&a« Previous page"));
        }
        if (page < maxPage) {
            inventory.setItem(NEXT_SLOT, namedItem(Material.ARROW, "&aNext page »"));
        }

        player.openInventory(inventory);
    }

    private ItemStack namedItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(MessageUtil.color(name));
            item.setItemMeta(meta);
        }
        return item;
    }
}
