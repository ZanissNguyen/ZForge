package org.zanissnguyen.ZForge.UI.craft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.Utils.utils;

public class UI_craft_horizontal extends UI_craft 
{
	public UI_craft_horizontal(ZForge plugin, utils utils, zrecipe recipe) {
		super(plugin, utils, 5, recipe);

		materialsSlot.add(10);
		materialsSlot.add(11);
		materialsSlot.add(12);
		
		materialsSlot.add(19);
		materialsSlot.add(20);
		materialsSlot.add(21);
		
		materialsSlot.add(28);
		materialsSlot.add(29);
		materialsSlot.add(30);
		
		materialsSlot.add(22);
	}

	@Override
	public Inventory setup(Player p, zrecipe recipe) 
	{
		Inventory inv = Bukkit.createInventory(null, rows*9, name_prefix+utils.color(" &e(#"+recipe.id+")"));
		
		ItemStack laner = utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, false, " ");
		ItemStack fill_slot = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, false, " ");
		ItemStack progress = utils.createItem(Material.LIME_STAINED_GLASS_PANE, 1, 102, false, " ");
		
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
		inv.setItem(10, fill_slot);
		inv.setItem(11, fill_slot);
		inv.setItem(12, fill_slot);
		inv.setItem(13, fillIcon());
		inv.setItem(14, fillIcon());
		inv.setItem(15, infoIcon(p));
		inv.setItem(16, fillIcon());
		inv.setItem(17, laner);
		
		inv.setItem(18, laner);
		inv.setItem(19, fill_slot);
		inv.setItem(20, fill_slot);
		inv.setItem(21, fill_slot);
		inv.setItem(22, fill_slot);
		inv.setItem(23, progress);
		inv.setItem(24, recipe.reward.convertToItemStack(true));
		inv.setItem(25, craftIcon());
		inv.setItem(26, laner);
		
		inv.setItem(27, laner);
		inv.setItem(28, fill_slot);
		inv.setItem(29, fill_slot);
		inv.setItem(30, fill_slot);
		inv.setItem(31, fillIcon());
		inv.setItem(32, fillIcon());
		inv.setItem(33, backIcon());
		inv.setItem(34, fillIcon());
		inv.setItem(35, laner);
		
		inv.setItem(36, laner);
		inv.setItem(37, laner);
		inv.setItem(38, laner);
		inv.setItem(39, laner);
		inv.setItem(40, laner);
		inv.setItem(41, laner);
		inv.setItem(42, laner);
		inv.setItem(43, laner);
		inv.setItem(44, laner);
		
		setUpMaterial(p, inv);
		
		return inv;
	}
}
