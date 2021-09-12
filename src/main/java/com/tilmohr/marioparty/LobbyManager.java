package com.tilmohr.marioparty;

import com.tilmohr.marioparty.formatting.ChatRecord;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitTask;

public class LobbyManager implements Listener {

	private App plugin;
	private GameWorld world;
	private BukkitTask task;

	public LobbyManager(App plugin, GameWorld world, BukkitTask task) {
		this.plugin = plugin;
		this.world = world;
		this.task = task;
	}

	public void registerEvents() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterEvents() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		ChatRecord cR = new ChatRecord(plugin.messages.getString("lobby.player_join")).player(p);
		String message = plugin.formatter.format(cR);
		e.setJoinMessage(message);
		plugin.getLogger().info(message);
		p.teleport(world.getSpawn());
		p.setGameMode(GameMode.ADVENTURE);

		// TODO: Countdown + run task
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		ChatRecord cR = new ChatRecord(plugin.messages.getString("lobby.player_quit")).player(p)
				.numPlayers(Bukkit.getOnlinePlayers().size() - 1);
		String message = plugin.formatter.format(cR);
		e.setQuitMessage(message);
		plugin.getLogger().info(message);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onThunderChange(ThunderChangeEvent e) {
		if (e.toThunderState()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.NATURAL) {
			e.setCancelled(true);
		}
	}
}
