package org.zanissnguyen.ZForge.Files.configuration;

import java.util.List;

import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;

public class zfile_attributes extends zfile 
{

	public zfile_attributes(ZForge plugin) 
	{
		super(plugin, "configuration/attributes.yml");
	}
	
	public final List<String> getAllStat()
	{
		return getOptions(false, "stat");
	}
	
	public final List<String> getAllType()
	{
		return getOptions(true, "item-type");
	}
	
	public final List<String> getAllBuff()
	{
		return getOptions(false, "buff");
	}
	
	public final String durabilityDisplay()
	{
		String path = "durability";
		return utils.color(getString(path));
	}
	
	public final String levelDisplay()
	{
		String path = "level";
		return utils.color(getString(path));
	}
	
	public final String getType(String type)
	{
		String path = "item-type."+type;
		return utils.color(getString(path));
	}
	
	public final String getStat(String type)
	{
		String path = "stat."+type;
		return utils.color(getString(path));
	}
	
	public final String getBuff(String type)
	{
		String path = "buff."+type;
		return utils.color(getString(path));
	}
}
