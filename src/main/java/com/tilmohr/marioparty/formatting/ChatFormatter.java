package com.tilmohr.marioparty.formatting;

import com.tilmohr.marioparty.App;

import org.bukkit.ChatColor;

public class ChatFormatter {

	App plugin;

	public ChatFormatter(App plugin) {
		this.plugin = plugin;
	}

	public String format(String message) {
		return format(new ChatRecord(message));
	}

	public String format(ChatRecord chatRecord) {
		return plugin.PREFIX + plugin.PREFIX_SEPERATOR + chatRecord;
	}

}
