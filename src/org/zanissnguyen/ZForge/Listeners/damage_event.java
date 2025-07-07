package org.zanissnguyen.ZForge.Listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.zanissnguyen.ZForge.ZForge;
import org.zanissnguyen.ZForge.Files.database.zfile_storage;
import org.zanissnguyen.ZForge.System.utils_attribute;
import org.zanissnguyen.ZForge.System.stat.Zstat;
import org.zanissnguyen.ZForge.System.stat.player_stat;
import org.zanissnguyen.ZForge.System.stat.stat_manager;

public class damage_event extends zevent
{
	public static boolean cooldown_msg;
	public static boolean is_play_sound;
	utils_attribute a_utils;
	zfile_storage storage;
	int digit;
	
	public static Map<Player, Boolean> onCooldown = new HashMap<>();

	public damage_event(ZForge plugin) {
		super(plugin, plugin.utils);
		a_utils = plugin.a_utils;
		storage = plugin.file_sto;
		is_play_sound = plugin.file_cfg.passiveSoundEnable();
		cooldown_msg = plugin.file_cfg.cooldownMessageEnable();
		digit = plugin.file_cfg.getDigit();
	}

	private void durabilityAndPowerWeapon(EntityDamageByEntityEvent event, player_stat stats, ItemStack toSet, Player p, String slot)
	{
		ItemStack weapon = null;
		weapon = toSet;
		stat_manager.increaseStat(weapon, stats, stat_manager.getScale(weapon, p, slot, false));
		if (a_utils.isBroke(weapon, p, slot, true)) 
		{
			event.setCancelled(true);
			return;
		}
		a_utils.durabilityProcess(weapon, p, slot, -1);
	}
	
	private void durabilityShield(EntityDamageByEntityEvent event, player_stat stats, ItemStack shield, Player p, String slot)
	{
		if (a_utils.isBroke(shield, p, slot, true)) 
		{
			return;
		}
		else
		{
			utils.playSound(Sound.ITEM_SHIELD_BLOCK, p, 1f, 1f);
			event.setCancelled(true);
			a_utils.durabilityProcess(shield, p, slot, -1);
		}
	}
	
	private void durabilityArmorAndAccessory(Player p)
	{
		a_utils.durabilityProcess(p.getInventory().getHelmet(), p, "helmet", -1);
		a_utils.durabilityProcess(p.getInventory().getChestplate(), p, "chestplate", -1);
		a_utils.durabilityProcess(p.getInventory().getLeggings(), p, "leggings", -1);
		a_utils.durabilityProcess(p.getInventory().getBoots(), p, "boots", -1);
		
		a_utils.durabilityProcess(storage.getGauntletSlot(p), p, "gauntlet", -1);
		a_utils.durabilityProcess(storage.getNecklaceSlot(p), p, "necklace", -1);
		a_utils.durabilityProcess(storage.getBeltSlot(p), p, "belt", -1);
		for (int i = 1; i<=5; i++) a_utils.durabilityProcess(storage.getRingSlot(p, i), p, "ring", i);
		for (int i = 1; i<=2; i++) a_utils.durabilityProcess(storage.getArtifactSlot(p, 1), p, "artifact", i);
		
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageByEntityEvent event)
	{
		Entity defender = event.getEntity();
		if (defender.getType()==EntityType.ARMOR_STAND) return;
		if (!(defender instanceof LivingEntity)) return;
		Entity attacker = null;
		boolean range = false;
		boolean valid = true;
		
		if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) 
		{
            Projectile projectile = (Projectile) event.getDamager();
            if(projectile.getShooter() instanceof LivingEntity) 
            {
                attacker = (Entity) projectile.getShooter();
                range = true;
            }
            else valid = false;
        }
		else
		{
			attacker = event.getDamager();
		}
		
		if (valid) calculateDamage(event, (LivingEntity) attacker, (LivingEntity) defender, range);
	}
	
	private void calculateDamage(EntityDamageByEntityEvent event, LivingEntity attacker, LivingEntity attacked, boolean range)
	{
		boolean isVanilla = plugin.file_cfg.vanillaDamageEnable();
		double range_bonus = plugin.file_cfg.getRangeBonus();
		String cdText = plugin.file_loc.getAttribute("stat.on_cooldown");
		
		double vanilla_armor_convert = plugin.file_cfg.getVanillaArmorConvert();
		double vanilla_armor_toughness_convert = plugin.file_cfg.getVanillaToughnessConvert();
		double vanilla_damage_convert = plugin.file_cfg.getVanillaDamageConvert();
		
		double mob_physic_resistance = plugin.file_cfg.getMobPhysicResistance();
		double mob_magic_resistance = plugin.file_cfg.getMobMagicResistance();
		
		int vanillaInt = (isVanilla) ? 1 : 0;
		
		if (!isVanilla) 
		{
			event.setDamage(0);
			mob_physic_resistance = 1;
			mob_magic_resistance = 1;
		}
		
		if (attacker instanceof Player && attacked instanceof Player) //PVP
		{
			Player p_atker = (Player) attacker;
			Player p_def = (Player) attacked;
			
			if (isOnCooldown(p_atker)) 
			{
				if (cooldown_msg) p_atker.sendMessage(cdText);
				event.setCancelled(true);
				return;
			}
			
			player_stat def_stat = new player_stat(stat_manager.player_stats.get(p_def));
			if (p_def.isBlocking())
			{
				ItemStack defMain = p_def.getInventory().getItemInMainHand();
				ItemStack defOff = p_def.getInventory().getItemInOffHand();
				if (defMain.getType().toString().contains("SHIELD")) 
					durabilityShield(event, def_stat, defMain, p_def, "mainhand");
				else durabilityShield(event, def_stat, defOff, p_def, "offhand");
			}
			if (event.isCancelled()) return;
			
			ItemStack mainhand = p_atker.getInventory().getItemInMainHand();
			ItemStack offhand = p_atker.getInventory().getItemInOffHand();
			
			String mainType = a_utils.getType(mainhand);
			String offType = a_utils.getType(offhand);
			boolean isRange = a_utils.isRangeTypes(offType) || utils.isVanillaRange(offhand.getType());
			
			player_stat stats = new player_stat(stat_manager.player_stats.get(p_atker));
			stat_manager.removeStat(mainhand, stats, stat_manager.getScale(mainhand, p_atker, "mainhand", false));
			if (!(a_utils.isOffHandTypes(offType)) 
					|| (a_utils.isOffHandTypes(offType) && isRange)) 
				stat_manager.removeStat(offhand, stats, stat_manager.getScale(offhand, p_atker, "offhand", false));
			
			if (range)
			{
				// attack by bow in main hand
				if (a_utils.isRangeTypes(mainType) || utils.isVanillaRange(mainhand.getType())) 
					durabilityAndPowerWeapon(event, stats, mainhand, p_atker, "mainhand");
				else durabilityAndPowerWeapon(event, stats, offhand, p_atker, "offhand");
			}
			else durabilityAndPowerWeapon(event, stats, mainhand, p_atker, "mainhand");
			
			if (event.isCancelled()) return;
			
			double physic_damage = stats.getStat(Zstat.PHYSIC_DAMAGE);
			double physic_penetration = stats.getStat(Zstat.PHYSIC_PENETRATION);
			double crit_chance = stats.getStat(Zstat.CRIT_CHANCE);
			double crit_damage = stats.getStat(Zstat.CRIT_DAMAGE);
			double magic_damage = stats.getStat(Zstat.MAGIC_DAMAGE);
			double magic_penetration = stats.getStat(Zstat.MAGIC_PENETRATION);
			double accurate = stats.getStat(Zstat.ACCURATE);
			double more_range = stats.getStat(Zstat.RANGE_BONUS);
			
			double physic_armor = stat_manager.player_stats.get(p_def).getStat(Zstat.PHYSIC_DEFENSE);
			double magic_armor = stat_manager.player_stats.get(p_def).getStat(Zstat.MAGIC_DEFENSE);
			double enhance_chance = stats.getStat(Zstat.ENHANCE_CHANCE);
			
			double dodge_chance = stat_manager.player_stats.get(p_def).getStat(Zstat.DODGE_CHANCE);
			double resistance = stat_manager.player_stats.get(p_def).getStat(Zstat.RESISTANCE);
			double block_chance = stat_manager.player_stats.get(p_def).getStat(Zstat.BLOCK_CHANCE);
			double parry_chance = stat_manager.player_stats.get(p_def).getStat(Zstat.PARRY_CHANCE);
			double absorp_chance = stat_manager.player_stats.get(p_def).getStat(Zstat.ABSORP_CHANCE);
			
			double attack_speed = stats.getStat(Zstat.ATTACK_SPEED);
			double cooldown = Math.max(1.0/attack_speed, 0.01);
			
			double deal_physic = physic_damage;
			double deal_magic = magic_damage*enhanceAttack(enhance_chance, p_def, p_atker);
			double deal_vanilla = utils.fixedDecimal(event.getFinalDamage(), digit);
			
			double final_range_bonus = range_bonus + more_range;
			
			// stage 1
			deal_physic*=I_physicArmorReduction(physic_damage, physic_penetration, physic_armor);
			if (deal_magic!=0) deal_magic*=I_magicArmorReduction(magic_damage, magic_penetration, magic_armor);
					
			// stage 2
			double dodge_scale = II_dodgeProcess(dodge_chance, accurate, p_def);
			deal_physic*=dodge_scale;
			if (deal_magic!=0) deal_magic*=dodge_scale;
			event.setDamage(event.getDamage()*dodge_scale);
			if (dodge_scale == 0) return;
			
			// stage 3
			double crit_scale = III_criticalProcess(crit_chance, crit_damage, attacked, attacker);
			deal_physic*=crit_scale;
			
			//stage 4: block
			double block_scale = IV_blockProcess(block_chance, p_def);
			deal_physic*=block_scale;
			
			//stage 5: parry
			double parry_scale = V_parryProcess(parry_chance, deal_physic+deal_vanilla, p_atker, p_def);
			deal_physic*=parry_scale;
			
			//stage 6: absorp
			VI_absorpProcess(absorp_chance, deal_physic+deal_magic+deal_vanilla, p_def);
			
			//stage 7: resistance
			deal_physic*=VII_resistanceProcess(resistance);
			if (deal_magic!=0) deal_magic*=VII_resistanceProcess(resistance);
			event.setDamage(event.getDamage()*(1-resistance));
			
			// spawn hologram
			deal_physic = utils.fixedDecimal(deal_physic * (range ? final_range_bonus : 1), digit);
			deal_magic = utils.fixedDecimal(deal_magic * (range ? final_range_bonus : 1), digit);
			
			// display hologram
			if (deal_physic!=0 && deal_magic==0)
			{
				spawnHologram(utils.color("&c"+utils.fixedDecimal(deal_physic+deal_vanilla, digit)), attacked, 0);
			}
			else if (deal_physic==0 && deal_magic!=0)
			{
				spawnHologram(utils.color("&c"+utils.fixedDecimal(deal_vanilla, digit)+" &f+ &3"+deal_magic), attacked, 0);
			}
			else if (deal_physic!=0 && deal_magic!=0)
			{
				spawnHologram(utils.color("&c"+utils.fixedDecimal(deal_physic+deal_vanilla, digit)+" &f+ &3"+deal_magic), attacked, 0);
			}
			
			double dealt = deal_physic + deal_magic;
//			attacker.sendMessage(dealt + " " + deal_vanilla);
			attacked.setHealth(Math.max(attacked.getHealth()-dealt, 0.00001));
			
			if (!range) cooldown(p_atker, 2, cooldown);
			
			
		}
		else if (attacker instanceof Player && !(attacked instanceof Player)) //PVE
		{	
			Player p_atker = (Player) attacker;
			
			if (isOnCooldown(p_atker)) 
			{
				if (cooldown_msg && !onCooldown.get(p_atker)) 
				{
					onCooldown.put(p_atker, true);
					p_atker.sendMessage(cdText);
				}
				event.setCancelled(true);
				return;
			}
			
			// stats modifier
			ItemStack mainhand = p_atker.getInventory().getItemInMainHand();
			ItemStack offhand = p_atker.getInventory().getItemInOffHand();
			
			String mainType = a_utils.getType(mainhand);
			String offType = a_utils.getType(offhand);
			boolean isRange = a_utils.isRangeTypes(offType) || utils.isVanillaRange(offhand.getType());
			
			player_stat stats = new player_stat(stat_manager.player_stats.get(p_atker));
			stat_manager.removeStat(mainhand, stats, stat_manager.getScale(mainhand, p_atker, "mainhand", false));
			if (!(a_utils.isOffHandTypes(offType)) 
					|| (a_utils.isOffHandTypes(offType) && isRange)) 
				stat_manager.removeStat(offhand, stats, stat_manager.getScale(offhand, p_atker, "offhand", false));
			
			if (range)
			{
				// attack by bow in main hand
				if (a_utils.isRangeTypes(mainType) || utils.isVanillaRange(mainhand.getType())) 
					durabilityAndPowerWeapon(event, stats, mainhand, p_atker, "mainhand");
				else durabilityAndPowerWeapon(event, stats, offhand, p_atker, "offhand");
			}
			else durabilityAndPowerWeapon(event, stats, mainhand, p_atker, "mainhand");
			
			if (event.isCancelled()) return;
			
			double generic_armor = attacked.getAttribute(Attribute.GENERIC_ARMOR).getValue();
			double generic_armor_toughness = attacked.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
		
			double true_defense = generic_armor*vanilla_armor_convert+generic_armor_toughness*vanilla_armor_toughness_convert;
			
			double physic_damage = stats.getStat(Zstat.PHYSIC_DAMAGE)*(1-mob_physic_resistance);
			double physic_penetration = stats.getStat(Zstat.PHYSIC_PENETRATION);
			double crit_chance = stats.getStat(Zstat.CRIT_CHANCE);
			double crit_damage = stats.getStat(Zstat.CRIT_DAMAGE);
			double more_range = stats.getStat(Zstat.RANGE_BONUS);
			
			double magic_damage = stats.getStat(Zstat.MAGIC_DAMAGE)*(1-mob_magic_resistance);
			double magic_penetration = stats.getStat(Zstat.MAGIC_PENETRATION);
			double enhance_chance = stats.getStat(Zstat.ENHANCE_CHANCE);
			
			double attack_speed = stats.getStat(Zstat.ATTACK_SPEED);
			double cooldown = Math.max(1.0/attack_speed, 0.01);
			
			double deal_physic = physic_damage;
			double deal_magic = magic_damage*enhanceAttack(enhance_chance, attacked, attacker);
			
			// stage 1
			deal_physic*=I_physicArmorReduction(physic_damage, physic_penetration, true_defense);
			if (deal_magic!=0) deal_magic*=I_magicArmorReduction(magic_damage, magic_penetration, true_defense);
			
			// stage 2
			double crit_scale = III_criticalProcess(crit_chance, crit_damage, attacked, attacker);
			deal_physic*=crit_scale;
			
			deal_physic = utils.fixedDecimal(deal_physic, digit);
			deal_magic = utils.fixedDecimal(deal_magic, digit);
			double vanillaDmg = utils.fixedDecimal(event.getFinalDamage()*vanillaInt, digit);
			
			// display hologram
			if (deal_physic!=0 && deal_magic==0)
			{
				spawnHologram(utils.color("&c"+utils.fixedDecimal(deal_physic+vanillaDmg, digit)), attacked, 0);
			}
			else if (deal_physic==0 && deal_magic!=0)
			{
				spawnHologram(utils.color("&c"+utils.fixedDecimal(vanillaDmg, digit)+" &f+ &3"+deal_magic), attacked, 0);
			}
			else if (deal_physic!=0 && deal_magic!=0)
			{
				spawnHologram(utils.color("&c"+utils.fixedDecimal(deal_physic+vanillaDmg, digit)+" &f+ &3"+deal_magic), attacked, 0);
			}
			
			double dealt = deal_physic + deal_magic * (range ? range_bonus+more_range : 1);
			attacked.setHealth(Math.max(attacked.getHealth()-dealt, 0.00001));	
			
			if (!range) cooldown(p_atker, 2, cooldown);
			
			
		}
		else if (!(attacker instanceof Player) && attacked instanceof Player) //EVP
		{
			double generic_damage = attacker.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue();
			
			Player p_def = (Player) attacked;
			player_stat def_stat = new player_stat(stat_manager.player_stats.get(p_def));
			if (p_def.isBlocking())
			{
				ItemStack defMain = p_def.getInventory().getItemInMainHand();
				ItemStack defOff = p_def.getInventory().getItemInOffHand();
				if (defMain.getType().toString().contains("SHIELD")) 
					durabilityShield(event, def_stat, defMain, p_def, "mainhand");
				else durabilityShield(event, def_stat, defOff, p_def, "offhand");
			}
			if (event.isCancelled()) return;
			
			double physic_armor = def_stat.getStat(Zstat.PHYSIC_DEFENSE)*(1-mob_physic_resistance);
			
			double dodge_chance = def_stat.getStat(Zstat.DODGE_CHANCE);
			double resistance = def_stat.getStat(Zstat.RESISTANCE);
			double block_chance = def_stat.getStat(Zstat.BLOCK_CHANCE);
			double parry_chance = def_stat.getStat(Zstat.PARRY_CHANCE);
			double absorp_chance = def_stat.getStat(Zstat.ABSORP_CHANCE);
			
//			double magic_armor = player_statUtils.statStorage.get(p_atker).get("magic_defense", false)*(1-mob_magic_resistance);
			
			double deal_damage = generic_damage*vanilla_damage_convert; 
			
//			System.out.println(deal_damage + " " + generic_damage);
			
			// stage 1: armor
			deal_damage*=I_physicArmorReduction(deal_damage, 0, physic_armor);
			
			// stage 2: dodge
			double dodge_scale = II_dodgeProcess(dodge_chance, 0, p_def);
			deal_damage*=dodge_scale;
			event.setDamage(event.getDamage()*dodge_scale);
			if (dodge_scale == 0) return;
			
			// stage 3: crit
			// null for mob attack
			
			//stage 4: block
			double block_scale = IV_blockProcess(block_chance, p_def);
			deal_damage*=block_scale;
			
			//stage 5: parry
			double parry_scale = V_parryProcess(parry_chance, deal_damage+event.getDamage(), attacker, p_def);
			deal_damage*=parry_scale;
			
			//stage 6: absorp
			VI_absorpProcess(absorp_chance, deal_damage, p_def);
			
			deal_damage = utils.fixedDecimal(deal_damage/(1+vanillaInt), digit);
			double vanillaDmg = utils.fixedDecimal((event.getDamage()*vanillaInt)/(1+vanillaInt), digit);
			
			//stage 7: resistance
			deal_damage *= VII_resistanceProcess(resistance);
			vanillaDmg *= VII_resistanceProcess(resistance);
			event.setDamage(vanillaDmg);
			
			// range bonus
			deal_damage *= (range ? range_bonus : 1);
			
			// display hologram
//			System.out.println(deal_damage + " " + vanillaDmg);
			spawnHologram(utils.color("&c"+utils.fixedDecimal(deal_damage+vanillaDmg, digit)), p_def, 0);
			
			attacked.setHealth(Math.max(attacked.getHealth()-deal_damage, 0.00001));
			
			durabilityArmorAndAccessory(p_def);
		}
	}

	private double enhanceAttack(double atker_enhance_chance, LivingEntity defender, LivingEntity attacker)
	{
		String text = plugin.file_loc.getAttribute("stat.active.enhance");
		boolean isEnhance = utils.roll(atker_enhance_chance);
		
		if (isEnhance)
		{
			spawnHologram(text, defender, 0.5);
			return 1.0;
		}
		else return 0.0;
	}
	
	private double I_magicArmorReduction(double atker_damage, double atker_penetration, double atked_armor)
	{
		double true_armor = Math.max(atked_armor-atker_penetration, 0);
		double delta = true_armor-atker_damage;
		double scale = delta/atker_damage;
		
		if (scale<-1)
		{
			return 1.0;
		}
		else if (scale>=-1 && scale <0)
		{
			return (0.6-0.4*scale);
		}
		else if (scale>=0 && scale<2)
		{
			return (0.6-0.2*scale);
		}
		else if (scale>=2 && scale<4)
		{
			return (0.5-0.1*scale);
		}
		else return 0.1;
	}
	
	private double I_physicArmorReduction(double atker_damage, double atker_penetration, double atked_armor)
	{
		double true_armor = Math.max(atked_armor-atker_penetration, 0);
		double delta = true_armor-atker_damage;
		double scale = delta/atker_damage;
		
		if (scale<-1)
		{
			return 1.0;
		}
		else if (scale>=-1 && scale <0)
		{
			return (0.5-0.5*scale);
		}
		else if (scale>=0 && scale<2)
		{
			return (0.5-0.175*scale);
		}
		else if (scale>=2 && scale<4)
		{
			return (0.15-0.025*scale);
		}
		else return 0.05;
	}
	
	private double II_dodgeProcess(double atked_dodge_chance, double atker_accurate_chance, LivingEntity defender)
	{
		String text = plugin.file_loc.getAttribute("stat.active.dodge");
		double maxDodge = plugin.file_cfg.getMaxDodge();
		double true_dodge = Math.min((atked_dodge_chance-atker_accurate_chance), maxDodge);
		
		boolean isDodge = utils.roll(true_dodge);
		
		if (isDodge)
		{
			spawnHologram(text, defender, 0.5);
			return 0.0;
		}
		else return 1.0;
	}
	
	private double III_criticalProcess(double atker_crit_chance, double atker_crit_damage, LivingEntity defender, LivingEntity attacker)
	{
		boolean isMultiple = plugin.file_cfg.multiCriticalEnable();
		
		int temp = 1;
		double true_chance = atker_crit_chance;
		
		if (isMultiple)
		{
			while (true_chance>1)
			{
				true_chance--;
				temp++;
			}
		}
		
		String text = plugin.file_loc.getAttribute("stat.active.crit");
		
		boolean isCrit = utils.roll(true_chance);
		
		if (isCrit)
		{
			spawnHologram(text.replace("<bonus>", ""+atker_crit_damage*temp), defender, 0.5);
			if (is_play_sound) utils.playSound(Sound.ENTITY_PLAYER_ATTACK_CRIT, (Player) attacker, 3f, 1f);
			return atker_crit_damage*temp;
		} else return 1.0;
	}
	
	private double IV_blockProcess(double atked_block_chance, LivingEntity defender)
	{
		String text = plugin.file_loc.getAttribute("stat.active.block");
		double blockPower = plugin.file_cfg.getBlockPower();
		boolean isBlock = utils.roll(atked_block_chance);
		
		if (isBlock)
		{
			spawnHologram(text, defender, 0.5);
			if (is_play_sound) utils.playSound(Sound.ITEM_SHIELD_BLOCK, (Player) defender, 3f, 1f);
			return blockPower;
		}
		else return 1.0;
	}
	
	private double V_parryProcess(double atked_parry_chance, double current_damage, LivingEntity attacker, LivingEntity defender)
	{
		String text_1 = plugin.file_loc.getAttribute("stat.active.parry");
		String text_2 = plugin.file_loc.getAttribute("stat.active.parry_damage");
		double power = plugin.file_cfg.getParryPower();
		boolean isParry = utils.roll(atked_parry_chance);
		
		if (isParry)
		{
			attacker.damage(power*current_damage);
			spawnHologram(text_2.replace("<dmg>", utils.fixedDecimal(power*current_damage, digit)+""), attacker, 0.5);
			spawnHologram(text_1, defender, 0.5);
			if (is_play_sound) utils.playSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP,(Player) defender, 3f, 1f);
			return power;
		}
		else return 1.0;
	}
	
	private void VI_absorpProcess(double atked_absorp_chance, double current_damage, LivingEntity defender)
	{
		double power = plugin.file_cfg.getAbsorpPower();
		String text = plugin.file_loc.getAttribute("stat.active.absorp");
		boolean isAbsorp = utils.roll(atked_absorp_chance);
		
		if (isAbsorp)
		{
			int effectLevel =(int) ((current_damage*power)/4.0);
			defender.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 40, effectLevel));
			if (is_play_sound) utils.playSound(Sound.BLOCK_METAL_HIT, (Player) defender, 3f, 1f);
			spawnHologram(text, defender, 0.5);
		}
	}
	
	private double VII_resistanceProcess(double atked_resistance)
	{
		double maxResistance = plugin.file_cfg.getMaxResistance();
		return (1-Math.min(maxResistance, atked_resistance));
	}
	
	private void spawnHologram(String hologram, LivingEntity object, double height)
	{
		if (object.getType()==EntityType.ARMOR_STAND) return;
		
		double duration = plugin.file_cfg.getHologramDuration();
		int fps = plugin.file_cfg.getHologramFPS();
		int frame = (int) Math.round(fps*duration);
		double step = duration/frame;
		
		Location location = object.getLocation();
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(new Vector(0, height, 0)), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomName(utils.color(hologram));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);

        // Ngẫu nhiên góc ném và tốc độ
        double angle = Math.random() * 360; // Góc ngẫu nhiên từ 0 đến 360 độ

        // Di chuyển ArmorStand theo quỹ đạo parabol với các yếu tố ngẫu nhiên
        new BukkitRunnable() {
            private double time = 0.0;

            @Override
            public void run() {
                if (armorStand.isValid()) 
                {
                    time += step;
                    double x = time * Math.cos(Math.toRadians(angle));
                    double y = time * Math.sin(Math.toRadians(angle)) - (0.4 * time * time);
                    double z = time * Math.sin(Math.toRadians(angle));

                    Location newLocation = location.clone().add(x, y, z);
                    armorStand.teleport(newLocation);
                    
                    if (time>=duration)
                    {
                    	armorStand.remove();
                    	cancel();
                    }

                } else 
                {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, (long) step*400);
	}
	
	private void setCooldown(Player p)
	{
		onCooldown.put(p, false);
	}
	
	private void removeCooldown(Player p)
	{
		onCooldown.remove(p);
	}
	
	public static boolean isOnCooldown(Player p)
	{
		return onCooldown.containsKey(p);
	}
	
	private void cooldown(Player p, int base, double cooldown)
	{
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() 
			{
				if (!isOnCooldown(p))
				{
					setCooldown(p);
					
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						
						@Override
						public void run()
						{
							removeCooldown(p);
							if (is_play_sound) utils.playSound(Sound.ENTITY_PLAYER_LEVELUP, p, 0.05f, 1f);
						}
						
					}, base+Math.round(cooldown*20));
				}
				
			}
		}, base);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player p = event.getPlayer();
		if (a_utils.isBroke(p.getInventory().getItemInMainHand(), p, "mainhand", true))
		{
			event.setCancelled(true);
		} else
		{
			a_utils.durabilityProcess(p.getInventory().getItemInMainHand(), p, "mainhand", -1);
		}
	}
	
	@EventHandler
	public void onHeal(EntityRegainHealthEvent event)
	{
		if (event.getEntityType()==EntityType.PLAYER)
		{
			Player p = (Player) event.getEntity();
			double heal = stat_manager.player_stats.get(p).getStat(Zstat.HEAL);
			event.setAmount(event.getAmount()*(1+heal));
		}
	}
}
