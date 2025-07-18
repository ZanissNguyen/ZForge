package org.zanissnguyen.ZForge.Listeners.UX;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.zevent;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.UI.craft.UI_craft;
import org.zanissnguyen.ZForge.UI.craft.UI_craft_custom;

public class UX_craft extends zevent
{
	private String name_prefix;
	private String prefix;
	
	private List<Player> onQueue = new ArrayList<>();
	
	public UX_craft(ZForge plugin) {
		super(plugin, plugin.utils);

		name_prefix = plugin.file_loc.getForge("recipe.craft.ui-name");
		prefix = plugin.file_loc.getForge("recipe.craft.prefix");
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
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
		
		if (onQueue.contains(p)) 
		{
			event.setCancelled(true);
			return;
		}
		
		if (clicked.getTitle().startsWith(name_prefix) && event.getSlotType()!=SlotType.OUTSIDE)
		{
			String title = clicked.getTitle();
			String recipeID = title.substring(title.lastIndexOf("#")+1, title.lastIndexOf(")"));
			
			zrecipe recipe = plugin.file_rep.getRecipe(recipeID);
			
			ItemStack craft;
			if (plugin.file_sty.isIDExist(recipe.type)) craft = UI_craft_custom._craftIcon_(recipe);
			else craft = UI_craft._craftIcon_(recipe);
			
			ItemStack back;
			if (plugin.file_sty.isIDExist(recipe.type)) back = UI_craft_custom._backIcon_(recipe);
			else back = UI_craft._backIcon_(recipe);
			
			int slot = event.getRawSlot();
			if (clicked.getItem(slot)!=null)
			{
				int time = recipe.time;
				String message = plugin.file_loc.getForge("recipe.craft.notice");
				
				if (clicked.getItem(slot).isSimilar(craft))
				{
					if (checkMoney(p, recipe, true) && checkLevel(p, recipe, true) && checkMaterials(p, recipe, true))
					{
						utils.takeMoney(p, recipe.money);
						utils.takeLevel(p, recipe.lvl);
						
						for (ItemStack item: recipe.materials().keySet())
						{
							utils.takeItem(item, recipe.materials().get(item), p);
						}
						
						utils.sendMessage(p, prefix +" "+ message
								.replace("<item>", utils.getName(recipe.reward.convertToItemStack(true)))
								.replace("<t>", time+""));
						for (int i=1; i<=Math.min(3, time); i++)
						{
							Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
							{
								@Override
								public void run() 
								{
									utils.playSound(Sound.BLOCK_ANVIL_USE, p, 1f, 1f);
								}
							}, 20*i);
						}
						
						Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
						{
							@Override
							public void run() 
							{
								utils.playSound(Sound.ENTITY_PLAYER_LEVELUP, p, 1f, 1.5f);
								p.getInventory().addItem(recipe.reward.convertToItemStack(false));
							}
						}, 20*time);
						
					}
					
					p.closeInventory();
				}
				
				if (clicked.getItem(slot).isSimilar(back))
				{
					p.closeInventory();
					plugin.ui_rep.open(p, "", 1);
				}
			}
						
			event.setCancelled(true);	
			onQueue.add(p);
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() 
			{
				@Override
				public void run() 
				{
					onQueue.remove(p);
				}
			}, 5);
		}
	}
	
	public boolean checkMoney(Player p, zrecipe recipe, boolean msg)
	{
		if (plugin.vaultHooked)
		{
			if (plugin.getEconomy().getBalance(p)<recipe.money) 
			{
				p.sendMessage(prefix+ " "+ plugin.file_loc.getError("not-money"));
				return false;
			}
		}
		return true;
	}
	
	public boolean checkLevel(Player p, zrecipe recipe, boolean msg)
	{
		double level = utils.getLevel(p);
		level = utils.fixedDecimal(level, 2);
		double levelRequire = recipe.lvl;
		
		String message = plugin.file_loc.getError("not-level");
		
		boolean result = level>=levelRequire;
		if (!result && msg) p.sendMessage(prefix +" "+ message);
		
		return result;
	}
	
	public boolean checkMaterials(Player p, zrecipe recipe, boolean msg)
	{
		boolean result = (new UI_craft(plugin, utils, 1, recipe)).enoughMaterial(p);
		String message = plugin.file_loc.getError("not-enough-material");
		if (!result && msg) utils.sendMessage(p, prefix +" "+ message);
		
		return result;
	}
}
