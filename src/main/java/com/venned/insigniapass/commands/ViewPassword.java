package com.venned.insigniapass.commands;

import com.venned.insigniapass.utils.MapUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ViewPassword implements CommandExecutor {

    private final MapUtils mapUtils;

    public ViewPassword(MapUtils mapUtils) {
        this.mapUtils = mapUtils;
    }

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (command.getName().equalsIgnoreCase("viewpassword")) {
            if (!commandSender.hasPermission("passwordlocks.admin") && !commandSender.hasPermission("passwordlocks.*") && !commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                return true;
            }
            if (!mapUtils.viewingPlayer.contains(commandSender)) {
                mapUtils.viewingPlayer.add((Player)commandSender);
            }
            commandSender.sendMessage(ChatColor.BLUE + "Right-click on the door, trapdoor, chest or shulkerbox you want to lock");
        }
        return false;
    }

}
