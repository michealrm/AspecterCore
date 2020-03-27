package net.aspecter.core.gui.dao;

import net.aspecter.core.gui.events.EventData;
import net.aspecter.core.gui.util.GUIEvent;
import net.aspecter.core.gui.util.ItemInvokeData;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public interface Storage {

	HashMap<String, List<GUIEvent>> getGUIEventData();
	HashMap<String, List<EventData>> getEventData();
	List<EventData> getEventData(String event);
	List<ItemInvokeData> getInvokeData();
	boolean removeInvokeData(ItemInvokeData invokeData);
	void upsertItemInvokerData(String invokes, ItemStack stack);
	void upsertEventData(EventData data);
	boolean removeEventData(String event);
	boolean removeEventData(EventData data);

}
