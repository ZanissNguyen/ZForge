package org.zanissnguyen.ZForge.UI.list;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.UX.UX_gem;
import org.zanissnguyen.ZForge.System.object.zgem;

public class UI_gem extends UI_list
{
	public final String permission = "zforge.gem";
	public static String name_prefix;
	public ItemStack laner = null;
	public ItemStack fill_slot = null;
	
	public UI_gem(ZForge plugin) {
		super(plugin, plugin.utils, plugin.file_loc.getForge("gem.ui-name"), 6);
		
		name_prefix = plugin.file_loc.getForge("gem.ui-name");
		laner = utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, false, " ");
		fill_slot = utils.createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, 0, false, " ");
		
		new UX_gem(plugin, utils, this);
	}

	public ItemStack loadIcon(zgem item, boolean is_admin)
	{
		ItemStack result = item.convertToItemStack(true);
		
		String current_name = (!utils.getName(result).equalsIgnoreCase(""))? utils.getName(result) : utils.standardString(item.type);
		result = utils.setName(result, current_name+utils.color("&7 (#"+item.id+")"));
		
		if (is_admin)
		{
			ItemMeta meta = result.getItemMeta();
			List<String> lore = utils.getLore(result);
			
						
			List<String> toAdd = plugin.file_loc.getList("forge", "list-tips");
			lore.addAll(toAdd);
			meta.setLore(lore);
			result.setItemMeta(meta);
		}
		else if (item.can_buy)
		{
			List<String> addTips = new ArrayList<>();
			List<String> lore = plugin.file_loc.getList("forge.recipe.tips.blueprint-buy");
			
			for (String s: lore)
			{
				if (s.contains("<blueprint>")) continue;
				else if (s.contains("<cost>") && plugin.vaultHooked) addTips.add(s.replace("<cost>", item.cost+"")); 
				else addTips.add(s);
			}
			
			result = utils.addAllLore(result, addTips);
		}
		
		return result;
	}

	@Override
	public Inventory setup(Player p, String searchKey, int page) {
		// TODO Auto-generated method stub
		ItemStack fill = fillIcon();
		List<ItemStack> items = loadList(p, searchKey, page);
		
		int maxPage = plugin.file_gem.searchFor(searchKey).size()/36+1;
//		int maxPage = items.size()/36+1;
		String new_name = name_prefix + utils.color(" &2["+searchKey+"] &f| &2["+page+"/"+maxPage+"]");
		Inventory result = Bukkit.createInventory(null, this.rows*9, new_name);
		
		boolean is_admin = utils.hasPermission(p, permission);
		
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
		
		// 39
		result.setItem(39, zgem.remover(true, is_admin));
		// 40
		result.setItem(40, zgem.driller(true, is_admin));
		// 41
		result.setItem(41, zgem.smasher(true, is_admin));
		
		// sixth row
		result.setItem(45, fill);
		result.setItem(46, fill);
		result.setItem(47, fill);
		result.setItem(48, refreshIcon());
		result.setItem(49, is_admin? addIcon() : balance(p));
		result.setItem(50, findIcon(searchKey, plugin.file_gem.searchFor(searchKey).size()));
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
		boolean show_tips = utils.hasPermission(p, permission);
		
		List<zgem> gems = new ArrayList<>();
		for (String s: plugin.file_gem.searchFor(search))
		{
			gems.add(plugin.file_gem.getGem(s));
		}
		
		List<ItemStack> itemStacks = new ArrayList<>();
		for (zgem gem: gems)
		{
			itemStacks.add(loadIcon(gem, show_tips));
		}
		
		for (int i = 0 ;i<itemStacks.size(); i++)
		{
			if (i>= (page-1)*36 && i<page*36)
			{
				result.add(itemStacks.get(i));
			}
		}
		
		return result;
	}
}