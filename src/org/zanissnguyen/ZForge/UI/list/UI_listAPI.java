package org.zanissnguyen.ZForge.UI.list;

import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.UI.UIAPI;

public interface UI_listAPI extends UIAPI 
{
	public ItemStack findIcon(String searchKey, int size);
	public ItemStack nextIcon();
	public ItemStack previousIcon();
	public ItemStack addIcon();
	public ItemStack refreshIcon();

}
