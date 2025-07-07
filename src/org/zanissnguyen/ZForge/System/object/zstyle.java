package org.zanissnguyen.ZForge.System.object;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class zstyle
{
	public String id;

	public String default_fill;
	public int rows;
	public int reward_slot;
	public int back_slot;
	public int craft_slot;
	public int info_slot;
	public List<Integer> materials;

	public Map<String, ItemStack> icon;
	public Map<String, List<Integer>> iconSlots;
	public zstyle(String id, int rows, int reward, int craft, int info, int back
		, List<Integer> materials, String default_fill
		, Map<String, ItemStack> icon, Map<String, List<Integer>> iconSlots)
	{
		this.id = id;
		this.rows = rows;
		this.reward_slot = reward;
		this.craft_slot =craft;
		this.info_slot = info;
		this.back_slot = back;
		this.materials = materials;
		this.default_fill = default_fill;
		this.icon = icon;
		this.iconSlots = iconSlots;
	
		//	for (int i: materials) System.out.println(i);
	}
}