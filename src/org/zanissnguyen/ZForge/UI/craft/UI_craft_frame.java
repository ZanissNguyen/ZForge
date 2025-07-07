package org.zanissnguyen.ZForge.UI.craft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.Utils.utils;

public class UI_craft_frame extends UI_craft 
{
	public UI_craft_frame(ZForge plugin, utils utils, zrecipe recipe) {
		super(plugin, utils, 5, recipe);

		materialsSlot.add(3);
		materialsSlot.add(11);
		materialsSlot.add(19);
		materialsSlot.add(29);
		materialsSlot.add(39);
		
		materialsSlot.add(5);
		materialsSlot.add(15);
		materialsSlot.add(25);
		materialsSlot.add(33);
		materialsSlot.add(41);
	}

	@Override
	public Inventory setup(Player p, zrecipe recipe) 
	{
		Inventory inv = Bukkit.createInventory(null, rows*9, name_prefix+utils.color(" &e(#"+recipe.id+")"));
		
		ItemStack laner = utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, false, " ");
		ItemStack fill_slot = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, false, " ");
		int type = 0;
		Material reward = recipe.reward.convertToItemStack(true).getType();
		if (reward.toString().contains("SWORD")) type =1;
		else if (reward.toString().contains("BOW")) type = 2;
		else if (reward.toString().contains("HELMET")) type = 3;
		else if (reward.toString().contains("CHESTPLATE")) type = 4;
		else if (reward.toString().contains("LEGGINGS")) type = 5;
		else if (reward.toString().contains("BOOTS")) type = 6;
		else if (reward.toString().contains("SHIELD")) type = 7;
		else if (reward.toString().contains("_AXE")) type = 8;
		else if (reward.toString().contains("PICKAXE") || reward.toString().contains("SHOVEL") ||
			reward.toString().contains("HOE")) type = 9;	
		else type = 0;
		
		List<ItemStack> progress = new ArrayList<>();
		for (int i = 0; i<8; i++)
		{
			if (type == 0)
			{
				progress.add(fillIcon());
			}
			else
			{
				progress.add(utils.createItem(Material.LIME_STAINED_GLASS_PANE, 1, 100+type*10+i, false, " "));
			}
		}
		
		inv.setItem(0, laner);
		inv.setItem(1, fillIcon());
		inv.setItem(2, fillIcon());
		inv.setItem(3, fill_slot);
		inv.setItem(4, fillIcon());
		inv.setItem(5, fill_slot);
		inv.setItem(6, fillIcon());
		inv.setItem(7, fillIcon());
		inv.setItem(8, laner);
		
		inv.setItem(9, laner);
		inv.setItem(10, fillIcon());
		inv.setItem(11, fill_slot);
		inv.setItem(12, progress.get(0));
		inv.setItem(13, progress.get(1));
		inv.setItem(14, progress.get(2));
		inv.setItem(15, fill_slot);
		inv.setItem(16, fillIcon());
		inv.setItem(17, laner);
		
		inv.setItem(18, laner);
		inv.setItem(19, fill_slot);
		inv.setItem(20, infoIcon(p));
		inv.setItem(21, progress.get(3));
		inv.setItem(22, recipe.reward.convertToItemStack(true));
		inv.setItem(23, progress.get(4));
		inv.setItem(24, craftIcon());
		inv.setItem(25, fill_slot);
		inv.setItem(26, laner);
		
		inv.setItem(27, laner);
		inv.setItem(28, fillIcon());
		inv.setItem(29, fill_slot);
		inv.setItem(30, progress.get(5));
		inv.setItem(31, progress.get(6));
		inv.setItem(32, progress.get(7));
		inv.setItem(33, fill_slot);
		inv.setItem(34, fillIcon());
		inv.setItem(35, laner);
		
		inv.setItem(36, laner);
		inv.setItem(37, fillIcon());
		inv.setItem(38, fillIcon());
		inv.setItem(39, fill_slot);
		inv.setItem(40, backIcon());
		inv.setItem(41, fill_slot);
		inv.setItem(42, fillIcon());
		inv.setItem(43, fillIcon());
		inv.setItem(44, laner);
		
		setUpMaterial(p, inv);
		
		return inv;
	}
}

