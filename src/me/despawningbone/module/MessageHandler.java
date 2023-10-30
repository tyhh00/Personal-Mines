package me.despawningbone.module;

import org.apache.commons.io.output.ThresholdingOutputStream;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

enum MESSAGE_TYPE 
{
	MINE_LOCKED_FOR_VISITORS,
	PLAYER_NOT_ONLINE,
	NOT_STANDING_ON_MINE,
	MAX_TEAM_MEMBERS_REACHED,
	PLAYER_NOT_IN_TEAM,
	INVALID_SYNTAX,
	PLAYER_EXIST_IN_TEAM,
	NOT_MINE_OWNER,
	INVALID_HOME,
	TOGGLE_AUTOMINER_ON,
	TOGGLE_AUTOMINER_OFF,
}

public class MessageHandler {
	
	public static final String DEFAULT_PREFIX = "§e§l◎ §7";
	public static final String DEFAULT_SUFFIX = " §e§l◎";
	public static final String DEFAULT_TITLE_PREFIX = "&e§l(!)";
	
	public static final String MINE_LOCKED_FOR_VISITORS = "This Mine is not opened to the public.";
	public static final String MINE_LOCKED_FOR_VISITORS_SHORT = "Mine isn't public.";
	
	public static final String PLAYER_NOT_ONLINE = "The player you have requested for is offline.";
	public static final String PLAYER_NOT_ONLINE_SHORT = "Player is offline.";
	
	public static final String NOT_STANDING_ON_MINE = "You are not standing on a PMine!";
	public static final String NOT_STANDING_ON_MINE_SHORT = "PMine not found.";
	
	public static final String MAX_TEAM_MEMBERS_REACHED = "Max players invitable into your PMine has been reached. Upgrade to add more people";
	public static final String MAX_TEAM_MEMBERS_REACHED_SHORT = "Max players reached.";
	
	public static final String PLAYER_NOT_IN_TEAM = "Player requested is not in this PMine's team!";
	public static final String PLAYER_NOT_IN_TEAM_SHORT = "Player not in team.";
	
	public static final String PLAYER_EXIST_IN_TEAM = "Player requested exists in this PMine's team!";
	public static final String PLAYER_EXIST_IN_TEAM_SHORT = "Player is in team.";
	
	public static final String NOT_MINE_OWNER = "This process requires Owner permissions to execute";
	public static final String NOT_MINE_OWNER_SHORT = "Insufficient permission.";
	
	public static final String INVALID_SYNTAX = "Not a valid syntax. Use /pmine help";
	public static final String INVALID_SYNTAX_SHORT = "Syntax not valid.";
	
	public static final String INVALID_HOME = "This home ID is not valid.";
	public static final String INVALID_HOME_SHORT = "Invalid Home ID.";
	
	public static final String TOGGLE_AUTOMINER_ON = "Autominer has been turned §a§lON§7.";
	public static final String TOGGLE_AUTOMINER_ON_SHORT = "Autominer turned §a§lON§7.";
	
	public static final String TOGGLE_AUTOMINER_OFF = "Autominer has been turned §c§LOFF§7.";
	public static final String TOGGLE_AUTOMINER_OFF_SHORT = "Autominer turned §c§lOFF§7.";
	
	public static void playMessage(Player player, MESSAGE_TYPE type, boolean title) {
		switch(type)
		{
		case MINE_LOCKED_FOR_VISITORS:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', MINE_LOCKED_FOR_VISITORS_SHORT), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + MINE_LOCKED_FOR_VISITORS + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		
		case PLAYER_NOT_ONLINE:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', PLAYER_NOT_ONLINE_SHORT), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + PLAYER_NOT_ONLINE + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		
		case NOT_STANDING_ON_MINE:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', NOT_STANDING_ON_MINE), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + NOT_STANDING_ON_MINE + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case MAX_TEAM_MEMBERS_REACHED:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', MAX_TEAM_MEMBERS_REACHED), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + MAX_TEAM_MEMBERS_REACHED + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case PLAYER_NOT_IN_TEAM:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', PLAYER_NOT_IN_TEAM), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + PLAYER_NOT_IN_TEAM + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case PLAYER_EXIST_IN_TEAM:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', PLAYER_EXIST_IN_TEAM), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + PLAYER_EXIST_IN_TEAM + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case NOT_MINE_OWNER:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', NOT_MINE_OWNER), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + NOT_MINE_OWNER + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case INVALID_SYNTAX:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', INVALID_SYNTAX), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + INVALID_SYNTAX + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case INVALID_HOME:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', INVALID_HOME), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + INVALID_HOME + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case TOGGLE_AUTOMINER_ON:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', TOGGLE_AUTOMINER_ON), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + TOGGLE_AUTOMINER_ON + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		case TOGGLE_AUTOMINER_OFF:
			if(title)
				player.sendTitle(ChatColor.translateAlternateColorCodes('&', DEFAULT_TITLE_PREFIX),ChatColor.translateAlternateColorCodes('&', TOGGLE_AUTOMINER_OFF), 10, 40, 10);
			player.sendMessage(DEFAULT_PREFIX + TOGGLE_AUTOMINER_OFF + DEFAULT_SUFFIX);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.5f);
			break;
		}
	}
}
