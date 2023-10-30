package me.despawningbone.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.despawningbone.module.Config.MineFactoryConfig;
import me.despawningbone.module.Config.MineFactoryGroup;
import me.despawningbone.modules.api.Variables;

public class MineFactory implements Serializable {
	private static final long serialVersionUID = -962308415368344153L;

	final String mineFactoryID;
	
	boolean unlocked;
	
	boolean isUnlocking;
	long unlockedAtTime;
	
	//Commited Amount
	long commitedMoney,
	commitedTokens,
	commitedEnergy;
	
	List<CustomItem> commitedItems;
	
	HashMap<UUID, HashMap<String, Long>> contribution;
	
	long blocksMined;
	
	public MineFactory(String mineFactoryID)
	{
		MineFactoryConfig mfc = Mines.getMinesConfig().mineFactories.get(mineFactoryID);
		System.out.println("factoryID: " + mineFactoryID);
		if(mfc.isUnlockedByDefault())
		{
			unlocked = true;
			unlockedAtTime = 0L;
			isUnlocking = true;
		}
		this.contribution = new HashMap<UUID, HashMap<String,Long>>();
		this.mineFactoryID = mineFactoryID;
		this.commitedItems = new ArrayList<CustomItem>();
	}
	
	public boolean isUnlocking()
	{
		return this.isUnlocking;
	}
	
	public void addBlocksMined(int count)
	{
		blocksMined += count;
	}
	
	public long getBlocksMined()
	{
		return blocksMined;
	}
	
	public String getID()
	{
		return mineFactoryID;
	}
	
	public List<CustomItem> getCommitedItems()
	{
		return commitedItems;
	}
	
	public int getLevel()
	{
		try {
			return Integer.parseInt(this.mineFactoryID.split(";")[1]);
		}catch(Exception e)
		{
			return 0;
		}
	}
	
	public MineFactoryGroup getFactoryGroup()
	{
		return Mines.getMinesConfig().mineFactoryGroups.get(this.mineFactoryID.split(";")[0]);
	}
	
	public long getCommitedMoney()
	{
		return commitedMoney;
	}
	
	public long getCommitedTokens()
	{
		return commitedTokens;
	}
	
	public long getCommitedEnergy()
	{
		return commitedEnergy;
	}
	
	public long getTimeUnlockedAt()
	{
		if(isUnlocking)
			return unlockedAtTime;
		return 0;
	}
	
	public void addMoney(long amt)
	{
		commitedMoney += amt;
	}
	
	public void addTokens(long tokens)
	{
		commitedTokens += tokens;
	}
	
	public void addEnergy(long energy)
	{
		commitedEnergy += energy;
	}
	
	public void addCommitedItem(CustomItem toAdd)
	{
		for(CustomItem item : commitedItems)
		{
			if(item.isEqual(toAdd, false))
			{
				item.addQuantity(toAdd.getQuantity());
			}
		}
	}
	
	public boolean canEquip()
	{
		if(isUnlocking && System.currentTimeMillis() > unlockedAtTime)
		{
			unlocked = true;
			return unlocked;
		}
		return false;
	}
	
	public MineOreTable getFactoryComposition()
	{
		MineFactoryConfig mfc = Mines.getMinesConfig().mineFactories.get(mineFactoryID);
		return mfc.getMineComposition();
	}
	
	public HashMap<PERK_TYPE, Float> getFactoryPerks()
	{
		MineFactoryConfig mfc = Mines.getMinesConfig().mineFactories.get(mineFactoryID);
		return mfc.getPerksList();
		
	}
	
	public boolean forceUnlock()
	{
		if(!isUnlocking)
		{
			isUnlocking = true;
			unlockedAtTime = System.currentTimeMillis();
		}
		return false;
	}
	
	public boolean unlock(Player owner, long totalBlocksMined, long progressiveBlocksMined, Collection<MineFactory> unlockedFactories)
	{
		MineFactoryConfig mfc = Mines.getMinesConfig().mineFactories.get(mineFactoryID);
		
		if(!isUnlocking)
		{
			
			//Unlocked previous level
			if(this.getLevel() > 1)
			{
				if(unlockedFactories == null || unlockedFactories.isEmpty())
				{
					return false;
				}
				String prev = mfc.getPreviousLevel();
				if(prev != null) // has prev level
				{
					boolean found = false;
					
					for(MineFactory factory : unlockedFactories)
					{
						if(factory.getID().equalsIgnoreCase(prev))
						{
							found = true;
							break;
						}
					}
					if(!found)
					{
						return false;
					}	
			}
			
			}
			
			if(mfc.getMoneyRequired() <= commitedMoney
					&& mfc.getEnergyRequired() <= commitedEnergy
					&& mfc.getTokensRequired() <= commitedTokens)
			{
				//Leveling checks
				String var = (String) Variables.getPlayer(owner, "rankup");
				//System.out.println(var + " eee " + (var == null));
				if(var == null) {  //not needed since placeholder should always be the first to execute?
					Variables.setPlayer(owner, "rankup", "1;0;0");
					var = "1;0;0";
					//System.out.println("set");
				}
				String[] split = var.split(";");  //using ; because it will not compile to regex and is much faster
				int lv = Integer.parseInt(split[0]);
				int pres = Integer.parseInt(split[2]);
				if(lv < mfc.getOwnerLevelingLevelRequirement()
						|| pres < mfc.getOwnerLevelingPrestigeRequirement())
					{
						return false;
					}
				
				//Permission checks
				if(mfc.hasUnlockPermissionNode())
				{
					if(!owner.hasPermission(mfc.getUnlockPermissionNode()))
						{
							return false;	
						}
				}
				
				//Blocks Mined
				if(mfc.getTotalBlocksMinedRequired() > totalBlocksMined
						|| mfc.getProgressiveBlocksMinedRequired() > progressiveBlocksMined)
				{
					return false;
				}
				
				//Custom Items
				int itemsFufilled = 0;
				for(CustomItem mfc_item : mfc.getCustomItemsRequired())
				{
					for(CustomItem item : this.commitedItems)
					{
						if(mfc_item.isEqual(item, false))
						{
							//If item in here is more than the required
							if(item.getQuantity() >= mfc_item.getQuantity())
							{
								++itemsFufilled;
								break;
							}
						}
					}
				}
				if(itemsFufilled < mfc.getCustomItemsRequired().size())
				{
					return false;
				}
				
				
				//All checks successful
				isUnlocking = true;
				unlockedAtTime = System.currentTimeMillis() + mfc.getDurationToUnlock() * 1000;
			}	
		}
		
		return isUnlocking;
	}
	
}
