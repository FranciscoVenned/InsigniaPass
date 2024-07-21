package com.venned.insigniapass.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MenuUtils {


    public static Inventory createCodeLockMenu(Plugin plugin) {
        Inventory menu = Bukkit.createInventory(null, 54, "Code Lock");

        // Randomize the numbers 1-9
        List<Integer> numbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Set the glass buttons
        int[] buttonSlots = {40, 12, 13, 14, 21, 22, 23, 30, 31, 32};
        Material[] glassColors = {
                Material.CYAN_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE,
                Material.GREEN_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.PURPLE_STAINED_GLASS_PANE,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                Material.LIME_STAINED_GLASS_PANE,
                Material.PINK_STAINED_GLASS_PANE
        };

        for (int i = 0; i < buttonSlots.length; i++) {
            ItemStack glassButton = new ItemStack(glassColors[i]);
            ItemMeta meta = glassButton.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "" + numbers.get(i));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "code"),
                    PersistentDataType.INTEGER, numbers.get(i));
            glassButton.setItemMeta(meta);
            menu.setItem(buttonSlots[i], glassButton);
        }

        // Set the special buttons
        menu.setItem(16, createItem(Material.REDSTONE, "§cDelete"));
        menu.setItem(25, createItem(Material.BARRIER, "§cClose"));
        menu.setItem(34, createItem(Material.EMERALD, "§aSave"));

        // Fill empty slots with black glass
        ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackGlassMeta = blackGlass.getItemMeta();
        blackGlassMeta.setDisplayName(ChatColor.BLACK + " ");
        blackGlass.setItemMeta(blackGlassMeta);

        for (int i = 0; i < 54; i++) {
            ItemStack currentItem = menu.getItem(i);
            if (currentItem == null || currentItem.getType() == Material.AIR) {
                menu.setItem(i, blackGlass);
            }
        }


        return menu;
    }

    public static Inventory createCodeLockMenu(String title, Plugin plugin) {
        Inventory menu = Bukkit.createInventory(null, 54, title);

        // Randomize the numbers 1-9
        List<Integer> numbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Set the glass buttons
        int[] buttonSlots = {40, 12, 13, 14, 21, 22, 23, 30, 31, 32};
        Material[] glassColors = {
                Material.CYAN_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE,
                Material.GREEN_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.PURPLE_STAINED_GLASS_PANE,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                Material.LIME_STAINED_GLASS_PANE,
                Material.PINK_STAINED_GLASS_PANE
        };

        for (int i = 0; i < buttonSlots.length; i++) {
            ItemStack glassButton = new ItemStack(glassColors[i]);
            ItemMeta meta = glassButton.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "" + numbers.get(i));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "code"),
                    PersistentDataType.INTEGER, numbers.get(i));
            glassButton.setItemMeta(meta);
            menu.setItem(buttonSlots[i], glassButton);
        }

        // Set the special buttons
        menu.setItem(16, createItem(Material.REDSTONE, "§cDelete"));
        menu.setItem(25, createItem(Material.BARRIER, "§cClose"));
        menu.setItem(34, createItem(Material.EMERALD, "§aSave"));

        // Fill empty slots with black glass
        ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackGlassMeta = blackGlass.getItemMeta();
        blackGlassMeta.setDisplayName(ChatColor.BLACK + " ");
        blackGlass.setItemMeta(blackGlassMeta);

        for (int i = 0; i < 54; i++) {
            ItemStack currentItem = menu.getItem(i);
            if (currentItem == null || currentItem.getType() == Material.AIR) {
                menu.setItem(i, blackGlass);
            }
        }
        return menu;
    }

    private static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

}
