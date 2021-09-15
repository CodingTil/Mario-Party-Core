package com.tilmohr.marioparty;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class FileConfiguration extends YamlConfiguration {

	private File file;

	public FileConfiguration(File file) {
		this.file = file;
	}

	public void saveConfig() {
		try {
			this.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
