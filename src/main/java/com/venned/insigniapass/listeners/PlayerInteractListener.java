package com.venned.insigniapass.listeners;

import com.venned.insigniapass.Main;
import com.venned.insigniapass.lock.Lock;
import com.venned.insigniapass.utils.MapUtils;
import com.venned.insigniapass.utils.MenuUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;

public class PlayerInteractListener implements Listener {

    private final Main plugin;
    private final MapUtils mapUtils;

    public PlayerInteractListener(Main plugin, MapUtils mapUtils) {
        this.plugin = plugin;
        this.mapUtils = mapUtils;
    }

    @EventHandler
    public void lockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        if(!isLockable(event.getBlock().getType())) {
            return;
        }
        Lock lock = plugin.getDatabase().getByLocation(location);
        if(lock != null) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.5f, 1.5f);
            player.sendMessage("§c§l(!) §7You cannot break a Lock you must use /removelock §8(You must be the owner)");
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();


        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }

        Location l = event.getClickedBlock().getLocation();

        if (!isLockable(event.getClickedBlock().getType())) {
            return;
        }

        BlockState blockState = event.getClickedBlock().getState();
        if(mapUtils.lockingPlayers.contains(player)){
            event.setCancelled(true);
            Lock lock = plugin.getDatabase().getByLocation(l);
            if (lock == null) {
                Inventory menu = MenuUtils.createCodeLockMenu(plugin);
                plugin.getLockManager().startLockCreation(player.getUniqueId(), l);
                mapUtils.setPlayerInventoryMap(player, menu);
                player.openInventory(menu);
            } else {
                player.sendMessage("§c§l(!) §7There is already a Lock registered.");
                mapUtils.lockingPlayers.remove(player);
            }
        }
        else if(mapUtils.viewingPlayer.contains(player)){
            Lock lock = plugin.getDatabase().getByLocation(l);
            if (lock != null) {
                event.setCancelled(true);
                player.sendMessage("§c§l(!) §7Code is " + lock.getPassword());
                mapUtils.viewingPlayer.remove(player);
            }
        }

        else if(mapUtils.unlockingPlayers.contains(player)){
            Lock lock = plugin.getDatabase().getByLocation(l);
            if (lock != null) {
                event.setCancelled(true);
                if(lock.getOwner().equals(player.getUniqueId())) {
                    try {
                        plugin.getDatabase().deleteLockById(lock.getId(), lock);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    mapUtils.unlockingPlayers.remove(player);
                    player.sendMessage("§c§l(!) §7Lock Deleted...");
                } else {
                    player.sendMessage("§c§l(!) §7You are not the owner");
                }
            }
        }
        else if(mapUtils.adminUnlockingPlayers.contains(player)){
            player.sendMessage("§c§l(!) §7You have bypassed a lock because you are an Admin");
            event.setCancelled(false);
        }
        else {
            Lock lock = plugin.getDatabase().getByLocation(l);
            if (lock != null) {
                event.setCancelled(true);
                Inventory menu = MenuUtils.createCodeLockMenu(plugin);
                plugin.getLockManager().startLockAccess(player.getUniqueId(), l, blockState);
                mapUtils.setPlayerInventoryMap(player, menu);
                player.openInventory(menu);
            }
        }



    }

    private boolean isLockable(Material material) {
        String name = material.name();
        return name.contains("DOOR") || name.contains("GATE") ||
                material == Material.CHEST || material == Material.TRAPPED_CHEST ||
                name.contains("SHULKER");
    }
}
