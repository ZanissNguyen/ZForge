package org.zanissnguyen.ZForge.System.stat;

import java.util.HashMap;
import java.util.Map;

import org.zanissnguyen.ZForge.ZForge;

public class player_stat 
{
	
	public Map<Zstat, Double> stats = new HashMap<>();

	public player_stat()
	{
		for (Zstat stat: Zstat.values())
		{
			stats.put(stat, stat.getDefault());
		}
	}
	
	public player_stat(player_stat copy)
	{
		for (Zstat stat: Zstat.values())
		{
			this.stats.put(stat, copy.stats.get(stat));
		}
	}

	public String toString()
	{
		String result = "";
		
		for (Zstat stat: Zstat.values())
		{
			result += stat.getID() + ":" + getStat(stat) +"\n";		
		}
		
		return result;
	}
	
	public double getStat(Zstat stat)
	{
		int digit = stat_manager.plugin.file_cfg.getDigit();
		return ZForge.main().utils.fixedDecimal(stats.get(stat), digit);
	}
}
