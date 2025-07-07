package org.zanissnguyen.ZForge.System.object;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public abstract class zobject implements zobjectAPI
{
	public String id;
	public List<String> description;
	public zobject(String id, List<String> description)
	{
		this.id = id;
		this.description = description;
	}
	
	@Override
	public abstract ItemStack convertToItemStack(boolean is_icon);

}
