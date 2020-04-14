package net.aspecter.core.pets.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GatherListener implements Listener {

        private JavaPlugin plugin;
        private ItemStack wand;
        private

        public GatherListener(JavaPlugin plugin) {
            this.plugin = plugin;

            wand = new ItemStack(Material)
            plugin.getConfig().addDefault("items.wand", );
        }



}
