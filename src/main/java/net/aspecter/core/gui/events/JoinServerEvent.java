package net.aspecter.core.gui.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class JoinServerEvent extends AbstractEvent {

	private Plugin plugin;
	private String serverName;

	public JoinServerEvent(String event, Plugin plugin, String serverName) {
		super(event);
		this.plugin = plugin;
		this.serverName = serverName;
	}

	public static void joinBungee(Plugin plugin, String playerName, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		Bukkit.getPlayerExact(playerName).sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	@Override
	public void onEvent(String event, Player player) {
		joinBungee(plugin, player.getName(), serverName);
	}
}
