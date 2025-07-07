package org.zanissnguyen.ZForge.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_zflag extends zcmds
{
	private final String permission = "zforge.edit";
	
	public cmd_zflag(ZForge plugin, utils utils)
	{
		super(plugin, utils, "zflag");
	}
	
	/*
	 * Player: add (2), remove (1), clear (0)
	 * Console: 
	 * 
	 * Line require: set, insert, remove
	 * Index limit: remove
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		String prefix = plugin.file_loc.getFlag("prefix");
		if (!checkPlayer(sender)) return false;
		Player p = (Player) sender;
		if (!checkPermission(p, permission)) return false;
		
		if (args.length==0) 
		{
			help(sender);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("help"))
		{
			help(sender);
			return true;
		}
			
		if (!checkHand(p)) return false;
		
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		
		// sflag remove <flag>
		if (args[0].equalsIgnoreCase("remove") && args.length>1)
		{
			if (!checkFlag(sender, args[1])) return false;
			
			item = utils.removeFlag(item, ItemFlag.valueOf(args[1]));
			p.getInventory().setItemInMainHand(item);
			
			String msg = plugin.file_loc.getFlag(args[0]);
			utils.sendMessage(sender, prefix + msg.replace("<f>", args[1]));
			return true;	
		}
		// sflag add <flag>
		if (args[0].equalsIgnoreCase("add") && args.length>1)
		{
			if (!checkFlag(sender, args[1])) return false;
			
			item = utils.addFlag(item, ItemFlag.valueOf(args[1]));
			p.getInventory().setItemInMainHand(item);
			
			String msg = plugin.file_loc.getFlag(args[0]);
			utils.sendMessage(sender, prefix + msg.replace("<f>", args[1]));
			return true;	
		}
		// sflag clear
		if (args[0].equalsIgnoreCase("clear"))
		{
			item = utils.clearFlags(item);
			p.getInventory().setItemInMainHand(item);
			
			String msg = plugin.file_loc.getFlag(args[0]);
			utils.sendMessage(sender, prefix + msg);
			return true;
		}
		
		checkSyntax(sender, "/zflag help");
		help(p);
		
		return false;
	}
	
	private void help(CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("flag.help");
		if (sender instanceof Player)
		{
			utils.sendMessage(sender, "&9-----=====<    &6ZFLAG    &9>=====-----");
			for (String s: help)
			{
				utils.sendMessage(sender, s);
			}
			utils.sendMessage(sender, "&6[]:opt, <>:req");
			utils.sendMessage(sender, "&9-----======================-----");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) 
	{
		List<String> result = new ArrayList<>();
		if ((sender instanceof Player && !utils.hasPermission(sender, permission))
			|| !(sender instanceof Player))return result;
		
		Player p = (Player) sender;
		
		List<String> args_0 = new ArrayList<>();
		args_0.add("add");
		args_0.add("remove");
		args_0.add("clear");
		args_0.add("help");
		if (args.length==1)
		{
			String current = args[0];
			for (String s: args_0)
			{
				if (s.startsWith(current)) result.add(s);
			}
			
		}
		
		if (args.length==2)
		{
			String current = args[1];
			if (args[0].equalsIgnoreCase("add"))
			{
				for (ItemFlag fl: ItemFlag.values())
				{
					if (fl.name().startsWith(current)) result.add(fl.name());
				}
			}
			if (args[0].equalsIgnoreCase("remove"))
			{
				ItemStack item = p.getInventory().getItemInMainHand();
				if (item!=null && item.getType()!=Material.AIR)
				{
					for (ItemFlag fl: item.getItemMeta().getItemFlags())					
					{
						if (fl.name().startsWith(current))result.add(fl.name());
					}
				}
			}
		}
		
		result.sort(null);
		return result;
	}
	
}

