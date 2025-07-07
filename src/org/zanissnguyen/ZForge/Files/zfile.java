package org.zanissnguyen.ZForge.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class zfile implements zfileAPI
{
	public File file;
	public FileConfiguration fileConfig;
	public String name;
	
	public ZForge plugin;
	public utils utils;
	
	public zfile(ZForge plugin, String name)
	{	
		this.plugin = plugin;
		this.name = name;
		this.utils = ZForge.main().utils;
		file = new File(plugin.getDataFolder(), "resources/"+name);
		if (!file.exists()) plugin.saveResource("resources/"+name, true);
		fileConfig = YamlConfiguration.loadConfiguration(file);
	}
	
	private String nullHandle(String path)
	{
		return utils.color("&cCan't find "+path+" in "+name+" file!");
	}
	
	@Override
	public void save()
	{
		try 
		{
			fileConfig.save(file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private String findPath(String... path)
	{
		String toGet = "";
		for (String s: path)
		{
			if (s.equalsIgnoreCase(path[0]))toGet = toGet + s;
			else toGet = toGet + "." + s;
		}
		return toGet;
	}
	
	@Override
	public String getString(String... str)
	{
		String toGet = findPath(str);
		String got = fileConfig.getString(toGet);
		if (got==null) return nullHandle(toGet);
		else return this.utils.color(got);
	}
	
	@Override
	public List<String> getList(String... str)
	{
		List<String> result = new ArrayList<>();
		String toGet = findPath(str);
		List<String> got = fileConfig.getStringList(toGet);
		for (String s: got)
		{
			result.add(utils.color(s));
		}
		
		return result;
	}

	@Override
	public int getInt(String... path) {
		String toGet = findPath(path);
		return fileConfig.getInt(toGet);
	}

	@Override
	public double getDouble(String... path) {
		String toGet = findPath(path);
		return fileConfig.getDouble(toGet);
	}

	@Override
	public Boolean getBool(String... path) {
		String toGet = findPath(path);
		return fileConfig.getBoolean(toGet);
	}

	@Override
	public List<String> getOptions(Boolean sorted, String... path) 
	{
		String toGet = findPath(path);
		Set<String> Types = fileConfig.getConfigurationSection(toGet).getKeys(false);
		List<String> result = new ArrayList<>();
		for (String s: Types)
		{
			result.add(s);
		}
		if (sorted) result.sort(null);
		return result;
	}
	
	@Override
	public Object getObject(String... path)
	{
		String toGet = findPath(path);
		Object got = fileConfig.get(toGet);
		if (got==null) return nullHandle(toGet);
		else return got;
	}
}
