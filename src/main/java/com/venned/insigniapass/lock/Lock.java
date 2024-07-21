package com.venned.insigniapass.lock;

import com.venned.insigniapass.abstracts.LockAbstract;
import org.bukkit.Location;

import java.util.UUID;

public class Lock extends LockAbstract {

    public Lock(int id, int password, UUID uuid, Location location) {
        super(id, password, uuid, location);
    }

}
