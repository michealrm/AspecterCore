package net.aspecter.core.coords;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CoordsDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent pde) {
        Player player = pde.getEntity();
        CoordsStorage.saveLocationToFile(player, "death");
        player.sendMessage(String.format("You died at %s", CoordsHelper.formatCurrentLocation(player) ));
    }

}
