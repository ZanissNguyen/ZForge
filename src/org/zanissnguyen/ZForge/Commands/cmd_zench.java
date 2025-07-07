package org.zanissnguyen.ZForge.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_zench extends zcmds
{
	private final String permission = "zforge.edit";
	
	public cmd_zench(ZForge plugin, utils utils)
	{
		super(plugin, utils, "zench");
	}
	
	/*
	 * Player: add (2), remove (1), clear (0)
	 * Console: 
	 * 
	 * Line require: set, insert, remove
	 * Index limit: remove
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		String prefix = plugin.file_loc.getEnch("prefix");
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
		
		// sench remove <enchantment>
		if (args[0].equalsIgnoreCase("remove") && args.length>1)
		{
			if (!checkEnch(sender, args[1])) return false;
			
			item = utils.removeEnchant(item, Enchantment.getByName(args[1]));
			p.getInventory().setItemInMainHand(item);
			
			String msg = plugin.file_loc.getEnch(args[0]);
			utils.sendMessage(sender, prefix + msg.replace("<e>", args[1]));
			return true;	
		}
		// sench add <enchantment> <level>
		if (args[0].equalsIgnoreCase("add") && args.length>2)
		{
			if (!checkEnch(sender, args[1])) return false;
			if (!checkNumber(sender, args[2])) return false;
			
			int lvl = Integer.parseInt(args[2]);
			if (lvl<0)
			{
				String e_out_idx = plugin.file_loc.getError("out-index");
				utils.sendMessage(p, e_out_idx.replace("<n>", args[2]).replace("<r>", ">0"));
				return false;
			}
			item = utils.addEnchant(item, Enchantment.getByName(args[1]), lvl);
			p.getInventory().setItemInMainHand(item);
			
			String msg = plugin.file_loc.getEnch(args[0]);
			utils.sendMessage(sender, prefix + msg.replace("<e>", utils.color(args[1])).replace("<l>", args[2]));
			return true;	
		}
		// sench clear
		if (args[0].equalsIgnoreCase("clear"))
		{
			item = utils.clearEnchants(item);
			p.getInventory().setItemInMainHand(item);
			
			String msg = plugin.file_loc.getEnch(args[0]);
			utils.sendMessage(sender, prefix + msg);
			return true;
		}
		
		checkSyntax(sender, "/zench help");
		help(p);
		
		return false;
	}
	
	private void help(CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("ench.help");
		if (sender instanceof Player)
		{
			utils.sendMessage(sender, "&9-----=====<    &6ZNAME    &9>=====-----");
			for (String s: help)
			{
				utils.sendMessage(sender, s);
			}
			utils.sendMessage(sender, "&6[]:opt, <>:req");
			utils.sendMessage(sender, "&9-----======================-----");
		}
		
	}

	@SuppressWarnings("deprecation")
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
				for (Enchantment ench: Enchantment.values())
				{
					if (ench.getName().startsWith(current)) result.add(ench.getName());
				}
			}
			if (args[0].equalsIgnoreCase("remove"))
			{
				ItemStack item = p.getInventory().getItemInMainHand();
				if (item!=null && item.getType()!=Material.AIR)
				{
					for (Enchantment ench: item.getItemMeta().getEnchants().keySet())
					{
						if (ench.getName().startsWith(current)) result.add(ench.getName());
					}
					
				}
			}
		}
		
		result.sort(null);
		return result;
	}
	
}
