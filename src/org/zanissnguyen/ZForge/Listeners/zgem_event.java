package org.zanissnguyen.ZForge.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.database.zfile_gems;
import org.zanissnguyen.ZForge.System.object.zgem;
import org.zanissnguyen.ZForge.System.stat.Zstat;

public class zgem_event extends zevent {

	zfile_gems file_gem;
	
	public zgem_event(ZForge plugin) {
		super(plugin, plugin.utils);
		
		file_gem = plugin.file_gem;
	}

	List<Player> onCooldown = new ArrayList<>();
	int cooldown = 6;
	int delay = 2;

	@EventHandler
	public void onApply(InventoryClickEvent event) 
	{	
		String failed = plugin.file_loc.getForge("gem.failed");
		
		String unlock_slot = plugin.file_loc.getForge("gem.unlock");
		String gem_put = plugin.file_loc.getForge("gem.match");
		String gem_removed = plugin.file_loc.getForge("gem.remove");
		String gem_smashed = plugin.file_loc.getForge("gem.smash");
		
		Player p = (Player) event.getWhoClicked();
		ItemStack cursor = event.getCursor();
		ItemStack clicked = event.getCurrentItem();
		int slot = event.getSlot();
		List<String> allGem = plugin.file_gem.getGemDisplayList();
		
		if (onCooldown.contains(p)) return;
		if (event.getSlotType()==SlotType.OUTSIDE) return;
		if (clicked == null) return;
		if (event.getAction()!=InventoryAction.SWAP_WITH_CURSOR) return;
		if (cursor.getType()==Material.AIR || cursor==null
				|| clicked.getType()==Material.AIR) return;
		
		ItemMeta Cmeta = null;
		String Cname = "";
		if (cursor.getItemMeta()==null) return;
		else
		{
			Cmeta = cursor.getItemMeta();
			if (Cmeta.getDisplayName()!=null) Cname = Cmeta.getDisplayName();
		}
		
		ItemMeta meta = null;
		List<String> lore = new ArrayList<String>();
		if (clicked.getItemMeta()==null) return;
		else
		{
			meta = clicked.getItemMeta();
			if (meta.getLore()!=null) lore = meta.getLore();
		}
		
		// unlock locked slot
		// cursor = driller
		// clicked = item
		if (Cname.equalsIgnoreCase(utils.getName(zgem.driller(false, false))))
		{	
			if (zgem.isItemHasSlot(clicked, true)==-1)
			{
				String message = plugin.file_loc.getForge("gem.no_locked-slot");
				utils.sendMessage(p, message);
				return;
			}
			onCooldown.add(p);
			
			if (utils.roll(plugin.file_cfg.getSocketDouble("driller.success_chance")))
			{	
				lore.remove(zgem.isItemHasSlot(clicked, true));
				lore.add(zgem.isItemHasSlot(clicked, true), plugin.file_cfg.getEmptySlot());
				meta.setLore(lore);
				clicked.setItemMeta(meta);
				
				delayTask(p, slot, cursor, clicked, true);
				utils.sendMessage(p, unlock_slot);
			}
			else 
			{
				delayTask(p, slot, cursor, clicked, false);
				utils.sendMessage(p, failed);
			}
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					onCooldown.remove(p);
				}
			}, cooldown);
		} else
			
		// apply gem
		// cursor = gem
		// clicked = item to match
		if (allGem.contains(Cname))
		{	
			if (zgem.isItemHasSlot(clicked, false)==-1)
			{
				String message = plugin.file_loc.getForge("gem.no-empty-slot");
				utils.sendMessage(p, message);
				return;
			}
			String id = plugin.file_gem.getGemByName(Cname);
			Zstat stat = plugin.file_gem.getStatByGem(id);
			double item_stat = 0;
			double gem_stat = plugin.a_utils.getStat(cursor, stat);
			
			if (plugin.a_utils.isItemHasStat(clicked, stat)!=-1)
				item_stat = plugin.a_utils.getStat(clicked, stat);
			else
			{
				String message = plugin.file_loc.getForge("gem.un-match");
				utils.sendMessage(p, message);
				return;
			}
			onCooldown.add(p);
			
			String gem_display = zgem.getGemString(cursor, false);
			double chance = zgem.getChance(utils.getLore(cursor));
			
			double final_stat = item_stat + gem_stat;
			
			p.getInventory().clear(slot);
			if (utils.roll(chance/100))
			{	
				lore.remove(zgem.isItemHasSlot(clicked, false));
				lore.add(zgem.isItemHasSlot(clicked, false), gem_display);
				
				lore.remove(plugin.a_utils.isItemHasStat(clicked, stat));
				lore.add(plugin.a_utils.isItemHasStat(clicked, stat), stat.getLore(final_stat+"", false));
				
				meta.setLore(lore);
				clicked.setItemMeta(meta);
				
				delayTask(p, slot, cursor, clicked, true);
				utils.sendMessage(p, gem_put);
			}
			else 
			{
				delayTask(p, slot, cursor, clicked, false);
				utils.sendMessage(p, failed);
			}
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					onCooldown.remove(p);
				}
			}, cooldown);
		} else

		// remove gem
		if (Cname.equalsIgnoreCase(utils.getName(zgem.remover(false, false))))
		{	
			if (zgem.isItemHasFilledSlot(clicked)==-1)
			{
				String message = plugin.file_loc.getForge("gem.no-gem");
				utils.sendMessage(p, message);
				return;
			}
			onCooldown.add(p);
			
			String line = lore.get(zgem.isItemHasFilledSlot(clicked));
			
			Zstat stat = zgem.getStatFromGemString(line);
			double gem_stat = zgem.getStatValueFromGemString(line);
			double item_stat = plugin.a_utils.getStat(clicked, stat);
			double final_stat = item_stat - gem_stat;
			
//			p.getInventory().clear(slot);
			if (utils.roll(plugin.file_cfg.getSocketDouble("remover.success_chance")))
			{	
				lore.remove(zgem.isItemHasFilledSlot(clicked));
				lore.add(zgem.isItemHasFilledSlot(clicked), plugin.file_cfg.getEmptySlot());
				
				lore.remove(plugin.a_utils.isItemHasStat(clicked, stat));
				lore.add(plugin.a_utils.isItemHasStat(clicked, stat), stat.getLore(final_stat+"", false));
				
				meta.setLore(lore);
				clicked.setItemMeta(meta);
				
				delayTask(p, slot, cursor, clicked, true);
				utils.sendMessage(p, gem_removed);
			}
			else 
			{
				delayTask(p, slot, cursor, clicked, false);
				utils.sendMessage(p, failed);
			}
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					onCooldown.remove(p);
				}
			}, cooldown);
		} else
			
		// smash gem into powder
		// clicked = gem
		// cursor = smasher
		if (Cname.equalsIgnoreCase(utils.getName(zgem.smasher(false, false))))
		{	
			if (plugin.file_gem.getGemByName(utils.getName(clicked))=="")
			{
				return;
			}
			onCooldown.add(p);
			
			double origin_chance = zgem.getChance(utils.getLore(clicked));
			
			int ratio = plugin.file_cfg.getSocketInt("powder.ratio");
			double new_chance = utils.fixedDecimal(origin_chance/ratio, plugin.file_cfg.getDigit());

			event.setCancelled(true);
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					p.getInventory().setItem(slot, zgem.powder(new_chance));
					utils.sendMessage(p, gem_smashed);
					utils.playSound(Sound.BLOCK_ANVIL_USE, p, 1f, 1f);
				}
			}, delay);
				
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					onCooldown.remove(p);
				}
			}, cooldown);
		}  else
			
		// use powder
		// clicked = gem
		// cursor = powder
		if (Cname.equalsIgnoreCase(utils.getName(zgem.powder(0))))
		{	
			if (plugin.file_gem.getGemByName(utils.getName(clicked))=="")
			{
				return;
			}
			onCooldown.add(p);
			
			double powder_chance = zgem.getChance(utils.getLore(cursor));
			double gem_chance = zgem.getChance(utils.getLore(clicked));
			
			double new_chance = gem_chance + powder_chance;
			
			ItemStack get = zgem.setChance(clicked, new_chance);

			p.getInventory().clear(slot);
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					p.getInventory().clear(slot);
					p.getInventory().setItem(slot, get);
					utils.playSound(Sound.ENTITY_PLAYER_LEVELUP, p, 1f, 1.5f);
				}
			}, delay);
				
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					onCooldown.remove(p);
				}
			}, cooldown);
		}
	}
	
	// clear before
	public void delayTask(Player p, int slot,ItemStack cursor, ItemStack clicked, boolean complete)
	{
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if (cursor != null && cursor.getType() != Material.AIR) {

					int new_amount = cursor.getAmount() - 1;

					if (new_amount <= 0) {
						p.getInventory().clear(slot);
						p.setItemOnCursor(new ItemStack(Material.AIR));
					} else {
						ItemStack new_cursor = cursor.clone();
						// Avoid setting invalid amount
						new_cursor.setAmount(Math.min(new_amount, new_cursor.getMaxStackSize()));
						p.setItemOnCursor(new_cursor);
					}
				}

				p.getInventory().setItem(slot, clicked);

				if (complete) {
					utils.playSound(Sound.BLOCK_ANVIL_USE, p, 1f, 1f);
				} else {
					utils.playSound(Sound.ENTITY_ITEM_BREAK, p, 1f, 0.7f);
				}
			}
		}, delay);
	}
	
}
