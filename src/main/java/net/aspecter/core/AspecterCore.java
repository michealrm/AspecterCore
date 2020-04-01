package net.aspecter.core;

import co.aikar.commands.PaperCommandManager;
import net.aspecter.core.commands.CoordsCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class AspecterCore extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CoordsCommand());
    }

    @Override
    public void onDisable() {

    }

    public BukkitScheduler getScheduler()
    {
        return this.getServer().getScheduler();
    }

    public BukkitTask runTaskAsynchronously(final Runnable run)
    {
        return this.getScheduler().runTaskAsynchronously(this, run);
    }

    public BukkitTask runTaskLaterAsynchronously(final Runnable run, final long delay)
    {
        return this.getScheduler().runTaskLaterAsynchronously(this, run, delay);
    }

    public BukkitTask runTaskTimerAsynchronously(final Runnable run, final long delay, final long period)
    {
        return this.getScheduler().runTaskTimerAsynchronously(this, run, delay, period);
    }

    public int scheduleSyncDelayedTask(final Runnable run)
    {
        return this.getScheduler().scheduleSyncDelayedTask(this, run);
    }

    public int scheduleSyncDelayedTask(final Runnable run, final long delay)
    {
        return this.getScheduler().scheduleSyncDelayedTask(this, run, delay);
    }

    public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period)
    {
        return this.getScheduler().scheduleSyncRepeatingTask(this, run, delay, period);
    }

}
