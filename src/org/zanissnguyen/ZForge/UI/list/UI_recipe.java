package org.zanissnguyen.ZForge.UI.list;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.UX.UX_recipe;
import org.zanissnguyen.ZForge.System.object.zrecipe;

public class UI_recipe extends UI_list
{
	public final String permission = "zforge.recipe";
	public static String name_prefix;
	public ItemStack laner = null;
	public ItemStack fill_slot = null;
	
	public UI_recipe(ZForge plugin) {
		super(plugin, plugin.utils, plugin.file_loc.getForge("recipe.ui-name"), 6);
		
		name_prefix = plugin.file_loc.getForge("recipe.ui-name");
		laner = utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, false, " ");
		fill_slot = utils.createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, 0, false, " ");
		
		new UX_recipe(plugin, utils, this);
	}

	public ItemStack loadIcon(zrecipe recipe, boolean is_admin, boolean have_recipe)
	{
		ItemStack result = recipe.convertToItemStack(true);
		
		result = utils.setName(result, utils.getName(result)+" &e(#"+recipe.id+")");
		
		if ((recipe.require_bp && (have_recipe || is_admin)) || !recipe.require_bp)
		{	
			List<String> tips = plugin.file_loc.getList("forge.recipe.tips.blueprint-acquire");
			result = utils.addAllLore(result, tips);
		}
		else if (recipe.require_bp && !have_recipe && recipe.buy_bp && plugin.vaultHooked && !is_admin)
		{
			List<String> addTips = new ArrayList<>();
			List<String> tips = plugin.file_loc.getList("forge", "recipe", "tips", "blueprint-buy");
			for (String str: tips)
			{
				if (str.contains("<blueprint>")) addTips.add(str.replace("<blueprint>", recipe.bp));
				else if (str.contains("<cost>") && plugin.vaultHooked) addTips.add(str.replace("<cost>", recipe.bp_cost+"")); 
				else addTips.add(str);
			}
			result = utils.addAllLore(result, addTips);
		}
		else if (recipe.require_bp && !recipe.buy_bp)
		{
			List<String> tips = plugin.file_loc.getList("forge.recipe.tips.blueprint-not-buy");
			result = utils.addAllLore(result, tips);
		}
		
		return result;
	}

	@Override
	public Inventory setup(Player p, String searchKey, int page) {
		// TODO Auto-generated method stub
		ItemStack fill = fillIcon();
		List<ItemStack> items = loadList(p, searchKey, page);
		
		int maxPage = plugin.file_mat.searchFor(searchKey).size()/36+1;
		String new_name = name_prefix + utils.color(" &2["+searchKey+"] &f| &2["+page+"/"+maxPage+"]");
		Inventory result = Bukkit.createInventory(null, this.rows*9, new_name);
		
		//basic frame
		for (int i = 0; i<36; i++)
		{
			result.setItem(i, fill_slot);
		}
		
		// fifth row
		for (int i = 36; i<45; i++)
		{
			result.setItem(i, laner);
		}
		
		// sixth row
		result.setItem(45, fill);
		result.setItem(46, fill);
		result.setItem(47, fill);
		result.setItem(48, refreshIcon());
		result.setItem(49, utils.hasPermission(p, permission) ? addIcon(): balance(p));
		result.setItem(50, findIcon(searchKey, plugin.file_mat.searchFor(searchKey).size()));
		result.setItem(51, fill);
		result.setItem(52, fill);
		result.setItem(53, fill);
		
		
		for (int i = 0 ; i<items.size(); i++) result.setItem(i, items.get(i));
		
		if (page != 1) result.setItem(45, previousIcon());
		if (page != maxPage) result.setItem(53, nextIcon());
		
		return result;
	}
	
	public List<ItemStack> loadList(Player p, String search, int page)
	{
		List<ItemStack> result = new ArrayList<>();
		
		List<ItemStack> items = new ArrayList<>();
		for (String s: plugin.file_rep.searchFor(search))
		{
			zrecipe recipe = plugin.file_rep.getRecipe(s);
			
			items.add(loadIcon(recipe, utils.hasPermission(p, permission), utils.hasPermission(p, recipe.bp)));
		}
		
		for (int i = 0 ;i<items.size(); i++)
		{
			if (i>= (page-1)*36 && i<page*36)
			{
				result.add(items.get(i));
			}
		}
		
		return result;
	}
}
