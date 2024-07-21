package com.venned.insigniapass.lock;

import com.venned.insigniapass.Main;
import com.venned.insigniapass.utils.MapUtils;
import com.venned.insigniapass.utils.MenuUtils;
import com.venned.insigniapass.utils.UtilLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LockManager {

    private Main plugin;
    private final Map<UUID, StringBuilder> playerCodes;
    private final Map<UUID, Location> locationMap;
    private final Map<UUID, BlockState> containerStates;
    private final MapUtils mapUtils;

    public LockManager(Main plugin, MapUtils mapUtils) {
        this.plugin = plugin;
        this.playerCodes = new HashMap<>();
        this.locationMap = new HashMap<>();
        this.containerStates = new HashMap<>();
        this.mapUtils = mapUtils;
    }

    public void startLockCreation(UUID playerUUID, Location location) {
        playerCodes.put(playerUUID, new StringBuilder());
        locationMap.put(playerUUID, location);
    }

    public void startLockAccess(UUID playerUUID, Location location, BlockState containerState) {
        playerCodes.put(playerUUID, new StringBuilder());
        locationMap.put(playerUUID, location);
        containerStates.put(playerUUID, containerState);
    }

    public Location getLocationLock(UUID playerUUID) {
        return locationMap.get(playerUUID);
    }

    public void addDigit(UUID playerUUID, int digit) {
        StringBuilder code = playerCodes.get(playerUUID);
        if (code != null && code.length() < 6) {  // Assuming a 4-digit code lock
            code.append(digit);
        }

        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            String title = "Code Lock: " + code.toString().replaceAll("[\\[\\],]", "");
            Inventory newMenu = MenuUtils.createCodeLockMenu(title, plugin);
            mapUtils.setPlayerInventoryMap(player, newMenu);
            player.openInventory(newMenu);
        }
    }

    public String getCode(UUID playerUUID) {
        StringBuilder code = playerCodes.get(playerUUID);
        return (code != null) ? code.toString() : null;
    }

    public void clearCodes(UUID playerUUID) {
        StringBuilder code = playerCodes.get(playerUUID);
        if (code != null) {
            code.setLength(0);  // Clear the current code
        }
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            String title = "Code Lock: " + code.toString().replaceAll("[\\[\\],]", "");
            Inventory newMenu = MenuUtils.createCodeLockMenu(title, plugin);
            mapUtils.setPlayerInventoryMap(player, newMenu);
            player.openInventory(newMenu);
        }
    }

    public void clearCode(UUID playerUUID) {
        playerCodes.remove(playerUUID);
    }

    public void clearLocation(UUID playerUUID){
        locationMap.remove(playerUUID);
    }

    public BlockState getContainerState(UUID playerUUID) {
        return containerStates.get(playerUUID);
    }

    public boolean verifyLock(UUID playerUUID) {
        String code = getCode(playerUUID);
        if (code != null && !code.isEmpty()) {
            Location location = locationMap.get(playerUUID);
            Lock lock = plugin.getDatabase().getByLocation(location);
            if (lock != null) {
                return lock.getPassword() == Integer.parseInt(code);
            }
        }
        return false;
    }

    public void createLock(UUID playerUUID, Location location){
        String code = getCode(playerUUID);
        if (code != null) {
            try {
            int password = Integer.parseInt(code);
            int id = plugin.getDatabase().getNextId();
            Lock lock = new Lock(id, password, playerUUID, location);
            plugin.getMapUtils().setLockMap(id, lock);
            plugin.getDatabase().saveLock(id, password, playerUUID.toString(), UtilLocation.serializeLocation(location));
            clearCode(playerUUID);
            clearLocation(playerUUID);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
