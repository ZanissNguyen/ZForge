package org.zanissnguyen.ZForge.Files.configuration;

import java.util.List;

import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;

public class zfile_format extends zfile {

	public zfile_format(ZForge plugin) {
		super(plugin, "configuration/format.yml");
	}
	
	public List<String> itemFormat()
	{
		return getList("format.item.general");
	}
	
	public List<String> materialFormat()
	{
		List<String> got = itemFormat();
		got.remove("<durability>");
		got.remove("<leveling>");
		got.remove("<buff>");
		got.remove("<stat>");
		return ZForge.main().utils.standardListString(got);
	}
	
	public List<String> gemFormat()
	{
		return getList("format.gem.general");
	}
	
	public List<String> recipeFormat()
	{
		return getList("format.recipe.general");
	}
	
	public List<String> recipeRequirement()
	{
		return getList("format.recipe.requirement");
	}
	
	public String recipeMaterial()
	{
		return getString("format.recipe.material");
	}
	
	public List<String> repairFormat()
	{
		return getList("format.repair-requirement");
	}
	
	public String durabilityFormat()
	{
		return getString("format.durability");
	}
	
	public String levelingFormat()
	{
		return getString("format.leveling");
	}
	
	public String statFormat()
	{
		return getString("format.stat");
	}
	
	public String buffFormat()
	{
		return getString("format.buff");
	}
	
	public String typeFormat()
	{
		return getString("format.type");
	}

	public List<String> inventoryFormat(String str)
	{
		return getList("format.stat-inventory."+str);
	}    
}
