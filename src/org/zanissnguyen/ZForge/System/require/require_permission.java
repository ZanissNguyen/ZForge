package org.zanissnguyen.ZForge.System.require;

import org.bukkit.entity.Player;
import org.zanissnguyen.ZForge.ZForge;

public class require_permission extends item_require
{
	public require_permission() {
		super("req-permission");
		this.has_value = true;
	}

	@Override
	public boolean check(Player p, String require) 
	{
		if (!validValue(require)) return false;
		
		return ZForge.main().utils.hasPermission(p, require);
	}

	@Override
	public boolean validValue(String value) {

		return !value.contains(" ");
	}

}
