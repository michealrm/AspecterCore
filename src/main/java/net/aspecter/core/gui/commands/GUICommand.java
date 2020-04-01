package net.aspecter.core.gui.commands;

import net.aspecter.core.gui.events.data.EventData;
import net.aspecter.core.gui.handlers.EasyGUIHandler;
import net.aspecter.core.gui.listeners.InventoryEditListener;
import net.aspecter.core.gui.util.ItemInvoker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class GUICommand implements CommandExecutor {

	private Plugin plugin;
	private EasyGUIHandler handler;

	public GUICommand(Plugin plugin, EasyGUIHandler handler) {
		this.plugin = plugin;
		this.handler = handler;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if(!(commandSender instanceof Player)) {
			commandSender.sendMessage(ChatColor.RED + "You must be a player to execute this command group.");
			return false;
		}
		Player player = (Player)commandSender;
		if(command.getName().equals("gui") && commandSender.hasPermission("gui.modify")) {
			if(strings.length > 0) {
				if(strings[0].equalsIgnoreCase("create")) {
					if(strings.length > 1) {
						String event = strings[1];
						if (strings.length > 2) {
							int size = -1;
							boolean error = false;
							try {
								size = Integer.parseInt(strings[2]);
								if(size % 9 != 0)
									error = true;

							} catch (NumberFormatException nfe) {
								error = true;
							}
							if(error) {
								player.sendMessage(ChatColor.DARK_RED + "create" + ChatColor.RED + " requires an inventory size (must be a multiple of 9). An inventory name is optional. Example: " + ChatColor.DARK_RED + "/gui create na-servers 9 &4&lNA Servers");
								return false;
							}
							Inventory inventory;
							if (strings.length > 3) {
								StringBuilder builder = new StringBuilder();
								for (int i = 3; i < strings.length; i++)
									builder.append(ChatColor.translateAlternateColorCodes('&', strings[i])).append(" ");
								inventory = Bukkit.createInventory(null, size, builder.toString().trim());
								player.sendMessage(ChatColor.GREEN + "Creating " + ChatColor.RESET + builder.toString().trim() + ChatColor.RESET + ChatColor.GREEN + " for " + ChatColor.DARK_GREEN + event);
							} else {
								inventory = Bukkit.createInventory(null, size);
								player.sendMessage(ChatColor.GREEN + "Creating GUI event for " + ChatColor.DARK_GREEN + event);
							}
							handler.registerAndStoreGUI(event, inventory);
							plugin.getServer().getPluginManager().registerEvents(new InventoryEditListener(event, handler, player, inventory), plugin);
							return true;
						}
						player.sendMessage(ChatColor.GREEN + "Creating GUI event for " + ChatColor.DARK_GREEN + event);
						handler.registerEvent(new EventData(event, null));
						return true;
					}
					player.sendMessage(ChatColor.DARK_RED + "create" + ChatColor.RED + " requires an inventory size (must be a multiple of 9). An inventory name is optional. Example: " + ChatColor.DARK_RED + "/gui create na-servers 9 &4&lNA Servers");
					return false;
				} else if(strings[0].equalsIgnoreCase("event")) {
					if(strings.length > 1) {
						if(strings[1].equalsIgnoreCase("list")) {
							if(handler.getEventNames().size() == 0) {
								player.sendMessage(ChatColor.RED + "There are no GUI events to display!");
								return true;
							}
							player.sendMessage(ChatColor.GRAY + "Events: " + ChatColor.DARK_GREEN + handler.getEventNames());
							return true;
						}
						String event = strings[1];
						if(handler.eventExists(event)) {
							if(strings.length > 2) {
								if(strings[2].equalsIgnoreCase("edit")) {
									Inventory inventory = handler.getInventory(event);
									plugin.getServer().getPluginManager().registerEvents(new InventoryEditListener(event, handler, player, inventory), plugin);
									return true;
								} else if(strings[2].equalsIgnoreCase("test")) {
									player.sendMessage(ChatColor.GREEN + "Invoking " + ChatColor.DARK_GREEN + event + ChatColor.GREEN + "...");
									return handler.invokeEvent(event, player);
								} else if(strings[2].equalsIgnoreCase("delete")) {
									if(handler.unregisterAndRemoveEvent(event)) {
										player.sendMessage(ChatColor.GREEN + "Successfully deleted " + ChatColor.DARK_GREEN + event + ChatColor.GREEN + " data");
										return true;
									} else {
										player.sendMessage(ChatColor.RED + "Couldn't find / delete " + ChatColor.DARK_RED + event + ChatColor.RED + " data");
										return false;
									}
								} else if(strings[2].equalsIgnoreCase("join")) {
									if(strings.length > 3) {
										handler.registerAndStoreJoin(event, strings[3]);
										player.sendMessage(ChatColor.GREEN + "The player will be transferred to " + ChatColor.DARK_GREEN + strings[3] + ChatColor.GREEN + " when " + ChatColor.DARK_GREEN + event + ChatColor.GREEN + " is invoked");
										return true;
									} else {
										player.sendMessage(ChatColor.RED + "You must include which bungee server you would like to send the player to.");
										return false;
									}
								} else if(strings[2].equalsIgnoreCase("message")) {
									if(strings.length > 3) {
										StringBuilder builder = new StringBuilder();
										for (int i = 3; i < strings.length; i++)
											builder.append(ChatColor.translateAlternateColorCodes('&', strings[i])).append(" ");
										handler.registerAndStoreMessage(event, builder.toString().trim());
										player.sendMessage(ChatColor.GREEN + "The player will be sent " + ChatColor.DARK_GREEN + "[" + ChatColor.RESET + builder.toString().trim() + ChatColor.RESET + ChatColor.DARK_GREEN + "]" + ChatColor.GREEN + " when " + ChatColor.DARK_GREEN + event + ChatColor.GREEN + " is invoked");
										return true;
									} else {
										player.sendMessage(ChatColor.RED + "You must include which bungee server you would like to send the player to.");
										return false;
									}
								}
							} else {
								player.sendMessage(ChatColor.GREEN + "Invoking " + ChatColor.DARK_GREEN + event + ChatColor.GREEN + "...");
								return handler.invokeEvent(event, player);
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + event + ChatColor.RED + " is not a valid event. Use " + ChatColor.DARK_RED + "/gui event list" + ChatColor.RED + " to list events.");
							return false;
						}
					} else {
						player.sendMessage(ChatColor.RED + "Please specify an event name to test invokation. Example: /gui event MyEvent");
						return false;
					}
				} else if(strings[0].equalsIgnoreCase("item")) {
					if(strings.length > 1) {
						if(strings[1].equalsIgnoreCase("list")) {
							player.sendMessage(ChatColor.GREEN + "Item invokers: " + handler.getInvokeData().toString());
							return true;
						} else if(strings[1].equalsIgnoreCase("set")) {
							if(player.getItemInHand() == null) {
								player.sendMessage(ChatColor.RED + "You must have an item in your hand to set an invocation item.");
								return false;
							}
							if(strings.length > 2) {
								String event = strings[2];
								handler.registerAndStoreInvokeData(new ItemInvoker(event, player.getItemInHand()));
								player.sendMessage(ChatColor.GREEN + "This item will now invoke " + ChatColor.DARK_GREEN + event);
								return true;
							} else {
								player.sendMessage(ChatColor.RED + "You must include an event name that this item invokes. Example: /gui item set na_servers");
								return false;
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "Possible subcommands: list, set");
						return false;
					}
				}
			} else {
				player.sendMessage(ChatColor.GRAY + "Running EasyGUI - made with " + ChatColor.DARK_RED + "<3" + ChatColor.GRAY + " by Aspecting");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You do not have permissions to modify GUI events.");
			return false;
		}
		return false;
	}

}
