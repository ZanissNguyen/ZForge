package org.zanissnguyen.ZForge.UI.craft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.Utils.utils;

public class UI_craft_vertical extends UI_craft
{
	public UI_craft_vertical(ZForge plugin, utils utils, zrecipe recipe) {
		super(plugin, utils, 6, recipe);

		materialsSlot.add(29);
		materialsSlot.add(30);
		materialsSlot.add(31);
		materialsSlot.add(32);
		materialsSlot.add(33);
		
		materialsSlot.add(38);
		materialsSlot.add(39);
		materialsSlot.add(40);
		materialsSlot.add(41);
		materialsSlot.add(42);
	}

	@Override
	public Inventory setup(Player p, zrecipe recipe) 
	{
		Inventory inv = Bukkit.createInventory(null, rows*9, name_prefix+utils.color(" &e(#"+recipe.id+")"));
		
		ItemStack laner = utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, false, " ");
		ItemStack fill_slot = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, false, " ");
		ItemStack progress = utils.createItem(Material.LIME_STAINED_GLASS_PANE, 1, 101, false, " ");
		
		inv.setItem(0, laner);
		inv.setItem(1, laner);
		inv.setItem(2, laner);
		inv.setItem(3, laner);
		inv.setItem(4, laner);
		inv.setItem(5, laner);
		inv.setItem(6, laner);
		inv.setItem(7, laner);
		inv.setItem(8, laner);
		
		inv.setItem(9, laner);
		inv.setItem(10, fillIcon());
		inv.setItem(11, infoIcon(p));
		inv.setItem(12, fillIcon());
		inv.setItem(13, recipe.reward.convertToItemStack(true));
		inv.setItem(14, fillIcon());
		inv.setItem(15, craftIcon());
		inv.setItem(16, fillIcon());
		inv.setItem(17, laner);
		
		inv.setItem(18, laner);
		inv.setItem(19, fillIcon());
		inv.setItem(20, fillIcon());
		inv.setItem(21, fillIcon());
		inv.setItem(22, progress);
		inv.setItem(23, fillIcon());
		inv.setItem(24, fillIcon());
		inv.setItem(25, fillIcon());
		inv.setItem(26, laner);
		
		inv.setItem(27, laner);
		inv.setItem(28, fillIcon());
		inv.setItem(29, fill_slot);
		inv.setItem(30, fill_slot);
		inv.setItem(31, fill_slot);
		inv.setItem(32, fill_slot);
		inv.setItem(33, fill_slot);
		inv.setItem(34, fillIcon());
		inv.setItem(35, laner);
		
		inv.setItem(36, laner);
		inv.setItem(37, fillIcon());
		inv.setItem(38, fill_slot);
		inv.setItem(39, fill_slot);
		inv.setItem(40, fill_slot);
		inv.setItem(41, fill_slot);
		inv.setItem(42, fill_slot);
		inv.setItem(43, fillIcon());
		inv.setItem(44, laner);
		
		inv.setItem(45, laner);
		inv.setItem(46, laner);
		inv.setItem(47, laner);
		inv.setItem(48, laner);
		inv.setItem(49, backIcon());
		inv.setItem(50, laner);
		inv.setItem(51, laner);
		inv.setItem(52, laner);
		inv.setItem(53, laner);
		
		setUpMaterial(p, inv);
		
		return inv;
	}
	
}
