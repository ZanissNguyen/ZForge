package org.zanissnguyen.ZForge.Files.database;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;
import org.zanissnguyen.ZForge.System.object.zitem;

public class zfile_items extends zfile
{
	public static List<String> allItems = new ArrayList<>();
	
	public zfile_items(ZForge plugin)
	{
		super(plugin, "database/items.yml");
		getItemList();
	}
	
	public String genarteID()
	{
		String prefix = "auto_generate_";
		String result = prefix;
		int number = 0;
		while (isIDExist(result))
		{
			result = prefix + number++;
		}

		return result;
	}
	
	public Boolean isIDExist(String id)
	{
		return (allItems.contains(id));
	}
	
	public void getItemList()
	{
		allItems = getOptions(true, "items");
	}
	
	public List<String> searchFor(String keyword)
	{
		if (keyword.equalsIgnoreCase("")) return allItems;
		
		List<String> result = new ArrayList<>();
		
		for (String s: allItems)
		{
			if (s.contains(keyword)) result.add(s);
		}
		
		return result;
	}
	
	public Boolean isItem(String id)
	{
		if (id.equalsIgnoreCase("")) return false;
		
		for (String s: allItems)
		{
			if (s.equalsIgnoreCase(id)) return true;
		}
		return false;
	}
	
	public zitem getItem(String id)
	{
		String type = getType(id);
		String name = getDisplayName(id);
		int data = getData(id);
		List<String> lore = getLore(id);
		List<String> enchants = getEnchants(id);
		List<String> flags = getFlags(id);
		List<String> stats = getStats(id);
		List<String> buffs = getBuffs(id);
		stats.sort(null);
		Boolean unbreakable = getUnbreak(id);
		String item_type = getItemType(id);
		String durability = getDurability(id);
		String gems = getGems(id);
		String rate = getRate(id);
		boolean leveling = getLeveling(id);
		List<String> requirements = getRequirements(id);
	
		zitem result = new zitem(id, type, data, name, lore, item_type, durability, enchants, flags, stats, buffs, unbreakable, gems,
				rate, leveling, requirements);
		
		return result;
	}
	
	public void saveItem(String id, ItemStack item)
	{
		zitem toSave = new zitem(id, item);
		
		String path = "items."+id+".<attr>";
		FileConfiguration config = fileConfig;
		
		config.set(path.replace("<attr>", "type"), toSave.type);
		config.set(path.replace("<attr>", "display"), toSave.display);
		config.set(path.replace("<attr>", "data"), toSave.data);
		config.set(path.replace("<attr>", "lore"), toSave.description);
		config.set(path.replace("<attr>", "enchants"), toSave.enchants);
		config.set(path.replace("<attr>", "flags"), toSave.flags);
		config.set(path.replace("<attr>", "unbreakable"), toSave.unbreak);
		config.set(path.replace("<attr>", "stats"), toSave.stats);
		config.set(path.replace("<attr>", "buffs"), toSave.buffs);
		config.set(path.replace("<attr>", "item-type"), toSave.item_type);
		config.set(path.replace("<attr>", "durability"), toSave.durability);
		config.set(path.replace("<attr>", "gems"), toSave.gems);
		config.set(path.replace("<attr>", "rate"), toSave.rate);
		config.set(path.replace("<attr>", "leveling"), toSave.leveling);
		config.set(path.replace("<attr>", "requirements"), toSave.requirements);

		this.save();
		this.getItemList();
	}
	
	private String getDisplayName(String id)
	{
		String path = "items."+id+".display";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "Not Found";
		return config.getString(path);
	}
	private String getType(String id)
	{
		String path = "items."+id+".type";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "Not Found";
		return config.getString(path);
	}
	
	private String getDurability(String id)
	{
		String path = "items."+id+".durability";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "Not Found";
		return config.getString(path);
	}
	
	private String getItemType(String id)
	{
		String path = "items."+id+".item-type";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "";
		return config.getString(path);
	}
	private int getData(String id)
	{
		String path = "items."+id+".data";
		FileConfiguration config = fileConfig;
		return Math.max(0, config.getInt(path));
	}
	private List<String> getLore(String id)
	{
		String path = "items."+id+".lore";
		FileConfiguration config = fileConfig;
		if (config.getStringList(path)==null) return new ArrayList<>();;
		return config.getStringList(path);
	}
	private List<String> getEnchants(String id)
	{
		String path = "items."+id+".enchants";
		FileConfiguration config = fileConfig;
		if (config.getStringList(path)==null) return new ArrayList<>();;
		return config.getStringList(path);
	}
	private List<String> getFlags(String id)
	{
		String path = "items."+id+".flags";
		FileConfiguration config = fileConfig;
		if (config.getStringList(path)==null) return new ArrayList<>();;
		return config.getStringList(path);
	}
	private List<String> getStats(String id)
	{
		String path = "items."+id+".stats";
		FileConfiguration config = fileConfig;
		if (config.getStringList(path)==null) return new ArrayList<>();
		return config.getStringList(path);
	}
	private List<String> getBuffs(String id)
	{
		String path = "items."+id+".buffs";
		FileConfiguration config = fileConfig;
		if (config.getStringList(path)==null) return new ArrayList<>();
		return config.getStringList(path);
	}
	private List<String> getRequirements(String id)
	{
		String path = "items."+id+".requirements";
		FileConfiguration config = fileConfig;
		if (config.getStringList(path)==null) return new ArrayList<>();
		return config.getStringList(path);
	}
	private Boolean getUnbreak(String id)
	{
		String path = "items."+id+".unbreakable";
		FileConfiguration config = fileConfig;
		return config.getBoolean(path);
	}
	private Boolean getLeveling(String id)
	{
		String path = "items."+id+".leveling";
		FileConfiguration config = fileConfig;
		return config.getBoolean(path);
	}
	private String getRate(String id)
	{
		String path = "items."+id+".rate";
		FileConfiguration config = fileConfig;
		return config.getString(path);
	}
	private String getGems(String id)
	{
		String path = "items."+id+".gems";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "";
		return config.getString(path);
	}
}
