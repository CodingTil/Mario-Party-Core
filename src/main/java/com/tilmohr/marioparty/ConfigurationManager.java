package com.tilmohr.marioparty;

import com.tilmohr.marioparty.formatting.ChatRecord;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class ConfigurationManager extends Manager {

	public ConfigurationManager(App plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("marioparty.configure")) {
			e.disallow(Result.KICK_OTHER, "The server is being configured. You do not have the permission to join.");
			plugin.getLogger().info(new ChatRecord("{PLAYER} was not allowed to join the server during configuration.")
					.player(p).toString());
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		ChatRecord cR = new ChatRecord(plugin.messages.getString("configurating.player_join")).player(p);
		e.setJoinMessage(plugin.formatter.format(cR));
		plugin.getLogger().info(cR.toString());
		p.setGameMode(GameMode.CREATIVE);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		ChatRecord cR = new ChatRecord(plugin.messages.getString("configurating.player_quit")).player(p)
				.numPlayers(Bukkit.getOnlinePlayers().size() - 1);
		e.setQuitMessage(plugin.formatter.format(cR));
		plugin.getLogger().info(cR.toString());
	}
}
