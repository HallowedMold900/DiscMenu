package com.example.discsplugin.menu;

import com.example.discsplugin.DiscsPlugin;
import com.example.discsplugin.util.MessageUtil;
import com.example.discsplugin.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class MenuListener implements Listener {

    private final DiscsPlugin plugin;

    public MenuListener(DiscsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof DiscsMenuHolder)) {
            return;
        }
        // Запрещаем любые перемещения предметов в этом инвентаре
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        DiscsMenuHolder holder = (DiscsMenuHolder) event.getInventory().getHolder();

        int slot = event.getRawSlot();
        // Клик вне верхнего инвентаря (по своему инвентарю игрока) — игнорируем
        if (slot < 0 || slot >= event.getInventory().getSize()) {
            return;
        }

        if (slot == MenuBuilder.PREV_SLOT) {
            ItemStack current = event.getCurrentItem();
            if (current != null && current.getType() == Material.ARROW) {
                plugin.getMenuBuilder().openMenu(player, holder.getPage() - 1);
            }
            return;
        }

        if (slot == MenuBuilder.NEXT_SLOT) {
            ItemStack current = event.getCurrentItem();
            if (current != null && current.getType() == Material.ARROW) {
                plugin.getMenuBuilder().openMenu(player, holder.getPage() + 1);
            }
            return;
        }

        // Остальные слоты нижнего ряда (декор/инфо) — ничего не делают
        if (slot >= MenuBuilder.PAGE_SIZE) {
            return;
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }

        handleDiscClaim(player, clicked);
    }

    private void handleDiscClaim(Player player, ItemStack disc) {
        long cooldownHours = plugin.getConfig().getLong("cooldown-hours", 12);
        long cooldownMillis = cooldownHours * 3600_000L;
        UUID uuid = player.getUniqueId();

        long remaining = plugin.getCooldownManager().getRemainingMillis(uuid, cooldownMillis);
        if (remaining > 0) {
            MessageUtil.send(player, plugin, "cooldown", "%time%", TimeUtil.formatMillis(remaining));
            player.closeInventory();
            return;
        }

        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(disc.clone());
        if (!leftover.isEmpty()) {
            for (ItemStack extra : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), extra);
            }
            MessageUtil.send(player, plugin, "inventory-full");
        }

        plugin.getCooldownManager().markUsed(uuid);
        MessageUtil.send(player, plugin, "received", "%hours%", String.valueOf(cooldownHours));
        player.closeInventory();
    }
}
