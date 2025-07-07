package org.zanissnguyen.ZForge.Commands.zforge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Commands.cmd_zforge;
import org.zanissnguyen.ZForge.Files.database.zfile_gems;
import org.zanissnguyen.ZForge.System.object.zgem;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_gem {
	//TODO
	public static String permission = "zforge.gem";
	public static boolean gemOnCommand(cmd_zforge cmd_obj, ZForge plugin, utils utils, CommandSender sender, Command cmd, String label, String[] args)
	{
		String prefixMsg = plugin.file_loc.getForge("gem.prefix");
		String give_msg = plugin.file_loc.getForge("gem.give");
		String console = plugin.file_loc.getConsole();
		
		if (args.length==1)// forge gem
		{
			if (!cmd_obj.checkPlayer(sender)) return false;
			Player p = (Player) sender;
			plugin.ui_gem.open(p, "", 1);
			return true;
		}
		else
		{
			if (!cmd_obj.checkPermission(sender, console)) return false;
			
			
			if (args[1].equalsIgnoreCase("give") || args[1].equalsIgnoreCase("get"))
			{
				// forge gem give/get gem <id> [player] [amount]
				if (args.length>3 && args[2].equalsIgnoreCase("gem"))
				{
					String id = args[3];
					if (!plugin.file_gem.isGem(id)) {
						utils.sendMessage(sender, plugin.file_loc.getError("no-gem").replace("<o>", id));
						return false;
					}
					ItemStack gem = plugin.file_gem.getGem(id).convertToItemStack(false);
					
					if (args.length == 4)
					{
						if (!cmd_obj.checkPlayer(sender)) return false;
						
						Player p = (Player) sender;
						p.getInventory().addItem(gem);
						utils.sendMessage(p, prefixMsg + give_msg.replace("<n>", 1+"")
								.replace("<id>", utils.getName(gem))
								.replace("<p>", p.getName()));
						return true;
					}
					if (args.length == 5)
					{
						if (!cmd_obj.checkPlayerOnline(sender, args[4])) return false;
						
						giveItem(plugin, utils, args[4], sender, 1, gem);
						return true;
					}
					if (args.length == 6)
					{
						if (!cmd_obj.checkPlayerOnline(sender, args[4])) return false;
						if (!cmd_obj.checkNumber(sender, args[5])) return false;
						
						int amount = Integer.parseInt(args[5]);
						
						giveItem(plugin, utils, args[4], sender, amount, gem);
						return true;
					}
				}
				
				// forge gem give/get remover/driller/smasher [player] [amount]
				if (args.length>2 && (args[2].equalsIgnoreCase("remover") ||
									  args[2].equalsIgnoreCase("driller") ||
									  args[2].equalsIgnoreCase("smasher")))
				{
					ItemStack getItem = utils.createItem(Material.AIR, 1);
					if (args[2].equalsIgnoreCase("remover")) getItem = zgem.remover(false, false);
					else if (args[2].equalsIgnoreCase("driller")) getItem = zgem.driller(false, false);
					else if (args[2].equalsIgnoreCase("smasher")) getItem = zgem.smasher(false, false);
					
					if (args.length == 3)
					{
						if (!cmd_obj.checkPlayer(sender)) return false;
						Player p = (Player) sender;
						
						p.getInventory().addItem(getItem);
						utils.sendMessage(p, prefixMsg + give_msg.replace("<n>", 1+"")
								.replace("<id>", utils.getName(getItem))
								.replace("<p>", p.getName()));
						return true;
						
					}
					if (args.length == 4)
					{
						if (!cmd_obj.checkPlayerOnline(sender, args[3])) return false;
						
						giveItem(plugin, utils, args[3], sender, 1, getItem);
						return true;
					}
					if (args.length == 5)
					{
						if (!cmd_obj.checkPlayerOnline(sender, args[3])) return false;
						if (!cmd_obj.checkNumber(sender, args[4])) return false;
						int amount = Integer.parseInt(args[4]);
						
						giveItem(plugin, utils, args[3], sender, amount, getItem);
					}
				}
				
				// forge gem give/get powder <chance> [player] [amount]
				if (args.length>3 && args[2].equalsIgnoreCase("powder"))
				{
					if (!cmd_obj.checkNumber(sender, args[3])) return false;
					double chance = Double.parseDouble(args[3]);
					ItemStack powder = zgem.powder(chance);
					
					if (args.length==4)
					{
						if (!cmd_obj.checkPlayer(sender)) return false;
						Player p = (Player) sender;
						
						p.getInventory().addItem(powder);
						utils.sendMessage(p, prefixMsg + give_msg.replace("<n>", 1+"")
								.replace("<id>", utils.getName(powder))
								.replace("<p>", p.getName()));
						return true;
					}
					if (args.length==5)
					{
						if (!cmd_obj.checkPlayerOnline(sender, args[4])) return false;
						
						giveItem(plugin, utils, args[4], sender, 1, powder);
						return true;
					}
					if (args.length==6)
					{
						if (!cmd_obj.checkPlayerOnline(sender, args[4])) return false;
						if (!cmd_obj.checkNumber(sender, args[5])) return false;
						
						int amount = Integer.parseInt(args[5]);
						
						giveItem(plugin, utils, args[4], sender, amount, powder);
						return true;
					}
				}
			}
			
			if (args[1].equalsIgnoreCase("help"))
			{
				help(plugin, utils, sender);
				return true;
			}
		}
		
		cmd_obj.checkSyntax(sender, "/forge material help");
		help(plugin, utils, sender);
		
		return false;
	}
	
	private static void giveItem(ZForge plugin, utils utils, String reciver_name, CommandSender sender, int amount, ItemStack item)
	{
		String prefixMsg = plugin.file_loc.getForge("gem.prefix");
		String give_msg = plugin.file_loc.getForge("gem.give");
		String gain_msg = plugin.file_loc.getForge("gem.gain");
		
		Player reciver = Bukkit.getPlayer(reciver_name);
		String sender_name = (sender instanceof Player) ? ((Player)sender).getName()
				: plugin.file_loc.getConsole();
		
		item.setAmount(amount);
		reciver.getInventory().addItem(item);
		utils.sendMessage(sender, prefixMsg + give_msg.replace("<id>", utils.getName(item))
				.replace("<p>", reciver_name)
				.replace("<n>", 1+""));
		if (!sender_name.equalsIgnoreCase(reciver.getName())) // sender = receiver
		{
			utils.sendMessage(reciver, prefixMsg + gain_msg.replace("<id>", utils.getName(item))
					.replace("<p>", sender_name)
					.replace("<n>", 1+""));
		}
	}
	
	private static void help(ZForge plugin, utils utils, CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("forge.gem.help");
		utils.sendMessage(sender, "&9-----=====< &6/FORGE GEM HELP &9>=====-----");
		
		for (String s: help)
		{
			utils.sendMessage(sender, s);
		}
		
		utils.sendMessage(sender, "&6[]:opt, <>:req");
		utils.sendMessage(sender, "&9-----===================================-----");
	}
	
	public static List<String> gemOnTabCompletor(cmd_zforge cmd_obj, ZForge plugin, utils utils, CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> result = new ArrayList<>();
		
		if (!utils.hasPermission(sender, permission)) return result;
		
		List<String> args_1 = new ArrayList<>();
		args_1.add("give");args_1.add("get");args_1.add("help");
		
		if (args.length==2)// forge material ...
		{
			String current = args[1];
			if (!(sender instanceof Player))
			{
				for (String s: args_1)
				{
					if (s.startsWith(current)) result.add(s);
				}
			}
			else
			{
				args_1.add("save");
				for (String s: args_1)
				{
					if (s.startsWith(current)) result.add(s);
				}
			}
			return result;
		}
		else
		{	
			if ((args.length>=2) && (args[1].equalsIgnoreCase("give") || args[1].equalsIgnoreCase("get")))
			{
				List<String> args_2 = new ArrayList<>();
				args_2.add("gem");args_2.add("remover");args_2.add("driller");args_2.add("smasher");
				args_2.add("powder");
				// TODO:
				if (args.length==3) // forge gem give/get ...
				{
					String current = args[2];
					
					for (String s: args_2)
					{
						if (s.startsWith(current)) result.add(s);
					}	
				}
				
				if (args.length==4) // forge material give/get <label>  ...
				{
					if (args[2].equalsIgnoreCase("gem"))
					{
						String current = args[3];
						
						for (String s: zfile_gems.allGems)
						{
							if (s.startsWith(current)) result.add(s);
						}	
					}
					
					if (args[2].equalsIgnoreCase("remover")
							|| args[2].equalsIgnoreCase("driller")
							|| args[2].equalsIgnoreCase("smasher"))
					{
						String current = args[3];
						
						for (Player p: Bukkit.getOnlinePlayers())
						{
							if (p.getName().startsWith(current)) result.add(p.getName());
						}
					}
					
					if (args[2].equalsIgnoreCase("powder"))
					{
						for (double i = 0.5; i<10; i+=0.5)
						{
							result.add(i+"");
						}
						result.add("...");
					}
				}
				
				if (args.length==5) // forge gem give/get <label> <label> ...
				{
					if (args[2].equalsIgnoreCase("gem") || args[2].equalsIgnoreCase("powder"))
					{
						String current = args[4];
						
						for (Player p: Bukkit.getOnlinePlayers())
						{
							if (p.getName().startsWith(current)) result.add(p.getName());
						}
					}
				}
			}
		}

		return result;
	}
}
