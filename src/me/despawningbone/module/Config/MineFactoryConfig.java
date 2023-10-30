package me.despawningbone.module.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;

import me.despawningbone.module.CustomItem;
import me.despawningbone.module.MineOreTable;
import me.despawningbone.module.Mines;
import me.despawningbone.module.PERK_TYPE;

/*
 * Used by PlayerMine class to manage the current mine factory of the PMine, upgradables and effects
 * 
 */

public class MineFactoryConfig {
	
	//ID
	final String mineFactoryID; //LEVEL1;1
	String requiredID;
	
	//Stats
	MineOreTable mineComposition;
	HashMap<PERK_TYPE, Float> perks;
	FactoryTier tier;
	
	//Equip requirements
	int ownerLevelingLevel;
	int ownerPrestigeLevel;
	String permissionNode; //Null means no permission node
	
	//To unlock requirements
	boolean unlockedByDefault;
	long totalBlocksMined; //Total blocks mined by the entire PMine
	long progressiveBlocksMined; //How many blocks mined in previous MineFactory level to be eligible to unlock this level
	
	long moneyRequired;
	long tokensRequired;
	long energyRequired;
	
	List<CustomItem> requiredItems;
	
	int durationToUnlock;
	
	public MineFactoryConfig(String ID)
	{
		mineFactoryID = ID;
		requiredID = null;
		
		
		perks = new HashMap<PERK_TYPE, Float>();
		tier = FactoryTier.COMMON;
		
		mineComposition = new MineOreTable();
		
		ownerLevelingLevel = 0;
		ownerPrestigeLevel = 0;
		permissionNode = null;
		
		unlockedByDefault = false;
		totalBlocksMined = 0L;
		progressiveBlocksMined = 0L;
		
		moneyRequired = 0L;
		tokensRequired = 0L;
		energyRequired = 0L;
		
		requiredItems = new ArrayList<CustomItem>();
		
		durationToUnlock = 0;
	}
	
	public void setRequiredID(String requiredID)
	{
		this.requiredID = requiredID;
	}
	
	public boolean hasPreviousLevel()
	{
		return (requiredID != null);
	}
	
	public String getPreviousLevel()
	{
		return requiredID;
	}
	
	public void setTier(FactoryTier tier)
	{
		this.tier = tier;
	}
	
	public void addPerk(PERK_TYPE perk, float val)
	{
		perks.put(perk, val);
	}
	
	public void setPerks(HashMap<PERK_TYPE, Float> perks)
	{
		this.perks = perks;
	}
	
	public void setMineComposition(MineOreTable composition)
	{
		this.mineComposition = composition;
	}
	
	public void setOwnerLevelRequirements(int prestige, int level)
	{
		if(prestige >= 0 & level >= 0)
		{
			this.ownerLevelingLevel = level;
			this.ownerPrestigeLevel = prestige;	
		}
	}
	
	public void setUnlockedByDefault(boolean ubd)
	{
		this.unlockedByDefault = ubd;
	}
	
	public void setUnlockPermissionNode(String node)
	{
		this.permissionNode = node;
	}
	
	public void setTotalBlocksMinedRequired(long tbm)
	{
		this.totalBlocksMined = tbm;
	}
	
	public void setProgressiveBlocksMined(long pbm)
	{
		this.progressiveBlocksMined = pbm;
	}
	
	public void setEconomyRequired(long money, long tokens, long energy)
	{
		this.moneyRequired = money;
		this.tokensRequired = tokens;
		this.energyRequired = energy;
	}
	
	public void setDurationToUnlock(int timeInSeconds)
	{
		this.durationToUnlock = timeInSeconds;
	}
	
	public void addCustomItemRequired(CustomItem required)
	{
		this.requiredItems.add(required);
	}
	
	public void setCustomitemRequired(List<CustomItem> cI)
	{
		this.requiredItems = cI;
	}
	
	//Getters
	public String getFactoryID()
	{
		return this.mineFactoryID;
	}
	
	public FactoryTier getFactoryTier()
	{
		return this.tier;
	}
	
	public MineOreTable getMineComposition()
	{
		return this.mineComposition;
	}
	
	public int getOwnerLevelingPrestigeRequirement()
	{
		return this.ownerPrestigeLevel;
	}
	
	public int getOwnerLevelingLevelRequirement()
	{
		return this.ownerLevelingLevel;
	}
	
	public boolean hasUnlockPermissionNode()
	{
		if(permissionNode != null)
			return true;
		return false;
	}
	
	public String getUnlockPermissionNode()
	{
		return this.permissionNode;
	}
	
	public boolean isUnlockedByDefault()
	{
		return this.unlockedByDefault;
	}
	
	public long getTotalBlocksMinedRequired()
	{
		return this.totalBlocksMined;
	}
	
	public long getProgressiveBlocksMinedRequired()
	{
		return this.progressiveBlocksMined;
	}
	
	public long getMoneyRequired()
	{
		return this.moneyRequired;
	}
	
	public long getTokensRequired()
	{
		return this.tokensRequired;
	}
	
	public long getEnergyRequired()
	{
		return this.energyRequired;
	}
	
	public List<CustomItem> getCustomItemsRequired()
	{
		return this.requiredItems;
	}
	
	public int getDurationToUnlock()
	{
		return this.durationToUnlock;
	}
	
	public HashMap<PERK_TYPE, Float> getPerksList()
	{
		return perks;
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
	
}
