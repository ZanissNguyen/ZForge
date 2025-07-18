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
import org.zanissnguyen.ZForge.System.require.item_require;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.Utils.utils;

public class zitem extends zobject {

	private static ZForge plugin = ZForge.main();
	private static utils utils = ZForge.main().utils;
	private static utils_attribute a_utils = ZForge.main().a_utils;
	
	public String type;
	public int data;
	public String display;
	public String item_type;
	public String durability;
	public List<String> enchants;
	public List<String> flags;
	public List<String> stats;
	public List<String> buffs;
	public String gems;
//	public List<String> inv_gems;
	public boolean unbreak;
	public boolean leveling;
//	public int level;
//	public int exp;
	public String rate;
	public List<String> requirements;
	
	public zitem(String id, String type, int data, String display, List<String> description, 
			String item_type, String durability, List<String> enchants, List<String> flags, 
			List<String> stats, List<String> buffs, boolean unbreak, String gems, String rate,
			boolean leveling, List<String> requirements) {
		super(id, description);
		this.type = type;
		this.data = data;
		this.display = display;
		this.item_type = item_type;
		this.durability = durability;
		this.enchants = enchants;
		this.flags = flags;
		this.stats = stats;
		this.unbreak = unbreak;
		this.buffs = buffs;
		this.gems = gems;
		this.rate = rate;
		this.leveling = leveling;
//		this.inv_gems = inv_gems;
		this.requirements = requirements;
	}
	
	@SuppressWarnings("deprecation")
	public zitem(String id, ItemStack item)
	{
		super(id, new ArrayList<>());
		this.type = item.getType().toString();
		
		if (type.equalsIgnoreCase("AIR") || !item.hasItemMeta())
		{
			this.display = null;
			this.data = 0;
			this.enchants = new ArrayList<>();
			this.flags = new ArrayList<>();
			this.unbreak = false;
			this.description = new ArrayList<>();
			this.stats = new ArrayList<>();
			this.item_type = "";
			this.buffs = new ArrayList<>();
			this.rate = "";
			this.leveling = false;
			this.requirements = new ArrayList<>();
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		//name
		if (meta.hasDisplayName())
		{
			this.display = meta.getDisplayName();
		}
		else
		{
			this.display = null;
		}
		
		//lore
		if (meta.hasLore())
		{
			List<String> lore = meta.getLore();
			
			// stats
			List<String> stats = new ArrayList<>();
			for (Zstat stat: Zstat.values())
			{
				if (plugin.a_utils.isItemHasStat(lore, stat)!=-1)
				{	
					double value = plugin.a_utils.getStat(item, stat);
					int scale = stat.isPercent() ? 100 : 1;
					if (value != 0);
					{
						stats.add(stat.getID()+":"+value*scale);
					}
					lore.remove(plugin.a_utils.isItemHasStat(lore, stat));
				}
			}
			this.stats = stats;
			
			// buffs
			List<String> buffs = new ArrayList<>();
			for (Zbuff buff: Zbuff.values())
			{
				if (plugin.a_utils.hasBuff(lore, buff)!=-1)
				{
					int level = plugin.a_utils.getBuffLevel(item, buff);
					if (level != 0)
					{
						buffs.add(buff.getId()+":"+level);
					}
					lore.remove(plugin.a_utils.hasBuff(lore,buff));
				}
			}
			this.buffs = buffs;
			
			// durability
			if (plugin.a_utils.isItemSetDurability(lore)!=-1)
			{
				this.durability = plugin.a_utils.getDurability(item);
				lore.remove(plugin.a_utils.isItemSetDurability(lore));
			}
			
			// item-type
			if (plugin.a_utils.isItemHasType(lore)!=-1) {
				this.item_type = plugin.a_utils.getType(item);
				lore.remove(plugin.a_utils.isItemHasType(lore));
			}

			// rate
			if (plugin.a_utils.isItemRated(lore)!=-1)
			{
				this.rate = zrate.getRateFromLine(lore.get(plugin.a_utils.isItemRated(lore))).id;
				lore.remove(plugin.a_utils.isItemRated(lore));
			}
			
			// leveling
			if (plugin.a_utils.hasLeveling(lore)!=-1)
			{
				this.leveling = true;
				lore.remove(plugin.a_utils.hasLeveling(lore));
			}
			
			// gems
			int gems = 0;
			while (zgem.isItemHasFilledSlot(lore)!=-1)
			{
				int idx = zgem.isItemHasFilledSlot(lore);
				lore.remove(idx);
				gems++;
			}
			while (zgem.isItemHasSlot(lore, false)!=-1)
			{
				int idx = zgem.isItemHasSlot(lore, false);
				lore.remove(idx);
				gems++;
			}
			while (zgem.isItemHasSlot(lore, true)!=-1)
			{
				int idx = zgem.isItemHasSlot(lore, true);
				lore.remove(idx);
				gems++;
			}
			this.gems = gems+"";
			
			List<String> requirements = new ArrayList<>();
			for (item_require req: a_utils.allRequirement())
			{
				if (a_utils.hasRequire(lore, req)!=-1)
				{
					String value = a_utils.getRequire(item, req);
					requirements.add(req.id+":"+value);
					lore.remove(a_utils.hasRequire(lore, req));
				}
			}
			this.requirements = requirements;
			
			this.description = lore;
		}
		else
		{
			this.item_type = "";
			this.stats = new ArrayList<>();
			this.description = new ArrayList<>();
		}
		
		//data
		if (meta.hasCustomModelData())
		{
			this.data = meta.getCustomModelData();
		}
		else
		{
			this.data = 0;
		}
		
		//enchants
		if (meta.hasEnchants())
		{
			List<String> addEnchant = new ArrayList<>();
			for (Enchantment ench: meta.getEnchants().keySet())
			{
				addEnchant.add(ench.getName()+":"+meta.getEnchantLevel(ench));
			}
			this.enchants = addEnchant;
		}
		else 
		{
			this.enchants = new ArrayList<>();
		}
		
		//flags
		if (meta.getItemFlags()!=null || meta.getItemFlags().size()!=0)
		{
			List<String> addFlag = new ArrayList<>();
			for (ItemFlag fl: meta.getItemFlags())
			{
				addFlag.add(fl.name());
			}
			this.flags = addFlag;
		}
		else
		{
			this.flags = new ArrayList<>();
		}
		
		//unbreakable
		this.unbreak = meta.isUnbreakable();
		
	}

	@Override
	@SuppressWarnings("deprecation")
	public ItemStack convertToItemStack(boolean is_icon) {
		ItemStack result;
		List<String> errors = new ArrayList<>();
		
		// type define and data
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
				if (!this.leveling) continue;
				
				zrate rate = null;
				if (zrate.allRates().contains(this.rate))
					rate = new zrate(this.rate);
				else rate = new zrate(zrate.getDefault());
				
				leveling level_info = new leveling(1, 0, rate.base_exp);
				lore.add(level_info.getLore());
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
			else if (s.equalsIgnoreCase("<requirement>"))
			{
				for (String req_str: this.requirements)
				{
					String key = req_str.substring(0, req_str.indexOf(":"));
					String value = req_str.substring(req_str.indexOf(":")+1);
					
					if (!a_utils.allRequirementID().contains(key))
					{
						errors.add(utils.color("&4+ requirements: &c"+key+"&4 is not a requirement"));
						continue;
					}
					
					item_require req = a_utils.getRequirementFromID(key);
					
					if (!req.validValue(value))
					{
						errors.add(utils.color("&4+ requirements: &c"+value+"&4 is not valid for &c"+key));
						continue;
					}
					
					lore.add(req.display + "&c "+value);
				}
			}
			else if (s.equalsIgnoreCase("<stat>"))
			{
				for (String stat: stats)
				{
					String key = stat.substring(0, stat.indexOf(":"));
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
				if (gems.equalsIgnoreCase("") || gems.equalsIgnoreCase("0")) continue;
				
				if (gems.contains("~"))
				{
					String start_str = gems.substring(0, gems.indexOf("~"));
					String end_str = gems.substring(gems.indexOf("~")+1);
						
					if (!utils.isNumber(start_str))
					{
						errors.add(utils.color("&4+ gems: &c"+start_str+"&4 is not a number"));
						continue;
					}
					if (!utils.isNumber(end_str))
					{
						errors.add(utils.color("&4+gems: &c"+end_str+"&4 is not a number"));
						continue;
					}
						
					int start = Integer.parseInt(start_str);
					int end = Integer.parseInt(end_str);
						
					int slot = utils.randomInt(start, end);
						
					if (is_icon)
					{
						String toAdd = plugin.file_cfg.getSocketIcon();
						lore.add(toAdd+ " "+ gems.replace("~", plugin.file_loc.getSymbol("range")));
					}
					else lore.addAll(generateGems(slot));	
				}
				else
				{
					if (utils.isNumber(gems))
					{
						int slot = Integer.parseInt(gems);	
						if (is_icon)
						{
							String toAdd = plugin.file_cfg.getSocketIcon();
							lore.add(toAdd+ " "+ gems);
						}
						else lore.addAll(generateGems(slot));
					}
					else
					{
						errors.add(utils.color("&4+ gems: &c"+gems+"&4 is not a number"));
						continue;
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
	
	private List<String> generateGems(int slot)
	{
		List<String> result = new ArrayList<>();
		int success = 0;
		double chance = plugin.file_cfg.getPassiveUnlock();
		
		for (int i = 0; i<slot; i++)
		{
			success += utils.roll(chance) ? 1 : 0;
		}
		
		for (int i = 0; i<success; i++)
		{
			result.add(plugin.file_cfg.getEmptySlot());
		}
		
		for (int i = 0; i<slot-success; i++)
		{
			result.add(plugin.file_cfg.getLockedSlot());
		}
		
		return result;
	}

}
