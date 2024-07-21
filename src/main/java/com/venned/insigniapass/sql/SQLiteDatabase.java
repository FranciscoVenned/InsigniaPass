package com.venned.insigniapass.sql;

import com.venned.insigniapass.abstracts.AbstractDatabase;
import com.venned.insigniapass.interfaces.LockInterface;
import com.venned.insigniapass.lock.Lock;
import com.venned.insigniapass.utils.MapUtils;
import com.venned.insigniapass.utils.UtilLocation;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLiteDatabase extends AbstractDatabase {

    private final Plugin plugin;
    private final MapUtils mapUtils;

    public SQLiteDatabase(String url, Plugin plugin, MapUtils mapUtils) {
        super(url);
        this.plugin = plugin;
        this.mapUtils = mapUtils;
    }

    @Override
    public void loadLocks(){
        try {
            List<Lock> locks = loadLocksAll();
            for (Lock lock : locks){
                mapUtils.setLockMap(lock.getId(), lock);
            }
        } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    @Override
    public int getNextId() throws SQLException {
        String query = "SELECT MAX(id) AS max_id FROM locks;";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            } else {
                return 1;
            }
        }
    }

    @Override
    public void saveLock(int id, int password, String owner, String location) throws SQLException {
        String insertChest = "INSERT INTO locks (id, password, owner, location) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(insertChest)) {
            stmt.setInt(1, id);
            stmt.setInt(2, password);
            stmt.setString(3, owner);
            stmt.setString(4, location);
            stmt.executeUpdate();
        }
    }

    @Override
    public void saveAllLocks() throws SQLException {
        String insertChest = "INSERT OR REPLACE INTO locks (id, password, owner, location) VALUES (?, ?, ?, ?);";

        for (LockInterface locks : mapUtils.lockMap.values()) {
            try (PreparedStatement stmt = connection.prepareStatement(insertChest)) {
                stmt.setInt(1, locks.getId());
                stmt.setInt(2, locks.getPassword());
                stmt.setString(3, locks.getOwner().toString());
                stmt.setString(4, UtilLocation.serializeLocation(locks.getLocation())); // Assuming UtilLocation.serializeLocation() serializes the location
                stmt.executeUpdate();
            }
        }
    }


    public List<Lock> loadLocksAll() throws SQLException {
        List<Lock> locks = new ArrayList<>();

        String query = "SELECT id, password, owner, location FROM locks;";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int password = rs.getInt("password");
                String owner = rs.getString("owner");
                String location = rs.getString("location");
                Location location_deserealizado = UtilLocation.deserializeLocation(location);
                UUID uuid = UUID.fromString(owner);

                Lock lock = new Lock(id, password, uuid, location_deserealizado);
                locks.add(lock);
            }
        }

        return locks;
    }

    @Override
    public void deleteLockById(int id, Lock lock) throws SQLException {
        String deleteQuery = "DELETE FROM locks WHERE id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            mapUtils.lockMap.remove(lock.getId());
        }
    }

    @Override
    public Lock getByLocation(Location location) {
        for(Lock lock : mapUtils.lockMap.values()){
            if (lock.getLocation().equals(location)){
                return lock;
            }
        }
        return null;
    }
}
