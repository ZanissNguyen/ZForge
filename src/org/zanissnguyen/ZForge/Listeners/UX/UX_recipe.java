package org.zanissnguyen.ZForge.Listeners.UX;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Listeners.zevent;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.UI.craft.UI_craft_corner;
import org.zanissnguyen.ZForge.UI.craft.UI_craft_custom;
import org.zanissnguyen.ZForge.UI.craft.UI_craft_frame;
import org.zanissnguyen.ZForge.UI.craft.UI_craft_horizontal;
import org.zanissnguyen.ZForge.UI.craft.UI_craft_vertical;
import org.zanissnguyen.ZForge.UI.list.UI_recipe;
import org.zanissnguyen.ZForge.Utils.utils;

@SuppressWarnings("deprecation")
public class UX_recipe extends zevent
{
	public List<Player> onSearch;
	public UI_recipe ui;
	
	public UX_recipe(ZForge plugin, utils utils, UI_recipe ui) {
		super(plugin, utils);
		this.ui = ui;
		onSearch = new ArrayList<>();
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
		
		if (clicked.getTitle().startsWith(ui.name) && event.getRawSlot()<ui.rows*9 )
		{
			String title = clicked.getTitle();
			String pageStr = title.substring(title.lastIndexOf("[")+1, title.lastIndexOf("/"));
//			String maxPageStr = title.substring(title.lastIndexOf("/")+1, title.lastIndexOf("]"));
			
			int page = Integer.parseInt(pageStr);
//			int maxPage = Integer.parseInt(maxPageStr);
			
			String s = title.substring(0, title.lastIndexOf("|"));			
			String keyword = s.substring(s.lastIndexOf("[")+1, s.lastIndexOf("]"));
			
			int slot = event.getRawSlot();
			if (clicked.getItem(slot)==null) return;
			if (slot==45 && clicked.getItem(slot).isSimilar(ui.previousIcon()))
			{
				p.closeInventory();
				ui.open(p, keyword, page-1);
			}
			
			if (slot==48 && clicked.getItem(slot).isSimilar(ui.refreshIcon()))
			{
				p.closeInventory();
				ui.open(p, "", 1);
			}
			
			if (slot==49 && clicked.getItem(slot).isSimilar(ui.addIcon()))
			{
				//coming soon
			}
			
			if (slot==50 && clicked.getItem(slot).isSimilar(ui.findIcon(keyword, plugin.file_rep.searchFor(keyword).size())))
			{
				p.closeInventory();
				onSearch.add(p);
				utils.sendMessage(p, plugin.file_loc.getForge("search"));
			}
			
			if (slot==53 && clicked.getItem(slot).isSimilar(ui.nextIcon()))
			{
				p.closeInventory();
				ui.open(p, keyword, page+1);
			}
			
			if (slot>=0 && slot<=35)
			{
				ItemStack toCheck = clicked.getItem(slot);
				ItemMeta meta = toCheck.getItemMeta();
				String name = meta.getDisplayName();
				if (name.contains("#"))
				{
					String id = name.substring(name.lastIndexOf("#")+1, name.lastIndexOf(")"));
					zrecipe recipe = plugin.file_rep.getRecipe(id);
					
					boolean canOpen = true;
					if (recipe.require_bp && !utils.hasPermission(p, recipe.bp))
					{
						if (recipe.buy_bp)
						{
							if (checkCost(p, recipe, true))
							{
								utils.takeMoney(p, recipe.bp_cost);
								utils.addPermission(p, recipe.bp);
								String prefix = plugin.file_loc.getForge("recipe.prefix");
								utils.sendMessage(p, prefix+" "+ plugin.file_loc.getForge("recipe.bought")
									.replace("<item>", utils.getName(recipe.reward.convertToItemStack(true))));
								event.setCancelled(true);
								clicked.close();
								ui.open(p, keyword, page);
								return;
							}
						}
						else
						{
							canOpen = false;
						}
					}
					
					
					if (!canOpen)
					{
						event.setCancelled(true);	
						return;
					}
					
					if (recipe.type.equalsIgnoreCase("vertical"))
					{
						(new UI_craft_vertical(plugin, utils, recipe)).open(p);
					} else if (recipe.type.equalsIgnoreCase("horizontal"))
					{
						(new UI_craft_horizontal(plugin, utils, recipe)).open(p);
					} else if (recipe.type.equalsIgnoreCase("corner"))
					{
						(new UI_craft_corner(plugin, utils, recipe)).open(p);
					} else if (recipe.type.equalsIgnoreCase("frame"))
					{
						(new UI_craft_frame(plugin, utils, recipe)).open(p);
					} else
					{
						(new UI_craft_custom(plugin, utils, recipe)).open(p);
					}
				}
			}
			event.setCancelled(true);	
		}
	}
	
	public boolean checkCost(Player p, zrecipe recipe, boolean msg)
	{
		if (plugin.vaultHooked)
		{
			if (plugin.getEconomy().getBalance(p)<recipe.bp_cost) 
			{
				String prefix = plugin.file_loc.getForge("recipe.prefix");
				utils.sendMessage(p, prefix+ " "+ plugin.file_loc.getError("not-money"));
				return false;
			}
		}
		return true;
	}
	
	@EventHandler
	public void onTyping(PlayerChatEvent event)
	{
		Player p = event.getPlayer();
		String mess = event.getMessage();
		if (onSearch.contains(p))
		{
			if (mess.equalsIgnoreCase("cancel"))
			{
				onSearch.remove(p);
				ui.open(p, "", 1);
				event.setCancelled(true);
				return;
			}
			onSearch.remove(p);
			ui.open(p, mess, 1);
			event.setCancelled(true);
			return;
		}
	}
}
