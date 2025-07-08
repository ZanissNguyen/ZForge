package org.zanissnguyen.ZForge.Files.database;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.zfile;
import org.zanissnguyen.ZForge.System.object.zitem_inv;

public class zfile_storage extends zfile
{
	public zfile_storage(ZForge plugin) {
		super(plugin, "database/storage.yml");
		
	}
	
	public ItemStack getRingSlot(Player p, int slot)
	{
		return getItem(p, "ring_"+slot).convertToItemStack(false);
	}

	public void setRingSlot(Player p, int slot, ItemStack item)
	{
		saveItem(p, "ring_"+slot, item);
	}
	
	public ItemStack getBeltSlot(Player p)
	{
		return getItem(p, "belt").convertToItemStack(false);
	}
	
	public void setBeltSlot(Player p, ItemStack item)
	{
		saveItem(p, "belt", item);
	}
	
	public ItemStack getGauntletSlot(Player p)
	{
		return getItem(p, "gauntlet").convertToItemStack(false);
	}
	
	public void setGauntletSlot(Player p, ItemStack item)
	{
		saveItem(p, "gauntlet", item);
	}
//	
	public ItemStack getNecklaceSlot(Player p)
	{
		return getItem(p, "necklace").convertToItemStack(false);
	}
	
	public void setNecklaceSlot(Player p, ItemStack item)
	{
		saveItem(p, "necklace", item);
	}
	
	public ItemStack getArtifactSlot(Player p, int slot)
	{
		return getItem(p, "artifact_"+slot).convertToItemStack(false);
	}

	public void setArtifactSlot(Player p, int slot, ItemStack item)
	{
		saveItem(p, "artifact_"+slot, item);
	}
	
	private zitem_inv getItem(Player p, String slot)
	{
		String type = getType(p, slot);
		String name = getDisplayName(p, slot);
		int data = getData(p, slot);
		List<String> lore = getLore(p, slot);
		List<String> enchants = getEnchants(p, slot);
		List<String> flags = getFlags(p, slot);
		List<String> stats = getStats(p, slot);
		List<String> buffs = getBuffs(p, slot);
		stats.sort(null);
		Boolean unbreakable = getUnbreak(p, slot);
		String item_type = getItemType(p, slot);
		String durability = getDurability(p, slot);
		List<String> gems = getGems(p, slot);
		String rate = getRate(p, slot);
		int level = getLevel(p, slot);
		int exp = getExp(p, slot);
		List<String> requirements = getRequirements(p, slot);
	
		zitem_inv result = new zitem_inv(p.getUniqueId()+"."+slot, type, data, name, lore, item_type, durability, enchants, flags
				, stats, buffs, unbreakable, gems, rate, level, exp, requirements);
		
		return result;
	}
	
	private void saveItem(Player p, String slot, ItemStack item)
	{
		zitem_inv toSave = new zitem_inv(p.getUniqueId()+"."+slot, item);
		
		String path = "storage."+p.getUniqueId()+"."+slot+".<attr>";
		FileConfiguration config = fileConfig;
		
		config.set(path.replace("<attr>", "type"), toSave.type);
		config.set(path.replace("<attr>", "display"), toSave.display);
		config.set(path.replace("<attr>", "data"), toSave.data);
		config.set(path.replace("<attr>", "lore"), toSave.description);
		config.set(path.replace("<attr>", "enchants"), toSave.enchants);
		config.set(path.replace("<attr>", "flags"), toSave.flags);
		config.set(path.replace("<attr>", "unbreakable"), toSave.unbreak);
		config.set(path.replace("<attr>", "stats"), toSave.stats);
		config.set(path.replace("<attr>", "buffs"), toSave.buffs);
		config.set(path.replace("<attr>", "item-type"), toSave.item_type);
		config.set(path.replace("<attr>", "durability"), toSave.durability);
		config.set(path.replace("<attr>", "gems"), toSave.inv_gems);
		config.set(path.replace("<attr>", "rate"), toSave.rate);
		config.set(path.replace("<attr>", "level"), toSave.level);
		config.set(path.replace("<attr>", "exp"), toSave.exp);
		config.set(path.replace("<attr>", "requirements"), toSave.requirements);
		
		this.save();
	}
	
	private String getDisplayName(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".display";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "Not Found";
		return config.getString(path);
	}
	private String getType(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".type";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "AIR";
		return config.getString(path);
	}
	
	private String getDurability(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".durability";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "Not Found";
		return config.getString(path);
	}
	
	private String getItemType(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".item-type";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "";
		return config.getString(path);
	}
	
	private String getRate(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".rate";
		FileConfiguration config = fileConfig;
		if (config.getString(path)==null) return "";
		return config.getString(path);
	}
	
	private int getData(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".data";
		return Math.max(0, getInt(path));
	}
	
	private int getLevel(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".level";
		return Math.max(0, getInt(path));
	}
	
	private int getExp(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".exp";
		return Math.max(0, getInt(path));
	}
	
	private List<String> getLore(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".lore";
		return getList(path);
	}
	private List<String> getEnchants(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".enchants";
		return getList(path);
	}
	private List<String> getFlags(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".flags";
		return getList(path);
	}
	private List<String> getStats(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".stats";
		return getList(path);
	}
	private List<String> getBuffs(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".buffs";
		return getList(path);
	}
	private List<String> getRequirements(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".requirements";
		return getList(path);
	}
	private Boolean getUnbreak(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".unbreakable";
		return getBool(path);
	}
	private List<String> getGems(Player p, String slot)
	{
		String path = "storage."+p.getUniqueId()+"."+slot+".gems";
		return getList(path);
	}

}
