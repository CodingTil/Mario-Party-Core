package com.tilmohr.marioparty;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import com.tilmohr.marioparty.ConfigurationFactory.ConfigurationType;
import com.tilmohr.marioparty.formatting.ChatFormatter;
import com.tilmohr.marioparty.formatting.ChatRecord;
import com.tilmohr.marioparty.formatting.SimpleLogFormatter;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin implements Listener {

	FileConfiguration config;
	FileConfiguration messages;

	String PREFIX;
	String PREFIX_SEPERATOR;

	ChatFormatter formatter;

	@Override
	public void onEnable() {
		// Load Configurations
		config = ConfigurationFactory.createConfiguration(this, ConfigurationType.DEFAULT);
		messages = ConfigurationFactory.createConfiguration(this, ConfigurationType.MESSAGES);

		PREFIX = config.getString("decorations.prefix");
		PREFIX_SEPERATOR = config.getString("decorations.prefix_seperator");

		// Logger
		try {
			File logFile = new File(getDataFolder(), "latest.log");
			logFile.getParentFile().mkdirs();
			logFile.createNewFile();
			FileHandler logFileHandler = new FileHandler(logFile.getCanonicalPath(), false);
			logFileHandler.setFormatter(new SimpleLogFormatter());
			getLogger().addHandler(logFileHandler);
			getLogger().setLevel(Level.INFO);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		formatter = new ChatFormatter(this);

		getServer().getPluginManager().registerEvents(this, this);

		getLogger().info("Hello, SpigotMC!");
	}

	@Override
	public void onDisable() {
		getLogger().info("See you again, SpigotMC!");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		ChatRecord cR = new ChatRecord(messages.getString("player_join")).player(p)
				.numPlayers(Bukkit.getOnlinePlayers().size());
		String message = PREFIX + PREFIX_SEPERATOR + formatter.format(cR);
		e.setJoinMessage(message);
		getLogger().info(message);
		p.setGameMode(GameMode.ADVENTURE);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		ChatRecord cR = new ChatRecord(messages.getString("player_quit")).player(p)
				.numPlayers(Bukkit.getOnlinePlayers().size() - 1);
		String message = PREFIX + PREFIX_SEPERATOR + formatter.format(cR);
		e.setQuitMessage(message);
		getLogger().info(message);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		boolean rain = e.toWeatherState();
		if (rain)
			e.setCancelled(true);
	}

	@EventHandler
	public void onThunderChange(ThunderChangeEvent e) {
		boolean storm = e.toThunderState();
		if (storm)
			e.setCancelled(true);
	}

}