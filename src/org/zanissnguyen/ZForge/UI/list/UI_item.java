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
import org.zanissnguyen.ZForge.Listeners.UX.UX_item;
import org.zanissnguyen.ZForge.System.object.zitem;

public class UI_item extends UI_list
{
	public final String permission = "zforge.item";
	public static String name_prefix;
	public ItemStack laner = null;
	public ItemStack fill_slot = null;
	
	public UI_item(ZForge plugin) {
		super(plugin, plugin.utils, plugin.file_loc.getForge("item.ui-name"), 6);
		
		name_prefix = plugin.file_loc.getForge("item.ui-name");
		laner = utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, 1, 0, false, " ");
		fill_slot = utils.createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, 0, false, " ");
		
		new UX_item(plugin, utils, this);
	}

	public ItemStack loadIcon(zitem item, boolean tips)
	{
		ItemStack result = item.convertToItemStack(true);
		
		if (tips)
		{
			ItemMeta meta = result.getItemMeta();
			List<String> lore = utils.getLore(result);
			
			String current_name = (meta.hasDisplayName())? meta.getDisplayName() : utils.standardString(item.type);
			meta.setDisplayName(current_name+utils.color("&7 (#"+item.id+")"));
			
			List<String> toAdd = plugin.file_loc.getList("forge", "list-tips");
			lore.addAll(toAdd);
			meta.setLore(lore);
			result.setItemMeta(meta);
		}
		
		return result;
	}

	@Override
	public Inventory setup(Player p, String searchKey, int page) {
		// TODO Auto-generated method stub
		ItemStack fill = fillIcon();
		List<ItemStack> items = loadList(p, searchKey, page);
		
		int maxPage = plugin.file_itm.searchFor(searchKey).size()/36+1;
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
		result.setItem(49, addIcon());
		result.setItem(50, findIcon(searchKey, plugin.file_itm.searchFor(searchKey).size()));
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
		boolean showTips = utils.hasPermission(p, permission);
		
		List<zitem> items = new ArrayList<>();
		for (String s: plugin.file_itm.searchFor(search))
		{
			items.add(plugin.file_itm.getItem(s));
		}
		
		List<ItemStack> itemStacks = new ArrayList<>();
		for (zitem i: items)
		{
			itemStacks.add(loadIcon(i, showTips));
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

