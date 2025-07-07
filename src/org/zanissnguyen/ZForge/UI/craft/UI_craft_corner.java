package org.zanissnguyen.ZForge.UI.craft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.Utils.utils;

public class UI_craft_corner extends UI_craft 
{
	public UI_craft_corner(ZForge plugin, utils utils, zrecipe recipe) {
		super(plugin, utils, 5, recipe);

		materialsSlot.add(4);
		materialsSlot.add(5);
		materialsSlot.add(6);
		materialsSlot.add(15);
		materialsSlot.add(24);
		
		materialsSlot.add(20);
		materialsSlot.add(29);
		materialsSlot.add(38);
		materialsSlot.add(40);
		materialsSlot.add(41);
	}

	@Override
	public Inventory setup(Player p, zrecipe recipe) 
	{
		Inventory inv = Bukkit.createInventory(null, rows*9, name_prefix+utils.color(" &e(#"+recipe.id+")"));
		
		ItemStack laner = utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, false, " ");
		ItemStack fill_slot = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, false, " ");
		ItemStack progress_right = utils.createItem(Material.LIME_STAINED_GLASS_PANE, 1, 102, false, " ");
		ItemStack progress_left = utils.createItem(Material.LIME_STAINED_GLASS_PANE, 1, 103, false, " ");
		
		inv.setItem(0, laner);
		inv.setItem(1, fillIcon());
		inv.setItem(2, fillIcon());
		inv.setItem(3, fillIcon());
		inv.setItem(4, fill_slot);
		inv.setItem(5, fill_slot);
		inv.setItem(6, fill_slot);
		inv.setItem(7, fillIcon());
		inv.setItem(8, laner);
		
		inv.setItem(9, laner);
		inv.setItem(10, fillIcon());
		inv.setItem(11, fillIcon());
		inv.setItem(12, fillIcon());
		inv.setItem(13, fillIcon());
		inv.setItem(14, fillIcon());
		inv.setItem(15, fill_slot);
		inv.setItem(16, fillIcon());
		inv.setItem(17, laner);
		
		inv.setItem(18, laner);
		inv.setItem(19, infoIcon(p));
		inv.setItem(20, fill_slot);
		inv.setItem(21, progress_right);
		inv.setItem(22, recipe.reward.convertToItemStack(true));
		inv.setItem(23, progress_left);
		inv.setItem(24, fill_slot);
		inv.setItem(25, craftIcon());
		inv.setItem(26, laner);
		
		inv.setItem(27, laner);
		inv.setItem(28, fillIcon());
		inv.setItem(29, fill_slot);
		inv.setItem(30, fillIcon());
		inv.setItem(31, backIcon());
		inv.setItem(32, fillIcon());
		inv.setItem(33, fillIcon());
		inv.setItem(34, fillIcon());
		inv.setItem(35, laner);
		
		inv.setItem(36, laner);
		inv.setItem(37, fillIcon());
		inv.setItem(38, fill_slot);
		inv.setItem(39, fill_slot);
		inv.setItem(40, fill_slot);
		inv.setItem(41, fillIcon());
		inv.setItem(42, fillIcon());
		inv.setItem(43, fillIcon());
		inv.setItem(44, laner);
		
		setUpMaterial(p, inv);
		
		return inv;
	}
}
