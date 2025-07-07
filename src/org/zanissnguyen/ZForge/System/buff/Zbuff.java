package org.zanissnguyen.ZForge.System.buff;

import org.bukkit.potion.PotionEffectType;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Utils.utils;

public enum Zbuff 
{
	RESISTANCE("resistance", PotionEffectType.DAMAGE_RESISTANCE),
	FIRE_RESISTANCE("fire_resistance", PotionEffectType.FIRE_RESISTANCE),
	ABSORPTION("absorption", PotionEffectType.ABSORPTION),
	STRENGTH("strength", PotionEffectType.INCREASE_DAMAGE),
	SPEED("speed", PotionEffectType.SPEED),
	NIGHT_VISION("night_vision", PotionEffectType.NIGHT_VISION),
	INVISIBILITY("invisibility", PotionEffectType.INVISIBILITY),
	WATER_BREATHING("water_breathing", PotionEffectType.WATER_BREATHING),
	JUMP_BOOST("jump_boost", PotionEffectType.JUMP),
	GLOWING("glowing", PotionEffectType.GLOWING),
	HEALTH_BOOST("health_boost", PotionEffectType.HEALTH_BOOST),
	SATURATION("saturation", PotionEffectType.SATURATION),
	HASTE("haste", PotionEffectType.FAST_DIGGING),
	REGENERATION("regeneration", PotionEffectType.REGENERATION),
	LUCK("luck", PotionEffectType.LUCK);
	
	private final String id;
	private final PotionEffectType effect;
	
	Zbuff(String id, PotionEffectType effect)
	{
		this.id = id;
		this.effect = effect;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public PotionEffectType getEffect()
	{
		return this.effect;
	}
	
	public static boolean isBuff(String id)
	{
		for (Zbuff b: values())
		{
			if (id.equalsIgnoreCase(b.id)) return true;
		}
		return false;
	}
	
	public static Zbuff getFromID(String id)
	{
		for (Zbuff b: values())
		{
			if (id.equalsIgnoreCase(b.id)) return b;
		}
		return null;
	}
	
	public String getDisplay()
	{
		return ZForge.main().file_attr.getBuff(this.id);
	}
	
	public String getLore()
	{
		ZForge plugin = ZForge.main();
		utils utils = plugin.utils;
		
		String prefix = plugin.file_form.getString("format.buff_prefix");
		String format = plugin.file_form.buffFormat();
		String display = getDisplay();
		return utils.color(format.replace("<prefix>", prefix).replace("<buff>", display));
	}
	
	public String getLore(String level)
	{
		ZForge plugin = ZForge.main();
		utils utils = plugin.utils;
		
		String prefix = plugin.file_form.getString("format.buff_prefix");
		String format = plugin.file_form.buffFormat();

		String display = getDisplay();
		return utils.color(format.replace("<prefix>", prefix).replace("<buff>", display+ " "+level));
	}
}
