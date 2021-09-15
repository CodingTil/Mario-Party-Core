package com.tilmohr.marioparty;

import com.tilmohr.marioparty.formatting.ChatRecord;
import com.tilmohr.marioparty.utils.CommandRegistration;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class ConfigurationManager extends Manager {

	BukkitCommand command;

	public ConfigurationManager(App plugin) {
		super(plugin);
		command = new BukkitCommand("set") {
			@Override
			public boolean execute(CommandSender sender, String alias, String[] args) {
				boolean result = onCommand(sender, this, getLabel(), args);
				if (!result)
					sender.sendMessage(this.getUsage());
				return false;
			}
		};
		command.setDescription("Set the lobby spawn point and the field's locations");
		command.setPermission("marioparty.configure");
		// TODO: Wiki
		command.setUsage("Usage: /<command> [spawn | field <number>]\nSee [wiki] for more");
	}

	@Override
	public void register() {
		super.register();
		CommandRegistration.registerCommand(plugin, command);
	}

	@Override
	public void unregister() {
		super.unregister();
		CommandRegistration.unregisterCommand(plugin, plugin.getCommand(command.getName()));
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

	private boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (args == null || args.length == 0)
			return false;

		if (args[0].equalsIgnoreCase("spawn")) {
			Location location = player.getLocation();
			plugin.world.set("spawn.x", location.getX());
			plugin.world.set("spawn.y", location.getY());
			plugin.world.set("spawn.z", location.getZ());
			plugin.world.set("spawn.pitch", location.getPitch());
			plugin.world.set("spawn.yaw", location.getYaw());
			plugin.world.saveConfig();

			String message = "Set the new Spawn to: " + location.toVector().toString();
			plugin.getLogger().info(message);
			player.sendMessage(plugin.formatter.format(message));

			return true;
		}

		if (args.length >= 2 && args[0].equalsIgnoreCase("field")) {
			try {
				int index = Integer.parseInt(args[1]);
				ConfigurationSection section = plugin.world.getConfigurationSection("fields");
				int max = section.getKeys(false).size();
				if (0 > index || index > max)
					throw new NumberFormatException();
				Location location = player.getLocation();
				section.set(index + ".x", location.getX());
				section.set(index + ".y", location.getY());
				section.set(index + ".z", location.getZ());
				plugin.world.saveConfig();

				String message = "Set Field " + index + " to: " + location.toVector().toString();
				plugin.getLogger().info(message);
				player.sendMessage(plugin.formatter.format(message));

				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return false;
	}
}
