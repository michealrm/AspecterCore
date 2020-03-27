package net.aspecter.core.gui.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ItemInvokeData {

	private ItemStack itemStack;
	private String invokes;

	public ItemInvokeData() {}

	public ItemInvokeData(String invokes, ItemStack itemStack) {
		this.itemStack = itemStack;
		this.invokes = invokes;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public boolean equalsItem(ItemStack itemStack) {
		return this.itemStack.getType() == itemStack.getType() && ((this.itemStack.getItemMeta().getDisplayName() == null && itemStack.getItemMeta().getDisplayName() == null ) || this.itemStack.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()));
	}

	public String getInvokes() {
		return invokes;
	}

	public void setInvokes(String invokes) {
		this.invokes = invokes;
	}

	public String toString() {
		return ChatColor.translateAlternateColorCodes('&', itemStack.getItemMeta().getDisplayName() + "&f&7 (" + itemStack.getType() + ") invokes event &c&l" + itemStack.getType() + "&f\n");
	}

}
