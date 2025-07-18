package org.zanissnguyen.ZForge.System.require;

import org.bukkit.entity.Player;
import org.zanissnguyen.ZForge.ZForge;

public class require_level extends item_require
{
	public require_level() {
		super("req-level");
		this.has_value = true;
	}

	@Override
	public boolean check(Player p, String require) 
	{
		if (!validValue(require)) return false;
		int level_require = Integer.parseInt(require);
		return ZForge.main().utils.getLevel(p)>=level_require;
	}

	@Override
	public boolean validValue(String value) 
	{
		return ZForge.main().utils.isNumber(value);
	}

}
