package com.tilmohr.marioparty;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigurationFactory {

	public static FileConfiguration createConfiguration(JavaPlugin plugin, ConfigurationType type) {
		File file = new File(plugin.getDataFolder(), type + ".yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource(type + ".yml", false);
		}

		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return config;
	}

	enum ConfigurationType {
		DEFAULT("config"), MESSAGES("messages"), WORLD("world");

		private final String filename;

		ConfigurationType(String filename) {
			this.filename = filename;
		}

		@Override
		public String toString() {
			return filename;
		}
	}

}
