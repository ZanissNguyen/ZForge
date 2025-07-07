package org.zanissnguyen.ZForge.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zanissnguyen.ZForge.ZForge;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class utils implements utilsAPI 
{
	@Override
	public double randomDouble(double start, double end, int digit) {
		// get a random double number
		double range = end - start;
		double result = Math.random()*range+start;
		
		result = fixedDecimal(result, digit);
		
		return result;
	}

	@Override
	public int randomInt(int start, int end) {
		// get a random int
		return (int) Math.round(randomDouble(start, end, 2));
	}
	
	public boolean roll(double chance)
	{
		double rnd = Math.random();
		return rnd<=chance;
	}

	@Override
	public double fixedDecimal(double number, int amount) {
		// round to specific digit
		int temp = 1;
		for (int i = 0; i<amount; i++)
		{
			temp*=10;
		}
		
		double result = 0;
		number*=temp;
		int IntRs = (int) Math.ceil(number);
		result = IntRs/(double) temp;
		
		return result;
	}

	@Override
	public String standardString(String str) {
		// make a standard string:
		// input: 'iron_sword'
		// output: 'Iron Sword'
		str = str.toLowerCase();
		String result = "";
		
		boolean startWord = true;
		for (int i = 0 ;i<str.length(); i++)
		{
			if (str.charAt(i)=='_')
			{
				result+=" ";
				startWord = true;
			}
			else if (startWord)
			{
				result+=(char)(str.charAt(i)-32);
				startWord=false;
			}
			else result+=str.charAt(i);
		}
		return result;
	}
	
	@Override
	public List<String> standardListString(List<String> list)
	{
		// there is no two continuous empty line
		// there is no empty line at end
		List<String> result = new ArrayList<>();
		String last = "";
		for (String s: list)
		{
			if (!(s.equalsIgnoreCase("") && last.equalsIgnoreCase("")))
			{
				result.add(s);
				last = s;
			}
		}
		int last_idx = result.size()-1;
		if (result.get(last_idx).equalsIgnoreCase("")) result.remove(last_idx);
		return result;
	}

	@Override
	public String color(String s) {
		// translate & to color code
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	@Override
	public Boolean isMaterial(String str) {
		// check if a string is material
		for (Material mat: Material.values())
		{
			if (mat.toString().equalsIgnoreCase(str)) return true;
		}
		
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Boolean isEnchantment(String str) {
		// check if a string is enchantment
		for (Enchantment ench : Enchantment.values())
		{
			if (ench.getName().equalsIgnoreCase(str)) return true;
		}
			
		return false;
	}

	@Override
	public Boolean isFlag(String str) {
		// check if a string is flag
		for (ItemFlag fl : ItemFlag.values())
		{
			if (fl.name().equalsIgnoreCase(str)) return true;
		}
		return false;
	}

	@Override
	public Boolean isNumber(String str) {
		// check if a string is number
		try 
		{
			Double.parseDouble(str);
	        return true;
	    } 
		catch (NumberFormatException e) 
		{
	    	return false;
	    }
	}

	@Override
	public boolean isVanillaArmor(Material type) 
	{
		// check if a material is armor (chestplate, helmet, leggings, boots...)
		String str = type.toString();
		if (str.contains("CHESTPLATE")
				|| str.contains("HELMET")
				|| str.contains("LEGGINGS")
				|| str.contains("BOOTS")
				|| str.contains("_SKULL")
				|| str.contains("_HEAD")
				|| str.contains("ELYTRA")) return true;
		return false;
	}

	@Override
	public boolean isVanillaRange(Material type) {
		// check if a material is bow or crossbow (how about bowl???)
		String str = type.toString();
		if (str.contains("BOW")
				|| str.contains("CROSSBOW")) return true;
		return false;
	}

	@Override
	public ItemStack createItem(Material type, int amount) 
	{
		return new ItemStack(type, amount);
	}

	@Override
	public ItemStack createItem(Material type, int amount, int model_data) {
		ItemStack item = createItem(type, amount);
		ItemMeta meta = item.getItemMeta();
		
		if (model_data!=0) meta.setCustomModelData(model_data);
		item.setItemMeta(meta);
		
		return item;
	}

	@Override
	public ItemStack createItem(Material type, int amount, int model_data, String name, String... lore) {
		ItemStack item = createItem(type, amount, model_data);

		ItemMeta meta = item.getItemMeta();
		if (name!=null) meta.setDisplayName(color(name));
		List<String> loreItem = new ArrayList<>();
		if (lore!=null) for (String s : lore)
		{
			loreItem.add(color(s));
		}
		meta.setLore(loreItem);
		item.setItemMeta(meta);
		
		return item;
	}

	@Override
	public ItemStack createItem(Material type, int amount, int model_data, Boolean unbreak, String name,
			String... lore) {
		ItemStack item = createItem(type, amount, model_data, name, lore);
//		if (item.hasItemMeta()) 
//		{
			ItemMeta meta = item.getItemMeta();
			
			meta.setUnbreakable(unbreak);
			item.setItemMeta(meta);
//		}
		
		return item;
	}

	@Override
	public ItemStack setName(ItemStack item, String name) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		meta.setDisplayName(color(name));
		
		result.setItemMeta(meta);
		return result;
	}

	@Override
	public ItemStack clearName(ItemStack item) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		meta.setDisplayName(null);
		
		result.setItemMeta(meta);
		return result;
	}

	@Override
	public String getName(ItemStack item) {
		if (!item.getItemMeta().hasDisplayName()) return "";
		else 
		{
			return item.getItemMeta().getDisplayName();
		}
	}

	@Override
	public ItemStack addLore(ItemStack item, int index, String str) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (meta.hasLore())
		{
			lore = getLore(result);
		}
		
		if (index==-1)
		{
			lore.add(color(str));
		} 
		/*
		 * if index is really in index just add
		 * else we add empty line until reach that index
		 */
		else // insert
		{
			int size = lore.size();
			if (index >=0 && index<size)
			{
				lore.add(index, color(str));
			}
			else
			{
				while (index!=size)
				{
					lore.add("");
					size = lore.size();
				}
				lore.add(index, color(str));	
			}
		}
		meta.setLore(lore);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public ItemStack setLore(ItemStack item, int index, String str) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (meta.hasLore())
		{
			lore = getLore(result);
		}
		
		int size = lore.size();
		if (index >=0 && index<=size)
		{
			lore.set(index, color(str));
		}
		else
		{
			while (index!=size)
			{
				lore.add("");
				size = lore.size();
			}
			lore.add(index, color(str));
			
		}
		
		meta.setLore(lore);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public ItemStack addAllLore(ItemStack item, List<String> list) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (meta.hasLore())
		{
			lore = getLore(result);
		}
		
		for (String s: list) lore.add(color(s));
		
		meta.setLore(lore);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public ItemStack removeLore(ItemStack item, int index) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (meta.hasLore())
		{
			lore = getLore(result);
		}
		
		int size = lore.size();
		if (index >=0 && index<=size)
		{
			lore.remove(index);
		}
		
		meta.setLore(lore);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public ItemStack clearLore(ItemStack item) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		meta.setLore(new ArrayList<>());
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public List<String> getLore(ItemStack item) {
		List<String> result = new ArrayList<>();
		
		if (item == null || item.getType()==Material.AIR) return new ArrayList<>();
		
		if (item.getItemMeta()!=null)
		{
			if (item.getItemMeta().getLore()!=null) 
			{
				result = item.getItemMeta().getLore();
			}
		}
		
		return result;
	}

	@Override
	public ItemStack setModelData(ItemStack item, int data) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		meta.setCustomModelData(data);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public int getModelData(ItemStack item) {
		if (item!=null)
		{
			if (item.getItemMeta()!=null) return item.getItemMeta().getCustomModelData();
			else return 0;
		}
		else return 0;
	}

	@Override
	public ItemStack addEnchant(ItemStack item, Enchantment ench, int level) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		meta.addEnchant(ench, level, true);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public ItemStack removeEnchant(ItemStack item, Enchantment ench) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		meta.removeEnchant(ench);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public ItemStack clearEnchants(ItemStack item) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		for (Enchantment ench: meta.getEnchants().keySet())
		{
			meta.removeEnchant(ench);
		}
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public ItemStack addFlag(ItemStack item, ItemFlag flag) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
			
		meta.addItemFlags(flag);
		result.setItemMeta(meta);
			
		return result;
	}

	@Override
	public ItemStack removeFlag(ItemStack item, ItemFlag flag) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
			
		meta.removeItemFlags(flag);
		result.setItemMeta(meta);
			
		return result;
	}

	@Override
	public ItemStack clearFlags(ItemStack item) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
			
		for (ItemFlag fl: meta.getItemFlags())
		{
			meta.removeItemFlags(fl);
		}
		result.setItemMeta(meta);
			
		return result;
	}

	@Override
	public ItemStack setUnbreak(ItemStack item, Boolean unbreak) {
		ItemStack result = item.clone();
		ItemMeta meta = result.getItemMeta();
		
		meta.setUnbreakable(unbreak);
		result.setItemMeta(meta);
		
		return result;
	}

	@Override
	public boolean hasPermission(CommandSender p, String permission) 
	{
		// TODO Auto-generated method stub
		if (p.hasPermission(permission)) return true;
		else
		{
			return false;
		}
	}

	@Override
	public void addPermission(Player p, String str) {
		if (!perm.playerHas(p, str)) perm.playerAdd(p, str);
	}

	@Override
	public void removePermission(Player p, String str) {
		if (perm.playerHas(p, str)) perm.playerRemove(p, str);
	}
	
	@Override
	public boolean isPlayerExist(String player) {
		// check if a string is player
		return Bukkit.getPlayer(player)!=null;
	}

	@Override
	public boolean isOnlinePlayer(String player) {
		// check if a string is player
		if (!isPlayerExist(player)) return false;
		
		Player p = Bukkit.getPlayer(player);
		
		if (!Bukkit.getOnlinePlayers().contains(p))
		{
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isHoldItem(Player p) {
		// check if player hold item
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!(item.getType()==Material.AIR || item == null)) return true;
		else
		{
			return false;
		}
	}

	@Override
	public void playSound(Sound s, Player p, float volume, float pitch) {
		// play sound at player location
		p.playSound(p.getLocation(), s, volume, volume);	
	}

	@Override
	public int getAmount(ItemStack item, Player p) 
	{
		// get amount of specific item in the player inventory
		int result = 0;
		for (ItemStack i: p.getInventory().getContents())
		{
			if (i==null) continue;
			
			ItemStack toCheck = i.clone();
			toCheck.setAmount(1);
			if (item.isSimilar(toCheck))
			{
				result+=i.getAmount();
			}
		}
		return result;
	}

	@Override
	public void takeItem(ItemStack item, int amount, Player p) {
		// took amount of specific item from player
		for (ItemStack i: p.getInventory().getContents())
		{
			if (i==null) continue;
			ItemStack inSlot = i.clone();
			inSlot.setAmount(1);
			if (inSlot.isSimilar(item))
			{
				if (amount-i.getAmount()>0)
				{
					amount -= i.getAmount();
					i.setAmount(0);
				}
				else
				{
					i.setAmount(i.getAmount()-amount);
					amount = 0;
				}
			}
			if (amount ==0) return;
		}
		
	}

	@Override
	public double getLevel(Player p)
	{
		// get current level of player
		double result = 0;
		
		result = p.getLevel() + p.getExp();
		
		return result;
	}
	
	@Override
	public void setLevel(Player p, double level)
	{
		// set current level of player
		int intPart = (int) level;
		float fPart = (float) (level - intPart);
		
		p.setLevel(intPart);
		p.setExp(fPart);
		
		return;
	}
	
	@Override
	public void takeLevel(Player p, double toTake)
	{
		// took amount of level from player
		setLevel(p, (float) getLevel(p)-toTake);
	}
	
	@Override
	public int getExp(Player p)
	{
		// get exp of player
		return p.getTotalExperience();
	}
	
	@Override
	public void setExp(Player p, int exp)
	{
		// set exp of player
		p.setTotalExperience(exp);
	}
	
	@Override
	public void takeExp(Player p, int toTake)
	{
		// took amount of exp from player
		setExp(p, getExp(p)-toTake);
	}

	@Override
	public void takeMoney(Player p, double amount) {
		ZForge.main().getEconomy().withdrawPlayer(p, amount);
		
	}

	@Override
	public void sendMessage(CommandSender p, String msg) 
	{
		p.sendMessage(color(msg));
		
	}

	private Economy eco = ZForge.main().getEconomy();
	private Permission perm = ZForge.main().getPermissions();
	
	@Override
	public void setMoney(Player p, double amount) {
		eco.withdrawPlayer(p, getMoney(p));
		eco.withdrawPlayer(p, -amount);
		
	}

	@Override
	public void giveMoney(Player p, double amount) {
		eco.withdrawPlayer(p, -amount);
		
	}

	@Override
	public void subtractMoney(Player p, double amount) {
		eco.withdrawPlayer(p, amount);
	}

	@Override
	public Boolean hasMoney(Player p, double amount) {
		return (eco.getBalance(p)>=amount);
	}

	@Override
	public double getMoney(Player p) {
		return eco.getBalance(p);
	}
	
}
