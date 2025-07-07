package org.zanissnguyen.ZForge.UI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface UIAPI {
	
	public ItemStack fillIcon();
	public ItemStack closeIcon();
	public ItemStack balance(Player p);
	public ItemStack backIcon();
	public ItemStack createIcon(Material type, int amount, int data, String name, boolean glow, String... lore);
	
}
