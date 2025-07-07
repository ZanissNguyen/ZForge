package org.zanissnguyen.ZForge.System.rate;

import java.util.ArrayList;
import java.util.List;

import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.configuration.zfile_attributes;
import org.zanissnguyen.ZForge.Utils.utils;

public class zrate {
	
	private static utils utils = ZForge.main().utils;
	private static zfile_attributes file = ZForge.main().file_attr;
	public static String prefix = ZForge.main().file_attr.getString("rate.prefix");
	
	public String id;
	public String display;
	public int max_level;
	public int base_exp;
	public double exp_modifier;
	public double stat_modifier;
	public double enchant_modifier;
	public double buff_modifier;
	
	public static List<String> allRates()
	{
		return file.getOptions(false, "rate.rates");
	}
	
	public static List<String> allDisplay()
	{
		List<String> result = new ArrayList<>();
		for (String id: allRates())
		{
			result.add(utils.color(file.getString("rate.rates."+id+".display")));
		}
		return result;
	}
	
	public static String getDefault()
	{
		List<String> all = allRates();
		if (all.isEmpty()) return "there is no rate in attribute.yml";
		String got = file.getString("rate.default");
		if (got==null) return all.get(0);
		else if (!all.contains(got)) return all.get(0);
		else return got;
	}
	
	public static String justPrefix()
	{
		return utils.color(prefix);
	}
	
	public zrate(String id)
	{
		String path = "rate.rates."+id+".";
		this.id = id;
		this.display = file.getString(path+"display")==null? "" : file.getString(path+"display");
		this.max_level = file.getInt(path+"level_system.max");
		this.base_exp = file.getInt(path+"level_system.base_exp");
		this.exp_modifier = file.getDouble(path+"level_system.exp_modifier");
		this.stat_modifier = file.getDouble(path+"level_system.stat_modifier");
		this.enchant_modifier = file.getDouble(path+"level_system.enchant_modifier");
		this.buff_modifier = file.getDouble(path+"level_system.buff_modifier");
	}
	
	public String getLore()
	{
		return utils.color(prefix + " " + display);
	}
	
	public static zrate getRateFromLine(String line)
	{
		for (String s: allRates())
		{
			zrate clone = new zrate(s);
			if (line.equalsIgnoreCase(clone.getLore())) return clone;
		}
		
		return new zrate(getDefault());
	}
}
