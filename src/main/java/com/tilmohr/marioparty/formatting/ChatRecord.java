package com.tilmohr.marioparty.formatting;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatRecord {
	private final String message;
	private int numPlayers;
	private final int maxPlayers;
	private Player player;

	public ChatRecord(@NotNull String message) {
		this.message = message;
		this.numPlayers = Bukkit.getOnlinePlayers().size();
		this.maxPlayers = Bukkit.getMaxPlayers();
	}

	/**
	 * For Copying
	 *
	 * @return
	 */
	private ChatRecord(@NotNull String message, int numPlayers, int maxPlayers, Player player) {
		this.message = message;
		this.numPlayers = numPlayers;
		this.maxPlayers = maxPlayers;
		this.player = player;
	}

	public ChatRecord numPlayers(int numPlayers) {
		return new ChatRecord(this.message, numPlayers, this.maxPlayers, this.player);
	}

	public ChatRecord player(Player player) {
		return new ChatRecord(this.message, this.numPlayers, this.maxPlayers, player);
	}

	public String getMessage() {
		return message;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public Player getPlayer() {
		return player;
	}

}
