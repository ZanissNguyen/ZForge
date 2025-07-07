package org.zanissnguyen.ZForge.System;

import org.zanissnguyen.ZForge.ZForge;

public class leveling 
{
	public int current_level;
	public int current_exp;
	public int require_exp;
	
	public leveling(int level, int exp, int max_exp)
	{
		this.current_level = level;
		this.current_exp = exp;
		this.require_exp = max_exp;
	}
	
	public String getLore()
	{
		String prefix = ZForge.main().file_attr.levelDisplay();
		String format = ZForge.main().file_form.levelingFormat();
		
		format = format.replace("<display>", prefix);
		format = format.replace("<level>", this.current_level+"");
		format = format.replace("<exp>", "["+current_exp+"/"+require_exp+"]");
		
		return ZForge.main().utils.color(format);
	}
	
	public static String justPrefix()
	{
		String prefix = ZForge.main().file_attr.levelDisplay();
		String format = ZForge.main().file_form.levelingFormat();
		
		format = format.replace("<display>", prefix);
		format = format.substring(0, prefix.length()+2);
		
		return ZForge.main().utils.color(format);
	}
}
