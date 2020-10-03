package com.github.sarhatabaot.tcmythicmobs;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import media.xen.tradingcards.CardUtil;
import media.xen.tradingcards.TradingCards;
import media.xen.tradingcards.addons.AddonListener;
import media.xen.tradingcards.addons.TradingCardsAddon;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MythicMobsListener extends AddonListener {
	private final TradingCards tradingCards;

	public MythicMobsListener(final TradingCardsAddon tradingCardsAddon, final TradingCards tradingCards) {
		super(tradingCardsAddon);
		this.tradingCards = tradingCards;
	}

	@EventHandler
	public void onMythicMobDeath(MythicMobDeathEvent e) {
		boolean drop = false;
		String worldName = "";
		List<String> worlds = new ArrayList<>();
		if (e.getKiller() instanceof Player) {
			Player p = (Player)e.getKiller();
			drop = (!this.tradingCards.isOnList(p) || this.tradingCards.blacklistMode() != 'b') && (!this.tradingCards.isOnList(p) && this.tradingCards.blacklistMode() == 'b' || this.tradingCards.isOnList(p) && this.tradingCards.blacklistMode() == 'w');
			worldName = p.getWorld().getName();
			worlds = this.tradingCards.getConfig().getStringList("World-Blacklist");
		}

		if (drop && !(worlds).contains(worldName)) {
			String rare = CardUtil.calculateRarity(e.getEntity().getType(), false);
			if (tradingCardsAddon.getJavaPlugin().getConfig().getBoolean("Per-Level-Chances")) {
				ActiveMob mob = e.getMob();
				double level = mob.getLevel();
				String oldRarity = rare;
				rare = this.calculateMMRarity(level, false);
				tradingCards.debug("Mob is a mythic mob of level " + level + ", rarity changed from " + oldRarity + " to " + rare);
			}
			if (this.tradingCards.getConfig().getBoolean("Chances.Boss-Drop") && this.tradingCards.isMobBoss(e.getEntity().getType())) {
				rare = this.tradingCards.getConfig().getString("Chances.Boss-Drop-Rarity");
			}
			boolean cancelled = false;
			if (!rare.equals("None")) {
				if (this.tradingCards.getConfig().getBoolean("General.Spawner-Block") && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals(this.tradingCards.getConfig().getString("General.Spawner-Mob-Name"))) {
					tradingCards.debug("Mob came from spawner, not dropping card.");
					cancelled = true;
				}

				if (!cancelled) {
					tradingCards.debug("Successfully generated card.");
					boolean isShiny = false;
					int shinyRandom = tradingCards.r.nextInt(100) + 1;
					tradingCards.debug("Shiny chance for level " + e.getMobLevel() + " is " + tradingCardsAddon.getJavaPlugin().getConfig().getInt("Levels." + (int)e.getMobLevel() + ".Shiny-Version-Chance"));
					if (shinyRandom <= tradingCardsAddon.getJavaPlugin().getConfig().getInt("Levels." + e.getMobLevel() + ".Shiny-Version-Chance")) {
						isShiny = true;
					}
					e.getDrops().add(CardUtil.getRandomCard(rare, isShiny));
				}
			}
		}

	}

	public String calculateMMRarity(double mobLvl, boolean alwaysDrop) {
		tradingCards.debug("Mythic mobs: Starting rarity calculation for level " + mobLvl + ", alwaysDrop is " + alwaysDrop);
		ConfigurationSection levels = tradingCardsAddon.getJavaPlugin().getConfig().getConfigurationSection("Levels");
		Set<String> levelKeys = levels.getKeys(false);
		int finalLvl = 0;
		Iterator<String> var6 = levelKeys.iterator();

		String type;
		while(var6.hasNext()) {
			type = var6.next();
			int level = Integer.parseInt(type);
			if (level == mobLvl) {
				if (level >= finalLvl) {
					finalLvl = level;
				}

				tradingCards.debug("Mythic mobs: Correct level is: " + level);
			} else {
				if (level >= finalLvl && level <= mobLvl) {
					finalLvl = level;
				}

				tradingCards.debug("Mythic mobs: Not the correct level.. iteration is: " + level);
			}
		}

		int shouldItDrop = tradingCards.r.nextInt(100) + 1;
		type = "";
		tradingCards.debug("shouldItDrop Num: " + shouldItDrop);
		if (!alwaysDrop) {
			if (shouldItDrop > tradingCardsAddon.getJavaPlugin().getConfig().getInt("Levels." + finalLvl + ".Drop-Chance")) {
				return "None";
			}

			type = "MythicMob";
		} else {
			type = "MythicMobs";
		}

		ConfigurationSection rarities = this.tradingCards.getConfig().getConfigurationSection("Rarities");
		Set<String> rarityKeys = rarities.getKeys(false);
		Map<Integer, String> rarityIndexes = new HashMap<>();
		int i = 0;
		int mini = 0;
		int random = tradingCards.r.nextInt(100000) + 1;
		tradingCards.debug("Random Card Num: " + random);
		tradingCards.debug("Type: " + type);

		for (final String key : rarityKeys) {
			rarityIndexes.put(i, key);
			++i;
			this.tradingCards.debug(i + ", " + key);
			if (tradingCardsAddon.getJavaPlugin().getConfig().contains("Levels." + finalLvl + ".Rarities." + key) && mini == 0) {
				tradingCards.debug("Path exists: Levels." + finalLvl + ".Rarities." + key);
				tradingCards.debug("Mini: " + i);
				mini = i;
			}
		}

		int chance;
		if (mini != 0) {
			tradingCards.debug("Mini: " + mini);
			tradingCards.debug("i: " + i);

			while(i >= mini) {
				--i;
				tradingCards.debug("i: " + i);
				chance = tradingCardsAddon.getJavaPlugin().getConfig().getInt("Levels." + finalLvl + ".Rarities." + rarityIndexes.get(i), -1);
				tradingCards.debug(" Chance: " + chance);
				tradingCards.debug("Rarity: " + rarityIndexes.get(i));
				if (chance > 0) {
					tradingCards.debug("Chance > 0");
					if (random <= chance) {
						tradingCards.debug("Random <= Chance, returning " + rarityIndexes.get(i));
						return rarityIndexes.get(i);
					}
				}
			}
		} else {
			while(i > 0) {
				--i;
				tradingCards.debug("Final loop iteration " + i);
				tradingCards.debug("Iteration " + i + " in HashMap is: " + rarityIndexes.get(i) + ", " + this.tradingCards.getConfig().getString("Rarities." + rarityIndexes.get(i) + ".Name"));
				chance = tradingCardsAddon.getJavaPlugin().getConfig().getInt("Levels." + finalLvl + ".Rarities." + rarityIndexes.get(i), -1);
				tradingCards.debug("" + this.tradingCards.getConfig().getString("Rarities." + rarityIndexes.get(i) + ".Name") + "'s chance of dropping: " + chance + " out of 100,000");
				tradingCards.debug("The random number we're comparing that against is: " + random);
				if (chance > 0 && random <= chance) {
					tradingCards.debug("Yup, looks like " + random + " is definitely lower than " + chance + "!");
					tradingCards.debug("Giving a " + this.tradingCards.getConfig().getString("Rarities." + rarityIndexes.get(i) + ".Name") + " card.");
					return rarityIndexes.get(i);
				}
			}
		}

		return "None";
	}

}
