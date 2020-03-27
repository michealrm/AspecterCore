package net.aspecter.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SummonCommand extends BaseCommand {

    public CoordsCommand() {
    }

    @CommandAlias("summon")
    public void onSummon(Player player) {
        Location loc = player.getLocation();
        player.chat(String.format("%d %d %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    @CommandAlias("coords")
    public void onCoords(Player player) {
        Location loc = player.getLocation();
        player.chat(String.format("%d %d %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

}
