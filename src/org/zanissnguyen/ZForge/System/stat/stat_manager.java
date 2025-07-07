package org.zanissnguyen.ZForge.System.stat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.database.zfile_storage;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.Utils.utils;

public class stat_manager 
{
	public static Map<Player, player_stat> player_stats = new HashMap<>();
	public static ZForge plugin = ZForge.main();
	public static utils utils = ZForge.main().utils;
	public static utils_attribute a_utils = ZForge.main().a_utils;
	private static zfile_storage storage = ZForge.main().file_sto;
	
	@SuppressWarnings("deprecation")
	private static void pushStatIntoStorage(Player p, player_stat ps)
	{
		p.setMaxHealth(ps.stats.get(Zstat.HEALTH));
		p.setWalkSpeed((float)(Zstat.SPEED.getDefault() 
				+ Zstat.SPEED.getDefault()*ps.stats.get(Zstat.SPEED)));
		
		player_stats.put(p, ps);
	}
	
	public static double getScale(ItemStack item, Player p, String slot, boolean init)
	{
		if (item==null || item.getType()==Material.AIR) return 1;
		
		boolean unequipArmor = plugin.file_cfg.unequipArmorEnable();
		boolean unequipAccessory = plugin.file_cfg.unequipAccessoryEnable();
		double broke_modi = plugin.file_cfg.getBrokenItemModifier();
		double off_modi = plugin.file_cfg.getOffhandModifier();
		
		String type = a_utils.getType(item);
		Material mat = item.getType();
		double scale = 1;
		
		switch (slot)
		{
		case "mainhand":
			if (!unequipArmor && (a_utils.isArmorTypes(type) || utils.isVanillaArmor(mat))
					|| (!unequipAccessory && a_utils.isAccessoryTypes(type))) scale *= 0;
			
			scale *= a_utils.isBroke(item, p, slot, init) ? broke_modi : 1.0;
			break;
		case "offhand":
			if (!unequipArmor && (a_utils.isArmorTypes(type) || utils.isVanillaArmor(mat))
					|| (!unequipAccessory && a_utils.isAccessoryTypes(type))) scale *= 0;
			if (!a_utils.isOffHandTypes(type)) scale *= off_modi;
			
			scale *= a_utils.isBroke(item, p, slot, init) ? broke_modi : 1.0;
			break;
		case "ring":
		case "artifact":
		case "belt":
		case "gauntlet":
		case "necklace":
		case "helmet":
		case "chesplate":
		case "leggings":
		case "boots":
		default:
			scale *= a_utils.isBroke(item, p, slot, init) ? broke_modi : 1.0;
			break;
		}
		
		return scale;
	}
	
	public static void removeStat(ItemStack item, player_stat ps, double scale)
	{
		if (item==null || item.getType()==Material.AIR) return;
		
		for (Zstat stat: Zstat.values())
		{
			double get_value = a_utils.getStat(item, stat) * scale; // TODO getStat method
			
			ps.stats.put(stat, ps.stats.get(stat)-get_value);
			
		}
	}
	
	public static void increaseStat(ItemStack item, player_stat ps, double scale)
	{
		if (item==null || item.getType()==Material.AIR) return;
		
		for (Zstat stat: Zstat.values())
		{
			double get_value = a_utils.getStat(item, stat) * scale; // TODO getStat method
			
			ps.stats.put(stat, ps.stats.get(stat)+get_value);
			
		}
	}
	
	private static void updateSlot(player_stat stats, Player p, String slot, int intSlot, boolean initialize, ItemStack in_item, boolean isbreak)
	{
//		InventoryStorage storage = SupperForge.getInstance().storage; // TODO: custom inventory
		PlayerInventory inv = p.getInventory();
		
		if (stats == null) stats = new player_stat();
		
		ItemStack oldItem = null;
		ItemStack newItem = initialize ? null : in_item;
		double oldScale = 1;
		double newScale = 1;
		
		// get Old Item
		switch (slot)
		{
		case "ring":
			oldItem = storage.getRingSlot(p, intSlot);
			break;
		case "artifact":
			oldItem = storage.getArtifactSlot(p, intSlot);
			break;
		case "belt":
			oldItem = storage.getBeltSlot(p);
			break;
		case "gauntlet":
			oldItem = storage.getGauntletSlot(p);
			break;
		case "necklace":
			oldItem = storage.getNecklaceSlot(p);
			break;
		case "helmet":
			oldItem = inv.getHelmet();
			break;
		case "chesplate":
			oldItem = inv.getChestplate();
			break;
		case "leggings":
			oldItem = inv.getLeggings();
			break;
		case "boots":
			oldItem = inv.getBoots();
			break;
		case "mainhand":
			oldItem = inv.getItemInMainHand();
			break;
		case "offhand":
			oldItem = inv.getItemInOffHand();
			break;
		default:
			break;
		}
		
		// get scale
		if (!isbreak) oldScale = getScale(oldItem, p, slot, false);
		if (!initialize) newScale = getScale(newItem, p, slot, true);
		
		if (initialize)
		{
			if (oldItem!=null && oldItem.getType()!=Material.AIR && oldScale !=0) increaseStat(oldItem, stats, oldScale);
		}
		else
		{
			// remove old stat in slot 
			if (oldItem!=null && oldItem.getType()!=Material.AIR && oldScale != 0) removeStat(oldItem, stats, oldScale);
			
			// update new Item
			if (newItem!=null && newItem.getType()!=Material.AIR && newScale != 0) increaseStat(newItem, stats, newScale);
			
			switch (slot)
			{
			case "ring":
				storage.setRingSlot(p, intSlot, newItem);
				break;
			case "artifact":
				storage.setArtifactSlot(p, intSlot, newItem);
				break;
			case "belt":
				storage.setBeltSlot(p, newItem);
				break;
			case "gauntlet":
				storage.setGauntletSlot(p, newItem);
				break;
			case "necklace":
				storage.setNecklaceSlot(p, newItem);
				break;
			case "helmet":
				inv.setHelmet(newItem);
				break;
			case "chesplate":
				inv.setChestplate(newItem);
				break;
			case "leggings":
				inv.setLeggings(newItem);
				break;
			case "boots":
				inv.setBoots(newItem);
				break;
			case "mainhand":
				inv.setItemInMainHand(newItem);
				break;
			case "offhand":
				inv.setItemInOffHand(newItem);
				break;
			default:
				break;
			}
		}
		
		pushStatIntoStorage(p, stats);
	}
	
	public static void updateRingSlot(ItemStack newItem, Player p, int i, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "ring", i, false, newItem, isBreak);
	}
	
	public static void updateArtifactSlot(ItemStack newItem, Player p, int i, boolean isBreak)
	{	
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "artifact", i, false, newItem, isBreak);
	}
	
	public static void updateBeltSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "belt", -1, false, newItem, isBreak);
	}
	
	public static void updateGauntletSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "gauntlet", -1, false, newItem, isBreak);
	}
	
	public static void updateNecklaceSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "necklace", -1, false, newItem, isBreak);
	}
	
	public static void updateHelmetSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "helmet", -1, false, newItem, isBreak);
	}
	
	public static void updateChestplateSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "chestplate", -1, false, newItem, isBreak);
	}
	
	public static void updateLeggingsSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "leggings", -1, false, newItem, isBreak);
	}
	
	public static void updateBootsSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "boots", -1, false, newItem, isBreak);
	}
	
	public static void updateOffhandSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "offhand", -1, false, newItem, isBreak);
	}
	
	public static void updateMainhandSlot(ItemStack newItem, Player p, boolean isBreak)
	{
		player_stat current = player_stats.get(p);
		
		updateSlot(current, p, "mainhand", -1, false, newItem, isBreak);
	}
	
	
	public static void updateStat(Player p)
	{
		player_stat stats = player_stats.get(p);
		stats = new player_stat();
		for (int i = 1; i<=5; i++) 
			updateSlot(stats, p, "ring", i, true, null, false);
		for (int i = 1; i<=2; i++) 
			updateSlot(stats, p, "artifact", i, true, null, false);
		updateSlot(stats, p, "belt", -1, true, null, false);
		updateSlot(stats, p, "gauntlet", -1, true, null, false);
		updateSlot(stats, p, "necklace", -1, true, null, false);
		updateSlot(stats, p, "helmet", -1, true, null, false);
		updateSlot(stats, p, "chestplate", -1, true, null, false);
		updateSlot(stats, p, "leggings", -1, true, null, false);
		updateSlot(stats, p, "boots", -1, true, null, false);
		updateSlot(stats, p, "offhand", -1, true, null, false);
		updateSlot(stats, p, "mainhand", -1, true, null, false);
		
		
	}
}
