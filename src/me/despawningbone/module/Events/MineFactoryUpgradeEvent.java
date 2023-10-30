package me.despawningbone.module.Events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.despawningbone.module.MineFactory;
import me.despawningbone.module.PlayerMine;

public final class MineFactoryUpgradeEvent extends Event {
	
	OfflinePlayer player;
	PlayerMine mine;
	MineFactory upgradedFactory;
	
	public MineFactoryUpgradeEvent(OfflinePlayer player, PlayerMine mine, MineFactory upgradedFactory) {
		this.player = player;
		this.mine = mine;
		this.upgradedFactory = upgradedFactory;
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	public PlayerMine getMine() {
		return mine;
	}
	
	public MineFactory getUpgradedFactory()
	{
		return upgradedFactory;
	}
	
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
}
