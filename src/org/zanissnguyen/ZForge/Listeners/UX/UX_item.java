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
import org.zanissnguyen.ZForge.System.object.zitem;
import org.zanissnguyen.ZForge.UI.list.UI_item;
import org.zanissnguyen.ZForge.Utils.utils;

@SuppressWarnings("deprecation")
public class UX_item  extends zevent
{
	public List<Player> onSearch;
	
	private UI_item ui;
	public UX_item(ZForge plugin, utils utils, UI_item ui) {
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
		
		if (clicked.getTitle().startsWith(ui.name) && event.getRawSlot()<ui.rows*9)
		{
			String title = clicked.getTitle();
			String pageStr = title.substring(title.lastIndexOf("[")+1, title.lastIndexOf("/"));
//			String maxPageStr = title.substring(title.lastIndexOf("/")+1, title.lastIndexOf("]"));
			
			int page = Integer.parseInt(pageStr);
//			int maxPage = Integer.parseInt(maxPageStr);
			
			String s = title.substring(0, title.lastIndexOf("|"));
			String keyword = s.substring(s.lastIndexOf("[")+1, s.lastIndexOf("]"));
			
			int slot = event.getRawSlot();
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
			
			if (slot==50 && clicked.getItem(slot).isSimilar(ui.findIcon(keyword, plugin.file_itm.searchFor(keyword).size())))
			{
				p.closeInventory();
				onSearch.add(p);
				p.sendMessage(plugin.file_loc.getForge("search"));
			}
			
			if (slot==53 && clicked.getItem(slot).isSimilar(ui.nextIcon()))
			{
				p.closeInventory();
				ui.open(p, keyword, page+1);
			}

			ItemStack item = clicked.getItem(slot);
			if ((slot>=0 && slot<=35) && !( item.isSimilar(ui.fillIcon()) || item.isSimilar(ui.fill_slot) || item.isSimilar(ui.laner) ))
			{
				if (utils.hasPermission(p, ui.permission)) 
				{
					ItemStack toCheck = clicked.getItem(slot);
					ItemMeta meta = toCheck.getItemMeta();
					String name = meta.getDisplayName();
					if (name.contains("#"))
					{
						String id = name.substring(name.lastIndexOf("#")+1, name.lastIndexOf(")"));
						zitem got = plugin.file_itm.getItem(id);
						p.getInventory().addItem(got.convertToItemStack(false));
					}
				}
			}
			
			event.setCancelled(true);	
		}
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
