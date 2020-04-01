package net.aspecter.core.oldstorae;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

import net.aspecter.core.AspecterCore;
import org.bukkit.Bukkit;


public abstract class AbstractDelayedYamlFileReader<T extends StorageObject> implements Runnable
{
	private final transient File file;
	private final transient Class<T> clazz;
	protected final transient AspecterCore plugin;

	public AbstractDelayedYamlFileReader(final AspecterCore ac, final File file, final Class<T> clazz)
	{
		this.file = file;
		this.clazz = clazz;
		this.plugin = ac;
		ac.runTaskAsynchronously(this);
	}

	public abstract void onStart();

	@Override
	public void run()
	{
		onStart();
		try
		{
			final FileReader reader = new FileReader(file);
			try
			{
				final T object = new YamlStorageReader(reader, plugin).load(clazz);
				onSuccess(object);
			}
			finally
			{
				try
				{
					reader.close();
				}
				catch (IOException ex)
				{
					Bukkit.getLogger().log(Level.SEVERE, "File can't be closed: " + file.toString(), ex);
				}
			}

		}
		catch (FileNotFoundException ex)
		{
			onException();
			Bukkit.getLogger().log(Level.INFO, "File not found: " + file.toString());
		}
		catch (ObjectLoadException ex)
		{
			onException();
			Bukkit.getLogger().log(Level.SEVERE, "File broken: " + file.toString(), ex.getCause());
		}
	}

	public abstract void onSuccess(T object);

	public abstract void onException();
}
