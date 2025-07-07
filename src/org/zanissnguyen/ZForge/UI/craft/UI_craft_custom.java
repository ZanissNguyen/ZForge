package org.zanissnguyen.ZForge.UI.craft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.System.object.zstyle;
import org.zanissnguyen.ZForge.Utils.utils;

public class UI_craft_custom extends UI_craft {

	private ItemStack fill = utils.createItem(Material.WHITE_STAINED_GLASS_PANE, 1, 0, " ");
	public String style_id;
	
	public UI_craft_custom(ZForge plugin, utils utils, zrecipe recipe) {
		super(plugin, utils, 1, recipe);
		
		style_id = recipe.type;
	}

	@Override
	public Inventory setup(Player p, zrecipe recipe) 
	{
		style_id = recipe.type;
		
		String styleStr = recipe.type;
		zstyle sty = plugin.file_sty.getStyle(styleStr);
		rows = sty.rows;
		materialsSlot.addAll(sty.materials);
		
		//is default_fill valid
		if (sty.icon.keySet().contains(sty.default_fill))
			fill = plugin.file_sty.getIcon(sty.id+".icons."+sty.default_fill);
		
		Inventory inv = Bukkit.createInventory(null, rows*9, name_prefix+utils.color(" &e(#"+recipe.id+")"));
		
		//default
		for (int i = 0; i<rows*9; i++) inv.setItem(i, fill);
		
		//icons
		for (String s: sty.icon.keySet())
		{
			for (int i: sty.iconSlots.get(s))
			{
				inv.setItem(i, sty.icon.get(s));
			}
		}
		
		// materials
		ItemStack fill_slot = utils.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, false, " ");
		for (int i = 0; i<materialsSlot.size(); i++)
		{
			inv.setItem(materialsSlot.get(i), fill_slot);
		}
		setUpMaterial(p, inv);
		
		// reward
		inv.setItem(sty.reward_slot, recipe.reward.convertToItemStack(true));
		
		// craft, back, info
		inv.setItem(sty.back_slot, backIcon());
		inv.setItem(sty.craft_slot, craftIcon());
		inv.setItem(sty.info_slot, infoIcon(p));
		
		return inv;
	}
	
	@Override
	public ItemStack craftIcon() {return plugin.file_sty.getIcon(style_id+".craft");}
	
	@Override
	public ItemStack backIcon() {return plugin.file_sty.getIcon(style_id+".back");}
	
	@Override
	public ItemStack infoIcon(Player p)
	{
		ItemStack info = plugin.file_sty.getIcon(style_id+".info");
		
		List<String> current = utils.getLore(info);
		List<String> lore = new ArrayList<>();
		String yesIcon = plugin.file_loc.getSymbol("tick");
		String noIcon = plugin.file_loc.getSymbol("cross");

		for (String s: current)
		{
			if (s.contains("<lvl>"))
			{
				String colorCode = "";
				double level = utils.getLevel(p);
				level = utils.fixedDecimal(level, 2);
				double levelRequire = recipe.lvl;
				
				if (level<levelRequire)
				{
					colorCode = "&c";
					lore.add(utils.color(s.replace("<lvl>", colorCode +level+"/"+levelRequire+" "+noIcon)));
				}
				else
				{
					colorCode = "&a";
					lore.add(utils.color(s.replace("<lvl>", colorCode +level+"/"+levelRequire+" "+yesIcon)));
				}
			}
			else if (s.contains("<time>"))
			{
				lore.add(utils.color(s.replace("<time>", recipe.time+"")));
			}
			else if (s.contains("<material_status>"))
			{
				String status = "";
				if (enoughMaterial(p))
				{
					status = plugin.file_loc.getForge("info-icon.material-status.enough");
					lore.add(utils.color(s.replace("<material_status>", status+" "+yesIcon)));
				}
				else
				{
					status = plugin.file_loc.getForge("info-icon.material-status.not-enough");
					lore.add(utils.color(s.replace("<material_status>", status+" "+noIcon)));
				}
			} else
			{
				lore.add(utils.color(s));
			}
		}
		ItemMeta meta = info.getItemMeta();
		meta.setLore(lore);
		info.setItemMeta(meta);
		return info;
	}
	
	public static ItemStack _craftIcon_(zrecipe recipe)
	{
		return (new UI_craft_custom(ZForge.main(), ZForge.main().utils, recipe)).craftIcon();
	}
	
	public static ItemStack _backIcon_(zrecipe recipe)
	{
		return (new UI_craft_custom(ZForge.main(), ZForge.main().utils, recipe)).backIcon();
	}
	
//	public static ItemStack _craftIcon_()
//	{
//		return (new UI_craft_custom(ZForge.main(), ZForge.main().utils)).craftIcon();
//	}
}
