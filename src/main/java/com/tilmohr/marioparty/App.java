package com.tilmohr.marioparty;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import com.tilmohr.marioparty.ConfigurationFactory.ConfigurationType;
import com.tilmohr.marioparty.formatting.ChatFormatter;
import com.tilmohr.marioparty.formatting.SimpleLogFormatter;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {

	FileConfiguration config;
	FileConfiguration messages;
	FileConfiguration world;

	public String PREFIX;
	public String PREFIX_SEPERATOR;

	ChatFormatter formatter;

	Manager manager;

	@Override
	public void onEnable() {
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

		// Load Configurations
		config = ConfigurationFactory.createConfiguration(this, ConfigurationType.DEFAULT);
		messages = ConfigurationFactory.createConfiguration(this, ConfigurationType.MESSAGES);
		world = ConfigurationFactory.createConfiguration(this, ConfigurationType.WORLD);

		PREFIX = config.getString("decorations.prefix");
		PREFIX_SEPERATOR = config.getString("decorations.prefix_seperator");

		if (config.getBoolean("configurating")) {
			manager = new ConfigurationManager(this);
			getLogger().info(formatter.format("Configuration Manager loaded."));
		} else {
			manager = new GameManager(this);
			getLogger().info(formatter.format("Game Manager loaded."));
		}

		getLogger().info(formatter.format("Plugin started."));
		manager.start();
	}

	@Override
	public void onDisable() {
		getLogger().info(formatter.format("Plugin is shutting down..."));
	}

	public void disablePlugin() {
		getPluginLoader().disablePlugin(this);
	}

}