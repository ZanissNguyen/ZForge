package org.zanissnguyen.ZForge.UI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.UX.UX_repair;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.zrepair;

public class UI_repair extends UI
{
	public zrepair rp = new zrepair();
	public int rs_slot = 13;
	public utils_attribute a_utils;
	public String prefix;
	
	public UI_repair(ZForge plugin) {
		super(plugin, plugin.utils, plugin.file_loc.getForge("repair.ui-name"), 3);
		a_utils = plugin.a_utils;
		prefix = plugin.file_loc.getForge("repair.prefix");
		
		new UX_repair(this, plugin, utils);
	}
	
	public ItemStack repair_unslot(boolean full)
	{	
		String name = full ? plugin.file_loc.getForge("repair.icon.no-damage") 
				: plugin.file_loc.getForge("repair.icon.usage");
		
		return createIcon(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, name, false);
	}
	
	public ItemStack process(Player p, int stage)
	{	
		Material mat = Material.AIR;
		switch(stage)
		{
		case 0:
			mat = Material.WHITE_STAINED_GLASS_PANE;
			break;
		case 1:
			mat = Material.RED_STAINED_GLASS_PANE;
			break;
		case 2:
			mat = Material.YELLOW_STAINED_GLASS_PANE;
			break;
		case 3:
			mat = Material.LIME_STAINED_GLASS_PANE;
			break;
		}
		
		ItemStack result = utils.createItem(mat, 1, 0, " ");
		
		return result;
	}
	
	
	public ItemStack slot()
	{	
		String name = plugin.file_loc.getForge("repair.icon.repair-slot");
		
		return createIcon(Material.BLACK_STAINED_GLASS_PANE, 1, 107, name, false);
	}

	public Inventory setUp(Player p)
	{
		Inventory result = Bukkit.createInventory(null, this.rows*9, name);
		
		//basic frame
		for (int i = 0; i<rows*9; i++)
		{
			result.setItem(i, fillIcon());
		}
		
		result.setItem(3, process(p, 0));
		result.setItem(4, process(p, 0));
		result.setItem(5, process(p, 0));
		
		result.setItem(10, backIcon());
		result.setItem(11, balance(p));
		result.setItem(12, process(p, 0));
		result.setItem(rs_slot, slot());
		result.setItem(14, process(p, 0));
		result.setItem(15, repair_unslot(false));
		result.setItem(16, repair_unslot(false));
		
		result.setItem(21, process(p, 0));
		result.setItem(22, process(p, 0));
		result.setItem(23, process(p, 0));
		
		return result;
	}

	public void open(Player p) 
	{
		p.openInventory(this.setUp(p));
	}
}
