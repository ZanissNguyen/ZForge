package org.zanissnguyen.ZForge.UI.list;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.UI.UI;
import org.zanissnguyen.ZForge.Utils.utils;

public abstract class UI_list extends UI implements UI_listAPI {
	
	public UI_list(ZForge plugin, utils utils, String name, int rows) {
		super(plugin, utils, name, rows);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack findIcon(String toFind, int size) {
		ItemStack find = createIcon(Material.PAPER, 1, 103, " ", false);
		
		ItemMeta meta = find.getItemMeta();
		String name = plugin.file_loc.getForge("search-icon.name");
		List<String> got = plugin.file_loc.getList("forge", "search-icon", "lore");
		List<String> lore = new ArrayList<>();
		meta.setDisplayName(name);
		String toReplace = ""+size;
		
		for (String s: got)
		{
			lore.add(s.replace("<w>", toFind).replace("<n>", toReplace));
		}
		meta.setLore(lore);
		find.setItemMeta(meta);
		
		return find;
	}

	@Override
	public ItemStack nextIcon() {
		// TODO Auto-generated method stub
		ItemStack nextPage = createIcon(Material.ARROW, 1, 102, " ", false);
		
		ItemMeta meta = nextPage.getItemMeta();
		String name = plugin.file_loc.getForge("next-icon.name");
		List<String> lore = plugin.file_loc.getList("forge", "next-icon", "lore");
		meta.setDisplayName(name);
		meta.setLore(lore);
		nextPage.setItemMeta(meta);
		
		return nextPage;
	}

	@Override
	public ItemStack previousIcon() {
		// TODO Auto-generated method stub
		ItemStack previousPage = createIcon(Material.ARROW, 1, 101, " ", false);
		
		ItemMeta meta = previousPage.getItemMeta();
		String name = plugin.file_loc.getForge("previous-icon.name");
		List<String> lore = plugin.file_loc.getList("forge", "previous-icon", "lore");
		meta.setDisplayName(name);
		meta.setLore(lore);
		previousPage.setItemMeta(meta);
		
		return previousPage;
	}

	@Override
	public ItemStack addIcon() {
		// TODO Auto-generated method stub
		ItemStack addNew = createIcon(Material.PAPER, 1, 102, " ", false);
		
//		ItemMeta meta = addNew.getItemMeta();
//		String name = plugin.locale.get("locale", "forge", "add-icon", "name");
//		List<String> lore = plugin.locale.getList("locale", "forge", "add-icon", "lore");
//		meta.setDisplayName(name);
//		for (String s: lore)
//		{
//			
//		}
//		meta.setLore(lore);
//		addNew.setItemMeta(meta);
		
		return addNew;
	}

	@Override
	public ItemStack refreshIcon() {
		// TODO Auto-generated method stub
		ItemStack refresh = createIcon(Material.PAPER, 1, 101, " ", false);
		
		ItemMeta meta = refresh.getItemMeta();
		String name = plugin.file_loc.getForge("refresh-icon.name");
		List<String> lore = plugin.file_loc.getList("forge", "refresh-icon", "lore");
		meta.setDisplayName(name);
		meta.setLore(lore);
		refresh.setItemMeta(meta);
		
		return refresh;
	}

}
