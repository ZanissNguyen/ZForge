package org.zanissnguyen.ZForge.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.leveling;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.buff.Zbuff;
import org.zanissnguyen.ZForge.System.buff.buff_manager;
import org.zanissnguyen.ZForge.System.rate.zrate;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.System.stat.stat_manager;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_zattribute extends zcmds
{
	private utils_attribute a_utils;
	private String permission = "zforge.edit";
	
	public cmd_zattribute(ZForge plugin, utils utils) {
		super(plugin, utils, "zattr");
		a_utils = plugin.a_utils;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		String prefix = plugin.file_loc.getAttribute("prefix");
		
		if (!checkPermission(sender, permission)) return false;
		
		if (!checkPlayer(sender)) return false;
		Player p = (Player) sender;
			
		if (!checkHand(p)) return false;
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		
		if (args.length==0) 
		{
			help(sender);
			return true;
		}
		
		if (args.length==1)
		{
			if (args[0].equalsIgnoreCase("help"))
			{
				help(sender);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("leveling"))
			{
				if (a_utils.hasLeveling(item)!=-1)
				{
					String msg = plugin.file_loc.getAttribute("level.disable");
					utils.sendMessage(p, prefix + msg);
					item = a_utils.setLevel(item, new leveling(-1, 0, 0));
					p.getInventory().setItemInMainHand(item);
				}
				else
				{
					zrate rate = a_utils.getRateFromItem(item);
					String msg = plugin.file_loc.getAttribute("level.enable");
					utils.sendMessage(p, prefix + msg);
					item = a_utils.setLevel(item, new leveling(1, 0, rate.base_exp));
					p.getInventory().setItemInMainHand(item);
				}
				return true;
			}
		}
		
		if (args.length==2)
		{
			if (args[0].equalsIgnoreCase("leveling"))
			{
				if (checkNumber(p, args[1]))
				{
					utils.sendMessage(p, prefix + " " + plugin.file_loc.getAttribute("level.give_exp").replace("<n>", args[1]));
					boolean is_up = false;
					item = a_utils.levelingProcess(item, Integer.parseInt(args[1]), is_up);
					p.getInventory().setItemInMainHand(item);
					if (is_up) utils_attribute.updateAttribute(p);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("durability"))
			{
				if (checkNumber(p, args[1]))
				{
					utils.sendMessage(p, prefix + " " + plugin.file_loc.getAttribute("durability.set").replace("<n>", args[1]));
					item = a_utils.setMaxDurability(item, (int) Double.parseDouble(args[1]));
					p.getInventory().setItemInMainHand(item);
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("type"))
			{
				if (!plugin.file_attr.getAllType().contains(args[1]))
				{
					String msg = plugin.file_loc.getError("no-type").replace("<t>", args[1]);
					p.sendMessage(msg);
					return false;
				}
				
				item = a_utils.setType(item, args[1]);
				p.getInventory().setItemInMainHand(item);
				String msg = plugin.file_loc.getAttribute("type").replace("<t>", args[1]);
				p.sendMessage(prefix + " " + msg);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("rate"))
			{
				if (!zrate.allRates().contains(args[1]))
				{
					String msg = plugin.file_loc.getError("no-rate").replace("<r>", args[1]);
					p.sendMessage(msg);
					return false;
				}
				
				item = a_utils.setRate(item, args[1]);
				if (a_utils.hasLeveling(item)!=-1)
				{
					item = a_utils.setLevel(item, new leveling(1, 0, (new zrate(args[1]).base_exp)));
				}
				p.getInventory().setItemInMainHand(item);
				String msg = plugin.file_loc.getAttribute("rate_set").replace("<r>", args[1]);
				p.sendMessage(prefix + " " + msg);
				return true;
			}
		}
		
		if (args.length==3)
		{
			if (args[0].equalsIgnoreCase("stat"))
			{
				if (!Zstat.isStat(args[1]))
				{
					String msg = plugin.file_loc.getError("no-stat").replace("<s>", args[1]);
					p.sendMessage(msg);
					return false;
				}
				
				if (checkNumber(p, args[2]))
				{
					item = a_utils.setStat(item, Zstat.getFromID(args[1]), Double.parseDouble(args[2]));
					p.getInventory().setItemInMainHand(item);
					String msg = plugin.file_loc.getAttribute("stat.set").replace("<s>", args[1]).replace("<n>", args[2]);
					utils.sendMessage(p, prefix + " " + msg);
					stat_manager.updateStat(p);
					return true;
				} else return false;
				
			}
			
			if (args[0].equalsIgnoreCase("buff"))
			{
				if (!Zbuff.isBuff(args[1]))
				{
					String msg = plugin.file_loc.getError("no-buff").replace("<b>", args[1]);
					p.sendMessage(msg);
					return false;
				}
				
				if (checkNumber(p, args[2]))
				{
					a_utils.addBuff(item, Zbuff.getFromID(args[1]), Integer.parseInt(args[2]));
					p.getInventory().setItemInMainHand(item);
					String msg = plugin.file_loc.getAttribute("buff_set").replace("<b>", args[1]).replace("<n>", args[2]);
					utils.sendMessage(p, prefix + " " + msg);
					buff_manager.updateBuffs(p);
					return true;
				} else return false;
				
			}
		}
		
		checkSyntax(sender, "/zattr help");
		help(p);

		return true;	
	}
	
	private void help(CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("attribute.help");
		if (sender instanceof Player)
		{
			utils.sendMessage(sender, "&9-----=====< &6/ZATTR HELP &9>=====-----");
			for(String s: help)
			{
				utils.sendMessage(sender, s);
			}
			utils.sendMessage(sender, "&9-----===========================-----");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> result = new ArrayList<>();
		
		if (!(sender instanceof Player)) return result;
		Player p = (Player) sender;
		
		if (!utils.hasPermission(p, permission)) return result;
		
		List<String> args_0 = new ArrayList<>();
		args_0.add("stat");
		args_0.add("buff");
		args_0.add("type");
		args_0.add("durability");
		args_0.add("leveling");
		args_0.add("rate");
		// more
		
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
			if (args[0].equalsIgnoreCase("stat"))
			{
				List<String> stats = plugin.file_attr.getAllStat();
				for (String s: stats)
				{
					if (s.startsWith(current)) result.add(s);
				}
				
			}
			
			if (args[0].equalsIgnoreCase("type"))
			{
				List<String> types = plugin.file_attr.getAllType();
				for (String s: types)
				{
					if (s.startsWith(current)) result.add(s);
				}
			}
			
			if (args[0].equalsIgnoreCase("buff"))
			{
				List<String> buffs = plugin.file_attr.getAllBuff();
				for (String s: buffs)
				{
					if (s.startsWith(current)) result.add(s);
				}
			}
			
			if (args[0].equalsIgnoreCase("rate"))
			{
				List<String> rates = zrate.allRates();
				for (String s: rates)
				{
					if (s.startsWith(current)) result.add(s);
				}
			}
		}
		
		result.sort(null);
		return result;
	}

}
