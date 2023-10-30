package me.despawningbone.module.Events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.despawningbone.module.PlayerMine;
import me.despawningbone.module.Config.SchematicConfig;

public final class PostChangeSchematicEvent extends Event {
	
	OfflinePlayer player;
	PlayerMine mine;
	SchematicConfig nextSchematic;
	
	public PostChangeSchematicEvent(OfflinePlayer player, PlayerMine mine, SchematicConfig nextSchematic) {
		this.player = player;
		this.mine = mine;
		this.nextSchematic = nextSchematic;
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	public PlayerMine getMine() {
		return mine;
	}
	
	public SchematicConfig getNextSchematic() {
		return nextSchematic;
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
