package org.zanissnguyen.ZForge.Commands.zforge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Commands.cmd_zforge;
import org.zanissnguyen.ZForge.Files.database.zfile_items;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_item 
{
	// TODO: not done
	public static String permission = "zforge.item";
	public static boolean itemOnCommand(cmd_zforge cmd_obj, ZForge plugin, utils utils, CommandSender sender, Command cmd, String label, String[] args)
	{
		String prefixMsg = plugin.file_loc.getForge("item.prefix");
		String console = plugin.file_loc.getConsole();
//		zfile_Items mat = plugin.file_mat;
		zfile_items itm = plugin.file_itm;
		
		if (args.length==1)// forge item
		{
			if (!cmd_obj.checkPlayer(sender)) return false;
			Player p = (Player) sender;
			plugin.ui_itm.open(p, "", 1);
			return true;
		}
		else
		{
			if (!cmd_obj.checkPermission(sender, console)) return false;
			
			if (args[1].equalsIgnoreCase("save"))
			{
				if (!cmd_obj.checkPlayer(sender)) return false;
				Player p = (Player) sender;
				if (!cmd_obj.checkHand(p)) return false;
				ItemStack inHand = p.getInventory().getItemInMainHand();
				
				String msg = plugin.file_loc.getForge("item.save");
				if (args.length==2) // forge item save
				{
					String toCreate = itm.genarteID();
					itm.saveItem(toCreate, inHand);
					
					utils.sendMessage(p, prefixMsg + msg.replace("<id>", toCreate));
					return true;
				}
				
				if (args.length==3) // forge item save <id>
				{
					String prefix = args[2];
					int number = 0;
					String id = args[2];
					while (itm.isIDExist(id)) id = prefix + number++;
					itm.saveItem(id, inHand);

					utils.sendMessage(p, prefixMsg + msg.replace("<id>", id));
					return true;
				}
			}
			
			String give_msg = plugin.file_loc.getForge("item.give");
			String gain_msg = plugin.file_loc.getForge("item.gain");
			if (args[1].equalsIgnoreCase("give") || args[1].equalsIgnoreCase("get"))
			{
				if (args.length==3) // forge Item give/get <id> 
				{
					if (!cmd_obj.checkPlayer(sender)) return false;
					Player p = (Player) sender;
					
					if (!plugin.file_itm.isItem(args[2]))
					{
						utils.sendMessage(sender, plugin.file_loc.getError("no-item").replace("<o>", args[2]));
					}
					
					ItemStack item = itm.getItem(args[2]).convertToItemStack(false);
					p.getInventory().addItem(item);
					utils.sendMessage(p, prefixMsg + give_msg.replace("<id>", utils.getName(item)).replace("<p>", p.getName()).replace("<n>", 1+""));
					return true;
				}
				
				if (args.length==4) // forge item give/get <id> <player>
				{
					if (!(sender instanceof Player))
					{
						if (!cmd_obj.checkPlayerOnline(sender, args[3])) return false;
						Player reciver = Bukkit.getPlayer(args[3]);
						
						if (!plugin.file_itm.isItem(args[2]))
						{
							utils.sendMessage(sender, plugin.file_loc.getError("no-item").replace("<o>", args[2]));
						}
						
						ItemStack item = itm.getItem(args[2]).convertToItemStack(false);
						reciver.getInventory().addItem(item);
						utils.sendMessage(sender, prefixMsg + give_msg.replace("<id>", utils.getName(item)).replace("<p>", args[3])
								.replace("<n>", 1+""));
						utils.sendMessage(reciver, prefixMsg + gain_msg.replace("<id>", utils.getName(item)).replace("<p>", console)
								.replace("<n>", 1+""));
						return true;
					}
					else
					{
						Player p = (Player) sender;
						if (!cmd_obj.checkPlayerOnline(sender ,args[3])) return false;
						Player reciver = Bukkit.getPlayer(args[3]);
						
						if (!plugin.file_itm.isItem(args[2]))
						{
							utils.sendMessage(sender, plugin.file_loc.getError("no-item").replace("<o>", args[2]));
						}
						
						ItemStack item = itm.getItem(args[2]).convertToItemStack(false);
						reciver.getInventory().addItem(item);
						utils.sendMessage(p, prefixMsg + give_msg.replace("<id>", utils.getName(item)).replace("<p>", args[3])
								.replace("<n>", 1+""));
						if (p!=reciver) utils.sendMessage(reciver, prefixMsg + gain_msg.replace("<id>", utils.getName(item)).replace("<p>", p.getName())
								.replace("<n>", 1+""));
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
		
		cmd_obj.checkSyntax(sender, "/forge item help");
		help(plugin, utils, sender);
		
		return false;
	}
	
	private static void help(ZForge plugin, utils utils, CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("forge.item.help");
		utils.sendMessage(sender, "&9-----=====< &6/FORGE ITEM HELP &9>=====-----");
		
		for (String s: help)
		{
			utils.sendMessage(sender, s);
		}
		
		utils.sendMessage(sender, "&6[]:opt, <>:req");
		utils.sendMessage(sender, "&9-----===============================-----");
	}
	
	public static List<String> itemOnTabCompletor(cmd_zforge cmd_obj, ZForge plugin, utils utils, CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> result = new ArrayList<>();
		
		if (!utils.hasPermission(sender, permission)) return result;
		
		List<String> args_1 = new ArrayList<>();
		args_1.add("give");args_1.add("get");args_1.add("help");
		
		if (args.length==2)// forge Item ...
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
				if (args.length==3) // forge Item give/get ...
				{
					String current = args[2];
					
					for (String item: zfile_items.allItems)
					{
						if (item.startsWith(current)) result.add(item);
					}	
				}
				
				if (args.length==4) // forge Item give/get <id> ...
				{
					String current = args[3];
					
					for (Player p: Bukkit.getOnlinePlayers())
					{
						if (p.getName().startsWith(current)) result.add(p.getName());
					}
				}
			}
		}

		return result;
	}
}
