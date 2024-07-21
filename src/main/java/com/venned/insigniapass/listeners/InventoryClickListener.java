package com.venned.insigniapass.listeners;


import com.venned.insigniapass.Main;
import com.venned.insigniapass.lock.LockManager;
import com.venned.insigniapass.utils.MapUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;
import java.util.Objects;

public class InventoryClickListener implements Listener {

    private Main plugin;
    private MapUtils mapUtils;
    private final LockManager lockManager;

    public InventoryClickListener(Main plugin, MapUtils mapUtils, LockManager lockManager) {
        this.plugin = plugin;
        this.mapUtils = mapUtils;
        this.lockManager = lockManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws SQLException {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        if (mapUtils.lockingPlayers.contains(player)) {
            Inventory map = mapUtils.getPlayerInventoryMap().get(player);
            if (Objects.equals(event.getClickedInventory(), map)) {

                event.setCancelled(true); // Prevent taking the items
                ItemMeta meta_click = clickedItem.getItemMeta();
                String itemName = clickedItem.getItemMeta().getDisplayName();
                if (meta_click.getPersistentDataContainer().has(new NamespacedKey(plugin, "code"))) {
                    int digit = meta_click.getPersistentDataContainer().get(new NamespacedKey(plugin, "code"), PersistentDataType.INTEGER);
                    lockManager.addDigit(player.getUniqueId(), digit);
                } else if (itemName.contains("Delete")) {
                    lockManager.clearCodes(player.getUniqueId());
                    player.sendMessage("§c§l(!) §7Code entry reset.");
                } else if (itemName.contains("Close")) {
                    mapUtils.playerInventoryMap.remove(player);
                    mapUtils.lockingPlayers.remove(player);
                    player.closeInventory();
                } else if (itemName.contains("Save")) {
                    Location lock_location = lockManager.getLocationLock(player.getUniqueId());
                    lockManager.createLock(player.getUniqueId(), lock_location);
                    mapUtils.playerInventoryMap.remove(player);
                    mapUtils.lockingPlayers.remove(player);
                    player.closeInventory();
                    player.sendMessage("§c§l(!) §7Code lock created!");
                }
            }
        } else {
            Inventory map = mapUtils.getPlayerInventoryMap().get(player);
            if (Objects.equals(event.getClickedInventory(), map)) {
                event.setCancelled(true); // Prevent taking the items

                String itemName = clickedItem.getItemMeta().getDisplayName();
                ItemMeta meta_click = clickedItem.getItemMeta();
                if (meta_click.getPersistentDataContainer().has(new NamespacedKey(plugin, "code"))) {
                    int digit = meta_click.getPersistentDataContainer().get(new NamespacedKey(plugin, "code"),
                            PersistentDataType.INTEGER);
                    lockManager.addDigit(player.getUniqueId(), digit);
                } else if (itemName.contains("Delete")) {
                    lockManager.clearCodes(player.getUniqueId());
                    player.sendMessage("§c§l(!) §7Code entry reset.");
                } else if (itemName.contains("Close")) {
                    mapUtils.playerInventoryMap.remove(player);
                    mapUtils.lockingPlayers.remove(player);
                    player.closeInventory();
                } else if (itemName.contains("Save")) {
                    if(lockManager.verifyLock(player.getUniqueId())){
                        mapUtils.playerInventoryMap.remove(player);
                        player.sendMessage("§c§l(!) §7You have successfully authenticated");
                        player.closeInventory();

                        BlockState containerState = lockManager.getContainerState(player.getUniqueId());
                        if (containerState != null) {
                            player.openInventory(((InventoryHolder) containerState).getInventory());
                        }
                    } else {
                        player.sendMessage("§c§l(!) §cWrong code try again");
                        lockManager.clearCodes(player.getUniqueId());
                    }
                }
            }
        }
    }
}
