package net.aspecter.core.gui;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import net.aspecter.core.gui.dao.impl.MongoStorage;
import net.aspecter.core.gui.commands.GUICommand;
import net.aspecter.core.gui.handlers.EasyGUIHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class EasyGUI extends JavaPlugin {

	private MongoClient mongoClient;
	private MongoStorage storage;
	private EasyGUIHandler handler;
	private Logger log;

	public void onEnable() {

		// BungeeCoord
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		// Logger
		log = getLogger();

		// Configuration
		saveDefaultConfig();
		final String MONGO_CONNECTION_STRING = getConfig().getString("MONGO_URI");
		final String MONGO_DB = getConfig().getString("MONGO_DB");
		mongoClient = new MongoClient(new MongoClientURI(MONGO_CONNECTION_STRING));
		MongoDatabase mongoDB = mongoClient.getDatabase(MONGO_DB);
		log.info("Connected to Mongo");

		// Setup
		storage = new MongoStorage(this, log, mongoDB);
		handler = new EasyGUIHandler(this, storage);

		getCommand("gui").setExecutor(new GUICommand(this, handler));

		// Ready
		log.info("EasyGUI enabled");
	}

	public void onDisable() {
		mongoClient.close();

		log.info("EasyGUI disabled");
	}

	public MongoStorage getStorage() {
		return storage;
	}

	public EasyGUIHandler getHandler() {
		return handler;
	}
}