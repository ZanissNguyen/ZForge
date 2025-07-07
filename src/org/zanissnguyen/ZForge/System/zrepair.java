package org.zanissnguyen.ZForge.System;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.configuration.zfile_config;
import org.zanissnguyen.ZForge.System.object.zrecipe;
import org.zanissnguyen.ZForge.Utils.utils;

public class zrepair 
{
	private static ZForge plugin = ZForge.main();
	private static utils utils = ZForge.main().utils;
	
	public double money;
	public double level;
	public List<String> materials;
	
	private List<String> errors;
	
	public zrepair()
	{
		zfile_config cfg = plugin.file_cfg;
		errors = new ArrayList<>();
		
		this.money = cfg.getRepairCost();
		this.level = cfg.getRepairLevel();
		this.materials = cfg.getRepairMaterial();
	}
	
	public int canRepairAmount(int needRepair, Player p)
	{
		double balance = plugin.getEconomy().getBalance(p);
		int repairMoney = (int) (balance / this.money);
		
		double level = utils.getLevel(p);
		int repairLv = (int) (level / this.level);
		
		int tempRs = Math.min(repairMoney, repairLv);
		
		Map<ItemStack, Integer> materials = materials();
		int repairMat = Integer.MAX_VALUE;
		for (ItemStack mat: materials.keySet())
		{
			int have = utils.getAmount(mat, p);
			repairMat = Math.min(repairMat, have/materials.get(mat));
		}
		
		int result = Math.min(tempRs, repairMat);
		result = Math.max(0, result);
		result = Math.min(result, needRepair);
		
		return result;
	}
	
	public Map<ItemStack, Integer> materials()
	{
		return zrecipe.materials(this.materials, this.errors);
	}
}
