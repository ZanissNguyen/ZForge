package org.zanissnguyen.ZForge.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.database.zfile_storage;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.System.stat.stat_manager;

public class exp_gain_event extends zevent
{	
	public exp_gain_event(ZForge plugin) {
		super(plugin, plugin.utils);
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent event)
	{
		utils_attribute a_utils = plugin.a_utils;
		
		if (event.getEntity().getKiller() instanceof Player)
		{
			Player p = event.getEntity().getKiller();
			double boost = stat_manager.player_stats.get(p).getStat(Zstat.XP_BOOST);
			
			int item_xp = event.getEntityType()!=EntityType.PLAYER ?
					plugin.file_cfg.getDropExp("entity."+getType(event.getEntityType())) :
						plugin.file_cfg.getDropExp("player");
			item_xp = (int) (Math.round(item_xp*(1+boost)));
			
			if (add_exp(p, item_xp)) utils_attribute.updateAttribute(p);
			
			a_utils.levelingProcess(null, item_xp, false);
			
			event.setDroppedExp((int) Math.round(event.getDroppedExp()*(1+boost)));
		}
	}
	
	public boolean add_exp(Player p, int exp)
	{
		utils_attribute a_utils = plugin.a_utils;
		zfile_storage storage = plugin.file_sto;
		
		// main hand
		boolean main_up = false;
		ItemStack main_item = p.getInventory().getItemInMainHand();
		p.getInventory().setItemInMainHand(a_utils.levelingProcess(main_item, exp, main_up));
		
		// off hand
		boolean off_up = false;
		ItemStack off_item = p.getInventory().getItemInOffHand();
		p.getInventory().setItemInOffHand(a_utils.levelingProcess(off_item, exp, off_up));
		
		// helmet hand
		boolean helmet_up = false;
		ItemStack helmet_item = p.getInventory().getHelmet();
		p.getInventory().setHelmet(a_utils.levelingProcess(helmet_item, exp, helmet_up));
		
		// chestplate
		boolean chestplate_up = false;
		ItemStack chestplate_item = p.getInventory().getChestplate();
		p.getInventory().setChestplate(a_utils.levelingProcess(chestplate_item, exp, chestplate_up));
		
		// leggings
		boolean leg_up = false;
		ItemStack leg_item = p.getInventory().getLeggings();
		p.getInventory().setLeggings(a_utils.levelingProcess(leg_item, exp, leg_up));
		
		// boots
		boolean boots_up = false;
		ItemStack boots_item = p.getInventory().getBoots();
		p.getInventory().setBoots(a_utils.levelingProcess(boots_item, exp, boots_up));
				
		// belt
		boolean belt_up = false;
		ItemStack belt_item = storage.getBeltSlot(p);
		storage.setBeltSlot(p, a_utils.levelingProcess(belt_item, exp, belt_up));
		
		// gauntlet
		boolean gauntlet_up = false;
		ItemStack gauntlet_item = storage.getGauntletSlot(p);
		storage.setGauntletSlot(p, a_utils.levelingProcess(gauntlet_item, exp, gauntlet_up));
				
		// necklace
		boolean necklace_up = false;
		ItemStack necklace_item = storage.getNecklaceSlot(p);
		storage.setNecklaceSlot(p, a_utils.levelingProcess(necklace_item, exp, necklace_up));
				
		//artifact
		boolean artifact_up = false;
		for(int i = 1; i<=2; i++)
		{
			boolean artifact_up_temp = false;
			ItemStack artifact_item = storage.getArtifactSlot(p, i);
			storage.setArtifactSlot(p, i, a_utils.levelingProcess(artifact_item, exp, artifact_up_temp));
			artifact_up |= artifact_up_temp;
		}
		
		//artifact
		boolean ring_up = false;
		for(int i = 1; i<=5; i++)
		{
			boolean ring_up_temp = false;
			ItemStack ring_item = storage.getRingSlot(p, i);
			storage.setRingSlot(p, i, a_utils.levelingProcess(ring_item, exp, ring_up_temp));
			ring_up |= ring_up_temp;
		}
		
		return main_up || off_up || helmet_up || chestplate_up || leg_up || boots_up
				|| ring_up || artifact_up || necklace_up || gauntlet_up || belt_up;
	}

	private String getType(EntityType t)
	{
		String tStr = t.toString();
		if (listBoss().contains(tStr)) return "boss";
		if (listAnimal().contains(tStr)) return "animal";
		if (listMonster().contains(tStr)) return "monster";
		if (listElite().contains(tStr)) return "elite";
		else return "animal";
	}
	
	private List<String> listAnimal()
	{
		List<String> animals = new ArrayList<>();
		animals.add("PIG");
		animals.add("CAT");
		animals.add("CHICKEN");
		animals.add("COW");
		animals.add("DONKEY");
		animals.add("LLAMA");
		animals.add("MULE");
		animals.add("OCELOT");
		animals.add("MUSHROOM_COW");
		animals.add("PARROT");
		animals.add("POLAR_BEAR");
		animals.add("RABBIT");
		animals.add("SHEEP");
		animals.add("SILVERFISH");
		animals.add("HORSE");
		animals.add("SQUID");
		animals.add("TROPICAL_FISH");
		animals.add("SALMON");
		animals.add("PUFFERFISH");
		animals.add("WOLF");
		return animals;
	}
	
	private List<String> listMonster()
	{
		List<String> animals = new ArrayList<>();
		animals.add("ZOMBIE_VILLAGER");
		animals.add("ZOMBIE");
		animals.add("HUSK");
		animals.add("DROWNER");
		animals.add("ZOMBIE_HORSE");
		animals.add("SKELETON");
		animals.add("STRAY");
		animals.add("SKELETON_HORSE");
		animals.add("CREEPER");
		animals.add("SPIDER");
		animals.add("CAVE_SPIDER");
		animals.add("PIG_ZOMBIE");
		animals.add("GUARDIAN");
		animals.add("GHAST");
		animals.add("VINDICATOR");
		animals.add("MAGAMA_CUBE");
		animals.add("SLIME");
		animals.add("PHANTOM");
		animals.add("PILLAGER");
		animals.add("ENDERMITE");
		return animals;
	}
	
	private List<String> listElite()
	{
		List<String> animals = new ArrayList<>();
		animals.add("ENDERMAN");
		animals.add("BALZE");
		animals.add("WITHER_SKELETON");
		animals.add("GHAST");
		animals.add("EVOKER");
		animals.add("SNOWMAN");
		animals.add("RAVAGER");
		animals.add("SHULKER");
		animals.add("ELDER_GUARDIAN");
		animals.add("WITCH");
		animals.add("IRON_GOLEM");
		return animals;
	}
	
	private List<String> listBoss()
	{
		List<String> animals = new ArrayList<>();
		animals.add("ENDERDRAGON");
		animals.add("WITHER");
		animals.add("GIANT");
		return animals;
	}
}
