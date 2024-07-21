package com.venned.insigniapass.abstracts;

import com.venned.insigniapass.interfaces.LockInterface;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public abstract class LockAbstract implements LockInterface {


    protected int password;
    protected UUID uuid;
    protected Location location;
    protected World world;
    private final int id;

    public LockAbstract(int id, int password, UUID uuid, Location location) {
        this.password = password;
        this.uuid = uuid;
        this.location = location;
        this.world = location.getWorld();
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(int password) {
        this.password = password;
    }

    @Override
    public UUID getOwner() {
        return this.uuid;
    }

    @Override
    public void setOwner(UUID owner) {
        this.uuid = owner;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
