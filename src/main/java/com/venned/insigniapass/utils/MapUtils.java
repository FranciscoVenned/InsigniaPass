package com.venned.insigniapass.utils;

import com.venned.insigniapass.lock.Lock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapUtils {

    private final Plugin plugin;

    public MapUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public ArrayList<CommandSender> adminUnlockingPlayers = new ArrayList<>();
    public ArrayList<Player> unlockingPlayers = new ArrayList<>();
    public ArrayList<Player> viewingPlayer = new ArrayList<>();
    public ArrayList<Player> lockingPlayers = new ArrayList<>();
    public Map<Integer, Lock> lockMap = new ConcurrentHashMap<>();
    public Map<Player, Inventory> playerInventoryMap = new ConcurrentHashMap<>();

    public ArrayList<Player> getLockingPlayers() {
        return lockingPlayers;
    }

    public void setPlayerInventoryMap(Player player, Inventory inventory) {
        this.playerInventoryMap.put(player, inventory);
    }

    public Map<Player, Inventory> getPlayerInventoryMap() {
        return playerInventoryMap;
    }

    public Map<Integer, Lock> getLockMap() {
        return lockMap;
    }
    public void setLockMap(int id, Lock lock) {
        this.lockMap.put(id, lock);
    }
}
