package org.zanissnguyen.ZForge.System.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class zmaterial extends zobject
{	
	private static ZForge plugin = ZForge.main();
	private static utils utils = ZForge.main().utils;
	
	public String type;
	public int data;
	public String display;
	public boolean glow;
	public boolean unbreak;
	
	public zmaterial(String id, String type, int data, String display, List<String> description, boolean glow, boolean unbreak) {
		super(id, description);
		this.type = type;
		this.data = data;
		this.display = display;
		this.glow = glow;
		this.unbreak = unbreak;
	}
	
	public zmaterial(String id, ItemStack item)
	{
		super(id, new ArrayList<>());
		ItemMeta meta = item.getItemMeta();
		
		// get type
		this.type = item.getType().toString();
		
		// get data
		if (meta.hasCustomModelData()) this.data = meta.getCustomModelData();
		
		// get display
		if (meta.hasDisplayName()) this.display = meta.getDisplayName();
		
		// get glow
		this.glow = meta.hasEnchants();
		
		// get unbreak
		this.unbreak = meta.isUnbreakable();
		
		// get description
		if (meta.hasLore()) this.description = meta.getLore();
	}

	@Override
	public ItemStack convertToItemStack(boolean is_icon) 
	{
		return convertToItemStack(is_icon, true);
	}
	
	public ItemStack convertToItemStack(boolean is_icon, boolean type_display) 
	{
		ItemStack result;
		// type define and data
		if (utils.isMaterial(this.type)) 
			result = plugin.utils.createItem(Material.valueOf(this.type), 1, this.data);
		else 
		{
			String msg = plugin.file_loc.getError("no-type").replace("<t>", this.type);
			return utils.createItem(Material.BARRIER, 1, 0, msg);
		}
		// display
		if (!this.display.equalsIgnoreCase("Not Found"))
			result = utils.setName(result, this.display);
		// glow
		if (this.glow)
		{
			result = utils.addEnchant(result, Enchantment.LUCK, 1);
			result = utils.addFlag(result, ItemFlag.HIDE_ENCHANTS);
		}
		// unbreak
		result = utils.setUnbreak(result, this.unbreak);
		// description
		List<String> lore = new ArrayList<>();
		List<String> format = plugin.file_form.materialFormat();
		if (!type_display) format.remove("<type>");
		String type = plugin.file_form.typeFormat();
		type = type.replace("<type>", plugin.file_attr.getType("material"));
		for (String s: format)
		{
			if (s.equalsIgnoreCase("<type>")) lore.add(type);
			else if (s.equalsIgnoreCase("<lore>")) lore.addAll(this.description);
			else lore.add(s);
		}
		lore = utils.standardListString(lore);
		result = utils.addAllLore(result, lore);
		
		return result;
	}

}
