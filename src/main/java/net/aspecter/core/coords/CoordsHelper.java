package net.aspecter.core.coords;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CoordsHelper {
    static String formatLocation(Player player) {
        Location loc = player.getLocation();
        return formatWorldLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }

    static String formatCurrentLocation(Player player) {
        Location loc = player.getLocation();
        return formatCurrentLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    static String formatCurrentLocation(double x, double y, double z) {
        return String.format("%d %d %d", (int) x, (int) y, (int) z);
    }

    static String formatWorldLocation(String world, String commaSeparatedCoords) {
        double[] coords = Arrays.stream(commaSeparatedCoords.split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
        return formatWorldLocation(world, coords[0], coords[1], coords[2]);
    }

    static String formatWorldLocation(String world, double x, double y, double z) {
        if (world.equals("world"))
            return formatCurrentLocation(x, y, z);
        return String.format("%s [%s]", formatCurrentLocation(x, y, z), world);
    }
}