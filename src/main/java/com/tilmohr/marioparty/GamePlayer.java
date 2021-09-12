package com.tilmohr.marioparty;

import org.bukkit.entity.Player;

public class GamePlayer {

	private App plugin;
	private Player player;

	public GamePlayer(App plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	public int rollDie(GameDie die) {
		/*
		 * Bukkit Runnable: - Register Events (listen for click) - Click if player runs
		 * out of time - return random possible result - Unregister Events
		 */
		return 0;
	}

	public boolean isOnline() {
		return player.isOnline();
	}

	public boolean equalsPlayer(Player player) {
		return this.player.getUniqueId().equals(player.getUniqueId());
	}

	public static GamePlayer playerIn(Iterable<GamePlayer> iterable, Player player) {
		for (GamePlayer p : iterable) {
			if (p.equalsPlayer(player)) {
				return p;
			}
		}
		return null;
	}

}
