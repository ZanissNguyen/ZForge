package org.zanissnguyen.ZForge.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public abstract class zcmds implements CommandExecutor, TabCompleter
{
	protected ZForge plugin;
	protected utils utils;
	
	public zcmds(ZForge plugin, utils utils, String name)
	{
		this.plugin = plugin;
		this.utils = utils;
		this.plugin.getCommand(name).setExecutor(this);
		this.plugin.getCommand(name).setTabCompleter(this);
	}
	
	public Boolean checkPlayer(CommandSender sender)
	{
		if (!(sender instanceof Player))
		{
			String e_msg = plugin.file_loc.getError("only-player");
			utils.sendMessage(sender, e_msg);
			return false;
		}
		return true;
	}
	
	public Boolean checkNumber(CommandSender sender, String num)
	{
		if (!(utils.isNumber(num)))
		{
			String e_msg = plugin.file_loc.getError("not-number");
			utils.sendMessage(sender, e_msg.replace("<n>", num));
			return false;
		}
		return true;
	}
	
	public void checkSyntax(CommandSender sender, String guide)
	{
		String e_msg = plugin.file_loc.getError("wrong-syntax");
		utils.sendMessage(sender, e_msg.replace("<cmd>", guide));
	}
	
	public Boolean checkPermission(CommandSender p, String perm)
	{
		if (!(utils.hasPermission(p, perm)))
		{
			String e_msg = plugin.file_loc.getError("no-permission");
			utils.sendMessage(p, e_msg);
			return false;
		}
		return true;
	}
	
	public Boolean checkHand(Player sender)
	{
		if (!(utils.isHoldItem(sender)))
		{
			String e_msg = plugin.file_loc.getError("hand-nothing");
			utils.sendMessage(sender, e_msg);
			return false;
		}
		return true;
	}
	
	public Boolean checkIndexRange(CommandSender sender, Boolean range_cond, String idx, String range)
	{
		if (!(range_cond))
		{
			String e_msg = plugin.file_loc.getError("out-index");
			utils.sendMessage(sender, e_msg.replace("<n>", idx).replace("<r>", range));
			return false;
		}
		return true;
	}
	
	public Boolean checkEnch(CommandSender sender, String str)
	{
		if (!(utils.isEnchantment(str)))
		{
			String e_msg = plugin.file_loc.getError("no-enchantment");
			utils.sendMessage(sender, e_msg.replace("<e>", str));
			return false;
		}
		return true;
	}
	
	public Boolean checkFlag(CommandSender sender, String str)
	{
		if (!(utils.isFlag(str)))
		{
			String e_msg = plugin.file_loc.getError("no-flag");
			utils.sendMessage(sender, e_msg.replace("<f>", str));
			return false;
		}
		return true;
	}
	
	public Boolean checkPlayerExist(CommandSender sender, String str)
	{
		if (!(utils.isPlayerExist(str)))
		{
			String e_msg = plugin.file_loc.getError("no-player");
			utils.sendMessage(sender, e_msg.replace("<p>", str));
			return false;
		}
		return true;
	}
	
	public Boolean checkPlayerOnline(CommandSender sender, String str)
	{
		if (!(utils.isOnlinePlayer(str)))
		{
			String e_msg = plugin.file_loc.getError("player-not-online");
			utils.sendMessage(sender, e_msg.replace("<p>", str));
			return false;
		}
		return true;
	}
	
	public Boolean checkStat(CommandSender sender, String str)
	{
		// TODO: not now wait for stat
		return false;
		// no-stat
	}
	
	public Boolean checkType(CommandSender sender, String str)
	{
		// TODO: not now wait for type
		// no-type
		return false;
	}
}
