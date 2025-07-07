package org.zanissnguyen.ZForge.Utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public interface utilsAPI 
{
	//
	// GENERAL METHOD
	//
	
	// MATHEMATIC
	public double randomDouble(double start, double end, int digit);
	public int randomInt(int start, int end);
	public double fixedDecimal(double number, int amount);
	public String standardString(String str);
	public List<String> standardListString(List<String> list);
	public String color(String s);
	public boolean roll(double chance);
	// CHECK
	public Boolean isMaterial(String str);
	public Boolean isEnchantment(String str);
	public Boolean isFlag(String str);
	public Boolean isNumber(String str);
	public boolean isVanillaArmor(Material type);
	public boolean isVanillaRange(Material type);
	
	//
	// MINENCRAFT METHOD
	//
	
	// ITEMSTACK METHOD
	public ItemStack createItem(Material type, int amount);
	public ItemStack createItem(Material type, int amount, int model_data);
	public ItemStack createItem(Material type, int amount, int model_data, String name, String... lore);
	public ItemStack createItem(Material type, int amount,int model_data, Boolean unbreak, String name, String... lore);
	//name
	public ItemStack setName(ItemStack item, String name);
	public ItemStack clearName(ItemStack item);
	
	public String getName(ItemStack item);
	//lore
	public ItemStack addLore(ItemStack item, int index, String str);
	public ItemStack setLore(ItemStack item, int index, String str);
	public ItemStack addAllLore(ItemStack item, List<String> list);
	public ItemStack removeLore(ItemStack item, int index);
	public ItemStack clearLore(ItemStack item);
	
	public List<String> getLore(ItemStack item);
	//model data
	public ItemStack setModelData(ItemStack item, int data);
	
	public int getModelData(ItemStack item);
	//enchant
	public ItemStack addEnchant(ItemStack item, Enchantment ench, int level);
	public ItemStack removeEnchant(ItemStack item, Enchantment ench);
	public ItemStack clearEnchants(ItemStack item);
	//flag
	public ItemStack addFlag(ItemStack item, ItemFlag flag);
	public ItemStack removeFlag(ItemStack item, ItemFlag flag);
	public ItemStack clearFlags(ItemStack item);
	//unbreak
	public ItemStack setUnbreak(ItemStack item, Boolean unbreak);
	
	// PERMISSION & PLAYER METHOD
	public void sendMessage(CommandSender p, String msg);
	public boolean hasPermission(CommandSender p, String permission);
	public void addPermission(Player p, String str);
	public void removePermission(Player p, String str);
	
	public boolean isPlayerExist(String player);
	public boolean isOnlinePlayer(String player);
	
	public boolean isHoldItem(Player p);
	
	public void playSound(Sound s, Player p, float volume, float pitch);
	
	public int getAmount(ItemStack item, Player p);
	public void takeItem(ItemStack item, int amount, Player p);
	
	public double getLevel(Player p);
	public void setLevel(Player p, double level);
	public void takeLevel(Player p, double toTake);
	
	public int getExp(Player p);
	public void setExp(Player p, int exp);
	public void takeExp(Player p, int toTake);
	
	// MONEY METHOD
	public void takeMoney(Player p, double amount);
	public void setMoney(Player p, double amount);
	public void giveMoney(Player p, double amount);
	public void subtractMoney(Player p, double amount);
	public Boolean hasMoney(Player p, double amount);
	public double getMoney(Player p);
}
