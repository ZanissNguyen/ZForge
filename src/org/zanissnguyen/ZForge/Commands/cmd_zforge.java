package org.zanissnguyen.ZForge.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Commands.zforge.cmd_gem;
import org.zanissnguyen.ZForge.Commands.zforge.cmd_item;
import org.zanissnguyen.ZForge.Commands.zforge.cmd_material;
import org.zanissnguyen.ZForge.Utils.utils;

public class cmd_zforge extends zcmds
{
	public cmd_zforge(ZForge plugin, utils utils) {
		super(plugin, utils, "forge");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
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
		
		if (args.length>=1)
		{
			if (args[0].equalsIgnoreCase("reload"))
			{
				if (checkPermission(sender, "reload")) 
				{
					plugin.reload();
					utils.sendMessage(sender, plugin.file_loc.getReload()
							.replace("<plugin_name>", plugin.getName()).replace("<version>", plugin.getDescription().getVersion()));
				}
				return true;
			}
			
			if (args[0].equalsIgnoreCase("item"))
			{
				// item menu, command
				cmd_item.itemOnCommand(this, plugin, utils, sender, cmd, label, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("material"))
			{
				// material menu, command
				cmd_material.materialOnCommand(this, plugin, utils, sender, cmd, label, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("recipe"))
			{
				// recipe menu, command
				if (!checkPlayer(sender)) return false;
				Player p = (Player) sender;
				plugin.ui_rep.open(p, "", 1);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("repair"))
			{
				if (!checkPlayer(sender)) return false;
				Player p = (Player) sender;
				plugin.ui_rpa.open(p);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("stat"))
			{
				// stat menu
				if (!checkPlayer(sender)) return false;
				Player p = (Player) sender;
				plugin.ui_sta.open(p);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("gem"))
			{
				cmd_gem.gemOnCommand(this, plugin, utils, sender, cmd, label, args);
				return true;
			}
		}
		checkSyntax(sender, "/forge help");
		help(sender);
		return true;
	}

	private void help(CommandSender sender)
	{
		List<String> help = plugin.file_loc.getList("forge.help");
		if (sender instanceof Player)
		{
			utils.sendMessage(sender, "&9-----=====<    &6FORGE    &9>=====-----");
			for (String s: help)
			{
				utils.sendMessage(sender, s);
			}
			utils.sendMessage(sender, "&6[]:opt, <>:req");
			utils.sendMessage(sender, "&9-----=========================-----");
		}
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> result = new ArrayList<>();
		
		List<String> args_0 = new ArrayList<>();
		args_0.add("item");
		args_0.add("material");
		args_0.add("recipe");
		args_0.add("stat");
		if (utils.hasPermission(sender, "zforge.reload")) args_0.add("reload");
		args_0.add("repair");
		if (args.length==1)
		{
			String current = args[0];
			for (String s: args_0)
			{
				if (s.startsWith(current)) result.add(s);
			}
		}
		
		if (args.length>=2)
		{
			if (args[0].equalsIgnoreCase("item"))
			{
				result = cmd_item.itemOnTabCompletor(this, plugin, utils, sender, cmd, label, args);
			}
			if (args[0].equalsIgnoreCase("material"))
			{
				result = cmd_material.materialOnTabCompletor(this, plugin, utils, sender, cmd, label, args);
			}
			if (args[0].equalsIgnoreCase("gem"))
			{
				result = cmd_gem.gemOnTabCompletor(this, plugin, utils, sender, cmd, label, args);
			}
		}
		
		result.sort(null);
		return result;
	}

}
