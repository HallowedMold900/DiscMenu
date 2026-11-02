package com.example.discsplugin.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Маркерный holder, чтобы отличать наше меню от других инвентарей
 * и хранить номер текущей страницы.
 */
public class DiscsMenuHolder implements InventoryHolder {

    private final int page;
    private Inventory inventory;

    public DiscsMenuHolder(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
