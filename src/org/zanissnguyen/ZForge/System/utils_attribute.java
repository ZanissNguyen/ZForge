package org.zanissnguyen.ZForge.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.database.zfile_storage;
import org.zanissnguyen.ZForge.System.buff.Zbuff;
import org.zanissnguyen.ZForge.System.buff.buff_manager;
import org.zanissnguyen.ZForge.System.object.zgem;
import org.zanissnguyen.ZForge.System.rate.zrate;
import org.zanissnguyen.ZForge.System.requirement.require_level;
import org.zanissnguyen.ZForge.System.requirement.require_permission;
import org.zanissnguyen.ZForge.System.requirement.requirement;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.System.stat.stat_manager;
import org.zanissnguyen.ZForge.Utils.utils;

public class utils_attribute 
{
	private static ZForge plugin = ZForge.main();
	private static utils utils = ZForge.main().utils;
	
	public static void updateAttribute(Player p)
	{
		stat_manager.updateStat(p);
		buff_manager.updateBuffs(p);
	}
	
	public boolean canUse(Player p, ItemStack i)
	{
		boolean result = true;
				
		// requirement
		for (requirement req: allRequirement())
		{
			if (hasRequire(i, req)!=-1)
			{
				result &= req.checkRequire(p, i);
			}
		}
		
		return result;
	}
	
	// type
	public boolean isType(String type)
	{
		return plugin.file_attr.getAllType().contains(type);
	}
	
	public String getType(ItemStack item)
	{
		if (isItemHasType(item)==-1) return "";
		else
		{
			String lore = utils.getLore(item).get(isItemHasType(item));
			
			for (String s: plugin.file_attr.getAllType())
			{
				String display = getLoreType(s);
				
				if (lore.equalsIgnoreCase(display)) return s;
			}
		}
		return "";
	}
	
	public int isItemHasType(ItemStack item)
	{
		List<String> lore = utils.getLore(item);
		
		return isLoreTypeSetted(lore);
	}
	
	public int isItemHasType(List<String> lore)
	{
		return isLoreTypeSetted(lore);
	}
	
	public int isLoreTypeSetted(List<String> lore)
	{
		for (String s: lore)
		{
			String format = plugin.file_form.typeFormat().replace("<type>", "");
			
			if (s.startsWith(format)) 
			{
				for (String type: plugin.file_attr.getAllType())
				{
					String display = getLoreType(type);
					if (s.equalsIgnoreCase(display)) return lore.indexOf(s);
				}
			}
		}
		
		return -1; // mean no type setted
	}
	
	public String getTypeDisplay(String type)
	{
		return  plugin.file_attr.getType(type);
	}
	
	public String getLoreType(String type)
	{
		String format = plugin.file_form.typeFormat();
		String loreDisplay = getTypeDisplay(type);
		
		return format.replace("<type>", loreDisplay);
	}
	
	public ItemStack setType(ItemStack item, String type)
	{
		ItemStack result = item.clone();
		
		String loreType = getLoreType(type);
		int index = isItemHasType(item);
		if (index==-1)
		{
			result = utils.addLore(result, -1, loreType);
		} else result = utils.setLore(result, index, loreType);
		
		return result;
	}
	
	public List<String> offHandTypes()
	{
		List<String> result = new ArrayList<>();
		result.add("offhand");
		result.add("shield");
		result.addAll(rangeTypes());
		return result;
	}
	
	public List<String> rangeTypes()
	{
		List<String> result = new ArrayList<>();
		result.add("bow");
		result.add("crossbow");
		return result;
	}
	
	public boolean isRangeTypes(String type)
	{
		return rangeTypes().contains(type);
	}
	
	public boolean isOffHandTypes(String type)
	{
		return offHandTypes().contains(type);
	}
	
	public List<String> armorTypes()
	{
		List<String> result = new ArrayList<>();
		result.add("helmet");
		result.add("chestplate");
		result.add("leggings");
		result.add("boots");
		return result;
	}
	
	public boolean isArmorTypes(String type)
	{
		return armorTypes().contains(type);
	}
	
	public List<String> accessoryTypes()
	{
		List<String> result = new ArrayList<>();
		result.add("ring");
		result.add("belt");
		result.add("necklace");
		result.add("gaunlet");
		result.add("artifact");
		return result;
	}
	
	public boolean isAccessoryTypes(String type)
	{
		return accessoryTypes().contains(type);
	}
	
	//stat
	public double getStat(ItemStack item, Zstat stat)
	{
		String percentSymbol = plugin.file_loc.getSymbol("percent");
		if (isItemHasStat(item, stat)==-1) return 0;
		else
		{
			String format =plugin.file_form.statFormat();
			String lore = utils.getLore(item).get(isItemHasStat(item, stat));
			String display = stat.getDisplay();
			
			String over = format.substring(Math.max(format.lastIndexOf("<value>")+7, format.lastIndexOf("<stat>")+6));
			
			int statIndex = format.indexOf("<stat>");
			int valueIndex = format.indexOf("<value>");
			String value_str = "";
			
			if (statIndex<valueIndex)
			{
				int argumentDistance = valueIndex - (statIndex+6);
				String temp = lore.substring(statIndex+display.length()+argumentDistance, lore.length()-over.length());
				value_str = temp.substring(Math.max(temp.indexOf("+"), temp.indexOf("-")));
			}
			else
			{
				int argumentDistance = statIndex - (valueIndex+7);
				String temp = lore.substring(0, lore.indexOf(display)-argumentDistance);
				value_str = temp.substring(Math.max(temp.lastIndexOf("+"), temp.lastIndexOf("-")));
			}
			
			if (stat.isPercent())
			{
				return (Double.parseDouble(value_str.substring(0, value_str.length()-percentSymbol.length()))/100.0);
			}
			else return Double.parseDouble(value_str);
		}
	}
	
	public int isItemHasStat(ItemStack item, Zstat stat)
	{
		List<String> lore = utils.getLore(item);
		
		return isLoreHasStat(lore, stat); 
	}
	
	public int isItemHasStat(List<String> list, Zstat stat)
	{
		return isLoreHasStat(list, stat); 
	}
	
	public int isLoreHasStat(List<String> lore, Zstat stat)
	{
		for (String s: lore)
		{
			String display = stat.getDisplay();
			if (!s.contains(display)) continue;
			
			String format = plugin.file_form.statFormat();
			String over = format.substring(Math.max(format.lastIndexOf("<value>")+7, format.lastIndexOf("<stat>")+6));
			
			int statIndex = format.indexOf("<stat>");
			int valueIndex = format.indexOf("<value>");
			String value_str = "";
			
			if (statIndex<valueIndex)
			{
				if (statIndex==s.indexOf(display))
				{
					int argumentDistance = valueIndex - (statIndex+6);
					String temp = s.substring(statIndex+display.length()+argumentDistance, s.length()-over.length());
					value_str = temp.substring(Math.max(temp.indexOf("+"), temp.indexOf("-")));
					
					if (value_str.length()+2>=temp.length()) return lore.indexOf(s);
					else continue;
				}
				else continue;
			}
			else
			{
				int argumentDistance = statIndex - (valueIndex+7);
				String temp = s.substring(0, s.indexOf(display)-argumentDistance);
				value_str = temp.substring(Math.max(temp.lastIndexOf("+"), temp.lastIndexOf("-")));
	
				if (utils.isNumber(value_str)) return lore.indexOf(s);
				else continue;
			}
		}
		
		return -1; // mean no stat
	}
	
	public ItemStack setStat(ItemStack item, Zstat stat, double value)
	{
		ItemStack result = item.clone();
		
		String loreStat = stat.getLore(value+"", false);
		
		int index = isItemHasStat(item, stat);
		if (index==-1)
		{
			if (value!=0) result = utils.addLore(result, -1, loreStat);
		} else 
		{
			if (value!=0) result = utils.setLore(result, index, loreStat);
			else result = utils.removeLore(result, index);
		}
		
		return result;
	}
	
	// Durability Method
	private String durability_display() {return plugin.file_attr.durabilityDisplay();}
	private String durability_format() {return plugin.file_form.durabilityFormat();}
	
	public String getLoreDurability(int current, int max)
	{	
		current = Math.max(current, 1);
		current = Math.min(current, max);
		String toAdd = durability_format().replace("<display>", durability_display()).replace("<value>", "["+current+"/"+max+"]");
		return toAdd;
	}
	
	public ItemStack setMaxDurability(ItemStack item, int value)
	{
		ItemStack result = item.clone();
		
		String toAdd = durability_format().replace("<display>", durability_display()).replace("<value>", "["+value+"/"+value+"]");
		int index = isItemSetDurability(item);
		if (index==-1)
		{
			if (value!=0) result = utils.addLore(result, -1, toAdd);
		} else 
		{
			if (value!=0) result = utils.setLore(result, index, toAdd);
			else result = utils.removeLore(result, index);
		}
		
		ItemMeta meta = result.getItemMeta();
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		result.setItemMeta(meta);
		
		return result;
	}
	
	public ItemStack setCurrentDurability(ItemStack item, int value)
	{
		ItemStack result = item.clone();
		
		int index = isItemSetDurability(item);
		if (index==-1)
		{
			if (value!=0) return setMaxDurability(item, value);
		} else 
		{
			int maxDurability = getMaxDurability(item);
			
			value = Math.min(value, maxDurability);
			value = Math.max(0, value);
			
			String toAdd = durability_format().replace("<display>", durability_display()).replace("<value>", "["+value+"/"+maxDurability+"]");
			
			if (value>=0) result = utils.setLore(result, index, toAdd);
		}
		
		return result;
	}
	
	public int isItemSetDurability(ItemStack item)
	{
		String lore = durability_format().replace("<display>", durability_display());
		String toCheck = lore.substring(0, lore.indexOf("<value>"));
		
		if (item == null) return -1;
		if (item.getItemMeta() == null) return -1;
		
		List<String> itemLore = utils.getLore(item);
		
		if (itemLore.isEmpty()) return -1;
		
		for (String s: itemLore)
		{
			if (s.startsWith(toCheck)) return itemLore.indexOf(s);
		}
		
		return -1;
	}
	
	public int isItemSetDurability(List<String> itemLore)
	{
		String lore = durability_format().replace("<display>", durability_display());
		String toCheck = lore.substring(0, lore.indexOf("<value>"));
		
		if (itemLore.isEmpty()) return -1;
		
		for (String s: itemLore)
		{
			if (s.startsWith(toCheck)) return itemLore.indexOf(s);
		}
		
		return -1;
	}
	
	public int getCurrentDurability(ItemStack item)
	{
		int line = isItemSetDurability(item);
		
		if (line == -1) return line;
		
		String lore = utils.getLore(item).get(line);
		String format = durability_format().replace("<display>", durability_display());
		int over = format.length()-7-format.indexOf("<value>");
		String value = lore.substring(format.indexOf("<value>"), lore.length()-over);
		
		String rs_str = value.substring(value.indexOf("[")+1, value.indexOf("/"));
		
		int result = Integer.parseInt(rs_str);
		
		return result;
	}
	
	public int getMaxDurability(ItemStack item)
	{
		int line = isItemSetDurability(item);
		
		if (line == -1) return line;
		
		String lore = utils.getLore(item).get(line);
		String format = durability_format().replace("<display>", durability_display());
		int over = format.length()-7-format.indexOf("<value>");
		String value = lore.substring(format.indexOf("<value>"), lore.length()-over);
		
		String rs_str = value.substring(value.indexOf("/")+1, value.indexOf("]"));
		
		int result = Integer.parseInt(rs_str);
		
		return result;
	}
	
	public String getDurability(ItemStack item)
	{
		int current = getCurrentDurability(item);
		int max = getMaxDurability(item);
		
		if (current == -1 && max != -1) return max+"";
		if (max == -1 && current != -1) return current+"";
		if (current == -1 && max == -1) return "";
		
		return current+"/"+max;
	}
	
	public boolean isBroke(ItemStack item, Player p, String slot, boolean msg)
	{
		int line = isItemSetDurability(item);
		
		String replaceSlot = plugin.file_attr.getType(slot);
		boolean isBroke = plugin.file_cfg.itemBreakEnable();
		String message = (isBroke ? plugin.file_loc.getAttribute("durability.break")
				: plugin.file_loc.getAttribute("durability.run-out")).replace("<slot>", replaceSlot);
		
		if (line == -1) return false;
		int currentDurability = getCurrentDurability(item);
		
		if (currentDurability == 0)
		{
			if (msg) p.sendMessage(message);
			if (isBroke) item.setAmount(item.getAmount()-1);
			return true;
		}
		else return false;
	}
	
	public void durabilityProcess(ItemStack item, Player p, String slot, int intSlot)
	{
		if (item==null || item.getType()==Material.AIR) return;
		zfile_storage storage = plugin.file_sto;
		PlayerInventory inv = p.getInventory();
		ItemStack result = item.clone();
		int current = getCurrentDurability(result);
		
		if (current == -1) return;
		if (current==0) return;
		
		result = setCurrentDurability(result, current-1);
		
		switch (slot)
		{
		case "ring":
			storage.setRingSlot(p, intSlot, result);
			break;
		case "artifact":
			storage.setArtifactSlot(p, intSlot, result);
			break;
		case "belt":
			storage.setBeltSlot(p, result);
			break;
		case "gauntlet":
			storage.setGauntletSlot(p, result);
			break;
		case "necklace":
			storage.setNecklaceSlot(p, result);
			break;
		case "helmet":
			inv.setHelmet(result);
			break;
		case "chesplate":
			inv.setChestplate(result);
			break;
		case "leggings":
			inv.setLeggings(result);
			break;
		case "boots":
			inv.setBoots(result);
			break;
		case "mainhand":
			inv.setItemInMainHand(result);
			break;
		case "offhand":
			inv.setItemInOffHand(result);
			break;
		default:
			break;
		}
		
		if (isBroke(result,p,slot,plugin.file_cfg.itemBreakEnable()))
		{
			switch (slot)
			{
			case "ring":
				stat_manager.updateRingSlot(result, p, intSlot, true);
				break;
			case "artifact":
				stat_manager.updateArtifactSlot(result, p, intSlot, true);
				break;
			case "belt":
				stat_manager.updateBeltSlot(result, p, true);
				break;
			case "gauntlet":
				stat_manager.updateGauntletSlot(result, p, true);
				break;
			case "necklace":
				stat_manager.updateNecklaceSlot(result, p, true);
				break;
			case "helmet":
				stat_manager.updateHelmetSlot(result, p, true);
				break;
			case "chesplate":
				stat_manager.updateChestplateSlot(result, p, true);
				break;
			case "leggings":
				stat_manager.updateLeggingsSlot(result, p, true);
				break;
			case "boots":
				stat_manager.updateBootsSlot(result, p, true);
				break;
			case "mainhand":
				stat_manager.updateMainhandSlot(result, p, true);
				break;
			case "offhand":
				stat_manager.updateOffhandSlot(result, p, true);
				break;
			default:
				break;
			}
		}
	}
	
	// buff:
	// isItemHasBuff
	// isLoreHasBuff
	// getBuff
	// setBuff
	public int hasBuff(ItemStack item, Zbuff buff)
	{
		ItemMeta meta = null;
		List<String> lore = new ArrayList<>();
		if (item.getType()!=Material.AIR)
		{
			if (item.getItemMeta()!=null) meta = item.getItemMeta();
			if (meta.hasLore()) lore = meta.getLore();
			for (String s: lore)
			{
				if (s.startsWith(buff.getLore())) 
				{
					return lore.indexOf(s);
				}	
			}
		}
		return -1;
	}
	
	public int hasBuff(List<String> lore, Zbuff buff)
	{
		for (String s: lore)
		{
			if (s.startsWith(buff.getLore())) 
			{
				return lore.indexOf(s);
			}	
		}
		return -1;
	}
	
	public void addBuff(ItemStack item, Zbuff buff, int level)
	{
		String toAdd = buff.getLore(buff_manager.getLevel(level));
		if (item.getType()!=Material.AIR)
		{
			List<String> lore = new ArrayList<>();
			ItemMeta meta = null;
			if (item.getItemMeta()!=null) meta = item.getItemMeta();
			if (meta.hasLore()) lore = meta.getLore();
			if (level!=0)
			{
				if (hasBuff(item, buff)!=-1)
				{
					lore.remove(hasBuff(item, buff));
					lore.add(hasBuff(item, buff), toAdd);
				} else lore.add(toAdd);
			} else
			{
				if (hasBuff(item, buff)!=-1)
				{
					lore.remove(hasBuff(item, buff));
				} 
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}
	
	public Map<Zbuff, Integer> getBuff(ItemStack item)
	{
		Map<Zbuff, Integer> buffs = new HashMap<>();
		if (item==null) return buffs;
		
		ItemMeta meta = null;
		List<String> lore = new ArrayList<>();
		if (item.getType()!=Material.AIR)
		{
			if (item.getItemMeta()!=null) meta = item.getItemMeta();
			if (meta.hasLore()) lore = meta.getLore();
			for (String s: lore)
			{
				for (Zbuff b: Zbuff.values())
				{
					if (s.startsWith(b.getLore()))
					{
						String levelStr = s.substring(s.lastIndexOf(" ")+1, s.length());
						int level = buff_manager.getLevel(levelStr);
						buffs.put(b, level);
					}
				}
			}
		}
		return buffs;
	}
	
	public byte getBuffLevel(ItemStack item, Zbuff buff)
	{
		if (item==null) return 0;
		
		ItemMeta meta = null;
		List<String> lore = new ArrayList<>();
		if (item.getType()!=Material.AIR)
		{
			if (item.getItemMeta()!=null) meta = item.getItemMeta();
			if (meta.hasLore()) lore = meta.getLore();
			for (String s: lore)
			{
				if (s.startsWith(buff.getLore()))
				{
					String levelStr = s.substring(s.lastIndexOf(" ")+1, s.length());
					byte level = buff_manager.getLevel(levelStr);
					return level;
				}
			}
		}
		return 0;
	}
	
	// leveling >>
	public int hasLeveling(ItemStack item)
	{
		ItemMeta meta = null;
		List<String> lore = new ArrayList<>();
		if (item.getType()!=Material.AIR)
		{
			if (item.getItemMeta()!=null) meta = item.getItemMeta();
			if (meta.hasLore()) lore = meta.getLore();
			for (String s: lore)
			{
				if (s.startsWith(leveling.justPrefix())) 
				{
					return lore.indexOf(s);
				}	
			}
		}
		return -1;
	}

	public int hasLeveling(List<String> lore)
	{
		for (String s: lore)
		{
			if (s.startsWith(leveling.justPrefix())) 
			{
				return lore.indexOf(s);
			}	
		}
		
		return -1;
	}
	
	public ItemStack setLevel(ItemStack item, leveling level)
	{
		ItemStack result = item.clone();		
		if (hasLeveling(result)==-1)
		{
			result = utils.addLore(result, -1, level.getLore());
			if (level.current_level<=0) return result;
		}
		else
		{
			if (level.current_level<=0) result = utils.removeLore(result, hasLeveling(result));
			else result = utils.setLore(result, hasLeveling(result), level.getLore());
		}
		
		return result;
	}
	
	public leveling getItemLevel(ItemStack item)
	{
		if (hasLeveling(item)==-1) return new leveling(0,0,0);
		
		List<String> lore = utils.getLore(item);
		if (lore.isEmpty()) return new leveling(0, 0, 0);
		
		String line = lore.get(hasLeveling(lore));
		
		return getItemLevel(line);		
	}
	public leveling getItemLevel(String level_line)
	{
		String str = level_line;
		String cur_exp = str.substring(str.lastIndexOf("[")+1, str.lastIndexOf("/"));
		int cur_exp_int = Integer.parseInt(cur_exp);
		
		String max_exp = str.substring(str.lastIndexOf("/")+1, str.lastIndexOf("]"));
		int max_exp_int = Integer.parseInt(max_exp);
		
		str = str.substring(0, str.lastIndexOf(" "));
		String level = str.substring(str.lastIndexOf(" ")+1);
		level = level.substring(2);
		int level_int = Integer.parseInt(level);
		
		return new leveling(level_int, cur_exp_int, max_exp_int);
	}
	
	public ItemStack levelingProcess(ItemStack item, int exp_gain, boolean is_up)
	{
		
		if (item==null || item.getType()==Material.AIR) return utils.createItem(Material.AIR, 1);
		ItemStack result = item.clone();
		if (hasLeveling(result)==-1) return result;
		
		zrate rate = getRateFromItem(result);
		int rate_max = rate.max_level;
		
		leveling level_info = getItemLevel(result);
//		System.out.println(level_info.current_level + " " + level_info.current_exp + " " + level_info.require_exp);
		int level_up = 0;
		level_info.current_exp += exp_gain;
		while (level_info.current_exp >= level_info.require_exp
				&& rate_max > level_info.current_level)
		{
			// level up
			level_info.current_exp -= level_info.require_exp;
			level_info.current_level ++;
			level_info.require_exp 
				= (int) (rate.base_exp * Math.pow(1+rate.exp_modifier, level_info.current_level-1));
			level_up++;
//			System.out.println("levelup");
//			System.out.println(level_info.current_level + " " + level_info.current_exp + " " + level_info.require_exp);
		}
		result = setLevel(result, level_info);
		if (level_up==0) return result;
		is_up = true;
		
		Map<Zstat, Double> item_stat = new HashMap<>();
		Map<Zstat, Double> gem_stat = new HashMap<>();
		
		// get current item stat
		for (Zstat stat: Zstat.values())
		{
			if (isItemHasStat(result, stat)!=-1)
			{
				item_stat.put(stat, getStat(result, stat));
			}
		}
		
		// get gems stat
		List<String> lore = utils.getLore(result);
		while (zgem.isItemHasFilledSlot(lore)!=-1)
		{
			String gem_line = lore.get(zgem.isItemHasFilledSlot(lore));
			Zstat stat = zgem.getStatFromGemString(gem_line);
			double value = zgem.getStatValueFromGemString(gem_line);
			gem_stat.put(stat, value);
			lore.remove(zgem.isItemHasFilledSlot(lore));
		}
		
		// find raw item stat, increase and  at current level
		for (Zstat stat: item_stat.keySet())
		{
			boolean gem_affect = gem_stat.get(stat)!=null && gem_stat.get(stat)!=0;
			
			double raw_value = item_stat.get(stat) - (gem_affect? gem_stat.get(stat): 0);
//			System.out.println(stat.getID() +" "+raw_value);
			
			double new_value = utils.fixedDecimal(
					raw_value * Math.pow(1+rate.stat_modifier, level_up)
					+ (gem_affect? gem_stat.get(stat):0)
					, plugin.file_cfg.getDigit());
			
			result = setStat(result, stat, new_value * (stat.isPercent() ? 100:1));
		}
		
		int old_level = level_info.current_level - level_up;
		
		Map<Zbuff, Integer> item_buff = getBuff(item);
		int buff_scale = (int) Math.round(1.0/rate.buff_modifier);
		for (Zbuff buff: item_buff.keySet())
		{
			int raw_buff_level = item_buff.get(buff) - old_level % buff_scale;
			int new_level = raw_buff_level + level_info.current_level % buff_scale;
			
			addBuff(result, buff, new_level);
		}
		
		ItemMeta meta = result.getItemMeta();
		int enchant_scale = (int) Math.round(1.0/rate.enchant_modifier);
		
		for (Enchantment e: meta.getEnchants().keySet())
		{
			int current_e_level = meta.getEnchantLevel(e);
			int raw_e_level = current_e_level - old_level % enchant_scale;
			int new_e_level = raw_e_level + level_info.current_level % enchant_scale;
			
			meta.removeEnchant(e);
			meta.addEnchant(e, new_e_level, false);
		}
		
		return result;
	}
	
	// rate >>
	public int isItemRated(ItemStack item)
	{
		ItemMeta meta = null;
		List<String> lore = new ArrayList<>();
		if (item.getType()!=Material.AIR)
		{
			if (item.getItemMeta()!=null) meta = item.getItemMeta();
			if (meta.hasLore()) lore = meta.getLore();
			for (String s: lore)
			{
				if (s.startsWith(zrate.justPrefix())) 
				{
					return lore.indexOf(s);
				}	
			}
		}
		return -1;
	}
	
	public int isItemRated(List<String> lore)
	{
		for (String s: lore)
		{
			if (s.startsWith(zrate.justPrefix())) 
			{
				return lore.indexOf(s);
			}	
		}
		
		return -1;
	}
	
	public ItemStack setRate(ItemStack item, String rate_id)
	{
		ItemStack result = item.clone();
		zrate rate = new zrate(rate_id);
		
		if (isItemRated(item)==-1)
		{
			result = utils.addLore(result, -1, rate.getLore());
		}
		else
		{
			result = utils.setLore(item, isItemRated(item), rate.getLore());
		}
		
		return result;
	}
	
	// getRateFromItem(Item)
	public zrate getRateFromItem(ItemStack item)
	{
		if (isItemRated(item)!=-1)
		{
			zrate got = zrate.getRateFromLine(utils.getLore(item).get(isItemRated(item)));
			return got;
		}
		else return new zrate(zrate.getDefault());
	}	
	
	public zrate getRateFromItem(List<String> lore)
	{
		if (isItemRated(lore)!=-1)
		{
			zrate got = zrate.getRateFromLine(lore.get(isItemRated(lore)));
			return got;
		}
		else return new zrate(zrate.getDefault());
	}
	
	// requirement
	public List<requirement> allRequirement()
	{
		List<requirement> result = new ArrayList<>();
		result.add(new require_level());
		result.add(new require_permission());
		
		return result;
	}
	
	public List<String> allRequirementID()
	{
		List<String> result = new ArrayList<>();
		for (requirement req: allRequirement())
		{
			result.add(req.id);
		}
		
		return result;
	}
	
	public requirement getRequirementFromID(String id)
	{
		for (requirement req: allRequirement())
		{
			if (req.id.equalsIgnoreCase(id)) return req;
		}
		return null;
	}
	
	public String getRequire(ItemStack item, requirement req)
	{
		if (item==null) return null;
		else
		{
			ItemMeta meta=null;
			List<String> lore = new ArrayList<String>();
			if (item.hasItemMeta()) 
			{
				meta = item.getItemMeta();
				if (meta.getLore()!=null) lore = meta.getLore();
				for (String s: lore)
				{
					if (s.startsWith(req.display))
					{
						return s.substring(s.lastIndexOf(" ")+1, s.length());
					}
				}
			}
		}
		return null;
	}
	
	public int hasRequire(List<String> lore, requirement req)
	{
		for (String s: lore)
		{
			if (s.startsWith(req.display))
				return lore.indexOf(s);
		}
		return -1;
	}
	
	public int hasRequire(ItemStack item, requirement req)
	{
		ItemMeta meta=null;
		List<String> lore = new ArrayList<String>();
		if (item.hasItemMeta()) 
		{
			meta = item.getItemMeta();
			if (meta.getLore()!=null) lore = meta.getLore();
			for (String s: lore)
			{
				if (s.startsWith(req.display))
					return lore.indexOf(s);
			}
		}
		return -1;
	}
	
	public ItemStack setRequire(ItemStack item, String req_id, String value)
	{
		ItemStack result = item.clone();
		requirement req = getRequirementFromID(req_id);
		if (req==null) return result;
		
		ItemMeta meta=null;
		List<String> resultLore = new ArrayList<String>();
		if (item.getItemMeta()!=null) meta = result.getItemMeta();
		if (meta.getLore()!=null) resultLore = meta.getLore();
		
		String lore = req.display;
			
		lore = utils.color(lore + (req.has_value ? ("&c "+value) : ""));
		int index = hasRequire(result, req);
		if (index<0) resultLore.add(lore);
		else 
		{
			resultLore.remove(index);
			resultLore.add(index, lore);
		}
		
		meta.setLore(resultLore);
		result.setItemMeta(meta);
		return result;
	}
}
