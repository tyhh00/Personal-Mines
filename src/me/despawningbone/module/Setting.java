package me.despawningbone.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class Setting implements Serializable {
	private static final long serialVersionUID = -757934882156558470L;

	final String settingID;
	String name;
	String lore; //Use %n to split line
	boolean enabled;
	
	public Setting clone()
	{
		return new Setting(settingID, name, lore, enabled);
	}
	
	public Setting(String settingID, String name, String lore, boolean enabled)
	{
		this.settingID = settingID;
		this.enabled = enabled;
		this.name = name;
		this.lore = lore;
	}
	
	public boolean getEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public String getName()
	{
		return ChatColor.translateAlternateColorCodes('&', name);
	}
	
	public List<String> getLore()
	{
		String[] split = lore.split("%n");
		List<String> lore = new ArrayList<String>();
		
		for(String s : split)
		{
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
		}
		
		return lore;
	}
	
	public String getID()
	{
		return settingID;
	}
	
}
