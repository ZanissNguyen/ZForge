package org.zanissnguyen.ZForge.Files.database;

import java.util.ArrayList;
import java.util.List;

import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;
import org.zanissnguyen.ZForge.System.object.zrecipe;

public class zfile_recipes extends zfile
{
	public static List<String> allRecipes = new ArrayList<>();
	
	public zfile_recipes(ZForge plugin)
	{
		super(plugin, "database/recipes.yml");
		getRecipeList();
	}
	
//	public String genarteID()
//	{
//		String prefix = "auto_generate_";
//		String result = prefix;
//		int number = 0;
//		while (isIDExist(result))
//		{
//			result = prefix + number++;
//		}
//
//		return result;
//	}
	
	public Boolean isIDExist(String id)
	{
		return (allRecipes.contains(id));
	}
	
	public void getRecipeList()
	{
		allRecipes = getOptions(true, "recipes");
	}
	
	public List<String> searchFor(String keyword)
	{
		if (keyword.equalsIgnoreCase("")) return allRecipes;
		
		List<String> result = new ArrayList<>();
		
		for (String s: allRecipes)
		{
			if (s.contains(keyword)) result.add(s);
		}
		
		return result;
	}
	
	public Boolean isSRecipe(String id)
	{
		if (id.equalsIgnoreCase("")) return false;
		
		for (String s: allRecipes)
		{
			if (s.equalsIgnoreCase(id)) return true;
		}
		return false;
	}
	
	public zrecipe getRecipe(String id)
	{
		String type = getType(id);
		double level = getLevel(id);
		int time = getTime(id);
		List<String> description = getDescription(id);
		List<String> material = getMaterials(id);
		boolean requireBlueprint = getBlueprintReq(id);
		double cost = getBlueprintCost(id);
		double money = getMoney(id);
		boolean canBuy = getBlueprintCanBuy(id);
	
		zrecipe result = new zrecipe(id, description, type, level, money, time, material, requireBlueprint, canBuy, cost);
		
		return result;
	}
	
	public void saveRecipe(zrecipe recipe)
	{
//		zrecipe toSave = new SItem(id, item);
//		fileConfig.set("recipes."+id+".type", toSave.type);
//		fileConfig.set("recipes."+id+".display", toSave.name);
//		fileConfig.set("recipes."+id+".data", toSave.data);
//		fileConfig.set("recipes."+id+".lore", toSave.lore);
//		fileConfig.set("recipes."+id+".enchants", toSave.enchants);
//		fileConfig.set("recipes."+id+".flags", toSave.flags);
//		fileConfig.set("recipes."+id+".unbreakable", toSave.unbreakable);
//		
//		this.save();
//		this.getSRecipeList();
	}
	
	private String getType(String id)
	{
		if (fileConfig.getString("recipes."+id+".type")==null) return "Not Found";
		return fileConfig.getString("recipes."+id+".type");
	}
	private List<String> getDescription(String id)
	{
		if (fileConfig.getStringList("recipes."+id+".description")==null) return new ArrayList<>();
		return fileConfig.getStringList("recipes."+id+".description");
	}
	private List<String> getMaterials(String id)
	{
		if (fileConfig.getStringList("recipes."+id+".material")==null) return new ArrayList<>();
		return fileConfig.getStringList("recipes."+id+".material");
	}
	private double getMoney(String id)
	{
		return getDouble("recipes."+id+".money");
	}
	private int getTime(String id)
	{
		return getInt("recipes."+id+".time");
	}
	private double getLevel(String id)
	{
		return getDouble("recipes."+id+".level");
	}
	private boolean getBlueprintReq(String id)
	{
		return getBool("recipes."+id+".blueprint.require");
	}
	private boolean getBlueprintCanBuy(String id)
	{
		return getBool("recipes."+id+".blueprint.can-buy");
	}
	private double getBlueprintCost(String id)
	{
		return getDouble("recipes."+id+".blueprint.cost");
	}
}
