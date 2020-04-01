package net.aspecter.core.gui.util;

import org.bukkit.inventory.ItemStack;

/**
 * An item that invokes an event
 * TODO: Possibly context
 */
public class ItemInvoker {

	public ItemStack itemStack;
	public String event;

	public ItemInvoker() {}

	public ItemInvoker(ItemStack itemStack, String event) {
		this.itemStack = itemStack;
		this.event = event;
	}

	public boolean equalsItem(ItemStack itemStack) {
		return this.itemStack.getType() == itemStack.getType() && this.itemStack.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName());
	}

}
