package org.zanissnguyen.ZForge.UI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.UX.UX_stat;
import org.zanissnguyen.ZForge.System.buff.Zbuff;
import org.zanissnguyen.ZForge.System.buff.buff_manager;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.System.stat.stat_manager;

public class UI_stat extends UI
{
	public UI_stat(ZForge plugin) {
		super(plugin, plugin.utils, plugin.file_loc.getForge("stat.ui-name"), 6);
		
		name = plugin.file_loc.getForge("stat.ui-name");
		new UX_stat(this, plugin, plugin.utils);
	}
	
	public ItemStack physic(Player p)
	{
		ItemStack result = utils.createItem(Material.IRON_SWORD, 1, 101, "");
		String name = plugin.file_loc.getForge("stat.physic-name");
		List<String> format = plugin.file_form.inventoryFormat("physic");
		format = unformattedStat(p, format);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(format);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack magic(Player p)
	{
		ItemStack result = utils.createItem(Material.BLAZE_POWDER, 1, 101, "");
		String name = plugin.file_loc.getForge("stat.magic-name");
		List<String> format = plugin.file_form.inventoryFormat("magic");
		format = unformattedStat(p, format);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(format);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack defense(Player p)
	{
		ItemStack result = utils.createItem(Material.IRON_CHESTPLATE, 1, 101, "");
		String name = plugin.file_loc.getForge("stat.defense-name");
		List<String> format = plugin.file_form.inventoryFormat("defense");
		format = unformattedStat(p, format);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(format);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack misc(Player p)
	{
		ItemStack result = utils.createItem(Material.ENDER_PEARL, 1, 101, "");
		String name = plugin.file_loc.getForge("stat.misc-name");
		List<String> format = plugin.file_form.inventoryFormat("misc");
		format = unformattedStat(p, format);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(format);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack buff(Player p)
	{
		ItemStack result = utils.createItem(Material.GLASS_BOTTLE, 1, 101, "?");
		String name = plugin.file_loc.getForge("stat.buff-name");
		List<String> format = plugin.file_form.inventoryFormat("buff");
		format = unformattedBuff(p, format);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(format);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack skill(Player p)
	{
		ItemStack result = utils.createItem(Material.BOOK, 1, 101, "?");
//		String name = plugin.file_loc.get("locale", "forge", "stat", "misc-name");
//		List<String> format = plugin.file_form.getUIMiscFormat();
//		format = unformattedStat(p, format);
//		
//		ItemMeta meta = result.getItemMeta();
//		meta.setDisplayName(name);
//		meta.setLore(format);
//		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack helmetSlot(Player p)
	{
		if (p.getEquipment().getHelmet()!=null)
		{
			return p.getEquipment().getHelmet();
		}
		
		ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 101, " ");
		String name = plugin.file_loc.getForge("stat.empty-slot.name");
		List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
		List<String> lore = new ArrayList<>();
		
		for (String s: format)
		{
			lore.add(s.replace("<type>", getSlotType("helmet")));
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack chestplateSlot(Player p)
	{
		if (p.getEquipment().getChestplate()!=null)
		{
			return p.getEquipment().getChestplate();
		}
		
		ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 102, " ");
		String name = plugin.file_loc.getForge("stat.empty-slot.name");
		List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
		List<String> lore = new ArrayList<>();
		
		for (String s: format)
		{
			lore.add(s.replace("<type>", getSlotType("chestplate")));
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack leggingsSlot(Player p)
	{
		if (p.getEquipment().getLeggings()!=null)
		{
			return p.getEquipment().getLeggings();
		}
		
		ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 103, " ");
		String name = plugin.file_loc.getForge("stat.empty-slot.name");
		List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
		List<String> lore = new ArrayList<>();
		
		for (String s: format)
		{
			lore.add(s.replace("<type>", getSlotType("leggings")));
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack bootsSlot(Player p)
	{
		if (p.getEquipment().getBoots()!=null)
		{
			return p.getEquipment().getBoots();
		}
		
		ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 104, " ");
		String name = plugin.file_loc.getForge("stat.empty-slot.name");
		List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
		List<String> lore = new ArrayList<>();
		
		for (String s: format)
		{
			lore.add(s.replace("<type>", getSlotType("boots")));
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack ringSlot(Player p, int slot)
	{
		ItemStack ring = plugin.file_sto.getRingSlot(p, slot);
		if (ring==null || ring.getType()==Material.AIR)
		{
			ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 105, " ");
			String name = plugin.file_loc.getForge("stat.empty-slot.name");
			List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
			List<String> lore = new ArrayList<>();
			
			for (String s: format)
			{
				lore.add(s.replace("<type>", getSlotType("ring")));
			}
			
			ItemMeta meta = result.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			result.setItemMeta(meta);
			
			return result;
		}
		else return ring;
		
	}
	
	public ItemStack beltSlot(Player p)
	{
		ItemStack belt = plugin.file_sto.getBeltSlot(p);
		if (belt==null || belt.getType()==Material.AIR)
		{
			ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 106, " ");
			String name = plugin.file_loc.getForge("stat.empty-slot.name");
			List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
			List<String> lore = new ArrayList<>();
			
			for (String s: format)
			{
				lore.add(s.replace("<type>", getSlotType("belt")));
			}
			
			ItemMeta meta = result.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			result.setItemMeta(meta);
			
			return result;
		}
		else return belt;
	}
	
	public ItemStack mainHandSlot(Player p)
	{
		if (p.getEquipment().getItemInMainHand()!=null && p.getEquipment().getItemInMainHand().getType()!=Material.AIR)
		{
			return p.getEquipment().getItemInMainHand();
		}
		
		ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 107, " ");
		String name = plugin.file_loc.getForge("stat.empty-slot.name");
		List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
		List<String> lore = new ArrayList<>();
		
		for (String s: format)
		{
			lore.add(s.replace("<type>", getSlotType("mainhand")));
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack gauntletSlot(Player p)
	{
		ItemStack gauntlet = plugin.file_sto.getGauntletSlot(p);
		if (gauntlet==null || gauntlet.getType()==Material.AIR)
		{
			ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 108, " ");
			String name = plugin.file_loc.getForge("stat.empty-slot.name");
			List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
			List<String> lore = new ArrayList<>();
			
			for (String s: format)
			{
				lore.add(s.replace("<type>", getSlotType("gauntlet")));
			}
			
			ItemMeta meta = result.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			result.setItemMeta(meta);
			
			return result;
		}
		else return gauntlet;
	}
	
	public ItemStack artifactSlot(Player p, int slot)
	{
		ItemStack artifact = plugin.file_sto.getArtifactSlot(p, slot);
		if (artifact==null || artifact.getType()==Material.AIR)
		{
			ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 109, " ");
			String name = plugin.file_loc.getForge("stat.empty-slot.name");
			List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
			List<String> lore = new ArrayList<>();
			
			for (String s: format)
			{
				lore.add(s.replace("<type>", getSlotType("artifact")));
			}
			
			ItemMeta meta = result.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			result.setItemMeta(meta);
			
			return result;
		}
		else return artifact;
	}
	
	public ItemStack necklaceSlot(Player p)
	{
		ItemStack necklace = plugin.file_sto.getNecklaceSlot(p);
		if (necklace==null || necklace.getType()==Material.AIR)
		{
			ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 110, " ");
			String name = plugin.file_loc.getForge("stat.empty-slot.name");
			List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
			List<String> lore = new ArrayList<>();
			
			for (String s: format)
			{
				lore.add(s.replace("<type>", getSlotType("necklace")));
			}
			
			ItemMeta meta = result.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			result.setItemMeta(meta);
			
			return result;
		}
		else return necklace;
	}
	
	public ItemStack offHandSlot(Player p)
	{
		if (p.getEquipment().getItemInOffHand()!=null && p.getEquipment().getItemInOffHand().getType()!=Material.AIR)
		{
//			p.sendMessage(p.getEquipment().getItemInOffHand().toString());
			return p.getEquipment().getItemInOffHand();
		}
		
		ItemStack result = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 111, " ");
		String name = plugin.file_loc.getForge("stat.empty-slot.name");
		List<String> format = plugin.file_loc.getList("forge", "stat", "empty-slot", "lore");
		List<String> lore = new ArrayList<>();
		
		for (String s: format)
		{
			lore.add(s.replace("<type>", getSlotType("offhand")));
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	private String getSlotType(String type)
	{
		
		return utils.color(plugin.file_form.typeFormat().replace("<type>", plugin.file_attr.getType(type)));
	}
	
	private List<String> unformattedStat(Player p, List<String> gotFormat)
	{
		List<String> result = new ArrayList<>();
		for (String s: gotFormat)
		{
			if (s.contains("<") && s.contains(">"))
			{
				String key = s.substring(s.indexOf("<")+1, (s.indexOf(">")-s.indexOf("<")));
//				p.sendMessage(key);
				if (Zstat.isStat(key))
				{
					Zstat stat = Zstat.getFromID(key);
					int scale = stat.isPercent() ? 100 : 1;
					result.add(stat.getLore(stat_manager.player_stats.get(p).getStat(stat)*scale+"", false));
				}
				else
				{
					result.add(utils.color("&c+ "+key +" is not a stat. Change in format.yml"));
				}
			} else result.add(utils.color(s));
		}
		
		return result;
	}
	
	private List<String> unformattedBuff(Player p, List<String> gotFormat)
	{
		List<String> result = new ArrayList<>();
		for (String s: gotFormat)
		{
			if (s.contains("<") && s.contains(">"))
			{
				String key = s.substring(s.indexOf("<")+1, (s.indexOf(">")-s.indexOf("<")));
//				p.sendMessage(key);
				if (Zbuff.isBuff(key))
				{
					Zbuff buff = Zbuff.getFromID(key);
					result.add(buff.getLore(buff_manager.player_buffs.get(p).getBuff(buff)));
				}
				else
				{
					result.add(utils.color("&c+ "+key +" is not a buff. Change in format.yml"));
				}
			} else result.add(utils.color(s));
		}
		
		return result;
	}
	
	public Inventory setUp(Player p)
	{
		String new_name = name.replace("<player>", p.getName());
		Inventory result = Bukkit.createInventory(null, this.rows*9, new_name);
		
		//basic frame
		for (int i = 0; i<rows*9; i++)
		{
			result.setItem(i, fillIcon());
		}
		
		result.setItem(1, ringSlot(p, 1));
		result.setItem(3, artifactSlot(p, 1));
		result.setItem(4, helmetSlot(p));
		result.setItem(5, artifactSlot(p, 2));
		result.setItem(7, physic(p));
		
		result.setItem(10, ringSlot(p, 2));
		result.setItem(12, gauntletSlot(p));
		result.setItem(13, chestplateSlot(p));
		result.setItem(14, necklaceSlot(p));
		result.setItem(16, magic(p));
		
		result.setItem(19, ringSlot(p, 3));
		result.setItem(21, mainHandSlot(p));
		result.setItem(22, leggingsSlot(p));
		result.setItem(23, offHandSlot(p));
		result.setItem(25, defense(p));
		
		result.setItem(28, ringSlot(p, 4));
		result.setItem(31, bootsSlot(p));
		result.setItem(34, misc(p));
		
		result.setItem(37, ringSlot(p, 5));
		result.setItem(43, buff(p));
		
		result.setItem(46, beltSlot(p));
		result.setItem(49, balance(p));
		result.setItem(52, skill(p));
		
		return result;
	}

	public void open(Player p) 
	{
		p.openInventory(this.setUp(p));
	}

}
