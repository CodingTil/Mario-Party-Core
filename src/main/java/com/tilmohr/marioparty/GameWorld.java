package com.tilmohr.marioparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class GameWorld {

	private App plugin;

	private World world;
	private Location spawn;

	private ArrayList<Field> fields;

	public GameWorld(App plugin) {
		this.plugin = plugin;

		this.world = Bukkit.getWorld(plugin.world.getString("world_name"));
		if (this.world == null) {
			plugin.getLogger().log(Level.SEVERE, plugin.formatter
					.format("World " + plugin.world.getString("world_name") + " not found! Shutting down..."));
			Bukkit.getPluginManager().disablePlugin(plugin);
			return;
		}
		this.world.setDifficulty(Difficulty.PEACEFUL);

		this.spawn = new Location(this.world, plugin.world.getDouble("spawn.x"), plugin.world.getDouble("spawn.y"),
				plugin.world.getDouble("spawn.z"), plugin.world.getInt("spawn.pitch"),
				plugin.world.getInt("spawn.yaw"));

		double currentProbability = 0;
		SortedMap<Double, ArrayList<Object>> specialFieldRecords = new TreeMap<>();
		for (String key : plugin.config.getConfigurationSection("special_fields").getKeys(false)) {
			ConfigurationSection section = plugin.config.getConfigurationSection("special_fields." + key);

			double prob = section.getDouble("probability");
			String name = section.getString("name");
			int value = section.getInt("value");

			currentProbability += prob; // "Cumulative"
			if (currentProbability > 1 || currentProbability < 0) {
				plugin.getLogger().log(Level.SEVERE, plugin.formatter.format(
						"The sum of probabilities of special_fields in config.yml must be in [0,1]. Also, no negative probabilities are allowed. Shutting down..."));
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}

			ArrayList<Object> list = new ArrayList<>();
			list.add(value);
			list.add(name);
			specialFieldRecords.putIfAbsent(currentProbability, list);

			plugin.getLogger().info(plugin.formatter
					.format("Found special field config: (" + value + ", " + prob + ", " + value + ")"));
		}

		this.fields = new ArrayList<Field>();
		for (String key : plugin.world.getConfigurationSection("fields").getKeys(false)) {
			ConfigurationSection section = plugin.config.getConfigurationSection("fields." + key);
			Location location = new Location(this.world, section.getDouble("spawn.x"), section.getDouble("spawn.y"),
					section.getDouble("spawn.z"), section.getInt("spawn.pitch"), section.getInt("spawn.yaw"));

			double r = Math.random();
			Double result = searchGaussianSortedMap(new ArrayList<>(specialFieldRecords.entrySet()), Double.valueOf(r));
			Field field;
			try {
				if (Integer.parseInt(key) >= 0 && result != null && result != 0) {
					ArrayList<Object> rList = specialFieldRecords.get(result);
					field = new Field(location, (int) rList.get(0), (String) rList.get(1));
				} else {
					field = new Field(location, 0, "");
				}

				int index = this.fields.indexOf(field);
				if (index != Integer.parseInt(key)) {
					throw new NumberFormatException(); // Also handle this case the same way!
				}

				this.fields.add(field);
				plugin.getLogger().info(plugin.formatter
						.format("Field nummer " + index + ": (" + field.name() + ", " + field.value() + ")"));
			} catch (NumberFormatException e) {
				plugin.getLogger().log(Level.SEVERE,
						plugin.formatter.format("Fields in world.yml are not correctly indexed. Shutting down..."));
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}
		}
		if (fields.size() < 6) {
			plugin.getLogger().log(Level.SEVERE,
					plugin.formatter.format("There must be a minimum of 6 Fields in world.yml. Shutting down..."));
			Bukkit.getPluginManager().disablePlugin(plugin);
			return;
		}
	}

	/**
	 * Will return the nearest upper value to key if exists, else null.
	 *
	 * @param <S>      Key Type
	 * @param <T>      Value Type
	 * @param entrySet Entry Set of Sorted Map
	 * @param key      Key
	 * @return Nearest upper value to key if exists, else null.
	 */
	private <K extends Comparable<K>, V> K searchGaussianSortedMap(ArrayList<Map.Entry<K, V>> entrySet, K key) {
		if (entrySet == null || entrySet.isEmpty()) {
			return null;
		}
		int index = (int) (entrySet.size() * 0.5);
		Map.Entry<K, V> entry = entrySet.get(index);
		if (entry.getKey().equals(key)) {
			return key;
		}
		if (entry.getKey().compareTo(key) < 0) {
			return searchGaussianSortedMap(new ArrayList<Map.Entry<K, V>>(entrySet.subList(index + 1, entrySet.size())),
					key);
		}
		if (entry.getKey().compareTo(key) > 0) {
			K result = searchGaussianSortedMap(new ArrayList<Map.Entry<K, V>>(entrySet.subList(0, index)), key);
			if (result == null || result.compareTo(key) > 0) {
				return key;
			}
			return result;
		}
		return null;
	}

	public World getWorld() {
		return world;
	}

	public Location getSpawn() {
		return spawn;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

}
