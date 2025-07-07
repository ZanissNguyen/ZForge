package org.zanissnguyen.ZForge.UI.craft;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface UI_craftAPI 
{
	public void open(Player p);
	public void setUpMaterial(Player p, Inventory inv);
	public ItemStack craftIcon();
	public ItemStack infoIcon(Player p);
	public boolean enoughMaterial(Player p);
}
