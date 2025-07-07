package org.zanissnguyen.ZForge.System.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.System.leveling;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.buff.Zbuff;
import org.zanissnguyen.ZForge.System.buff.buff_manager;
import org.zanissnguyen.ZForge.System.rate.zrate;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.Utils.utils;

public class zitem_inv extends zitem 
{
	private static ZForge plugin = ZForge.main();
	private static utils utils = ZForge.main().utils;
	private static utils_attribute a_utils = ZForge.main().a_utils;
	
	public List<String> inv_gems;
	public int level;
	public int exp;
	
	public zitem_inv(String id, String type, int data, String display, List<String> description, 
			String item_type, String durability, List<String> enchants, List<String> flags, 
			List<String> stats, List<String> buffs, boolean unbreak, List<String> inv_gems, String rate,
			int level, int exp) {
		super(id, type, data, display, description, item_type, durability, enchants
				, flags, stats, buffs, unbreak, "", rate, false);
		this.inv_gems = inv_gems;
		this.level = level;
		this.exp = exp;
	}

	public zitem_inv(String id, ItemStack item) {
		super(id, item);
		this.inv_gems = new ArrayList<>();
		
		if (type.equalsIgnoreCase("AIR") || !item.hasItemMeta())
		{
			this.inv_gems = new ArrayList<>();
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		if (meta.hasLore())
		{
			List<String> lore = meta.getLore();
			
			// gems
			List<String> gems = new ArrayList<>();
			while (zgem.isItemHasFilledSlot(lore)!=-1)
			{
				int idx = zgem.isItemHasFilledSlot(lore);
				String line = lore.get(idx);
				
				String gem_name = line.substring(0, line.lastIndexOf(" "));
				String gem_id = plugin.file_gem.getGemByName(gem_name);
				double value = zgem.getStatValueFromGemString(line);
				
				gems.add(zgem.getGemString(gem_id, value, true));
				lore.remove(idx);
			}
			
			while (zgem.isItemHasSlot(lore, false)!=-1)
			{
				int idx = zgem.isItemHasSlot(lore, false);
				
				gems.add("empty");
				lore.remove(idx);
			}
			
			while (zgem.isItemHasSlot(lore, true)!=-1)
			{
				int idx = zgem.isItemHasSlot(lore, true);
				
				gems.add("locked");
				lore.remove(idx);
			}
			
			this.inv_gems = gems;
			
			// level and exp
			if (a_utils.hasLeveling(lore)!=-1)
			{
				leveling level_info = a_utils.getItemLevel(lore.get(a_utils.hasLeveling(lore)));
				lore.remove(a_utils.hasLeveling(lore));
				this.level = level_info.current_level;
				this.exp = level_info.current_exp;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack convertToItemStack(boolean is_icon) {
		ItemStack result;
		List<String> errors = new ArrayList<>();
		
		// type define and data
		if (type.equalsIgnoreCase("AIR")) return utils.createItem(Material.AIR, 1);
		
		if (utils.isMaterial(this.type)) 
			result = utils.createItem(Material.valueOf(this.type), 1, this.data);
		else 
		{
			String msg = plugin.file_loc.getError("no-type").replace("<t>", this.type);
			errors.add(msg);
			result = utils.createItem(Material.BARRIER, 1, 0);
		}
		// display
		if (!this.display.equalsIgnoreCase("Not Found"))
			result = utils.setName(result, this.display);
		// enchant
		if (!this.enchants.isEmpty())
		{
			for (String e: enchants)
			{
				String ench = e.substring(0, e.indexOf(":"));
				String lvl = e.substring(e.indexOf(":")+1);
				
				if (!utils.isEnchantment(ench)) {
					errors.add("&4+ enchant &c"+ench+" &4is not an enchantment");
					continue;
				}
				
				if (!utils.isNumber(lvl)) {
					errors.add("&4+ enchant &c"+ench+" "+lvl+" &4is not a number");
					continue;
				}
						
				int lv = Integer.parseInt(lvl);
				result = utils.addEnchant(result, Enchantment.getByName(ench), lv);
				
			}
		}
		// flag
		if (!this.flags.isEmpty())
		{
			for (String f: flags)
			{
				if (!utils.isFlag(f)) {
					errors.add("&4+ flag &c"+f+" &4is not a flag");
					continue;
				}
				
				result = utils.addFlag(result, ItemFlag.valueOf(f));	
			}
		}
		// unbreak
		result = utils.setUnbreak(result, this.unbreak);
		
		// lore, stats, buff, item-type
		List<String> lore = new ArrayList<>();
		List<String> format = plugin.file_form.itemFormat();
		//type
		
		for (String s: format)
		{
			if (s.equalsIgnoreCase("<rate>"))
			{
				if (this.rate==null || this.rate.equalsIgnoreCase("")) continue;
				
				if (zrate.allRates().contains(this.rate))
				{
					String rate = (new zrate(this.rate)).getLore();
					lore.add(rate);
				}
				else errors.add("&4+ rate: &c"+this.rate+"&4 is not a item rate");
			}
			else if (s.equalsIgnoreCase("<leveling>")) 
			{
				if (this.level!=0 && this.exp>=0)
				{
					zrate rate = null;
					if (zrate.allRates().contains(this.rate)) 
						rate = (new zrate(this.rate));
					else rate = (new zrate(zrate.getDefault()));
					
					int req_exp = (int)(rate.base_exp * Math.pow(rate.exp_modifier ,this.level-1));
					leveling level_info = new leveling(this.level, this.exp, req_exp);
					lore.add(level_info.getLore());
				}
			}
			else if (s.equalsIgnoreCase("<type>")) 
			{
				if (a_utils.isType(this.item_type))
				{
					String type = plugin.file_form.typeFormat();
					type = type.replace("<type>", plugin.file_attr.getType(this.item_type));
					lore.add(type);
				}
				else errors.add("&4+ item_type: &c"+this.item_type+"&4 is not a item type");
			}
			else if (s.equalsIgnoreCase("<durability>"))
			{
				if (!durability.equalsIgnoreCase(""))
				{
					if (durability.contains("/"))
					{
						String current_str = durability.substring(0, durability.indexOf("/"));
						String max_str = durability.substring(durability.indexOf("/")+1);
						
						if (!utils.isNumber(current_str))
						{
							errors.add(utils.color("&4+ durability: &c"+current_str+"&4 is not a number"));
							continue;
						} 
						
						if (!utils.isNumber(max_str))
						{
							errors.add(utils.color("&4+ durability: &c"+max_str+"&4 is not a number"));
							continue;
						} 
						
						int current = Integer.parseInt(current_str);
						int max = Integer.parseInt(max_str);
						lore.add(a_utils.getLoreDurability(current, max));
						result = utils.addFlag(result, ItemFlag.HIDE_UNBREAKABLE);
						result = utils.setUnbreak(result, true);
					}
					else
					{
						if (durability.equalsIgnoreCase("Not Found")) continue;
						
						if (!utils.isNumber(durability))
						{
							errors.add(utils.color("&4+ durability: &c"+durability+"&4 is not a number"));
							continue;
						} 
						else 
						{
							int value = Integer.parseInt(durability);
							lore.add(a_utils.getLoreDurability(value, value));
							result = utils.addFlag(result, ItemFlag.HIDE_UNBREAKABLE);
							result = utils.setUnbreak(result, true);
						}
					}
				}
			}
			else if (s.equalsIgnoreCase("<stat>"))
			{
				for (String stat: stats)
				{
					String key = stat.substring(0, stat.indexOf(":"));;
					String value = stat.substring(stat.indexOf(":")+1);
					
					if (!Zstat.isStat(key))
					{
						errors.add(utils.color("&4+ stats: &c"+key+"&4 is not a stat"));
						continue;
					}
					
					Zstat zst = Zstat.getFromID(key);
					
					if (value.contains("~") && !is_icon)
					{
						String start_str = value.substring(0, value.indexOf("~"));
						String end_str = value.substring(value.indexOf("~")+1);
						
						if (!utils.isNumber(start_str))
						{
							errors.add(utils.color("&4+ stats: &c"+start_str+"&4 is not a number"));
							continue;
						}
						if (!utils.isNumber(end_str))
						{
							errors.add(utils.color("&4+stats: &c"+end_str+"&4 is not a number"));
							continue;
						}
						double toSetValue = utils.randomDouble(Double.parseDouble(start_str), Double.parseDouble(end_str)
								, plugin.file_cfg.getDigit());
						lore.add(zst.getLore(toSetValue+"", is_icon));
					}
					else if (is_icon)
					{
						lore.add(zst.getLore(value.replace("~", plugin.file_loc.getSymbol("range")), is_icon));
					} 
					else
					{
						if (!utils.isNumber(value))
						{
							errors.add(utils.color("&4+stats: &c"+value+"&4 is not a number"));
							continue;
						}
						lore.add(zst.getLore(Double.parseDouble(value)+"", is_icon));
					}
					
				}
			}
			else if (s.equalsIgnoreCase("<buff>"))
			{
				for (String buff: buffs)
				{
					String key = buff.substring(0, buff.indexOf(":"));;
					String value = buff.substring(buff.indexOf(":")+1);
					
					if (!Zbuff.isBuff(key))
					{
						errors.add(utils.color("&4+ buffs: &c"+key+"&4 is not a buff"));
						continue;
					}
					
					Zbuff zb = Zbuff.getFromID(key);
					
					// check
					if (value.contains("~"))
					{
						String start_str = value.substring(0, value.indexOf("~"));
						String end_str = value.substring(value.indexOf("~")+1);
						
						if (!utils.isNumber(start_str))
						{
							errors.add(utils.color("&4+ buffs: &c"+start_str+"&4 is not a number"));
							continue;
						}
						if (!utils.isNumber(end_str))
						{
							errors.add(utils.color("&4+buffs: &c"+end_str+"&4 is not a number"));
							continue;
						}
						
						int start = Integer.parseInt(start_str);
						int end = Integer.parseInt(end_str);
						
						if (is_icon)
						{
							lore.add(zb.getLore(buff_manager.getLevel(start)+plugin.file_loc.getSymbol("range")+
									buff_manager.getLevel(end)));
						}
						else
						{
							int toSet = utils.randomInt(start, end);
							lore.add(zb.getLore(buff_manager.getLevel(toSet)));
						}
					}
					else
					{
						if (!utils.isNumber(value))
						{
							errors.add(utils.color("&4+buffs: &c"+value+"&4 is not a number"));
							continue;
						}
						
						int toSet = Integer.parseInt(value);
						lore.add(zb.getLore(buff_manager.getLevel(toSet)));
					}
				}
			}
			else if (s.equalsIgnoreCase("<gem>"))
			{
				for (String g: inv_gems)
				{
					if (g.contains(":")) // a gem
					{
						String id = g.substring(0, g.indexOf(":"));
						String value = g.substring(g.indexOf(":")+1);
						
						lore.add(zgem.getGemString(id, Double.parseDouble(value), false));
					}
					else if (g.equalsIgnoreCase("locked"))
					{
						lore.add(plugin.file_cfg.getLockedSlot());
					}
					else if (g.equalsIgnoreCase("empty"))
					{
						lore.add(plugin.file_cfg.getEmptySlot());
					}
				}
			}
			else if (s.equalsIgnoreCase("<lore>")) lore.addAll(this.description);
			else lore.add(s);
		}
		
		lore = utils.standardListString(lore);
		result = utils.addAllLore(result, lore);
		if (errors.size()!=0) result = utils.addAllLore(result, errors);
		
		return result;
	}

}
