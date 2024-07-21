package com.venned.insigniapass.interfaces;

import org.bukkit.Location;

import java.util.UUID;

public interface LockInterface {

    int getId();
    int getPassword();
    void setPassword(int password);
    UUID getOwner();
    void setOwner(UUID owner);
    Location getLocation();
    void setLocation(Location location);
}
