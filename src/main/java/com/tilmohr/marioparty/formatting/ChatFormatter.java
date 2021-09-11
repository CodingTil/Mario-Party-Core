package com.tilmohr.marioparty.formatting;

import com.tilmohr.marioparty.App;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatFormatter {

	App plugin;

	public ChatFormatter(JavaPlugin plugin) {
		this.plugin = (App) plugin;
	}

	public String format(ChatRecord chatRecord) {
		String message = ChatColor.translateAlternateColorCodes('&', chatRecord.getMessage());
		message = message.replaceAll("\\{NUM_PLAYERS\\}", String.valueOf(chatRecord.getNumPlayers()));
		message = message.replaceAll("\\{MAX_PLAYERS\\}", String.valueOf(chatRecord.getMaxPlayers()));
		if (chatRecord.getPlayer() != null) {
			message = message.replaceAll("\\{PLAYER\\}", chatRecord.getPlayer().getName());
		}
		return message;
	}
}
