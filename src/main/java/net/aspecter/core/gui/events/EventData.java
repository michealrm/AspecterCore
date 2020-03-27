package net.aspecter.core.gui.events;

import net.aspecter.core.gui.util.GUIEvent;

public class EventData {

	private String event;
	private GUIEvent guiEvent;

	public EventData(String event, GUIEvent guiEvent) {
		this.event = event;
		this.guiEvent = guiEvent;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public GUIEvent getGuiEvent() {
		return guiEvent;
	}

	public void setGuiEvent(GUIEvent guiEvent) {
		this.guiEvent = guiEvent;
	}

}
