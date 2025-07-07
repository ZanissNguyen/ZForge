package org.zanissnguyen.ZForge.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public class zevent implements Listener
{
	public ZForge plugin;
	public utils utils;
	
	public zevent(ZForge plugin, utils utils)
	{
		this.plugin = plugin;
		this.utils = utils;
		
		Bukkit.getServer().getPluginManager().registerEvents(this,  plugin);
	}

}
