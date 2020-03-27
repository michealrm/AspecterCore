package net.aspecter.core.gui.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import net.aspecter.core.gui.events.EventData;
import net.aspecter.core.gui.events.data.JoinOtherServerEventData;
import net.aspecter.core.gui.events.data.MessageEventData;
import net.aspecter.core.gui.util.GUIEvent;
import net.aspecter.core.gui.util.ItemInvokeData;
import net.aspecter.core.gui.dao.Storage;
import net.aspecter.core.gui.events.data.InventoryEventData;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * NOTE: This Storage strategy doesn't implement a caching system, so it may not be good for iterating in a short amount of time
 */
public class MongoStorage implements Storage {

	private MongoDatabase db;
	private MongoCollection<Document> gui_events;
	private MongoCollection<Document> event_invoke;
	private Plugin plugin;
	private Logger log;

	public MongoStorage(Plugin plugin, Logger log, MongoDatabase db) {
		this.plugin = plugin;
		this.log = log;
		this.db = db;
		gui_events = db.getCollection("gui_events");
		event_invoke = db.getCollection("event_invoke");
	}

	@Override
	public HashMap<String, List<GUIEvent>> getGUIEventData() {
		HashMap<String, List<GUIEvent>> data = new HashMap<>();
		for(Document document : gui_events.find()) {
			try {
				EventData eventData = getEventDataFromDocument(document);
				if(eventData != null) {
					List<GUIEvent> list = data.computeIfAbsent(eventData.getEvent(), eventKey -> new ArrayList<>());
					list.add(eventData.getGuiEvent());
				}
			} catch(Exception e) {
				log.warning(e.getMessage());
			}
		}
		return data;
	}

	@Override
	public HashMap<String, List<EventData>> getEventData() {
		HashMap<String, List<EventData>> data = new HashMap<>();
		for(Document document : gui_events.find()) {
			try {
				EventData eventData = getEventDataFromDocument(document);
				if(eventData != null) {
					List<EventData> list = data.computeIfAbsent(eventData.getEvent(), eventKey -> new ArrayList<>());
					list.add(eventData);
				}
			} catch(Exception e) {
				log.warning(e.getMessage());
			}
		}
		return data;
	}

	/**
	 * @return List of event data. Note: This only supports inventory and message events
	 */
	@Override
	public List<EventData> getEventData(String event) {
		List<EventData> data = new ArrayList<>();
		for(Document document : gui_events.find(new Document("event", event))) {
			try {
				EventData eventData = getEventDataFromDocument(document);
				if(eventData != null)
					data.add(eventData);
			} catch(Exception e) {
				log.warning(e.getMessage());
			}
		}
		return data;
	}

	private EventData getEventDataFromDocument(Document document) throws Exception {
		if(document.getString("type").equals("inventory"))
			return new InventoryEventData(document.getString("event"), getInventory(document));
		if(document.getString("type").equals("message"))
			return new MessageEventData(document.getString("event"), document.getString("message"));
		if(document.getString("type").equals("join-bungee"))
			return new JoinOtherServerEventData(plugin, document.getString("event"), document.getString("server"));
		return null;
	}

	@Override
	public List<ItemInvokeData> getInvokeData() {
		List<ItemInvokeData> data = new ArrayList<>();
		for(Document document : event_invoke.find()) {
			String invokes = document.getString("invokes");
			try {
				data.add(new ItemInvokeData(invokes, getItemStack(document)));
			} catch(Exception e) {
				log.warning(e.getMessage());
			}
		}
		return data;
	}

	@Override
	public void upsertItemInvokerData(String invokes, ItemStack stack) {
		Document document = getItemDocument(stack)
				.append("invokes", invokes);
		Document findBy = getItemDocument(stack)
				.append("invokes", invokes);
		findBy.remove("name");
		event_invoke.replaceOne(findBy, document, new UpdateOptions().upsert(true));
	}

	@Override
	public boolean removeInvokeData(ItemInvokeData invokeData) {
		return event_invoke.deleteMany(getItemDocument(invokeData.getItemStack()).append("invokes", invokeData.getInvokes())).getDeletedCount() > 0;
	}

	@Override
	public void upsertEventData(EventData data) {
		if(data instanceof InventoryEventData) {
			InventoryEventData invData = (InventoryEventData)data;
			Document replaceWith = getInventoryDocument(invData.getInventory())
					.append("event", invData.getEvent())
					.append("type", "inventory");
			gui_events.replaceOne(newEventDocument(invData.getEvent(), "inventory") /* There can only be one inventory opened */, replaceWith, new UpdateOptions().upsert(true));
		} else if(data instanceof MessageEventData) { // Plain insert here since we want to support multiple messages
			MessageEventData msgData = (MessageEventData)data;
			gui_events.insertOne(newEventDocument(msgData.getEvent(), "message").append("message", msgData.getMessage()));
		} else if(data instanceof JoinOtherServerEventData) {
			JoinOtherServerEventData joinData = (JoinOtherServerEventData)data;
			Document document = new Document()
					.append("event",  joinData.getEvent())
					.append("type", "join-bungee")
					.append("server", joinData.getServerName());
			gui_events.replaceOne(newEventDocument(data.getEvent(), "join-bungee").append("join-bungee", joinData.getServerName()) /* There can only be one join */, document, new UpdateOptions().upsert(true));
		}
	}

	private Document newEventDocument(String event, String type) {
		return new Document()
				.append("event", event)
				.append("type", type);
	}

	private ItemStack getItemStack(Document itemDoc) {
		if(itemDoc == null)
			return null;
		ItemStack itemStack = new ItemStack(Material.valueOf(itemDoc.getString("material")));
		itemStack.setDurability(itemDoc.getInteger("durability").shortValue());
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(itemDoc.getString("name"));
		meta.setLore((List<String>)itemDoc.get("lore"));
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	private Document getItemDocument(ItemStack itemStack) {
		if(itemStack == null)
			return null;
		return new Document()
				.append("material", itemStack.getType().toString())
				.append("name", itemStack.getItemMeta().getDisplayName())
				.append("lore", itemStack.getItemMeta().getLore())
				.append("durability", itemStack.getDurability());
	}

	private Inventory getInventory(Document document) {
		String name = document.getString("name");
		int size = document.getInteger("size");
		Document inv = (Document)document.get("inventory");
		Inventory inventory = Bukkit.createInventory(null, size, name);
		for(int i = 0; i < size - 1; i++) {
			inventory.setItem(i, getItemStack((Document)inv.get(String.valueOf(i))));
		}
		return inventory;
	}

	private Document getInventoryDocument(Inventory inventory) {
		Document document = new Document()
				.append("name", inventory.getName())
				.append("size", inventory.getSize());
		HashMap<String, Document> invHM = new HashMap<>();
		for(int i = 0; i < inventory.getSize(); i++)
			invHM.put(String.valueOf(i), getItemDocument(inventory.getItem(i)));
		document.append("inventory", invHM);
		return document;
	}

	@Override
	public boolean removeEventData(String event) {
		return gui_events.deleteMany(new Document("event", event)).getDeletedCount() > 0;
	}

	@Override
	public boolean removeEventData(EventData data) {
		if(data instanceof InventoryEventData) {
			Document document = new Document()
					.append("event", data.getEvent())
					.append("type", "inventory");
			return gui_events.deleteMany(document).getDeletedCount() > 0;
		} else if(data instanceof MessageEventData) {
			MessageEventData msgData = (MessageEventData)data;
			Document document = new Document()
					.append("event", msgData.getEvent())
					.append("type", "message")
					.append("message", msgData.getMessage());
			return gui_events.deleteMany(document).getDeletedCount() > 0;
		} else if(data instanceof JoinOtherServerEventData) {
			Document document = new Document()
					.append("event", data.getEvent())
					.append("type", "join-bungee");
			return gui_events.deleteMany(document).getDeletedCount() > 0;
		}
		return false;
	}
}
