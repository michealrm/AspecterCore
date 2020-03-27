package net.aspecter.core.gui.events.data;

import net.aspecter.core.gui.events.EventData;
import org.bukkit.inventory.Inventory;

public class InventoryEventData extends EventData {

	private Inventory inventory;

	public InventoryEventData(String event, Inventory inventory) {
		super(event, (eventName, player) -> player.openInventory(inventory));
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

}
