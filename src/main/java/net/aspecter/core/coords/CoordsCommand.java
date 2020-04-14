package net.aspecter.core.coords;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import net.aspecter.core.AspecterCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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

    public CoordsCommand(PaperCommandManager manager) {

        manager.getCommandCompletions().registerCompletion("coords.locationnames", context -> CoordsStorage.getLocationNamesFromFile(context.getPlayer()));

        AspecterCore.shutdownHooks.add(() -> {
            CoordsStorage.saveData();
        });
    }

    @Default
    public void onChat(Player player) {
        player.chat(CoordsStorage.getLocation(player, null));
    }

    @Subcommand("list")
    public void onList(Player player) {
        String uuid = player.getUniqueId().toString();

        if(CoordsStorage.getCoordsCount(player) == 0) {
            player.sendMessage("No coords saved. Try /coords save [name]");
            return;
        }

        ConfigurationSection locs = CoordsStorage.data.getConfigurationSection(uuid);
        for(String name : locs.getKeys(false)) {
            ConfigurationSection locSection = locs.getConfigurationSection(name);
            String world = locSection.getString("world");
            String coords = locSection.getString("coords");

            player.sendMessage(
                    String.format("%s: %s", name, CoordsHelper.formatWorldLocation(world, coords))
            );
        }
    }

    @Subcommand("save")
    public void onSave(Player player, String name) {

        if(CoordsStorage.getCoordsCount(player) >= 100) {
            player.sendMessage("What are you using for more than 100 coords..? Message Aspecting. Otherwise, NEXT!");
            return;
        }

        CoordsStorage.saveLocationToFile(player, name);
        player.sendMessage(String.format("Saved \"%s\" as %s", CoordsHelper.formatCurrentLocation(player), name));
    }

    @Subcommand("delete")
    @CommandCompletion("@coords.locationnames")
    public void onDelete(Player player, @Values("@coords.locationnames") String name) {
        if (CoordsStorage.isLocationInFile(player, name)) {
            CoordsStorage.deleteLocationFromFile(player, name);
            player.sendMessage(String.format("%s has been deleted", name));
        } else {
            player.sendMessage(String.format("%s not found", name)); // TODO: Use localization or config-side messages
        }
    }

    @Subcommand("say|show")
    @CommandCompletion("@coords.locationnames")
    public void onChat(Player player, @Values("@coords.locationnames") @Optional String name) {
        if(name == null || CoordsStorage.isLocationInFile(player, name)) {
            player.chat(CoordsStorage.getLocation(player, name));
        }
        else {
            player.sendMessage(String.format("%s not found", name)); // TODO: Use localization or config-side messages
        }
    }

    @Subcommand("pm")
    @CommandCompletion("@coords.locationnames")
    public void onPm(Player player, @Values("@coords.locationnames") @Optional String name) {
        player.sendMessage(CoordsStorage.getLocation(player, name));
    }

}
