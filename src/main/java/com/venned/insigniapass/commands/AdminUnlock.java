package com.venned.insigniapass.commands;

import com.venned.insigniapass.Main;
import com.venned.insigniapass.utils.MapUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminUnlock implements CommandExecutor{

    private MapUtils mapUtils;

    public AdminUnlock(MapUtils mapUtils) {
        this.mapUtils = mapUtils;
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (command.getName().equalsIgnoreCase("adminunlock")) {
            if (!commandSender.hasPermission("passwordlocks.admin") && !commandSender.hasPermission("passwordlocks.*") && !commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                return true;
            }
            if (!mapUtils.adminUnlockingPlayers.contains(commandSender)) {
                mapUtils.adminUnlockingPlayers.add(commandSender);
                commandSender.sendMessage(ChatColor.GREEN + "You are now in admin unlock mode.");
            }
            else {
                mapUtils.adminUnlockingPlayers.remove(commandSender);
                commandSender.sendMessage(ChatColor.RED + "You are no longer in admin unlock mode.");
            }
        }
        return false;
    }

}
