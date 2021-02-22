package com.github.sarhatabaot.tcmythicmobs;

import media.xen.tradingcards.TradingCards;
import media.xen.tradingcards.api.addons.AddonLogger;
import media.xen.tradingcards.api.addons.TradingCardsAddon;
import org.bukkit.plugin.java.JavaPlugin;

public final class TCMythicMobs extends JavaPlugin implements TradingCardsAddon {
	private AddonLogger addonLogger;

	@Override
	public void onEnable() {
		TradingCards tradingCards = (TradingCards) getServer().getPluginManager().getPlugin("TradingCards");
		addonLogger = new AddonLogger(getName(),tradingCards);
		getServer().getPluginManager().registerEvents(new MythicMobsListener(this, tradingCards), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	@Override
	public JavaPlugin getJavaPlugin() {
		return this;
	}

	@Override
	public AddonLogger getAddonLogger() {
		return addonLogger;
	}
}
