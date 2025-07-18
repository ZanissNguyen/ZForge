package org.zanissnguyen.ZForge.System.require;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;

public abstract class item_require {
	
	public String id;
	public String display;
	public String message;
	public boolean has_value;
	
	public item_require(String id)
	{
		this.id = id;
		this.display = ZForge.main().file_attr.getString("requirement."+id+".display");
		this.message = ZForge.main().file_attr.getString("requirement."+id+".message");
	}
	
	public boolean checkRequire(Player p, ItemStack item)
	{
		String require = ZForge.main().a_utils.getRequire(item, this);
		
		if (check(p, require)) return true;
		else
		{
			ZForge.main().utils.sendMessage(p, message.replace("<req>", require));
			return false;
		}
	}
	
	public abstract boolean check(Player p, String require);
	
	public abstract boolean validValue(String value);
}
