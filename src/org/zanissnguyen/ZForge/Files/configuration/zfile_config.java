package org.zanissnguyen.ZForge.Files.configuration;

import java.util.List;

import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;

public class zfile_config extends zfile
{

	public zfile_config(ZForge plugin) {
		super(plugin, "configuration/config.yml");
	}
	
	private final String socket = "config.socket.";
	
	private Object getConfig(String path)
	{
		return getObject("config."+path);
	}
	
	private Object getCombat(String path)
	{
		return getObject("config.combat."+path);
	}
	
	private Object getGemConfig(String path)
	{
		return getObject("config.socket."+path);
	}
	
	public final int getHologramFPS()
	{
		return (int) getConfig("hologram_fps");
	}
	 
	public final int getHologramDuration()
	{
		return (int) getConfig("hologram_duration");
	}

	public final boolean catalogueEnable()
	{
		return (boolean) getConfig("catalogue");
	}
	
	public final boolean updateCheckEnable()
	{
		return (boolean) getConfig("update_check");
	}
	
	public final boolean vanillaDamageEnable()
	{
		return (boolean) getConfig("vanilla_damage");
	}

	public final int getDigit()
	{
		return (int) getConfig("digit");
	}
	
	public final double getOffhandModifier()
	{
		return (double) getConfig("offhand_modifier");
	}
	
	public final boolean offhandBuffEnable()
	{
		return (boolean) getConfig("offhand_buff");
	}
	
	public final boolean unequipArmorEnable()
	{
		return (boolean) getConfig("unequip_armor");
	}
	
	public final boolean unequipAccessoryEnable()
	{
		return (boolean) getConfig("unequip_accessory");
	}
	
	public final boolean buffStackableEnable()
	{
		return (boolean) getConfig("buff_stackable");
	}
	
	public final boolean itemBreakEnable()
	{
		return (boolean) getConfig("item_break");
	}
	
	public final double getBrokenItemModifier()
	{
		return (double) getConfig("broken_item_modifier");
	}
	
	public final double getRepairCost()
	{
		return (double) getConfig("repair.money");
	}
	
	public final double getRepairLevel()
	{
		return (double) getConfig("repair.level");
	}
	
	public final List<String> getRepairMaterial()
	{
		return getList("config.repair.material");
	}
	
	public final boolean cooldownMessageEnable()
	{
		return (boolean) getCombat("cooldown_msg");
	}
	
	public final boolean passiveSoundEnable()
	{
		return (boolean) getCombat("passive_sound");
	}
	
	public final double getRangeBonus()
	{
		return (double) getCombat("bonus_range_damage");
	}
	
	public final double getBlockPower()
	{
		return (double) getCombat("block_power");
	}
	
	public final double getAbsorpPower()
	{
		return (double) getCombat("absorp_power");
	}
	
	public final double getParryPower()
	{
		return (double) getCombat("parry_power");
	}
	
	public final double getMaxDodge()
	{
		return (double) getCombat("max_dodge");
	}
	
	public final double getMaxResistance()
	{
		return (double) getCombat("max_resistance");
	}
	
	public final boolean multiCriticalEnable()
	{
		return (boolean) getCombat("multiple_critical");
	}
	
	public final double getDefaultCritDamage()
	{
		return (double) getCombat("base_crit_damage");
	}
	
	public final double getVanillaArmorConvert()
	{
		return (double) getCombat("vanilla_armor_convert");
	}
	
	public final double getVanillaToughnessConvert()
	{
		return (double) getCombat("vanilla_armor_toughness_convert");
	}
	
	public final double getVanillaDamageConvert()
	{
		return (double) getCombat("vanilla_damage_convert");
	}
	
	public final double getMobPhysicResistance()
	{
		return (double) getCombat("mob_physic_resistance");
	}
	
	public final double getMobMagicResistance()
	{
		return (double) getCombat("mob_magic_resistance");
	}
	
	public final double getPassiveUnlock()
	{
		return (double) getGemConfig("passive_unlock");
	}

	public final String getSocketIcon()
	{
		return getString(socket+"icon");
	}
	
	public final String getEmptySlot()
	{
		return getString(socket+"empty_slot");
	}
	
	public final String getLockedSlot()
	{
		return getString(socket+"locked_slot");
	}
	
	public final String getSocketString(String str)
	{
		return getString(socket+str);
	}
	
	public final List<String> getSocketList(String str)
	{
		return getList(socket+str);
	}
	
	public final int getSocketInt(String str)
	{
		return getInt(socket+str);
	}
	
	public final double getSocketDouble(String str)
	{
		return getDouble(socket+str);
	}
	
	public final int getDropExp(String str)
	{
		return getInt("config.drop_exp."+str);
	}
}
