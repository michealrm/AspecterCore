package net.aspecter.core.coords;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class CoordsStorage {
    static YamlConfiguration data;
    static File file;

    static {
        file = new File("plugins/Core/coords/data.yml");
        data = YamlConfiguration.loadConfiguration(file);
    }

    static int getCoordsCount(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!data.contains(uuid))
            return 0;
        else
            return data.getConfigurationSection(uuid).getKeys(false).size();
    }

    static Set<String> getLocationNamesFromFile(Player player) {
        String uuid = player.getUniqueId().toString();
        if (data.contains(uuid))
            return data.getConfigurationSection(uuid).getKeys(false);
        else
            return Collections.emptySet();
    }

    static void saveData() {
        try {
            data.save(file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    static void saveLocationToFile(Player player, String locationName) {
        Location loc = player.getLocation();
        String basePath = getBasePath(player, locationName);
        data.set(
                basePath + "world",
                loc.getWorld().getName()
        );
        data.set(
                basePath + "coords",
                String.format("%.02f,%.02f,%.02f", loc.getX(), loc.getY(), loc.getZ())
        );
    }

    static String getLocationFromFile(Player player, String locationName) {
        String basePath = getBasePath(player, locationName);

        String world = data.getString(basePath + "world");
        String coords = data.getString(basePath + "coords");

        return CoordsHelper.formatWorldLocation(world, coords);

    }

    static void deleteLocationFromFile(Player player, String locationName) {
        String path = getBasePath(player, locationName);
        path = path.substring(0, path.length() - 1);
        data.set(path, null);
    }

    static String getLocation(Player player, @Nullable String locationName) {
        if (locationName == null)
            return CoordsHelper.formatLocation(player);
        else
            return getLocationFromFile(player, locationName);

    }

    static boolean isLocationInFile(Player player, String locationName) {
        return data.contains(getBasePath(player, locationName) + "coords");
    }

    private static String getBasePath(Player player, String locationName) {
        return String.format("%s.%s.", player.getUniqueId(), locationName);
    }

}