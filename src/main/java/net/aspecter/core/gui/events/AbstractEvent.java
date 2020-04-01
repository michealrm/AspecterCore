package net.aspecter.core.gui.events;

import org.bukkit.entity.Player;

public abstract class AbstractEvent {
	String event;
	public AbstractEvent(String event) {
		this.event = event;
	}
	abstract void onEvent(String event, Player player);
}
