package org.zanissnguyen.ZForge;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.zanissnguyen.ZForge.Commands.cmd_zattribute;
import org.zanissnguyen.ZForge.Commands.cmd_zdata;
import org.zanissnguyen.ZForge.Commands.cmd_zench;
import org.zanissnguyen.ZForge.Commands.cmd_zflag;
import org.zanissnguyen.ZForge.Commands.cmd_zforge;
import org.zanissnguyen.ZForge.Commands.cmd_zlore;
import org.zanissnguyen.ZForge.Commands.cmd_zname;
import org.zanissnguyen.ZForge.Files.configuration.zfile_attributes;
import org.zanissnguyen.ZForge.Files.configuration.zfile_config;
import org.zanissnguyen.ZForge.Files.configuration.zfile_format;
import org.zanissnguyen.ZForge.Files.configuration.zfile_locale;
import org.zanissnguyen.ZForge.Files.database.zfile_gems;
import org.zanissnguyen.ZForge.Files.database.zfile_items;
import org.zanissnguyen.ZForge.Files.database.zfile_materials;
import org.zanissnguyen.ZForge.Files.database.zfile_recipes;
import org.zanissnguyen.ZForge.Files.database.zfile_storage;
import org.zanissnguyen.ZForge.Files.database.zfile_styles;
import org.zanissnguyen.ZForge.Listeners.attribute_update_event;
import org.zanissnguyen.ZForge.Listeners.damage_event;
import org.zanissnguyen.ZForge.Listeners.exp_gain_event;
import org.zanissnguyen.ZForge.Listeners.zgem_event;
import org.zanissnguyen.ZForge.Listeners.UX.UX_craft;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.buff.buff_manager;
import org.zanissnguyen.ZForge.UI.UI_repair;
import org.zanissnguyen.ZForge.UI.UI_stat;
import org.zanissnguyen.ZForge.UI.list.UI_gem;
import org.zanissnguyen.ZForge.UI.list.UI_item;
import org.zanissnguyen.ZForge.UI.list.UI_material;
import org.zanissnguyen.ZForge.UI.list.UI_recipe;
import org.zanissnguyen.ZForge.Utils.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class ZForge extends JavaPlugin
{	
	public utils utils;
	public utils_attribute a_utils;
	
	public zfile_attributes file_attr;
	public zfile_locale file_loc;
	public zfile_format file_form;
	public zfile_config file_cfg;
	
	public zfile_materials file_mat;
	public zfile_items file_itm;
	public zfile_recipes file_rep;
	public zfile_styles file_sty;
	public zfile_storage file_sto;
	public zfile_gems file_gem;
	
	public UI_material ui_mat;
	public UI_item ui_itm;
	public UI_recipe ui_rep;
	public UI_repair ui_rpa;
	public UI_gem ui_gem;
	public UI_stat ui_sta;
	
	public boolean vaultHooked;
	
	// get instance
	public static ZForge main()
	{
		return(ZForge) Bukkit.getPluginManager().getPlugin("ZForge");
	}
	
	@Override
    public void onEnable() 
	{
		hookAPI();
		
		initializeUtils();
		
		initializeFiles();
		
		initializeUIUX();
		
		initializeCommands();
		
		initializeRunner();
		
//		UpdateChecker();
//		System.out.println(file_cfg.catalogueEnable());
		if (file_cfg.catalogueEnable()) catalogue(true);
    }
	
    @Override
    public void onDisable() {
    	Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
    	if (file_cfg.catalogueEnable()) catalogue(false);
    	
    }
    
    public void reload()
	{
		this.getPluginLoader().disablePlugin(this);
		this.getPluginLoader().enablePlugin(this);
	}
    
    private void initializeCommands()
    {
    	new cmd_zlore(this, this.utils);
    	new cmd_zname(this, this.utils);
    	new cmd_zench(this, this.utils);
    	new cmd_zflag(this, this.utils);
    	new cmd_zdata(this, this.utils);
    	new cmd_zforge(this, this.utils);
    	new cmd_zattribute(this, this.utils);
    }
    
    private void initializeFiles()
    {
    	file_cfg = new zfile_config(this);
    	file_attr = new zfile_attributes(this);
    	file_loc = new zfile_locale(this);
    	file_form = new zfile_format(this);
    	
    	file_mat = new zfile_materials(this);
    	file_itm = new zfile_items(this);
    	file_rep = new zfile_recipes(this);
    	file_sty = new zfile_styles(this);
    	file_gem = new zfile_gems(this);
    	file_sto = new zfile_storage(this);
    }
    
    private void initializeUtils()
    {
//    	String version = Bukkit.getServer().getVersion();
    	
// 		in compatible method...
//    	if (version.contains("1.14"))
//    	{
//    		
//    	}
//    	else if (version.contains("1.14"))
    	utils = new utils();
    	a_utils = new utils_attribute();
    }

    private void initializeUIUX()
    {
    	ui_mat = new UI_material(this);
    	ui_itm = new UI_item(this);
    	ui_rep = new UI_recipe(this);
    	ui_rpa = new UI_repair(this);
    	ui_gem = new UI_gem(this);
    	ui_sta = new UI_stat(this);
    	
    	new UX_craft(this);
    	new zgem_event(this);
    	new attribute_update_event(this);
    	new damage_event(this);
    	new exp_gain_event(this);
    }
    
    private void initializeRunner()
    {
    	new buff_manager();
    }
    
    private void hookAPI()
    {
    	hookVault();
    }
    
    private void catalogue(boolean status)
    {
		String status_str = status ? file_loc.getCatalogue("status.enable") : file_loc.getCatalogue("status.disable");
		List<String> got = file_loc.getList("catalogue.general");
		for (String s: got)
		{
			if (s.contains("<plugin_name>"))
			{
				s=s.replace("<plugin_name>", this.getName());
			}
			
			if (s.contains("<status>"))
			{
				s=s.replace("<status>", status_str);
			}
			
			if (s.contains("<version>"))
			{
				s=s.replace("<version>", this.getDescription().getVersion());
			}
			
			if (s.contains("<item_amount>"))
			{
				s=s.replace("<item_amount>", zfile_items.allItems.size()+"");
			}
			
			if (s.contains("<material_amount>"))
			{
				s=s.replace("<material_amount>", zfile_materials.allMaterials.size()+"");
			}
			
			if (s.contains("<recipe_amount>"))
			{
				s=s.replace("<recipe_amount>", zfile_recipes.allRecipes.size()+"");
			}
			
			if (s.contains("<style_amount>"))
			{
				s=s.replace("<style_amount>", zfile_styles.allStyle.size()+"");
			}
			
			if (s.contains("<gem_amount"))
			{
				s=s.replace("<gem_amount>", zfile_gems.allGems.size()+"");
			}
			
			Bukkit.getConsoleSender().sendMessage(s);
		}
    }
    
    // VAULT HOOKER
 	private static Economy econ = null;
 	private static Permission perms = null;
 	    
 	public void hookVault()
 	{
 		if (!setupEconomy()) 
 		{
 			this.getLogger().warning("Could not find Vault! This plugin is important. Without this may cause some issue");
 	        return;
 	    }
 		else 
 		{
 			this.getLogger().info("Vault Hooked");
 		}
 	    vaultHooked = this.setupPermissions();
 	}
 	
 	 private boolean setupEconomy() 
 	 {
 		 if (Bukkit.getPluginManager().getPlugin("Vault") == null) 
 		 { 
 			 return false;
 	     }
 	     RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
 	     if (rsp == null) 
 	     {
 	         return false;
 	     }
 	     econ = rsp.getProvider();
 	     return econ != null;
 	    }

 	 private boolean setupPermissions() 
 	 {
 		 RegisteredServiceProvider<Permission> rsp = this.getServer().getServicesManager().getRegistration(Permission.class);
 		 if (rsp==null)
 		 {
 			 return false;
 		 }
 		 perms = rsp.getProvider();
 		 return (perms != null);
 	 }

 	 public Economy getEconomy()
 	 {
 		 return econ;
 	 }

 	 public Permission getPermissions() 
 	 {
 		 return perms;
 	 }
}
