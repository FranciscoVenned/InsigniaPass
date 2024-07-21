package com.venned.insigniapass.listeners;

import com.venned.insigniapass.lock.LockManager;
import com.venned.insigniapass.utils.MapUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    private final MapUtils mapUtils;
    private final LockManager lockManager;

    public InventoryCloseListener(MapUtils mapUtils, LockManager lockManager) {
        this.mapUtils = mapUtils;
        this.lockManager = lockManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
    }
}
