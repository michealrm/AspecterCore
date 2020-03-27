package net.aspecter.core.gui.events.data;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aspecter.core.gui.events.EventData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class JoinOtherServerEventData extends EventData {

	private String serverName;

	public JoinOtherServerEventData(Plugin plugin, String event, String serverName) {
		super(event, (eventName, player) -> joinBungee(plugin, player.getName(), serverName));
		this.serverName = serverName;
	}

	public static void joinBungee(Plugin plugin, String playerName, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		Bukkit.getPlayerExact(playerName).sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
