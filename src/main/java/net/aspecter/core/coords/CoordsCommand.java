package net.aspecter.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import net.aspecter.core.AspecterCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * /coords - same as coords show
 * /coords save [name] - lists name
 * /coords chat [name]
 * /coords pm [name]
 * /coords delete [name]
 *
 * Coords are stored player -> name -> info
 *
 * This should be eventually sorted from most function uses to least
 *
 */

@CommandAlias("coords")
public class CoordsCommand extends BaseCommand {

    private YamlConfiguration data;
    private File file;

    public CoordsCommand(PaperCommandManager manager) {
        file = new File("plugins/Core/coords/data.yml");
        data = YamlConfiguration.loadConfiguration(file);

        manager.getCommandCompletions().registerCompletion("coords.locationnames", context -> getLocationNamesFromFile(context.getPlayer()));

        AspecterCore.shutdownHooks.add(() -> {
            saveData();
        });
    }

    @Default
    public void onChat(Player player) {
        player.chat(getLocation(player, null));
    }

    @Subcommand("list")
    public void onList(Player player) {
        String uuid = player.getUniqueId().toString();

        if(getCoordsCount(player) == 0) {
            player.sendMessage("No coords saved. Try /coords save [name]");
            return;
        }

        ConfigurationSection locs = data.getConfigurationSection(uuid);
        for(String name : locs.getKeys(false)) {
            ConfigurationSection locSection = locs.getConfigurationSection(name);
            String world = locSection.getString("world");
            String coords = locSection.getString("coords");

            player.sendMessage(
                    String.format("%s: %s", name, formatWorldLocation(world, coords))
            );
        }
    }

    @Subcommand("save")
    public void onSave(Player player, String name) {

        if(getCoordsCount(player) >= 100) {
            player.sendMessage("It was a valiant effort, but no, you cannot add more than 100 coords");
            return;
        }

        saveLocationToFile(player, name);
        player.sendMessage(String.format("Saved \"%s\" as %s", formatCurrentLocation(player), name));
    }

    @Subcommand("delete")
    @CommandCompletion("@coords.locationnames")
    public void onDelete(Player player, @Values("@coords.locationnames") String name) {
        if (isLocationInFile(player, name)) {
            deleteLocationFromFile(player, name);
            player.sendMessage(String.format("%s has been deleted", name));
        } else {
            player.sendMessage(String.format("%s not found", name)); // TODO: Use localization or config-side messages
        }
    }

    @Subcommand("say|show")
    @CommandCompletion("@coords.locationnames")
    public void onChat(Player player, @Values("@coords.locationnames") @Optional String name) {
        if(name == null || isLocationInFile(player, name)) {
            player.chat(getLocation(player, name));
        }
        else {
            player.sendMessage(String.format("%s not found", name)); // TODO: Use localization or config-side messages
        }
    }

    @Subcommand("pm")
    @CommandCompletion("@coords.locationnames")
    public void onPm(Player player, @Values("@coords.locationnames") @Optional String name) {
        player.sendMessage(getLocation(player, name));
    }

    private int getCoordsCount(Player player) {
        String uuid = player.getUniqueId().toString();
        if(!data.contains(uuid))
            return 0;
        else
            return data.getConfigurationSection(uuid).getKeys(false).size();
    }

    private Set<String> getLocationNamesFromFile(Player player) {
        String uuid = player.getUniqueId().toString();
        if(data.contains(uuid))
            return data.getConfigurationSection(uuid).getKeys(false);
        else
            return Collections.emptySet();
    }

    private boolean isLocationInFile(Player player, String locationName) {
        return data.contains(getBasePath(player, locationName) + "coords");
    }

    private void saveData() {
        try {
            data.save(file);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Saves coords and world from player location
     */
    public static void saveLocationToFile(Player player, String locationName) {
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

    private String getLocationFromFile(Player player, String locationName) {
        String basePath = getBasePath(player, locationName);

        String world = data.getString(basePath + "world");
        String coords = data.getString(basePath + "coords");

        return formatWorldLocation(world, coords);

    }

    private void deleteLocationFromFile(Player player, String locationName) {
        String path = getBasePath(player, locationName);
        path = path.substring(0, path.length() - 1);
        data.set(path, null);
    }

    public static String getBasePath(Player player, String locationName) {
        return String.format("%s.%s.", player.getUniqueId(), locationName);
    }

    private String getLocation(Player player, @Nullable String locationName) {
        if(locationName == null)
            return formatLocation(player);
        else
            return getLocationFromFile(player, locationName);

    }

    private String formatLocation(Player player) {
        Location loc = player.getLocation();
        return formatWorldLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
    }

    private String formatCurrentLocation(Player player) {
        Location loc = player.getLocation();
        return formatCurrentLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    private String formatCurrentLocation(double x, double y, double z) {
        return String.format("%d %d %d", (int)x, (int)y, (int)z);
    }

    private String formatWorldLocation(String world, String commaSeparatedCoords) {
        double[] coords = Arrays.stream(commaSeparatedCoords.split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
        return formatWorldLocation(world, coords[0], coords[1], coords[2]);
    }

    private String formatWorldLocation(String world, double x, double y, double z) {
        if(world.equals("world"))
            return formatCurrentLocation(x, y, z);
        return String.format("%s [%s]", formatCurrentLocation(x, y, z), world);
    }

}
