package com.venned.insigniapass.interfaces;

import com.venned.insigniapass.lock.Lock;
import org.bukkit.Location;

import java.sql.SQLException;

public interface Database {

    void connect() throws SQLException;
    void disconnect() throws SQLException;
    void createTables() throws SQLException;
    int getNextId() throws SQLException;
    void saveLock(int id, int password, String owner, String location) throws SQLException;
    void loadLocks() throws SQLException;
    Lock getByLocation(Location location);
    void saveAllLocks() throws SQLException;
    void deleteLockById(int id, Lock lock) throws SQLException;
}
