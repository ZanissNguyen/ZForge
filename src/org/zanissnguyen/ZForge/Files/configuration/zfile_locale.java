package org.zanissnguyen.ZForge.Files.configuration;

import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;

public class zfile_locale extends zfile
{

	public zfile_locale(ZForge plugin) {
		super(plugin, "configuration/locale.yml");
	}
	
	private final String UPDATE_CHECK = "update-checker";
	private final String CATALOGUE = "catalogue";
	private final String SYMBOL = "symbol";
	private final String ERROR = "error";
	private final String ATTRIBUTE = "attribute";
	private final String LORE = "lore";
	private final String NAME = "name";
	private final String ENCH = "ench";
	private final String FLAG = "flag";
	private final String DATA = "data";
	private final String FORGE = "forge";
	
	public final String getUpdateCheck(String msg)
	{
		String path = UPDATE_CHECK+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getSymbol(String msg)
	{
		String path = SYMBOL+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getError(String msg)
	{
		String path = ERROR+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getAttribute(String msg)
	{
		String path = ATTRIBUTE+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getLore(String msg)
	{
		String path = LORE+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getName(String msg)
	{
		String path = NAME+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getEnch(String msg)
	{
		String path = ENCH+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getFlag(String msg)
	{
		String path = FLAG+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getData(String msg)
	{
		String path = DATA+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getForge(String msg)
	{
		String path = FORGE+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getCatalogue(String msg)
	{
		String path = CATALOGUE+"."+msg;
		return utils.color(getString(path));
	}
	
	public final String getConsole()
	{
		String path = "console";
		return utils.color(getString(path));
	}
	
	public final String getReload()
	{
		String path = "reload";
		return utils.color(getString(path));
	}
	

}
