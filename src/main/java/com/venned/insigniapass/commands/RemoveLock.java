package com.venned.insigniapass.commands;

import com.venned.insigniapass.utils.MapUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveLock implements CommandExecutor {


    private final MapUtils mapUtils;

    public RemoveLock(MapUtils mapUtils) {
        this.mapUtils = mapUtils;
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You need to be a player to do this");
            return true;
        }
        if (command.getName().equalsIgnoreCase("removelock")) {
            if (!commandSender.hasPermission("passwordlocks.lock") && !commandSender.hasPermission("passwordlocks.*") && !commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                return true;
            }
            if (!mapUtils.unlockingPlayers.contains(commandSender)) {
                mapUtils.unlockingPlayers.add((Player)commandSender);
            }
            commandSender.sendMessage(ChatColor.BLUE + "Right-click on the door, trapdoor, chest or shulkerbox you want to unlock");
        }
        return false;
    }
}
