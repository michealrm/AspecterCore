package net.aspecter.core;

import co.aikar.commands.PaperCommandManager;
import net.aspecter.core.commands.CoordsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class AspecterCore extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CoordsCommand());
    }

    @Override
    public void onDisable() {

    }

}
