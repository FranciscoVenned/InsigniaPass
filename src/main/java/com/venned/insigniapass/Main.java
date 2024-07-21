package com.venned.insigniapass;

import com.venned.insigniapass.commands.AdminUnlock;
import com.venned.insigniapass.commands.LockCommand;
import com.venned.insigniapass.commands.RemoveLock;
import com.venned.insigniapass.commands.ViewPassword;
import com.venned.insigniapass.interfaces.Database;
import com.venned.insigniapass.listeners.InventoryClickListener;
import com.venned.insigniapass.listeners.InventoryCloseListener;
import com.venned.insigniapass.listeners.PlayerInteractListener;
import com.venned.insigniapass.lock.LockManager;
import com.venned.insigniapass.sql.SQLiteDatabase;
import com.venned.insigniapass.utils.MapUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Main extends JavaPlugin {

    private Database database;
    private MapUtils mapUtils;
    private LockManager lockManager;

    @Override
    public void onEnable() {
        this.mapUtils = new MapUtils(this);
        this.lockManager = new LockManager(this, mapUtils);
        saveDefaultConfig();

        try {
            database = new SQLiteDatabase("jdbc:sqlite:" + getDataFolder() + "/locks.db", this, mapUtils);
            database.connect();
            database.createTables();
            database.loadLocks();
        } catch (SQLException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new InventoryCloseListener(mapUtils, lockManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this, mapUtils), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this, mapUtils, lockManager), this);

        getCommand("viewpassword").setExecutor(new ViewPassword(mapUtils));
        getCommand("lock").setExecutor(new LockCommand(mapUtils));
        getCommand("removelock").setExecutor(new RemoveLock(mapUtils));
        getCommand("adminunlock").setExecutor(new AdminUnlock(mapUtils));
    }

    @Override
    public void onDisable() {
        try {
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LockManager getLockManager() {
        return lockManager;
    }

    public MapUtils getMapUtils() {
        return mapUtils;
    }

    public Database getDatabase() {
        return database;
    }
}
