package net.aspecter.core.gui.listeners;

import net.aspecter.core.gui.util.ItemInvoker;
import net.aspecter.core.gui.handlers.EasyGUIHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class InventoryEventListener implements Listener {

	private EasyGUIHandler handler;
	private Plugin plugin;
	private List<ItemInvoker> invokeList;
	private Player player;
	private Inventory inventory;

	public InventoryEventListener(EasyGUIHandler handler, Plugin plugin, List<ItemInvoker> invokeList, Player player, Inventory inventory) {
		this.handler = handler;
		this.plugin = plugin;
		this.invokeList = invokeList;
		this.player = player;
		this.inventory = inventory;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent ice) {
		if(ice.getInventory().equals(inventory) && ice.getWhoClicked().getUniqueId() == player.getUniqueId()) {
			if(ice.getCurrentItem() == null) {
				ice.setCancelled(true);
				return;
			}
			ice.setCancelled(true);
			for(ItemInvoker data : invokeList) {
				if (data.equalsItem(ice.getCurrentItem())) {
					ice.getWhoClicked().closeInventory();
					handler.invokeEvent(data.getEvent(), plugin.getServer().getPlayer(ice.getWhoClicked().getUniqueId()));
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent ice) {
		if(ice.getInventory().equals(inventory) && player.getUniqueId() == ice.getPlayer().getUniqueId())
			HandlerList.unregisterAll(this);
	}

}
