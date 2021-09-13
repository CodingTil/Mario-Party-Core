package com.tilmohr.marioparty;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
			plugin.getLogger().severe("World " + plugin.world.getString("world_name") + " not found! Shutting down...");
			plugin.disablePlugin();
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
				plugin.getLogger().severe(
						"The sum of probabilities of special_fields in config.yml must be in [0,1]. Also, no negative probabilities are allowed. Shutting down...");
				plugin.disablePlugin();
				return;
			}

			ArrayList<Object> list = new ArrayList<>();
			list.add(value);
			list.add(name);
			specialFieldRecords.putIfAbsent(currentProbability, list);

			plugin.getLogger().info("Found special field config: (" + name + ", " + prob + ", " + value + ")");
		}

		this.fields = new ArrayList<Field>();
		for (String key : plugin.world.getConfigurationSection("fields").getKeys(false)) {
			ConfigurationSection section = plugin.world.getConfigurationSection("fields." + key);
			Location location = new Location(this.world, section.getDouble("spawn.x"), section.getDouble("spawn.y"),
					section.getDouble("spawn.z"));

			double r = Math.random();
			Double result = searchGaussianSortedMap(new ArrayList<>(specialFieldRecords.keySet()), Double.valueOf(r));

			try {
				Field field = new Field(location, 0, "", Integer.parseInt(key));
				if (result != null && result != 0) {
					ArrayList<Object> rList = specialFieldRecords.get(result);
					if (rList != null) { // Shouldn't happen
						field = new Field(location, (int) rList.get(0), (String) rList.get(1), Integer.parseInt(key));
					} else {
						plugin.getLogger().severe(
								"This shouldn't happen! Contact a developer! (GameWorld.java: rList is null)\nShutting down...");
						plugin.disablePlugin();
						return;
					}
				}

				this.fields.add(field);
				int index = this.fields.indexOf(field);

				plugin.getLogger().info("Field nummer " + key + ": (" + field.name() + ", " + field.value() + ")");

				if (index != Integer.parseInt(key)) {
					throw new NumberFormatException(); // Also handle this case the same way!
				}
			} catch (NumberFormatException e) {
				plugin.getLogger().severe("Fields in world.yml are not correctly indexed. Shutting down...");
				e.printStackTrace();
				plugin.disablePlugin();
				return;
			}
		}
		if (fields.size() < 6) {
			plugin.getLogger().severe("There must be a minimum of 6 Fields in world.yml. Shutting down...");
			plugin.disablePlugin();
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
	private <K extends Comparable<K>> K searchGaussianSortedMap(ArrayList<K> entrySet, K key) {
		if (entrySet == null || entrySet.isEmpty()) {
			return null;
		}
		int index = (int) (entrySet.size() * 0.5);
		K entry = entrySet.get(index);
		if (entry.equals(key)) {
			return key;
		}
		if (entry.compareTo(key) < 0) {
			return searchGaussianSortedMap(new ArrayList<K>(entrySet.subList(index + 1, entrySet.size())), key);
		}
		if (entry.compareTo(key) > 0) {
			K result = searchGaussianSortedMap(new ArrayList<K>(entrySet.subList(0, index)), key);
			if (result == null || result.compareTo(key) > 0) {
				return entry;
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
