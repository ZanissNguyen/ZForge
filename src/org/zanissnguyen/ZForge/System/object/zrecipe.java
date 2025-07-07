package org.zanissnguyen.ZForge.System.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class zrecipe extends zobject 
{	
	private static ZForge plugin = ZForge.main();
	private static utils utils = ZForge.main().utils;
	
	public String type;
	public double lvl;
	public double money;
	public int time;
	public List<String> materials;
	public boolean require_bp;
	public boolean buy_bp;
	public double bp_cost;
	
	public zitem reward;
	public String bp;
	List<String> errors;
	
	public zrecipe(String id, List<String> description, String type, double lvl, double money,
			int time, List<String> materials, boolean require_bp, boolean buy_bp, double bp_cost) {
		super(id, description);
		this.type = type;
		this.lvl = lvl;
		this.money = money;
		this.time = time;
		this.materials = materials;
		this.require_bp = require_bp;
		this.buy_bp = buy_bp;
		this.bp_cost = bp_cost;
		
		this.errors = new ArrayList<>();
		this.reward = plugin.file_itm.getItem(id);
		this.bp = "zforge.recipe."+id;
	}

	@Override
	public ItemStack convertToItemStack(boolean is_icon) {
		ItemStack result = reward.convertToItemStack(true);
		ItemMeta meta = result.getItemMeta();
		
		List<String> format = plugin.file_form.recipeFormat();
		List<String> description = plugin.file_form.recipeRequirement();
		String material = plugin.file_form.recipeMaterial();
		
		List<String> lore = new ArrayList<>();
		for (String s: format)
		{
			if (s.equalsIgnoreCase("<requirement>"))
			{
				for (String s1: description)
				{
					if (s1.contains("<lvl>") && this.lvl!=0) lore.add(utils.color(s1.replace("<lvl>", lvl+"")));
					if (s1.contains("<t>") && time!=0) lore.add(utils.color(s1.replace("<t>", time+"")));
					if (s1.contains("<money>") && money!=0 && plugin.vaultHooked)
						lore.add(utils.color(s1.replace("<money>", money+""))); 
				}
			}
			else if (s.equalsIgnoreCase("<description>"))
			{
				for (String s1: this.description)
				{
					lore.add(utils.color(s1));
				}
			}
			else if (s.equalsIgnoreCase("<material>"))
			{
				Map<ItemStack, Integer> materials = materials(this.materials, this.errors);
				for (ItemStack mat: materials.keySet())
				{
					int number = 0;
					String display = "";
					if (mat.getItemMeta()==null || !mat.getItemMeta().hasDisplayName())
					{
						number = materials.get(mat);
						display = utils.standardString(mat.getType().toString());
					}
					else
					{
						number = materials.get(mat);
						display = mat.getItemMeta().getDisplayName();
					}
					lore.add(utils.color(material.replace("<amount>", number+"").replace("<display>", display)));
				}
			}
			else if (s.equalsIgnoreCase("<reward_lore>"))
			{
				for (String s1: reward.description)
				{
					lore.add(utils.color(s1));
				}
			}
			else lore.add(utils.color(s));
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		result.setItemMeta(meta);
		
		if (errors.size()!=0)
		{
			result = utils.addAllLore(result, errors);
		}
		
		return result;
	}
	
	public Map<ItemStack, Integer> materials()
	{
		return materials(this.materials, this.errors);
	}
	
	public static Map<ItemStack, Integer> materials(List<String> materials, List<String> errors)
	{	
		Map<ItemStack, Integer> result = new HashMap<>();
		for (String s: materials)
		{
			ItemStack item;
			int amount = 1;
			int data = 0;
			String data_str = "";
			
			String id_data = s.substring(0, s.indexOf(":"));
			String amount_str = s.substring(s.indexOf(":")+1);
			
			if (!utils.isNumber(amount_str)) 
			{
				errors.add(utils.color("&4+ amount: &c"+amount_str+"&4 is not a number"));
			} else amount = Integer.parseInt(amount_str);
			
			String itemCode = id_data;
			
			if (id_data.contains("-")) 
			{
				itemCode = id_data.substring(0, id_data.indexOf("-"));
				data_str = id_data.substring(id_data.indexOf("-")+1);
			}
			
			if (!data_str.equalsIgnoreCase(""))
			{
				if (!utils.isNumber(data_str))
				{
					errors.add(utils.color("&4+ data: &c"+data_str+"&4 is not a number"));
				} else data = Integer.parseInt(data_str);
			}
			
			if (plugin.file_mat.isIDExist(itemCode)) // material in materials.yml
			{
				item = plugin.file_mat.getMaterial(itemCode).convertToItemStack(false);
			}
			else // vanilla
			{
				if (!utils.isMaterial(itemCode))
				{
					item = utils.createItem(Material.BARRIER, 1, 0, utils.color("&c"+itemCode+" &4is not valid!"));
					errors.add(utils.color("&4+ material: &c"+itemCode+"&4 is not a vanilla material."));
				}
				else
				{
					item =utils.createItem(Material.valueOf(itemCode), 1, data);
				}
			}
			
			result.put(item, amount);
		}
		return result;
	}

}
