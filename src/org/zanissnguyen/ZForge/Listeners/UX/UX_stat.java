package org.zanissnguyen.ZForge.Listeners.UX;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.zevent;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.buff.buff_manager;
import org.zanissnguyen.ZForge.System.stat.stat_manager;
import org.zanissnguyen.ZForge.UI.UI_stat;
import org.zanissnguyen.ZForge.Utils.utils;

public class UX_stat extends zevent{

	public utils_attribute a_utils;
	public UI_stat ui;
	public UX_stat(UI_stat ui, ZForge plugin, utils utils) {
		super(plugin, utils);
		this.ui = ui;
		this.a_utils = plugin.a_utils;
	}

	@EventHandler
	public void onEsc(InventoryCloseEvent event)
	{
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event)
	{
		InventoryView clicked = event.getView();
		Player p = (Player) event.getWhoClicked();
		String format = plugin.file_loc.getForge("stat.ui-name").replace("<player>", p.getName());
		
		if (clicked.getTitle().equalsIgnoreCase(format) && !(event.getSlotType()==SlotType.OUTSIDE))
		{
			ItemStack cursor = event.getCursor();
			ItemStack click = event.getCurrentItem();
			int slot = event.getRawSlot();
			
			if (slot>=ui.rows*9 && slot == 81+p.getInventory().getHeldItemSlot())
			{
				event.setCancelled(true);
				return;
			}
			
			if (slot<ui.rows*9 && 
					(click==null || click.isSimilar(ui.fillIcon()) ||
					slot==7 || slot == 16 || slot == 25 || slot == 34 || slot == 43
					|| slot == 52 || slot ==49)
				)
			{
				event.setCancelled(true);
				return;
			}
			
			// hold something
			if (slot>=ui.rows*9) return;
			
			if (cursor!=null && cursor.getType()!=Material.AIR)
			{
				// is Valid Cursor for slot
				if (isValidCursor(slot, cursor))
				{
					// check if slot have item
					if (isEmptySlot(click))
					{
//						p.sendMessage("filled valid cursor and empty slot");
						swapItem(p, slot, clicked, cursor, utils.createItem(Material.AIR, 1));
					}
					else
					{
//						p.sendMessage("filled valid cursor and filled slot");
						swapItem(p, slot, clicked, cursor, click);
					}
				}
			}
			else
			{
				if (!isEmptySlot(click))
				{
//					p.sendMessage("empty cursor and slot filled");
					tookItem(p, slot, clicked);
				}
			}
				
			event.setCancelled(true);
			return;
		}
	}
	
	private void tookItem(Player p, int takenSlot, InventoryView inv)
	{
		ItemStack empty = utils.createItem(Material.AIR, 1);
				
		switch (takenSlot) 
		{
		case 4:
//			p.getInventory().setHelmet(utils.createItem(Material.AIR, 1));
			stat_manager.updateHelmetSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.helmetSlot(p);
			break;
					
		case 13:
//			p.getInventory().setChestplate(utils.createItem(Material.AIR, 1));
			stat_manager.updateChestplateSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.chestplateSlot(p);
			break;
					
		case 22:
//			p.getInventory().setLeggings(utils.createItem(Material.AIR, 1));
			stat_manager.updateLeggingsSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.leggingsSlot(p);
			break;
					
		case 23:
//			p.getInventory().setItemInOffHand(utils.createItem(Material.AIR, 1));
			stat_manager.updateOffhandSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.offHandSlot(p);
			break;
				
		case 31:
//			p.getInventory().setBoots(utils.createItem(Material.AIR, 1));
			stat_manager.updateBootsSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.bootsSlot(p);
			break;
		
		case 21:
			stat_manager.updateMainhandSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.mainHandSlot(p);
			break;
	
		case 46: 
//			plugin.storage.setBeltSlot(p, utils.createItem(Material.AIR, 1));
			stat_manager.updateBeltSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.beltSlot(p);
			break;
			
		case 12: 
//			plugin.storage.setGauntletSlot(p, utils.createItem(Material.AIR, 1));
			stat_manager.updateGauntletSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.gauntletSlot(p);
			break;
			
		case 14: 
//			plugin.storage.setNecklaceSlot(p, utils.createItem(Material.AIR, 1));
			stat_manager.updateNecklaceSlot(utils.createItem(Material.AIR, 1), p, false);
			empty = ui.necklaceSlot(p);
			break;
			
		case 1: 
		case 10:
		case 19:
		case 28:
		case 37:
			stat_manager.updateRingSlot(utils.createItem(Material.AIR, 1), p, (takenSlot/9)+1, false);
//			plugin.storage.setRingSlot(p, (takenSlot/9)+1, utils.createItem(Material.AIR, 1));
			empty = ui.ringSlot(p, (takenSlot/9)+1);
			break;
			
		case 3:
		case 5:
			stat_manager.updateArtifactSlot(utils.createItem(Material.AIR, 1), p, (takenSlot==3)?1:2, false);
//			plugin.storage.setArtifactSlot(p, (takenSlot==3) ? 1 : 2, utils.createItem(Material.AIR, 1));
			empty = ui.artifactSlot(p, (takenSlot==3)?1:2 );
			break;
			
		default:
			break;
		}
		buff_manager.updateBuffs(p);
		p.setItemOnCursor(inv.getItem(takenSlot));
		inv.setItem(takenSlot, empty);
		
//		stat_manager.updateStat(p);
		updateStat(inv, p);
	}
	
	private void swapItem(Player p, int swapSlot, InventoryView inv, ItemStack cursor, ItemStack clicked)
	{
		switch (swapSlot) 
		{
		case 4:
			stat_manager.updateHelmetSlot(cursor, p, false);
			break;
					
		case 13:
			stat_manager.updateChestplateSlot(cursor, p, false);
			break;
					
		case 22:
			stat_manager.updateLeggingsSlot(cursor, p, false);
			break;
					
		case 23:
			stat_manager.updateOffhandSlot(cursor, p, false);
			break;
				
		case 31:
			stat_manager.updateBootsSlot(cursor, p, false);
			break;
			
		case 21:
			stat_manager.updateMainhandSlot(cursor, p, false);
			break;
			
		case 46: 
			stat_manager.updateBeltSlot(cursor, p, false);
//			plugin.storage.setBeltSlot(p, cursor);
			break;
			
		case 12: 
			stat_manager.updateGauntletSlot(cursor, p, false);
//			plugin.storage.setGauntletSlot(p, cursor);
			break;
			
		case 14: 
			stat_manager.updateNecklaceSlot(cursor, p, false);
//			plugin.storage.setNecklaceSlot(p, cursor);
			break;
			
		case 1: 
		case 10:
		case 19:
		case 28:
		case 37:
			stat_manager.updateRingSlot(cursor, p, (swapSlot/9)+1, false);
//			plugin.storage.setRingSlot(p, (swapSlot/9)+1, cursor);
			break;
			
		case 3:
		case 5:
			stat_manager.updateArtifactSlot(cursor, p, (swapSlot==3)?1:2, false);
//			plugin.storage.setArtifactSlot(p, (swapSlot==3) ? 1 : 2, cursor);
			break;
			
		default:
			break;
		}
		buff_manager.updateBuffs(p);
		inv.setItem(swapSlot, cursor);
		p.setItemOnCursor(clicked);
		
//		stat_manager.updateStat(p);
		updateStat(inv, p);
	}
	
	private boolean isEmptySlot(ItemStack item)
	{
		if (item==null) return false;
		if (item.getType()==Material.AIR) return false;
		if (item.getItemMeta()==null) return false;
		if (item.getItemMeta().getDisplayName()==null) return false;
		
		String name = plugin.file_loc.getForge("stat.empty-slot.name");
		return (item.getItemMeta().getDisplayName().equalsIgnoreCase(name));
	}
	
	private void updateStat(InventoryView inv, Player p)
	{
		inv.setItem(7, ui.physic(p));
		inv.setItem(16, ui.magic(p));
		inv.setItem(25, ui.defense(p));
		inv.setItem(34, ui.misc(p));
		inv.setItem(43, ui.buff(p));
		inv.setItem(49, ui.balance(p));
		inv.setItem(52, ui.skill(p));
	}
	
	private boolean isValidCursor(int rawSlot, ItemStack toCheck)
	{
		switch (rawSlot) 
		{
		case 4:
			return toCheck.getType().toString().contains("HELMET") || a_utils.getType(toCheck).equalsIgnoreCase("helmet");
		case 13:
			return toCheck.getType().toString().contains("CHESTPLATE") || a_utils.getType(toCheck).equalsIgnoreCase("chestplate");
		case 22:
			return toCheck.getType().toString().contains("CHESTPLATE") || a_utils.getType(toCheck).equalsIgnoreCase("leggings");
		case 21:
		case 23:
			return true;
		case 31:
			return toCheck.getType().toString().contains("BOOTS") || a_utils.getType(toCheck).equalsIgnoreCase("boots");
		case 46: 
			return a_utils.getType(toCheck).equalsIgnoreCase("belt");
		case 12: 
			return a_utils.getType(toCheck).equalsIgnoreCase("gauntlet");
		case 14: 
			return a_utils.getType(toCheck).equalsIgnoreCase("necklace");
		case 1: 
		case 10:
		case 19:
		case 28:
		case 37:
			return a_utils.getType(toCheck).equalsIgnoreCase("ring");
		case 3:
		case 5:
			return a_utils.getType(toCheck).equalsIgnoreCase("artifact");
		
		default:
			break;
		}
		
		return false;
	}
}
