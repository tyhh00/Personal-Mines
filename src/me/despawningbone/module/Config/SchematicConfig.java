package me.despawningbone.module.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.despawningbone.module.PERK_TYPE;
import me.despawningbone.modules.api.SimpleLocation;


public class SchematicConfig {
	
	String rawID;
	Material iconMat;
	short iconData;
	String iconName;
	List<String> iconLore;
	HashMap<PERK_TYPE, Float> perks;
	boolean autoDeploy;
	int minMineSize, maxMineSize;
	SCHEMGROUP_TYPE schemGrpType;
	
	SimpleLocation botLeftPos, topRightPos, center;
	
	public SchematicConfig() 
	{
		perks = new HashMap<PERK_TYPE, Float>();
		iconLore = new ArrayList<String>();
	}
	
	public SimpleLocation getBotLeftPos()
	{
		return botLeftPos;
	}
	
	public SimpleLocation getTopRightPos()
	{
		return topRightPos;
	}
	
	public SimpleLocation getCenterPos()
	{
		return center;
	}
	
	public String getRawID()
	{
		return rawID;
	}
	
	public SCHEMGROUP_TYPE getSchematicGroupType()
	{
		return schemGrpType;
	}
	
	public Material getIconMaterial()
	{
		return iconMat;
	}
	
	public short getIconData()
	{
		return iconData;
	}
	
	public String getIconName()
	{
		return iconName;
	}
	
	public List<String> getIconLore()
	{
		return iconLore;
	}
	
	public HashMap<PERK_TYPE, Float> getPerksList()
	{
		return perks;
	}
	
	public boolean autoDeployedOnReceive()
	{
		return autoDeploy;
	}
	
	public List<String> getRawPerksList()
	{
    	List<String> rawPerks = new ArrayList<String>();
    	for(Entry<PERK_TYPE, Float> loadedPerk : perks.entrySet())
    	{
    		rawPerks.add(loadedPerk.getKey().toString() + ";" + loadedPerk.getValue().toString());
    	}
    	return rawPerks;
	}
	
	public boolean hasPerk(PERK_TYPE type)
	{
		return perks.containsKey(type);
	}
	
	public float getValueOfPerk(PERK_TYPE type)
	{
		return perks.getOrDefault(type, 0.0f);
	}
	
	public boolean autoDeploy()
	{
		return autoDeploy;
	}
	
	public int getMinMineSize()
	{
		return this.minMineSize;
	}
	
	public int getMaxMineSize()
	{
		return this.maxMineSize;
	}
	
	
	
	//Setters
	
	public void setSchematicWorldLocation(SimpleLocation botLeftPos, SimpleLocation topRightPos, SimpleLocation center)
	{
		this.botLeftPos = botLeftPos;
		this.topRightPos = topRightPos;
		this.center = center;
	}
	
	public void setRawID(String ID)
	{
		this.rawID = ID;
	}
	
	public void setSchematicGroupType(SCHEMGROUP_TYPE type)
	{
		this.schemGrpType = type;
	}
	
	public void setIconMaterial(Material mat)
	{
		iconMat = mat;
	}
	
	public void setIconData(short data)
	{
		iconData = data;
	}
	
	public void setIconName(String name)
	{
		iconName = ChatColor.translateAlternateColorCodes('&', name);
	}
	
	public void setIconLore(List<String> lore)
	{
		this.iconLore.clear();
		for(String l : lore)
			this.iconLore.add(ChatColor.translateAlternateColorCodes('&', l));
	}
	
//	public void addPerk(PERK_TYPE perk, float val)
//	{
//		perks.put(perk, val);
//	}
//	
//	public void setPerks(HashMap<PERK_TYPE, Float> perks)
//	{
//		this.perks = perks;
//	}
	
	public void setAutoDeploy(boolean autoDeploy)
	{
		this.autoDeploy = autoDeploy;
	}
	
	public void setMinMineSize(int min)
	{
		this.minMineSize = min;
	}
	
	public void setMaxMineSize(int max)
	{
		this.maxMineSize = max;
	}
	
	public void addPerk(PERK_TYPE perk, float val)
	{
		perks.put(perk, val);
	}
	
	public void setPerks(HashMap<PERK_TYPE, Float> perks)
	{
		this.perks = perks;
	}
	
}
