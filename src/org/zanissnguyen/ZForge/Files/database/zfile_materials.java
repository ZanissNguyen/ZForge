package org.zanissnguyen.ZForge.Files.database;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;
import org.zanissnguyen.ZForge.System.object.zmaterial;

public class zfile_materials extends zfile {
	
	public static List<String> allMaterials = new ArrayList<>();
	
	public zfile_materials(ZForge plugin) {
		super(plugin, "database/materials.yml");
		getMaterialList();
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
		return (allMaterials.contains(id));
	}
	
	public void getMaterialList()
	{
		allMaterials = getOptions(true, "materials");
	}
	
	public List<String> searchFor(String keyword)
	{
		if (keyword.equalsIgnoreCase("")) return allMaterials;
		
		List<String> result = new ArrayList<>();
		
		for (String s: allMaterials)
		{
			if (s.startsWith(keyword)) result.add(s);
		}
		
		return result;
	}
	
	public Boolean isSMatereial(String id)
	{
		if (id.equalsIgnoreCase("")) return false;
		
		for (String s: allMaterials)
		{
			if (s.equalsIgnoreCase(id)) return true;
		}
		return false;
	}
	
	public zmaterial getMaterial(String id)
	{
		String type = getType(id);
		String name = getDisplayName(id);
		int data = getData(id);
		List<String> lore = getLore(id);
		Boolean glow = getGlow(id);
		Boolean unbreakable = getUnbreak(id);
	
		zmaterial result = new zmaterial(id, type, data, name, lore, glow, unbreakable);
		
		return result;
	}
	
	public void saveMaterial(String id, ItemStack item)
	{
		zmaterial toSave = new zmaterial(id, item);
		fileConfig.set("materials."+id+".type", toSave.type);
		fileConfig.set("materials."+id+".display", toSave.display);
		fileConfig.set("materials."+id+".data", toSave.data);
		fileConfig.set("materials."+id+".lore", toSave.description);
		fileConfig.set("materials."+id+".glow", toSave.glow);
		fileConfig.set("materials."+id+".unbreakable", toSave.unbreak);
		
		this.save();
		this.getMaterialList();
	}
	
	private String getDisplayName(String id)
	{
		String path = "materials."+id+".display";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "Not Found";
		return config.getString(path);
	}
	private String getType(String id)
	{
		return getString("materials."+id+".type");
	}
	private int getData(String id)
	{
		return Math.max(0, getInt("materials."+id+".data"));
	}
	private List<String> getLore(String id)
	{
		return getList("materials."+id+".lore");
	}
	private Boolean getGlow(String id)
	{
		return getBool("materials."+id+".glow");
	}
	private Boolean getUnbreak(String id)
	{
		return getBool("materials."+id+".unbreakable");
	}
}
