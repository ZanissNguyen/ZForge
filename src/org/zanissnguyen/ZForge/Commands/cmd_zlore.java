package org.zanissnguyen.ZForge.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_zlore extends zcmds
{
	private final String permission = "zforge.edit";
	
	public cmd_zlore(ZForge plugin, utils utils)
	{
		super(plugin, utils, "zlore");
	}
	
	/*
	 * Player: add (1), set (2), insert (2), remove (2), clear (0)
	 * Console: 
	 * 
	 * Line require: set, insert, remove
	 * Index limit: remove
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		String prefix = plugin.file_loc.getLore("prefix");
		
		if (!checkPlayer(sender)) return false;
		
		Player p = (Player) sender;
		if (!checkPermission(p, permission)) return false;
		
		// usage
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
		
		// slore add <string>
		if (args[0].equalsIgnoreCase("add"))
		{
			String msg = plugin.file_loc.getLore(args[0]);
			
			String toAdd = "";
			for (int i=1; i<=args.length-1; i++)
				if (i>1) toAdd = toAdd+" "+args[i];
				else toAdd = toAdd+args[i];
			
			item = utils.addLore(item, -1, toAdd);
			p.getInventory().setItemInMainHand(item);
			utils.sendMessage(p, prefix+msg.replace("<s>", toAdd));
			return true;	
		}
		// slore set <line> <string>
		if (args[0].equalsIgnoreCase("set") && args.length>2)
		{
			if (!checkNumber(p, args[1])) return false;
			int line = Integer.parseInt(args[1]);
			if (!checkIndexRange(p, line>=0, args[1], ">=0")) return false;
			
			String msg = plugin.file_loc.getLore(args[0]);
			
			String toAdd = "";
			for (int i=2; i<=args.length-1; i++)
				if (i>2) toAdd = toAdd+" "+args[i];
				else toAdd = toAdd+args[i];
			item = utils.setLore(item, line, toAdd);
			
			p.getInventory().setItemInMainHand(item);
			utils.sendMessage(p, prefix+msg.replace("<s>", toAdd).replace("<l>", args[1]));
			return true;	
		}
		// slore insert <line> <string>
		if (args[0].equalsIgnoreCase("insert") && args.length>2)
		{
			if (!checkNumber(p, args[1])) return false;
			int line = Integer.parseInt(args[1]);
			if (!checkIndexRange(p, line>=0, args[1], ">=0")) return false;
			
			String msg = plugin.file_loc.getLore(args[0]);
			
			String toAdd = "";
			for (int i=2; i<=args.length-1; i++)
				if (i>2) toAdd = toAdd+" "+args[i];
				else toAdd = toAdd+args[i];
			item = utils.addLore(item, line, toAdd);
			
			p.getInventory().setItemInMainHand(item);
			utils.sendMessage(p, prefix+msg.replace("<s>", toAdd).replace("<l>", args[1]));
			return true;	
		}
		// slore remove <line>
		if (args[0].equalsIgnoreCase("remove") && args.length>1)
		{
			int current_size = utils.getLore(item).size();
			if (!checkNumber(p, args[1])) return false;
			int line = Integer.parseInt(args[1]);
			if (!checkIndexRange(p, line>=0 && line<current_size, args[1], "0~"+(current_size-1))) return false;
			
			String msg = plugin.file_loc.getLore(args[0]);
			
			item = utils.removeLore(item, line);
			
			p.getInventory().setItemInMainHand(item);
			utils.sendMessage(p, prefix+msg.replace("<l>", args[1]));
			return true;	
		}
		// slore clear
		if (args[0].equalsIgnoreCase("clear"))
		{
			String msg = plugin.file_loc.getLore(args[0]);
			item = utils.clearLore(item);
			p.getInventory().setItemInMainHand(item);
			utils.sendMessage(p, prefix + msg);
			return true;
		}
		
		checkSyntax(sender, "/zlore help");
		help(p);
		
		return false;
	}
	
	private void help(CommandSender sender)
	{	
		List<String> help = plugin.file_loc.getList("lore.help");
;		if (sender instanceof Player)
		{
			utils.sendMessage(sender, "&9-----=====<    &6ZLORE    &9>=====-----");
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
		args_0.add("add");
		args_0.add("insert");
		args_0.add("set");
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
		
		result.sort(null);
		return result;
	}
	
}

