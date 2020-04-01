package net.aspecter.core.oldstorae;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;

import net.aspecter.core.AspecterCore;
import org.bukkit.Bukkit;


public abstract class AbstractDelayedYamlFileWriter implements Runnable
{
	private final transient File file;

	public AbstractDelayedYamlFileWriter(AspecterCore ac, File file)
	{
		this.file = file;
		ac.runTaskAsynchronously(this);
	}

	public abstract StorageObject getObject();

	@Override
	public void run()
	{
		PrintWriter pw = null;
		try
		{
			final StorageObject object = getObject();
			final File folder = file.getParentFile();
			if (!folder.exists())
			{
				folder.mkdirs();
			}
			pw = new PrintWriter(file);
			new YamlStorageWriter(pw).save(object);
		}
		catch (FileNotFoundException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, file.toString(), ex);
		}
		finally
		{
			onFinish();
			if (pw != null)
			{
				pw.close();
			}
		}

	}

	public abstract void onFinish();
}
