package org.zanissnguyen.ZForge.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_zdata extends zcmds
{
	private final String permission = "zforge.edit";
	
	public cmd_zdata(ZForge plugin, utils utils)
	{
		super(plugin, utils, "zdata");
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
		
		// zdata int
		if (!checkNumber(sender, args[0])) return false;
		
		int data = Integer.parseInt(args[0]);
			
		item = utils.setModelData(item, data);
			
		p.getInventory().setItemInMainHand(item);
		String msg = plugin.file_loc.getData("set");
		utils.sendMessage(sender ,prefix + msg.replace("<n>", args[0]));
		return true;	
	}
	
	private void help(CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("data.help");
		if (sender instanceof Player)
		{
			utils.sendMessage(sender, "&9-----=====<    &6ZDATA    &9>=====-----");
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

