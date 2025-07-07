package org.zanissnguyen.ZForge.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_zname extends zcmds
{
	private final String permission = "zforge.edit";
	
	public cmd_zname(ZForge plugin, utils utils)
	{
		super(plugin, utils, "zname");
	}
	
	/*
	 * Player: set (1), clear (0), help (0)
	 * Console: 
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		String prefix = plugin.file_loc.getName("prefix");
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
		
		// sname set <string>
		if (args[0].equalsIgnoreCase("set"))
		{
			String toAdd = "";
			for (int i=1; i<=args.length-1; i++)
				if (i>1) toAdd = toAdd+" "+args[i];
				else toAdd = toAdd+args[i];
			
			item = utils.setName(item, toAdd);
			p.getInventory().setItemInMainHand(item);
			
			String msg = plugin.file_loc.getName(args[0]);
			utils.sendMessage(p, prefix+msg.replace("<s>", toAdd));
			return true;	
		}
	
		// sname clear
		if (args[0].equalsIgnoreCase("clear"))
		{
			item = utils.clearName(item);
			p.getInventory().setItemInMainHand(item);

			String msg = plugin.file_loc.getName(args[0]);
			utils.sendMessage(p, prefix+msg);
			return true;
		}
		
		checkSyntax(p, "/zname help");
		help(p);
		
		return false;
	}
	
	private void help(CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("name.help");
		if (sender instanceof Player)
		{
			utils.sendMessage(sender, "&9-----=====<    &6ZENCH    &9>=====-----");
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
		if (sender instanceof Player && !utils.hasPermission(sender, permission)) return result;
		
		List<String> args_0 = new ArrayList<>();
		args_0.add("set");
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
		
		result.sort(null);
		return result;
	}
	
}

