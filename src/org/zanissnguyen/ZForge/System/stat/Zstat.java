package org.zanissnguyen.ZForge.System.stat;

import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public enum Zstat {
	 PHYSIC_DAMAGE(0, "physic_damage", false, 1),
	 PHYSIC_PENETRATION(1, "physic_penetration", false, 0),
	 ACCURATE(2, "accurate", false, 0),
	 RANGE_BONUS(3, "range_bonus", true, 0),
	 CRIT_CHANCE(4, "crit_chance", true, 0.05),
	 CRIT_DAMAGE(5, "crit_damage", true, 0.3),
	 ATTACK_SPEED(6, "attack_speed", false, 1),
	 MAGIC_DAMAGE(7, "magic_damage", false, 0),
	 MAGIC_PENETRATION(8, "magic_penetration", false, 0),
	 ENHANCE_CHANCE(9, "enhance_chance", true, 0),
	 PHYSIC_DEFENSE(10, "physic_defense", false, 0),
	 MAGIC_DEFENSE(11, "magic_defense", false, 0),
	 BLOCK_CHANCE(12, "block_chance", true, 0),
	 PARRY_CHANCE(13, "parry_chance", true, 0),
	 DODGE_CHANCE(14, "dodge_chance", true, 0),
	 RESISTANCE(15, "resistance", true, 0),
	 ABSORP_CHANCE(16, "absorp_chance", true, 0),
	 SPEED(17, "speed", true, 0.2),
	 HEALTH(18, "health", false, 20),
	 HEAL(19, "heal", true, 0),
	 XP_BOOST(20, "xp_boost", true, 0);
	
	public static final int Zstat_no = 21;
	
	private final int index;
	private final String id;
	private final boolean is_percent;
	private final double default_value;
	
	Zstat(int idx, String id, boolean is_percent, double default_value)
	{
		this.index = idx;
		this.id = id;
		this.is_percent = is_percent;
		this.default_value = default_value;
	}
	
	public int getIdx()
	{
		return this.index;
	}
	public String getID()
	{
		return this.id;
	}
	public boolean isPercent()
	{
		return this.is_percent;
	}
	public double getDefault()
	{
		return this.default_value;
	}
	
	public String getDisplay()
	{
		return ZForge.main().file_attr.getStat(getID());
	}
	
	public String getLore(String value, boolean icon) {
		
		ZForge plugin = ZForge.main();
		utils utils = plugin.utils;
		
		final String positive = utils.color("&a")+"+";
		final String negative = utils.color("&c");
		String format = plugin.file_form.statFormat();
		String percentSymbol = plugin.file_loc.getSymbol("percent");
		
		String loreDisplay = getDisplay();
		String prefix = "";
		String setValue = "";
		if (icon)
		{
			prefix = "&e";
			setValue = value;
		}
		else
		{
			double valueDb = utils.fixedDecimal(Double.parseDouble(value), plugin.file_cfg.getDigit());
			prefix = (valueDb>=0) ? positive : negative;
			setValue = valueDb+"";
		}
		
		if (isPercent()) setValue+=percentSymbol;

		String result = format.replace("<stat>", loreDisplay).replace("<value>", utils.color(prefix+setValue));
				
		return result;
	}
	
	public static boolean isStat(String id)
	{
		for (Zstat stat: values())
		{
			if (id.equalsIgnoreCase(stat.id)) return true;
		}
		return false;
	}
	
	public static Zstat getFromID(String id)
	{
		for (Zstat stat: values())
		{
			if (id.equalsIgnoreCase(stat.id)) return stat;
		}
		return null;
	}
}
