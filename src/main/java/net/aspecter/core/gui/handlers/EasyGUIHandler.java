package net.aspecter.core.gui.handlers;

import net.aspecter.core.gui.dao.Storage;
import net.aspecter.core.gui.events.data.EventData;
import net.aspecter.core.gui.events.JoinServerEvent;
import net.aspecter.core.gui.events.MessageEvent;
import net.aspecter.core.gui.listeners.InventoryEventListener;
import net.aspecter.core.gui.events.AbstractEvent;
import net.aspecter.core.gui.util.ItemInvoker;
import net.aspecter.core.gui.events.InventoryEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Logger;

/**
 * TODO: Add caching option
 */
public class EasyGUIHandler {

	private HashMap<String, List<AbstractEvent>> events;
	private Storage storage;
	private List<ItemInvoker> invokers;
	private Plugin plugin;
	private Logger log;

	/**
	 * Loads the events from the storage type
	 */
	public EasyGUIHandler(Plugin plugin, Storage storage) {
		this.plugin = plugin;
		this.log = plugin.getLogger();
		this.storage = storage;
		events = storage.getEventData();
		invokers = storage.getInvokeData();
	}

	public Set<String> getEventNames() {
		return events.keySet();
	}

	public boolean eventExists(String event) {
		return events.get(event) != null;
	}

	public void registerEvent(EventData eventData) {
		List<EventData> list = events.computeIfAbsent(eventData.getEvent(), eventKey -> new ArrayList<>());
		list.add(eventData);
		events.put(eventData.getEvent(), list);
	}

	public void registerAndStoreGUI(String event, Inventory inventory) {
		storage.saveEventData(new InventoryEvent(event, inventory));
		registerGUI(event, inventory);
	}

	public void registerGUI(String event, Inventory inventory) {
		deleteInventory(event);
		registerEvent(new InventoryEvent(event, inventory));
	}

	private void deleteInventory(String event) {
		List<EventData> list = events.get(event);
		if(list != null) {
			for(int i = 0; i < list.size(); i++)
				if(list.get(i) instanceof InventoryEvent)
					list.remove(i--);
		}
	}

	public void registerMessage(String event, String message) {
		registerEvent(new MessageEvent(event, message));
	}

	public void registerAndStoreMessage(String event, String message) {
		storage.saveEventData(new MessageEvent(event, message));
		registerMessage(event, message);
	}

	public void registerJoin(String event, String join) {
		registerEvent(new JoinServerEvent(plugin, event, join));
	}

	public void registerAndStoreJoin(String event, String join) {
		JoinServerEvent data = new JoinServerEvent(plugin, event, join);
		storage.saveEventData(data);
		registerJoin(event, join);
	}

	public void registerData(EventData data) {
		storage.saveEventData(data);
		registerEvent(data);
	}

	public boolean invokeEvent(String event, Player player) {
		List<EventData> list = events.get(event);
		if(list == null)
			return false;
		else {
			for(EventData data : list) {
				if(data instanceof InventoryEvent) {
					plugin.getServer().getPluginManager().registerEvents(new InventoryEventListener(this, plugin, getInvokeData(), player, ((InventoryEvent)data).getInventory()), plugin);
				}
				log.info(player.getName() + " invoked " + event);
				data.getiEvent().onEvent(event, player);
			}
			return true;
		}
	}

	public boolean unregisterEvent(String event) {
		List<EventData> list = events.get(event);
		if(list == null)
			return false;
		else {
			events.put(event, null);
			return true;
		}
	}

	public boolean unregisterAndRemoveEvent(String event) {
		return unregisterEvent(event) && storage.removeEventData(event);
	}

	public boolean unregisterEvent(String event, AbstractEvent abstractEvent) {
		boolean exists = eventExists(event);
		events.put(event, null);
		return exists;
	}

	public void registerInvokeData(ItemInvoker data) {
		invokers.add(data);
	}

	public void registerAndStoreInvokeData(ItemInvoker data) {
		storage.saveItemInvokerData(data.getEvent(), data.getItemStack());
		registerInvokeData(data);
	}

	public List<ItemInvoker> getInvokeData() {
		return invokers;
	}

	public Inventory getInventory(String event) {
		List<EventData> eventList = events.get(event);
		if(eventList == null)
			return null;
		for(EventData data : eventList)
			if(data instanceof InventoryEvent)
				return ((InventoryEvent)data).getInventory();
		return null;
	}

}