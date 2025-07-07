package org.zanissnguyen.ZForge.Files.database;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;
import org.zanissnguyen.ZForge.System.object.zgem;
import org.zanissnguyen.ZForge.System.stat.Zstat;

public class zfile_gems extends zfile {
	
	public static List<String> allGems = new ArrayList<>();
	
	public zfile_gems(ZForge plugin) {
		super(plugin, "database/gems.yml");
		getGemList();
	}
	
	public Boolean isIDExist(String id)
	{
		return (allGems.contains(id));
	}
	
	public void getGemList()
	{
		allGems = getOptions(true, "gems");
	}
	
	public List<String> getGemDisplayList()
	{
		List<String> result = new ArrayList<>();
		for (String id: allGems)
		{
			result.add(utils.color(getDisplayName(id)));
		}
		return result;
	}
	
	public List<String> searchFor(String keyword)
	{
		if (keyword.equalsIgnoreCase("")) return allGems;
		
		List<String> result = new ArrayList<>();
		
		for (String s: allGems)
		{
			if (s.startsWith(keyword)) result.add(s);
		}
		
		return result;
	}
	
	public Boolean isGem(String id)
	{
		if (id.equalsIgnoreCase("")) return false;
		
		for (String s: allGems)
		{
			if (s.equalsIgnoreCase(id)) return true;
		}
		return false;
	}
	
	public String getGemByName(String name)
	{	
		for (String s: allGems)
		{
			if (name.startsWith(utils.color(getDisplayName(s)))) return s;
		}
		return "";
	}
	
	public Zstat getStatByGem(String gem_id)
	{	
		String stats = getStat(gem_id);
		String key = stats.substring(0, stats.indexOf(":"));
			
		return Zstat.getFromID(key);
	}
	
	public zgem getGem(String id)
	{
		String type = getType(id);
		String name = getDisplayName(id);
		int data = getData(id);
		List<String> lore = getLore(id);
		Boolean glow = getGlow(id);
		String chance = getChance(id);
		String stat = getStat(id);
		boolean can_buy = getCanBuy(id);
		double cost = getCost(id);
	
		zgem result = new zgem(id, lore, type, data, name, stat, chance, glow, can_buy, cost);
		
		return result;
	}
	
	public void saveGem(String id, ItemStack item)
	{
//		zgem toSave = new zgem(id, item);
//		fileConfig.set("gems."+id+".type", toSave.type);
//		fileConfig.set("gems."+id+".display", toSave.display);
//		fileConfig.set("gems."+id+".data", toSave.data);
//		fileConfig.set("gems."+id+".lore", toSave.description);
//		fileConfig.set("gems."+id+".glow", toSave.glow);
//		fileConfig.set("gems."+id+".unbreakable", toSave.unbreak);
//		
		this.save();
		this.getGemList();
	}
	
	private String getDisplayName(String id)
	{
		String path = "gems."+id+".display";
		FileConfiguration config = fileConfig;
		String got = config.getString(path);
		return (got==null) ? "Not Found" : got;
	}
	private String getType(String id)
	{
		return getString("gems."+id+".type");
	}
	private String getChance(String id)
	{
		return getString("gems."+id+".chance");
	}
	private String getStat(String id)
	{
		return getString("gems."+id+".stat");
	}
	private int getData(String id)
	{
		return Math.max(0, getInt("gems."+id+".data"));
	}
	private List<String> getLore(String id)
	{
		return getList("gems."+id+".lore");
	}
	private Boolean getGlow(String id)
	{
		return getBool("gems."+id+".glow");
	}
	private Boolean getCanBuy(String id)
	{
		return getBool("gems."+id+".can_buy");
	}
	private double getCost(String id)
	{
		return Math.max(0, getDouble("gems."+id+".cost"));
	}
}
