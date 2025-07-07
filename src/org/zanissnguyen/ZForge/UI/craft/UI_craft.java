package org.zanissnguyen.ZForge.UI.craft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.UI.UI;
import org.zanissnguyen.ZForge.Utils.utils;

public class UI_craft extends UI implements UI_craftAPI
{
	public List<Integer> materialsSlot = new ArrayList<>();
	public String name_prefix = "";
	public zrecipe recipe;
	
	public UI_craft(ZForge plugin, utils utils, int rows, zrecipe recipe) {
		super(plugin, utils, plugin.file_loc.getForge("recipe.craft.ui-name"), rows);
		name_prefix = plugin.file_loc.getForge("recipe.craft.ui-name");
		this.recipe = recipe;
	}

	public static ItemStack _backIcon_(zrecipe recipe)
	{
		return (new UI_craft(ZForge.main(), ZForge.main().utils, 1, recipe)).backIcon();
	}
	
	@Override
	public void setUpMaterial(Player p, Inventory inv)
	{
		Map<ItemStack, Integer> materials = recipe.materials();
		List<ItemStack> toShow = new ArrayList<>();
		for (ItemStack i: materials.keySet())
		{
			int current = utils.getAmount(i, p);
			int require = materials.get(i);
			String colorCode = "";
			if (current>=require) colorCode = "&a";
			else if (current>=0.25*require) colorCode = "&e";
			else colorCode = "&c";
			List<String> toAdd = new ArrayList<>();
			toAdd.add("");
			toAdd.add(colorCode+"&m----------"+colorCode+"<"+current+"/"+require+">"+"&m----------");
			
			i = utils.addAllLore(i, toAdd);
			toShow.add(i);
			
		}
		
		for (int i = 0; i<Math.min(toShow.size(),10); i++)
		{
			inv.setItem(materialsSlot.get(i), toShow.get(i));
		}
	}
	
	@Override
	public ItemStack craftIcon()
	{
		ItemStack craft = utils.createItem(Material.CRAFTING_TABLE, 1, 101);
		
		ItemMeta meta = craft.getItemMeta();
		String name = plugin.file_loc.getForge("craft-icon.name");
		List<String> lore = plugin.file_loc.getList("forge", "craft-icon", "lore");
		meta.setDisplayName(name);
		meta.setLore(lore);
		craft.setItemMeta(meta);
		
		return craft;
	}
	
	public static ItemStack _craftIcon_(zrecipe recipe)
	{
		return (new UI_craft(ZForge.main(), ZForge.main().utils, 1, recipe)).craftIcon();
	}
	
	@Override
	public ItemStack infoIcon(Player p)
	{
		ItemStack info = createIcon(Material.PAPER, 1, 104, " ", false);
		
		String yesIcon = plugin.file_loc.getSymbol("tick");
		String noIcon = plugin.file_loc.getSymbol("cross");
		
		ItemMeta meta = info.getItemMeta();
		String name = plugin.file_loc.getForge("info-icon.name");
		List<String> toAdd = plugin.file_loc.getList("forge", "info-icon", "lore");
		List<String> lore = new ArrayList<>();
		for (String s: toAdd)
		{
			if (s.contains("<lvl>"))
			{
				String colorCode = "";
				double level = utils.getLevel(p);
				level = utils.fixedDecimal(level, 2);
				double levelRequire = recipe.lvl;
				
				if (level<levelRequire)
				{
					colorCode = "&c";
					lore.add(utils.color(s.replace("<lvl>", colorCode +level+"/"+levelRequire+" "+noIcon)));
				}
				else
				{
					colorCode = "&a";
					lore.add(utils.color(s.replace("<lvl>", colorCode +level+"/"+levelRequire+" "+yesIcon)));
				}
			}
			else if (s.contains("<time>"))
			{
				lore.add(utils.color(s.replace("<time>", recipe.time+"")));
			}
			else if (s.contains("<material_status>"))
			{
				String status = "";
				if (enoughMaterial(p))
				{
					status = plugin.file_loc.getForge("info-icon.material-status.enough");
					lore.add(utils.color(s.replace("<material_status>", status+" "+yesIcon)));
				}
				else
				{
					status = plugin.file_loc.getForge("info-icon.material-status.not-enough");
					lore.add(utils.color(s.replace("<material_status>", status+" "+noIcon)));
				}
			} else
			{
				lore.add(utils.color(s));
			}
		}
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		info.setItemMeta(meta);
		
		return info;
	}
	
	public static ItemStack _infoIcon_(Player p, zrecipe recipe)
	{
		return (new UI_craft(ZForge.main(), ZForge.main().utils, 1, recipe)).infoIcon(p);
	}
	
	@Override
	public boolean enoughMaterial(Player p)
	{
		Map<ItemStack, Integer> toCheck = recipe.materials();
		for (ItemStack item: toCheck.keySet())
		{
			int playerHave = utils.getAmount(item, p);
			int require = toCheck.get(item);
			
			if (playerHave<require) return false;
		}
	
		
		return true;
	}

	@Override
	public void open(Player p) 
	{
		p.openInventory(setup(p, recipe));
	}

	@Override
	public Inventory setup(Player p, String recipe_id, int no_mean) {
		return setup(p, plugin.file_rep.getRecipe(recipe_id));
	}
	
	public Inventory setup(Player p, zrecipe recipe)
	{
		return setup(p, recipe);
	}
	
}

