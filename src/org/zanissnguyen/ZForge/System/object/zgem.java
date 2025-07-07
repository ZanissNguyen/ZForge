package org.zanissnguyen.ZForge.System.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.configuration.zfile_config;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.Utils.utils;

public class zgem extends zobject
{
	private static ZForge plugin = ZForge.main();
	private static utils utils = ZForge.main().utils;
	private static zfile_config cfg = ZForge.main().file_cfg;
//	private static utils_attribute a_utils = ZForge.main().a_utils;
	
	public String type;
	public int data;
	public String display;
	public String stat;
	public String chance;
	public boolean glow;
	public boolean can_buy;
	public double cost;
	
	public zgem(String id, List<String> description, String type, int data, String display,
			String stat, String chance, boolean glow, boolean can_buy, double cost) {
		super(id, description);
		this.type = type;
		this.data = data;
		this.display = display;
		this.stat = stat;
		this.chance = chance;
		this.glow = glow;
		this.can_buy = can_buy;
		this.cost = cost;
	}
	
	public static int getChanceLine(List<String> lore)
	{	
		String prefix = plugin.file_form.getString("format", "gem", "chance_prefix");
		for (String s: lore)
		{
			if (s.startsWith(prefix))
			{
				return lore.indexOf(s);
			}
		}
		
		return -1;
		
	}
	
	public static double getChance(List<String> lore)
	{
		double result = 0;
		
		String prefix = plugin.file_form.getString("format", "gem", "chance_prefix");
		for (String s: lore)
		{
			if (s.startsWith(prefix))
			{
				String value_str = s.substring(s.lastIndexOf(" ")+1, s.lastIndexOf("%"));
				return Double.parseDouble(value_str);
			}
		}
		
		return result;
		
	}
	
	public static String getGemString(String id, double value, boolean in_file)
	{
		String name = utils.getName(plugin.file_gem.getGem(id).convertToItemStack(true));
		Zstat gem_stat = plugin.file_gem.getStatByGem(id);

		if (in_file) return id+":"+value;
		else 
			return name + utils.color(" &8(+"+value+ (gem_stat.isPercent()? "%": "")+")");
	}
	
	public static String getGemString(ItemStack gem, boolean in_file)
	{
		String name = utils.getName(gem);
		String id = plugin.file_gem.getGemByName(name);
		Zstat gem_stat = plugin.file_gem.getStatByGem(id);
		
		double stat_value = plugin.a_utils.getStat(gem, gem_stat);
		
		if (in_file) return id+":"+stat_value;
		else 
			return name + utils.color(" &8(+"+stat_value+ (gem_stat.isPercent()? "%": "")+")");
		
	}
	
	public static int isItemHasFilledSlot(List<String> lore)
	{
		if (lore.isEmpty()) return -1;
		
		for (String s: lore)
		{
			for (String gem_display: plugin.file_gem.getGemDisplayList())
				if (s.startsWith(gem_display))
					return lore.indexOf(s);
		}
		return -1;
	}
	
	public static int isItemHasFilledSlot(ItemStack item)
	{
		if (item==null) return -1;
		if (item.getType()==Material.AIR) return -1;
		
		List<String> lore = utils.getLore(item);
		if (lore.isEmpty()) return -1;
		
		for (String s: lore)
		{
			for (String gem_display: plugin.file_gem.getGemDisplayList())
				if (s.startsWith(gem_display))
					return lore.indexOf(s);
		}
		return -1;
	}
	
	public static Zstat getStatFromGemString(String line)
	{
		// line : name + "&a(+"+stat_value+ (gem_stat.isPercent()? "%": "")+")";
		String gem_name = line.substring(0, line.lastIndexOf(" "));
		String gem_id = plugin.file_gem.getGemByName(gem_name);
		return plugin.file_gem.getStatByGem(gem_id);
	}
	
	public static double getStatValueFromGemString(String line)
	{
		// line : name + "&a(+"+stat_value+ (gem_stat.isPercent()? "%": "")+")";
		Zstat stat = getStatFromGemString(line);
		String str_value = line.substring(line.lastIndexOf("+")+1, 
				stat.isPercent() ? line.lastIndexOf("%") : line.lastIndexOf(")"));
		
		return Double.parseDouble(str_value);
	}
	
	public static int isItemHasSlot(List<String> lore, boolean locked)
	{		
		String toCheck = locked? plugin.file_cfg.getLockedSlot(): plugin.file_cfg.getEmptySlot();
		if (lore.isEmpty()) return -1;
		
		for (String s: lore)
		{
			if (s.startsWith(toCheck))
				return lore.indexOf(s);
		}
		return -1;
	}
	
	public static int isItemHasSlot(ItemStack item, boolean locked)
	{
		if (item==null) return -1;
		if (item.getType()==Material.AIR) return -1;
		
		String toCheck = locked? plugin.file_cfg.getLockedSlot(): plugin.file_cfg.getEmptySlot();
		List<String> lore = utils.getLore(item);
		if (lore.isEmpty()) return -1;
		
		for (String s: lore)
		{
			if (s.startsWith(toCheck))
				return lore.indexOf(s);
		}
		return -1;
	}

	@Override
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
		
		if (this.glow)
		{
			result = utils.addEnchant(result, Enchantment.LUCK, 1);
			result = utils.addFlag(result, ItemFlag.HIDE_ENCHANTS);
		}
		
		// lore 
		List<String> lore = new ArrayList<>();
		List<String> format = plugin.file_form.gemFormat();
		//type
		
		for (String s: format)
		{
			if (s.equalsIgnoreCase("<stat>"))
			{
//				System.out.println(stat);
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
			else if (s.equalsIgnoreCase("<chance>"))
			{
				if (chance.contains("~") && !is_icon)
				{
					String start_str = chance.substring(0, chance.indexOf("~"));
					String end_str = chance.substring(chance.indexOf("~")+1);
					
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
					
					lore.add(getChanceLore(is_icon, toSetValue, this.chance));
				}
				else if (is_icon)
				{
					lore.add(getChanceLore(is_icon, 0, this.chance));
				} 
				else
				{
					if (!utils.isNumber(this.chance))
					{
						errors.add(utils.color("&4+stats: &c"+this.chance+"&4 is not a number"));
						continue;
					}
					lore.add(getChanceLore(is_icon, Double.parseDouble(this.chance), this.chance));
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
	
	public static String getChanceLore(boolean is_icon, double percent, String chance)
	{
		String chance_prefix = plugin.file_form.getString("format", "gem", "chance_prefix");
		
		if (is_icon)
		{
			String colorCode = "&e";
			return chance_prefix + ":"+colorCode+" " + 
					chance.replace("~", plugin.file_loc.getSymbol("range")) + "%";
		}
		else
		{
			String colorCode;
			if (percent >= 80)
			{
				colorCode = "&a";
			} else if (percent >=50)
			{
				colorCode = "&e";
			} else if (percent >=20)
			{
				colorCode = "&c";
			}
			else colorCode = "&4";
			
			return chance_prefix + ":"+colorCode+" " + utils.fixedDecimal(percent, plugin.file_cfg.getDigit()) + "%";
		}
		
	}
	
	public static ItemStack setChance(ItemStack item, double new_percent)
	{
		ItemStack result = item.clone();
		
		List<String> lore = utils.getLore(result);
		
		String new_chance_lore = getChanceLore(false, new_percent, "");
				
		result = utils.setLore(result, getChanceLine(lore), new_chance_lore);
		
		return result;
	}

	private static ItemStack supportItem(String support, Material mat, int data, boolean is_icon, boolean is_admin, boolean has_chance, double pow_chance)
	{
		String name = cfg.getSocketString(support+".display");
		List<String> lore = cfg.getSocketList(support+".lore");
		double cost = cfg.getSocketDouble(support+".cost");
		
		ItemStack result = utils.createItem(mat, 1, data, name);
		result = utils.addEnchant(result, Enchantment.LUCK, 10);
		result = utils.setUnbreak(result, true);
		result = utils.addFlag(result, ItemFlag.HIDE_ATTRIBUTES);
		result = utils.addFlag(result, ItemFlag.HIDE_ENCHANTS);
		result = utils.addFlag(result, ItemFlag.HIDE_UNBREAKABLE);
		
		if (has_chance)
		{
			double chance = support.equalsIgnoreCase("powder") ? pow_chance : cfg.getSocketDouble(support+".success_chance")*100;
			int chance_idx = lore.indexOf("<chance>");
			String chance_str = getChanceLore(true, 0, chance+"");
			lore.set(chance_idx, chance_str);
		}
		
		if (is_icon)
		{
			if (is_admin)
			{
				List<String> toAdd = plugin.file_loc.getList("forge", "list-tips");
				lore.addAll(toAdd);
			}
			else
			{
				List<String> addTips = new ArrayList<>();
				List<String> tips = plugin.file_loc.getList("forge", "recipe", "tips", "blueprint-buy");
				for (String str: tips)
				{
					if (str.contains("<blueprint>")) continue;
					else if (str.contains("<cost>") && plugin.vaultHooked) addTips.add(str.replace("<cost>", cost+"")); 
					else addTips.add(str);
				}
				lore.addAll(addTips);
			}
		}
		
		result = utils.addAllLore(result, lore);
		
		return result;
	}
	
	public static ItemStack remover(boolean is_icon, boolean is_admin)
	{
		return supportItem("remover", Material.STICK, 101, is_icon, is_admin, true, 0);
	}
	
	public static ItemStack driller(boolean is_icon, boolean is_admin)
	{
		return supportItem("driller", Material.BLAZE_ROD, 101, is_icon, is_admin, true, 0);
	}
	
	public static ItemStack smasher(boolean is_icon, boolean is_admin)
	{
		return supportItem("smasher", Material.IRON_AXE, 101, is_icon, is_admin, false, 0);
	}
	
	public static ItemStack powder(double chance)
	{
		return supportItem("powder", Material.SUGAR, 101, false, false, true, chance);
	}
}
