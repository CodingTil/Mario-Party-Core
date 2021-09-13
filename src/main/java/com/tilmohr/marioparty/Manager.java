package com.tilmohr.marioparty;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Manager implements Listener {

	protected App plugin;

	public Manager(App plugin) {
		this.plugin = plugin;
	}

	public void registerEvents() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterEvents() {
		HandlerList.unregisterAll(this);
	}

	public boolean start() {
		registerEvents();
		return true;
	};

	public boolean stop() {
		unregisterEvents();
		return true;
	}
}
