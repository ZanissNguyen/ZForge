package org.zanissnguyen.ZForge.System.buff;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.database.zfile_storage;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.Utils.utils;

public class buff_manager extends BukkitRunnable
{
	public static Map<Player, player_buff> player_buffs = new HashMap<>();
	public static ZForge plugin = ZForge.main();
	public static utils utils = ZForge.main().utils;
	public static utils_attribute a_utils = ZForge.main().a_utils;
	private static zfile_storage storage = ZForge.main().file_sto;
	
	int period = 100; // tick
	
	public buff_manager()
	{
		this.runTaskTimer(plugin, 0, period);
	}
	
	private void cast(Player p, Zbuff buff, int level)
	{
		PotionEffectType type = buff.getEffect();
		PotionEffect pot = new PotionEffect(type, (int)(period*2), level-1);
		p.addPotionEffect(pot);
	}

	@Override
	public void run() 
	{
		if (Bukkit.getOnlinePlayers()!=null)
		for (Player p: Bukkit.getOnlinePlayers())
		{
			if (player_buffs.get(p)!=null)
			{
				player_buff pb = player_buffs.get(p);
				if (pb!=null)
				{	
					Map<Zbuff, Integer> buffs = pb.buffs;
					for (Zbuff b: buffs.keySet())
					{
						if (buffs.get(b)!=0) cast(p, b, buffs.get(b));
					}
				}
			}
		}
	}
	
	public static String getLevel(int number)
	{
		if (number>=0 && number <=10)
		{
			String toReturn = "N/A";
			switch(number)
			{
			case 0: toReturn = "-";break;
			case 1: toReturn = "I";break;
			case 2: toReturn = "II";break;
			case 3: toReturn = "III";break;
			case 4: toReturn = "IV";break;
			case 5: toReturn = "V";break;
			case 6: toReturn = "VI";break;
			case 7: toReturn = "VII";break;
			case 8: toReturn = "VIII";break;
			case 9: toReturn = "IX";break;
			case 10: toReturn = "X";break;
			}
			return toReturn;
		}
		else return number+"";
	}
	
	public static byte getLevel(String str)
	{
		byte toReturn = 0;
		if (str.equalsIgnoreCase("I")) toReturn = 1;
		else if (str.equalsIgnoreCase("II")) toReturn = 2;
		else if (str.equalsIgnoreCase("III")) toReturn = 3;
		else if (str.equalsIgnoreCase("IV")) toReturn = 4;
		else if (str.equalsIgnoreCase("V")) toReturn = 5;
		else if (str.equalsIgnoreCase("VI")) toReturn = 6;
		else if (str.equalsIgnoreCase("VII")) toReturn = 7;
		else if (str.equalsIgnoreCase("VIII")) toReturn = 8;
		else if (str.equalsIgnoreCase("IX")) toReturn = 9;
		else if (str.equalsIgnoreCase("X")) toReturn = 10;
		else return Byte.parseByte(str);
		return toReturn;
	}
	
	public static void updateBuffs(Player p)
	{
		player_buffs.remove(p);
		player_buff pb = new player_buff();
		boolean oB = plugin.file_cfg.offhandBuffEnable();
		boolean Bs = plugin.file_cfg.buffStackableEnable();
		
		
		
		for (Zbuff buff: Zbuff.values())
		{
			Vector<Integer> buff_level = new Vector<>();
			int finalValue = 0;
			buff_level.add(checkHelmet(p, buff));
			buff_level.add(checkChestplate(p, buff));
			buff_level.add(checkLeggings(p, buff));
			buff_level.add(checkBoots(p, buff));
			buff_level.add(checkItemInHand(p, buff));
			if (oB) buff_level.add(checkItemInOffHand(p, buff));
			for (int i = 1; i<=5; i++) buff_level.add(checkRing(p, i , buff));
			for (int i = 1; i<=2; i++) buff_level.add(checkArtifact(p, i, buff));
			buff_level.add(checkBelt(p, buff));
			buff_level.add(checkGauntlet(p, buff));
			buff_level.add(checkNecklace(p, buff));
			
			int temp_max = buff_level.get(0);
			for (int i: buff_level)
			{
				if (Bs)
				{
					finalValue += i;
				}
				else
				{
					finalValue = Math.max(i, temp_max);
					temp_max = finalValue;
				}
			}
			pb.buffs.put(buff, Math.min(255, finalValue));
		}
		player_buffs.put(p, pb);
	}
	
	public static int checkItemInHand(Player p, Zbuff buff)
	{
		ItemStack item = p.getInventory().getItemInMainHand();
		if (a_utils.canUse(p, item) && !utils.isVanillaArmor(item.getType()))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkItemInOffHand(Player p, Zbuff buff)
	{
		ItemStack item = p.getInventory().getItemInOffHand();
		if (a_utils.canUse(p, item) && !utils.isVanillaArmor(item.getType()))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkHelmet(Player p, Zbuff buff)
	{
		ItemStack item = p.getInventory().getHelmet();
		if (a_utils.canUse(p, item))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkChestplate(Player p, Zbuff buff)
	{
		ItemStack item = p.getInventory().getChestplate();
		if (a_utils.canUse(p, item))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkLeggings(Player p, Zbuff buff)
	{
		ItemStack item = p.getInventory().getLeggings();
		if (a_utils.canUse(p, item))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkBoots(Player p, Zbuff buff)
	{
		ItemStack item = p.getInventory().getBoots();
		if (a_utils.canUse(p, item))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkRing(Player p, int slot, Zbuff buff)
	{
		ItemStack item = storage.getRingSlot(p, slot);
		if (a_utils.canUse(p, item) && !utils.isVanillaArmor(item.getType()))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkArtifact(Player p, int slot, Zbuff buff)
	{
		ItemStack item = storage.getArtifactSlot(p, slot);
		if (a_utils.canUse(p, item) && !utils.isVanillaArmor(item.getType()))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkBelt(Player p, Zbuff buff)
	{
		ItemStack item = storage.getBeltSlot(p);
		if (a_utils.canUse(p, item) && !utils.isVanillaArmor(item.getType()))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkGauntlet(Player p, Zbuff buff)
	{
		ItemStack item = storage.getGauntletSlot(p);
		if (a_utils.canUse(p, item) && !utils.isVanillaArmor(item.getType()))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
	
	public static int checkNecklace(Player p, Zbuff buff)
	{
		ItemStack item = storage.getNecklaceSlot(p);
		if (a_utils.canUse(p, item) && !utils.isVanillaArmor(item.getType()))
			return a_utils.getBuffLevel(item, buff);
		else return 0;
	}
}
