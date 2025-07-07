package org.zanissnguyen.ZForge.Listeners.UX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.zevent;
import org.zanissnguyen.ZForge.UI.UI_repair;
import org.zanissnguyen.ZForge.Utils.utils;

public class UX_repair extends zevent
{
	private UI_repair ui;

	public UX_repair(UI_repair ui, ZForge plugin, utils utils) {
		super(plugin, utils);
		this.ui = ui;
	}
	
	@EventHandler
	public void onEsc(InventoryCloseEvent event)
	{
		InventoryView inv = event.getView();
		Player p = (Player) event.getPlayer();
		
		if (inv.getTitle().equalsIgnoreCase(ui.name))
		{
			if (!isEmptySlot(inv.getItem(ui.rs_slot))) p.getInventory().addItem(inv.getItem(ui.rs_slot));
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{	
		InventoryView inv = event.getView();
		Player p = (Player) event.getWhoClicked();
		
		if (inv.getTitle().equalsIgnoreCase(ui.name) && !(event.getSlotType()==SlotType.OUTSIDE))
		{	
			ItemStack cursor = event.getCursor();
			ItemStack click = event.getCurrentItem();
			int slot = event.getRawSlot();
		
			// hold something
			if (slot>=ui.rows*9) return;
			
			if (slot == ui.rs_slot)
			{
				if (cursor!=null && cursor.getType()!=Material.AIR)
				{
					// check if slot have item
					if (isEmptySlot(click))
					{
						swapItem(p, slot, inv, cursor, utils.createItem(Material.AIR, 1, 0));
					}
					else
					{
						swapItem(p, slot, inv, cursor, click);
					}
				}
				else
				{
					if (!isEmptySlot(click))
					{
						tookItem(p, slot, inv);
					}
				}
			}
			else if (!isEmptySlot(inv.getItem(ui.rs_slot)) && (slot == 15 || slot == 16))
			{
				int current = ui.a_utils.getCurrentDurability(inv.getItem(ui.rs_slot));
				int max = ui.a_utils.getMaxDurability(inv.getItem(ui.rs_slot));
				
				int needRepair = max - current;
				int canRepair = ui.rp.canRepairAmount(needRepair, p);
				
				if (needRepair >0)
				{
					if (slot == 15 && canRepair > 0)
					{
						if (canRepairItem(p, canRepair)) repair(p, canRepair, inv);
						utils.sendMessage(p, ui.prefix + " " + plugin.file_loc.getForge("repair.message.repair"));
					}
					else if (slot == 16)
					{
						if (canRepairItem(p,needRepair)) repair(p, needRepair, inv);
						utils.sendMessage(p, ui.prefix + " " + plugin.file_loc.getForge("repair.message.repair-all"));
					}
				}
			}
			
			event.setCancelled(true);
			return;
		}
	}
	
	public void repair(Player p, int amount, InventoryView inv)
	{
		takeRequire(p, amount);
		utils.playSound(Sound.BLOCK_ANVIL_USE, p, 1f, 1f);
		ItemStack item = inv.getItem(ui.rs_slot);
		inv.setItem(ui.rs_slot, ui.a_utils.setCurrentDurability(item,
				ui.a_utils.getCurrentDurability(item)+amount));
		inv.close();
	}
	
	public void takeRequire(Player p, int amount)
	{
		utils.takeMoney(p, amount*ui.rp.money);
		utils.takeLevel(p, ui.rp.level);
		for (ItemStack item : ui.rp.materials().keySet())
		{
			utils.takeItem(item, amount*ui.rp.materials().get(item), p);
		}
	}
	
	public boolean canRepairItem(Player p, int amount)
	{
		if (plugin.vaultHooked)
		{
			if (plugin.getEconomy().getBalance(p)<amount*ui.rp.money) 
			{
				utils.sendMessage(p, ui.prefix + " " + plugin.file_loc.getError("not-money"));
				return false;
			}
		}
		
		if (utils.getLevel(p)<amount*ui.rp.level)
		{
			utils.sendMessage(p, ui.prefix + " " + plugin.file_loc.getError("not-level"));
			return false;
		}
		
		for (ItemStack item : ui.rp.materials().keySet())
		{
			if (utils.getAmount(item, p)<ui.rp.materials().get(item)*amount)
			{
				p.sendMessage(ui.prefix + " " + plugin.file_loc.getError("not-enough-material"));
				return false;
			}
		}
		
		return true;
	}
	
	public void tookItem(Player p, int slot, InventoryView inv)
	{
		inv.setItem(3, ui.process(p, 0));
		inv.setItem(4, ui.process(p, 0));
		inv.setItem(5, ui.process(p, 0));
		
		inv.setItem(12, ui.process(p, 0));
		inv.setItem(14, ui.process(p, 0));
		inv.setItem(15, ui.repair_unslot(false));
		inv.setItem(16, ui.repair_unslot(false));
		
		inv.setItem(21, ui.process(p, 0));
		inv.setItem(22, ui.process(p, 0));
		inv.setItem(23, ui.process(p, 0));
		
		p.setItemOnCursor(inv.getItem(slot));
		inv.setItem(slot, ui.slot());
	}
	
	public void swapItem(Player p, int slot, InventoryView inv, ItemStack cursor, ItemStack clicked)
	{
		int current = ui.a_utils.getCurrentDurability(cursor);
		int max = ui.a_utils.getMaxDurability(cursor);
		int needRepair = max - current;
		
		int canRepair = ui.rp.canRepairAmount(needRepair, p);
	
		if (needRepair>0)
		{
			inv.setItem(15, repair(p, canRepair));
			inv.setItem(16, repairAll(p, needRepair));
		} else
		{
			inv.setItem(15, ui.repair_unslot(true));
			inv.setItem(16, ui.repair_unslot(true));
		}
		
		int i = 0;
		if (canRepair == 0)
		{
			i = 1;
		}
		else if (canRepair == needRepair) 
		{
			i = 3;
		} else
		{
			i = 2;
		}
		
		inv.setItem(3, ui.process(p, i));
		inv.setItem(4, ui.process(p, i));
		inv.setItem(5, ui.process(p, i));
		inv.setItem(12, ui.process(p, i));
		inv.setItem(14, ui.process(p, i));
		inv.setItem(21, ui.process(p, i));
		inv.setItem(22, ui.process(p, i));
		inv.setItem(23, ui.process(p, i));
		
		inv.setItem(slot, cursor);
		p.setItemOnCursor(clicked);
	}
	
	private ItemStack repair(Player p, int canRepair)
	{	
		canRepair = Math.max(canRepair, 1);
		ItemStack result = utils.createItem(Material.ANVIL, 1, 101, " ");
		String name = plugin.file_loc.getForge("repair.icon.repair.name");
		List<String> format = plugin.file_loc.getList("forge", "repair", "icon", "repair", "lore");
		format = unformatted(p, format, canRepair, -1, false);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(format);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	private ItemStack repairAll(Player p, int needRepair)
	{	
		ItemStack result = utils.createItem(Material.ANVIL, 1, 102, " ");
		String name = plugin.file_loc.getForge("repair.icon.repair-all.name");
		List<String> format = plugin.file_loc.getList("forge", "repair", "icon", "repair-all", "lore");
		format = unformatted(p, format, -1, needRepair, true);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(format);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		result.setItemMeta(meta);
		
		return result;
	}
	
	private List<String> unformatted(Player p, List<String> format, int canRepair, int needRepair, boolean isAll)
	{
		List<String> result = new ArrayList<>();
		int display = isAll ? needRepair : Math.max(1, canRepair);
		
		for (String s: format)
		{
			if (s.contains("<amount>"))
			{
				result.add(utils.color(s.replace("<amount>", canRepair+"")));
			} else if (s.contains("<requirement>"))
			{
				result.addAll(requirement(p, display));
			}
		}
		
		return result;
	}
	
	private List<String> requirement(Player p, int display)
	{
		List<String> result = new ArrayList<>();
		
		
		for (String s: plugin.file_form.repairFormat())
		{
			if (s.contains("<level>") && ui.rp.level!=0)
			{
				double have = utils.getLevel(p);
				boolean enough = (have>=display*ui.rp.level);
				String color = enough ? "&a" : "&c";
				String symbol = enough ? plugin.file_loc.getSymbol("tick") 
						: plugin.file_loc.getSymbol("cross");
				result.add(utils.color(s.replace("<level>", color+display*ui.rp.level)+" "+ color+symbol));
			} else if (s.contains("<money>") && plugin.vaultHooked && ui.rp.money!=0)
			{
				double have = plugin.getEconomy().getBalance(p);
				boolean enough = (have>=display*ui.rp.money);
				String color = enough ? "&a" : "&c";
				String symbol = enough ? plugin.file_loc.getSymbol("tick") 
						: plugin.file_loc.getSymbol("cross");
				result.add(utils.color(s.replace("<money>", color+display*ui.rp.money)+" "+ color+symbol));
			} else if (s.contains("<material>"))
			{
				result.addAll(displayMaterial(p, display));
			}
		}
		
		return result;
	}
	
	private List<String> displayMaterial(Player p, int display)
	{
		List<String> result = new ArrayList<>();
		Map<ItemStack, Integer> materials = ui.rp.materials();
		
		for (ItemStack item : materials.keySet())
		{
			int have = utils.getAmount(item, p);
			if (materials.get(item)!=0) result.add(displayMaterial(item, materials, display, (have>=display*materials.get(item))));
		}
		
		return result;
	}
	
	private String displayMaterial(ItemStack mat, Map<ItemStack, Integer> materials, int needRepair, boolean enough)
	{
		String color = "", symbol = "";
		
		if (enough)
		{
			color = "&a";
			symbol = plugin.file_loc.getSymbol("tick");
		}
		else
		{
			color = "&c";
			symbol = plugin.file_loc.getSymbol("cross");
		}
		
		String material = plugin.file_form.recipeMaterial();
		String display = "";
		if (mat.getItemMeta()==null || !mat.getItemMeta().hasDisplayName())
		{
			display = utils.standardString(mat.getType().toString());
		}
		else
		{
			display = mat.getItemMeta().getDisplayName();
		}
		return (utils.color(material.replace("<amount>", color+materials.get(mat)*needRepair).replace("<display>", display)+ " " + color+symbol));
	}
	
	private boolean isEmptySlot(ItemStack item)
	{
		if (item==null) return false;
		if (item.getType()==Material.AIR) return false;
		if (item.getItemMeta()==null) return false;
		if (item.getItemMeta().getDisplayName()==null) return false;
		
		return (item.isSimilar(ui.slot()));
	}
}
