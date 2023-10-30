package me.despawningbone.module.Config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MineFactoryGroup {
	final String groupID;
	
	//Display
	Material material;
	short data;
	String displayName;
	List<String> lore = new ArrayList<String>();
	
	int maxLevel;
	
	public MineFactoryGroup(String ID)
	{
		groupID = ID;
	}
	
	public void setMaterial(Material m)
	{
		this.material = m;
	}
	
	public void setData(short data)
	{
		this.data = data;
	}
	
	public void setDisplayName(String displayName)
	{
		this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
	}
	
	public void setLore(List<String> lore)
	{
		this.lore.clear();
		for(String l : lore)
			this.lore.add(ChatColor.translateAlternateColorCodes('&', l));
	}
	
	public void setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
	}
	
	public String getGroupID() {
		return groupID;
	}
	
	public Material getIconMaterial() {return material;}
	public short getIconData() {return data;}
	public String getIconDisplayName() {return displayName;}
	public List<String> getIconLore() {return lore;}
	public int getMaxLevel() {return maxLevel;}
}
