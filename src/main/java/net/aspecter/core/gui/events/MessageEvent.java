package net.aspecter.core.gui.events;

import org.bukkit.entity.Player;

public class MessageEvent extends AbstractEvent {

	private String msg;
	public MessageEvent(String event, String msg) {
		super(event);
		this.msg = msg;
	}

	@Override
	public void onEvent(String event, Player player) {
		player.sendMessage(msg);
	}
}
