package net.aspecter.core.gui.listeners;

import net.aspecter.core.gui.handlers.EasyGUIHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryEditListener implements Listener {

	private EasyGUIHandler handler;
	private Player player;
	private Inventory inventory;
	private String event;

	public InventoryEditListener(String event, EasyGUIHandler handler, Player player, Inventory inventory) {
		this.event = event;
		this.handler = handler;
		this.player = player;
		this.inventory = inventory;
		player.sendMessage(ChatColor.GREEN + "Editing " + ChatColor.DARK_GREEN + "[" + event + ChatColor.DARK_GREEN + "]");
		player.openInventory(inventory);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent ice) {
		if(ice.getInventory().equals(inventory) && player.getUniqueId() == ice.getPlayer().getUniqueId()) {
			handler.registerAndStoreGUI(event, ice.getInventory());
			player.sendMessage(ChatColor.GREEN + "Saved GUI event " + ChatColor.DARK_GREEN + "[" + event + ChatColor.DARK_GREEN + "]");
			ice.getHandlers().unregister(this);
		}
	}
}
