package org.zanissnguyen.ZForge.UI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;
public class UI implements UIAPI
{
	public ZForge plugin;
	public utils utils;
	
	protected Inventory inv;
	public String name;
	public int rows;
	
	public UI(ZForge plugin, utils utils, String name, int rows)
	{
		this.plugin = plugin;
		this.utils = utils;
		
		this.name = utils.color(name);
		this.rows = rows;
		this.inv = Bukkit.createInventory(null, this.rows*9, this.name);
	}
	
	@Override
	public ItemStack createIcon(Material type, int amount, int data, String name, boolean glow, String... str)
	{
		ItemStack result = utils.createItem(type, amount, data, name, str);
		
		result = utils.addFlag(result, ItemFlag.HIDE_ATTRIBUTES);
		result = utils.addFlag(result, ItemFlag.HIDE_ENCHANTS);
		result = utils.addFlag(result, ItemFlag.HIDE_UNBREAKABLE);
		if (glow)
		{
			result = utils.addEnchant(result, Enchantment.LUCK, 1);
		}
		return result;
	}
	
	public Inventory setup(Player p, String searchKey, int page)
	{
		return this.inv;
	}
	
	public void open(Player p, String searchKey, int page)
	{
		p.openInventory(setup(p, searchKey, page));
	}
	
	@Override
	public ItemStack fillIcon() {
		ItemStack fill = createIcon(Material.YELLOW_STAINED_GLASS_PANE, 1, 101, " ", false);
		
		return fill;
	}
	@Override
	public ItemStack closeIcon() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ItemStack balance(Player p) {
		// TODO Auto-generated method stub
		ItemStack result = utils.createItem(Material.CHEST, 1, 101, " ");
		String name = plugin.file_loc.getForge("balance.name").replace("<player>", p.getName());
		List<String> lore = new ArrayList<>();
		List<String> format = plugin.file_loc.getList("forge", "balance", "lore");
		for (String s: format)
		{
			if (s.contains("<money>") && plugin.vaultHooked)
			{
				lore.add(utils.color(s.replace("<money>", plugin.getEconomy().getBalance(p)+"")));
			}
			else if (s.contains("<level>"))
			{
				lore.add(utils.color(s.replace("<level>", utils.getLevel(p)+"")));
			}
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	@Override
	public ItemStack backIcon() {
		ItemStack back = utils.createItem(Material.ARROW, 1, 101);
		
		ItemMeta meta = back.getItemMeta();
		String name = plugin.file_loc.getForge("back-icon.name");
		List<String> lore = plugin.file_loc.getList("forge", "back-icon", "lore");
		meta.setDisplayName(name);
		meta.setLore(lore);
		back.setItemMeta(meta);
		
		return back;
	}
}
