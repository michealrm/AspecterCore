package net.aspecter.core.gui.events;

import net.aspecter.core.gui.util.ItemInvoker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.net.http.WebSocket;

/**
 * TODO: Will need to call the GUI handler to get the correct inventory, for now I'll just pass in a inventory object
 */
public class InventoryEvent extends AbstractEvent implements Listener {

	private final Player player;
	private Inventory inventory;

	class InventoryEditData {
		Player player;

	}

	public InventoryEvent(String event, Player player, Inventory inventory) {
		super(event);
		this.player = player;
		this.inventory = inventory;
	}

	@Override
	public void onEvent(String event, Player player) {
		player.openInventory(inventory);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent ice) {
		if(!ice.getInventory().equals(inventory) || ice.getWhoClicked().getUniqueId() != player.getUniqueId())
			return;

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

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent ice) {
		if(ice.getInventory().equals(inventory) && player.getUniqueId() == ice.getPlayer().getUniqueId())
			HandlerList.unregisterAll(this);
	}
}
