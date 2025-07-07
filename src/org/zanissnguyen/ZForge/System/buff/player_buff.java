package org.zanissnguyen.ZForge.System.buff;

import java.util.HashMap;
import java.util.Map;

public class player_buff {
	public Map<Zbuff, Integer> buffs = new HashMap<>();
	
	public player_buff()
	{
		for (Zbuff buff: Zbuff.values())
		{
			buffs.put(buff, 0);
		}
	}
	
	public player_buff(player_buff copy)
	{
		for (Zbuff buff: Zbuff.values())
		{
			this.buffs.put(buff, copy.buffs.get(buff));
		}
	}

	public String toString()
	{
		String result = "";
		
		for (Zbuff buff: Zbuff.values())
		{
			result += buff.getId() + ":" + buffs.get(buff) +"\n";		
		}
		
		return result;
	}
	
	public String getBuff(Zbuff buff)
	{
		return buff_manager.getLevel(buffs.get(buff));
	}
}
