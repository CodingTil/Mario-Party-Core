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
		String message = ChatColor.translateAlternateColorCodes('&', chatRecord.getMessage());
		message = message.replaceAll("\\{NUM_PLAYERS\\}", String.valueOf(chatRecord.getNumPlayers()));
		message = message.replaceAll("\\{MAX_PLAYERS\\}", String.valueOf(chatRecord.getMaxPlayers()));
		if (chatRecord.getPlayer() != null) {
			message = message.replaceAll("\\{PLAYER\\}", chatRecord.getPlayer().getName());
		}
		return plugin.PREFIX + plugin.PREFIX_SEPERATOR + message;
	}

}
