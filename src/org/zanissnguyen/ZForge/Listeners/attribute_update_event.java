package org.zanissnguyen.ZForge.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.buff.buff_manager;
import org.zanissnguyen.ZForge.System.buff.player_buff;
import org.zanissnguyen.ZForge.System.stat.player_stat;
import org.zanissnguyen.ZForge.System.stat.stat_manager;

@SuppressWarnings("deprecation")
public class attribute_update_event extends zevent
{
	public String cdText;
	
	public attribute_update_event(ZForge plugin) {
		super(plugin, plugin.utils);
		cdText = plugin.file_loc.getAttribute("stat.on_cooldown");
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event)
	{
		Player p = event.getPlayer();
		ItemStack droped = event.getItemDrop().getItemStack();
		
		if (droped.hasItemMeta() && p.getInventory().getItemInMainHand().getType()==Material.AIR)
		{						
			if (damage_event.isOnCooldown(p)) 
			{
				if (damage_event.cooldown_msg && !damage_event.onCooldown.get(p)) 
				{
					damage_event.onCooldown.put(p, true);
					p.sendMessage(cdText);
				}
				event.setCancelled(true);
				return;
			}
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				@Override
				public void run() {
					utils_attribute.updateAttribute(p);
				}
			}, 2);
		}
	}
	
	@EventHandler
	public void onPick(PlayerPickupItemEvent event)
	{
		Player p =event.getPlayer();
		ItemStack picked = event.getItem().getItemStack();
		ItemStack mainhand = p.getInventory().getItemInMainHand();
		
		if (mainhand!=null && mainhand.getType()!=Material.AIR) return;
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (picked.isSimilar(p.getInventory().getItemInMainHand()))
				{
					utils_attribute.updateAttribute(p);
				}
			}
		}, 2);
	}
	
	@EventHandler
	public void onItemBreak(PlayerItemBreakEvent event)
	{
		Player p = event.getPlayer();
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				utils_attribute.updateAttribute(p);
			}
		}, 2);
	}
	
	@EventHandler
	public void onSwapHand(PlayerSwapHandItemsEvent event)
	{
		Player p = event.getPlayer();
		if (damage_event.isOnCooldown(p)) 
		{
			if (damage_event.cooldown_msg && !damage_event.onCooldown.get(p)) 
			{
				damage_event.onCooldown.put(p, true);
				p.sendMessage(cdText);
			}
			event.setCancelled(true);
			return;
		}
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				utils_attribute.updateAttribute(p);
			}
		}, 2);
	}
	
	@EventHandler
	public void onChangeSlot(PlayerItemHeldEvent event)
	{
		Player p = event.getPlayer();
		if (damage_event.isOnCooldown(p)) 
		{
			if (damage_event.cooldown_msg && !damage_event.onCooldown.get(p)) 
			{
				damage_event.onCooldown.put(p, true);
				p.sendMessage(cdText);
			}
			event.setCancelled(true);
			return;
		}
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				utils_attribute.updateAttribute(p);
			}
		}, 2);
	}
	
	@EventHandler
	public void onEquipArmor(PlayerInteractEvent event)
	{
		Player p = event.getPlayer();
		if (damage_event.isOnCooldown(p)) 
		{
			if (damage_event.cooldown_msg && !damage_event.onCooldown.get(p)) 
			{
				damage_event.onCooldown.put(p, true);
				p.sendMessage(cdText);
			}
			event.setCancelled(true);
			return;
		}
		
		if (!event.hasItem()) return;
		if (event.getAction()==Action.RIGHT_CLICK_AIR || utils.isVanillaArmor(event.getItem().getType()))
		{
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				@Override
				public void run() {
					utils_attribute.updateAttribute(p);
				}
			}, 2);
		}
	}
	
	@EventHandler
	public void onDragAndClick(InventoryClickEvent event)
	{
		if (event.getSlotType()==SlotType.OUTSIDE) return;
		
		Player p = (Player) event.getWhoClicked();
		
		boolean shift = event.getClick().toString().contains("SHIFT");
		ItemStack clicked = utils.createItem(Material.AIR, 1, 0);
		Inventory inv = event.getClickedInventory();
		if (inv.getItem(event.getSlot())!=null)
		{
			clicked = inv.getItem(event.getSlot());
		}
		
		if (event.getSlot()==p.getInventory().getHeldItemSlot() && event.getSlotType()==SlotType.QUICKBAR)
		{
			if (damage_event.isOnCooldown(p)) 
			{
				if (damage_event.cooldown_msg && !damage_event.onCooldown.get(p)) 
				{
					damage_event.onCooldown.put(p, true);
					p.sendMessage(cdText);
				}
				event.setCancelled(true);
				return;
			}
		}
		
		if ((event.getSlotType()==SlotType.ARMOR && (event.getAction()==InventoryAction.PLACE_ALL || event.getAction()==InventoryAction.PICKUP_ALL))
				|| (event.getSlot()==40 && event.getSlotType()==SlotType.QUICKBAR)
				|| (event.getSlot()==p.getInventory().getHeldItemSlot() && event.getSlotType()==SlotType.QUICKBAR
				|| (event.getSlotType()==SlotType.ARMOR && shift)
				|| (utils.isVanillaArmor(clicked.getType()) && shift))
			)
		{
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				@Override
				public void run() {
					utils_attribute.updateAttribute(p);
				}
			}, 2);
		}
	}
	
	@EventHandler
	public void dispenseArmorEvent(BlockDispenseArmorEvent event){
		ItemStack item = event.getItem();
		
		if(item != null)
		{
			if(event.getTargetEntity() instanceof Player)
			{
				Player p = (Player) event.getTargetEntity();
				
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					
					@Override
					public void run() {
						utils_attribute.updateAttribute(p);
					}
				}, 2);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		stat_manager.player_stats.put(event.getPlayer(), new player_stat());
		buff_manager.player_buffs.put(event.getPlayer(), new player_buff());
		utils_attribute.updateAttribute(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		stat_manager.player_stats.remove(event.getPlayer());
		buff_manager.player_buffs.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		if (!event.getKeepInventory()) stat_manager.updateStat(event.getEntity());
	}
}
