package me.despawningbone.module.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.despawningbone.module.PlayerMine;

public final class PlayerMineExitEvent extends Event {
	
	Player player;
	PlayerMine mine;
	Location pFinalLocation;
	
	public PlayerMineExitEvent(Player player, PlayerMine mine, Location toLocation) {
		this.player = player;
		this.mine = mine;
		this.pFinalLocation = toLocation;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PlayerMine getMine() {
		return mine;
	}
	
	public Location getToLocation() {
		return pFinalLocation;
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
