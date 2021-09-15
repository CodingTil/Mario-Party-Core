package com.tilmohr.marioparty;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Manager implements Listener {

	protected App plugin;

	public Manager(App plugin) {
		this.plugin = plugin;
	}

	public void register() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	public boolean start() {
		register();
		return true;
	};

	public boolean stop() {
		unregister();
		return true;
	}
}
