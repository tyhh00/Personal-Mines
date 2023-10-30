package me.despawningbone.module.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import me.despawningbone.module.CustomItem;
import me.despawningbone.module.MineBlock;
import me.despawningbone.module.MineFactory;
import me.despawningbone.module.MineOreTable;
import me.despawningbone.module.PERK_TYPE;
import me.despawningbone.modules.Config;
import me.despawningbone.modules.api.SimpleLocation;

public class ConfigHandler {
	
	Config config;
	public HashMap<String, SchematicConfig> schematics = new HashMap<String, SchematicConfig>();
	public HashMap<String, MineFactoryConfig> mineFactories = new HashMap<String, MineFactoryConfig>();
	public HashMap<String, MineFactoryGroup> mineFactoryGroups = new HashMap<String, MineFactoryGroup>();
	public TreeMap<Integer, MineExpansionConfig> mineHeightExpansion = new TreeMap<Integer, MineExpansionConfig>();
	public TreeMap<Integer, MineExpansionConfig> mineWidthExpansion = new TreeMap<Integer, MineExpansionConfig>();
	
	
	private static boolean DEBUG = true;
	
    public ConfigHandler(Config config) {
    	this.config = config;
        getConfigValues(config.getFileConfiguration());
        
        config.toggleLiveReload(true);
        config.setReloadCallback((fc) -> {
            getConfigValues(fc);
        });
    }
    
    public void getConfigValues(FileConfiguration fc) {
        System.out.println("Reloading PlayerMines Config");
        
        String configHeader;
        
        configHeader = "MineExpansion.Height";
        Set<String> heightExpansions = fc.getConfigurationSection(configHeader).getKeys(false);
        for(String item : heightExpansions)
        {
        	
        	String configLoc = configHeader + "." + item;
        	String subLoc;
        	
        	//Bukkit.broadcastMessage("loading height " + configLoc);
        	
        	boolean valid = true;
        	
        	int newExpansionValue = 0;
        	int ownerPrestige = 0;
        	int ownerLeveling = 0;
        	double money = 0;
        	
        	subLoc = "Height";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			newExpansionValue = fc.getInt(configLoc + "." + subLoc); 
    		}
    		else
    		{
    			valid = false;
    		}
    		
    		subLoc = "Owner_Leveling_Prestige";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			ownerPrestige = fc.getInt(configLoc + "." + subLoc); 
    		}
    		
    		subLoc = "Owner_Leveling_Level";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			ownerPrestige = fc.getInt(configLoc + "." + subLoc); 
    		}
    		
    		subLoc = "Money";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			money = fc.getDouble(configLoc + "." + subLoc); 
    		}
    		
    		if(valid)
    		{
    			mineHeightExpansion.put(newExpansionValue, new MineExpansionConfig(newExpansionValue, ownerPrestige, ownerLeveling, money));
    		}else
    		{
    			LogConfigIssue("Failed to load a Height Expansion config value. Skipping...", configLoc);
    		}
        }
        
        configHeader = "MineExpansion.Width";

        Set<String> widthExpansions = fc.getConfigurationSection(configHeader).getKeys(false);
        for(String item : widthExpansions)
        {
        	String configLoc = configHeader + "." + item;
        	String subLoc;
        	
        	boolean valid = true;
        	
        	int newExpansionValue = 0;
        	int ownerPrestige = 0;
        	int ownerLeveling = 0;
        	double money = 0;
        	
        	subLoc = "Width";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			newExpansionValue = fc.getInt(configLoc + "." + subLoc); 
    		}
    		else
    		{
    			valid = false;
    		}
    		
    		subLoc = "Owner_Leveling_Prestige";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			ownerPrestige = fc.getInt(configLoc + "." + subLoc); 
    		}
    		
    		subLoc = "Owner_Leveling_Level";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			ownerLeveling = fc.getInt(configLoc + "." + subLoc); 
    		}
    		
    		subLoc = "Money";
    		if(fc.contains(configLoc + "." + subLoc))
    		{
    			money = fc.getDouble(configLoc + "." + subLoc); 
    		}
    		
    		if(valid)
    		{
    			mineWidthExpansion.put(newExpansionValue, new MineExpansionConfig(newExpansionValue, ownerPrestige, ownerLeveling, money));
    		}else
    		{
    			LogConfigIssue("Failed to load a Width Expansion config value. Skipping...", configLoc);
    		}
        }
        
        configHeader = "MineFactories";
        Set<String> mineFactory = fc.getConfigurationSection(configHeader).getKeys(false);
        for(String item : mineFactory)
        {
        	String configLoc = configHeader + "." + item;
        	String subLoc;
        	
        	Material m;
        	short data;
        	String name;
        	List<String> lore = new ArrayList<String>();
        	
        	subLoc = "Levels";
        	Set<String> factoryLevels = fc.getConfigurationSection(configLoc + "." + subLoc).getKeys(false);
        	int maxLevels = 0;
        	for(String lvl : factoryLevels)
        	{
        		++maxLevels;
        		
        		//System.out.println("Generating: " + item + ";" + lvl);
        		
        		String lvlLoc = configLoc + "." + subLoc + "." + lvl;
        		
        		MineOreTable mineComposition = new MineOreTable();
        		HashMap<PERK_TYPE, Float> perks = new HashMap<PERK_TYPE, Float>();
        		boolean unlockedByDefault;
        		int requiredOwnerLevelingLevel, requiredOwnerLevelingPrestige;
        		long totalBlocksMined, progressiveBlocksMined,
        		moneyRequired, tokensRequired, energyRequired;
        		List<CustomItem> customItemsRequired = new ArrayList<CustomItem>();
        		int unlockDuration;
        		
        		String lvlSubLoc;
        		
        		//Blocks
        		lvlSubLoc = "Blocks";
        		if(fc.contains(lvlLoc + "." + lvlSubLoc))
        		{
        			List<String> factoryBlocks = fc.getStringList(lvlLoc + "." + lvlSubLoc);	
        			for(String block : factoryBlocks)
        			{
        				String[] split = block.split(";");
        				String unparsedBlock = split[0];
        				Material b_m;
        				short b_data = 0;
        				if(unparsedBlock.contains("."))
        				{
        					//Bukkit.broadcastMessage(unparsedBlock);
        					String[] parsed = unparsedBlock.split("\\.");
        					b_m = Material.valueOf(parsed[0].toUpperCase());
        					b_data = Short.parseShort(parsed[1]);
        				}
        				else
        				{
        					b_m = Material.valueOf(split[0].toUpperCase());
        				}
        				mineComposition.addBlock(new MineBlock(b_m, b_data), Float.parseFloat(split[1]));
        			}
        		}
            	else
            	{
            		mineComposition.addBlock(new MineBlock(Material.STONE,(short)0), 1.0);
            		LogConfigIssue("Failed to load " + lvlSubLoc +  " for item", lvlLoc);
            	}
        		
        		//Perks
        		lvlSubLoc = "Perks";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		List<String> perkList = fc.getStringList(lvlLoc + "." + lvlSubLoc);
            		for(String perk : perkList)
            		{
            			String[] split = perk.split(";");
            			PERK_TYPE perkType = PERK_TYPE.PERK_COUNT;
            			float perkMulti = 0.f;
            			try
            			{
            				perkType = PERK_TYPE.valueOf(split[0]);
            			}catch(IllegalArgumentException e)
            			{
            				LogConfigIssue("Failed to load perk: " + perk + "'s PERK TYPE", lvlLoc);
            				
            			}
            			try 
            			{
            				perkMulti = Float.parseFloat(split[1]);
            			}catch(NumberFormatException e)
            			{
            				LogConfigIssue("Failed to load perk: " + perk + "'s value", lvlLoc);
            				
            			}
            			
            			perks.put(perkType, perkMulti);
            		}
            	}
            		
            	else
            	{
            		LogConfigIssue("Failed to load " + lvlSubLoc + " for item", lvlLoc);
            	}
            	
            	
            	lvlSubLoc = "Unlocking.UnlockedByDefault";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		unlockedByDefault = fc.getBoolean(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		unlockedByDefault = false;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.Owner_Leveling_Level";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		requiredOwnerLevelingLevel = fc.getInt(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		requiredOwnerLevelingLevel = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.Owner_Leveling_Prestige";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		requiredOwnerLevelingPrestige = fc.getInt(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		requiredOwnerLevelingPrestige = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.TotalBlocksMined";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		totalBlocksMined = fc.getLong(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		totalBlocksMined = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.ProgressiveBlocksMined";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		progressiveBlocksMined = fc.getLong(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		progressiveBlocksMined = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.Money";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		moneyRequired = fc.getLong(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		moneyRequired = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.Token";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		tokensRequired = fc.getLong(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		tokensRequired = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
        		
            	lvlSubLoc = "Unlocking.Energy";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		energyRequired = fc.getLong(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		energyRequired = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.UnlockDuration";
            	if(fc.contains(lvlLoc + "." + lvlSubLoc)) 
            	{
            		unlockDuration = fc.getInt(lvlLoc + "." + lvlSubLoc);
            	}
            	else
            	{
            		unlockDuration = 0;
            		LogConfigIssue("Failed to load " + lvlLoc +  " for item", lvlSubLoc);
            	}
            	
            	lvlSubLoc = "Unlocking.CustomItem";
            	if(fc.isConfigurationSection(lvlLoc + "." + lvlSubLoc))
            	{
            		Set<String> customItems = fc.getConfigurationSection(lvlLoc + "." + lvlSubLoc).getKeys(false);
                	for(String customItem : customItems)
                	{
                		String cItemLoc = lvlLoc + "." + lvlSubLoc + "." + customItem;
                		
                		String cI_ID;
                		String cI_displayName;
                		List<String> cI_lore = new ArrayList<String>();
                		Material cI_material;
                		short cI_data;
                		int cI_requiredQuantity;
                		
                		String cItemSubLoc;
                		
                		CustomItem cI_toAdd;
                		
                		cItemSubLoc = "ID";
                    	if(fc.contains(cItemLoc + "." + cItemSubLoc)) 
                    	{
                    		cI_ID = fc.getString(cItemLoc + "." + cItemSubLoc);
                    	}
                    	else
                    	{
                    		cI_ID = "UNDEFINED";
                    		LogConfigIssue("Failed to load " + cItemLoc +  " for item", cItemSubLoc);
                    	}
                    	
                    	cItemSubLoc = "Name";
                    	if(fc.contains(cItemLoc + "." + cItemSubLoc)) 
                    	{
                    		cI_displayName = fc.getString(cItemLoc + "." + cItemSubLoc);
                    	}
                    	else
                    	{
                    		cI_displayName = "UNDEFINED";
                    		LogConfigIssue("Failed to load " + cItemLoc +  " for item", cItemSubLoc);
                    	}
                    	
                    	cItemSubLoc = "Lore";
                    	if(fc.contains(cItemLoc + "." + cItemSubLoc)) 
                    	{
                    		cI_lore = fc.getStringList(cItemLoc + "." + cItemSubLoc);
                    	}
                    	else
                    	{
                    		LogConfigIssue("Failed to load " + cItemLoc +  " for item", cItemSubLoc);
                    	}
                    	
                    	cItemSubLoc = "Material";
                    	if(fc.contains(cItemLoc + "." + cItemSubLoc)) 
                    	{
                    		cI_material = Material.valueOf(fc.getString(cItemLoc + "." + cItemSubLoc));
                    	}
                    	else
                    	{
                    		cI_material = Material.STONE;
                    		LogConfigIssue("Failed to load " + cItemLoc +  " for item", cItemSubLoc);
                    	}
                    	
                    	cItemSubLoc = "Data";
                    	if(fc.contains(cItemLoc + "." + cItemSubLoc)) 
                    	{
                    		cI_data = (short) fc.getInt(cItemLoc + "." + cItemSubLoc);
                    	}
                    	else
                    	{
                    		cI_data = 0;
                    		LogConfigIssue("Failed to load " + cItemLoc +  " for item", cItemSubLoc);
                    	}
                    	
                    	cItemSubLoc = "Quantity";
                    	if(fc.contains(cItemLoc + "." + cItemSubLoc)) 
                    	{
                    		cI_requiredQuantity = fc.getInt(cItemLoc + "." + cItemSubLoc);
                    	}
                    	else
                    	{
                    		cI_requiredQuantity = 0;
                    		LogConfigIssue("Failed to load " + cItemLoc +  " for item", cItemSubLoc);
                    	}
                    	
                		cI_toAdd = new CustomItem(cI_ID, cI_material, cI_data, cI_displayName, cI_lore, cI_requiredQuantity);
                    	customItemsRequired.add(cI_toAdd);

                	}
            	}
            	
            	MineFactoryConfig mfc = new MineFactoryConfig(item + ";" + lvl);
        		mfc.setMineComposition(mineComposition);
        		mfc.setPerks(perks);
        		mfc.setUnlockedByDefault(unlockedByDefault);
        		mfc.setOwnerLevelRequirements(requiredOwnerLevelingPrestige, requiredOwnerLevelingLevel);
        		mfc.setTotalBlocksMinedRequired(totalBlocksMined);
        		mfc.setProgressiveBlocksMined(progressiveBlocksMined);
        		mfc.setEconomyRequired(moneyRequired, tokensRequired, energyRequired);
        		mfc.setCustomitemRequired(customItemsRequired);
        		mfc.setDurationToUnlock(unlockDuration);
        		
        		
        		String prev = item + ";" + String.valueOf(Integer.parseInt(lvl)-1);
        		if(mineFactories.containsKey(prev))
        		{
        			mfc.setRequiredID(prev);
        		}
        		System.out.println("Adding: " + item + ";" + lvl + " to list");
        		
        		mineFactories.put(item+";"+lvl, mfc);
            }
            	
        	
        	//Mat
        	subLoc = "Material";
        	if(fc.contains(configLoc + "." + subLoc))
        		m = Material.getMaterial(fc.getString(configLoc + "." + subLoc));
        	else
        	{
        		m = Material.STONE;
        		LogConfigIssue("Failed to load " + subLoc +  " for item", configLoc);
        	}
        	
        	//Data
        	subLoc = "Data";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		data = (byte) fc.getInt(configLoc + "." + subLoc);
        	else
        	{
        		data = 0;
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Name";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		name = fc.getString(configLoc + "." + subLoc);
        	else
        	{
        		name = "UNDEFINED";
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Lore";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		lore.add(fc.getString(configLoc + "." + subLoc));
        	else
        	{
        		lore.add("ERROR WITH LORE");
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	MineFactoryGroup mfg = new MineFactoryGroup(item);
        	mfg.setMaterial(m);
        	mfg.setData(data);
        	mfg.setDisplayName(name);
        	mfg.setLore(lore);
        	mfg.setMaxLevel(maxLevels);
        	mineFactoryGroups.put(item, mfg);
        }
        
        configHeader = "Schematics";
        Set<String> schematics = fc.getConfigurationSection(configHeader).getKeys(false);
        for (String item : schematics)
        {
        	String configLoc = configHeader + "." + item;
        	String subLoc;
        	
        	//Schem attributes
        	Material m;
        	short data;
        	String name;
        	List<String> lore = new ArrayList<String>();
        	HashMap<PERK_TYPE, Float> perks = new HashMap<PERK_TYPE, Float>();
        	boolean autoDeploy;
        	int minMineSize = 10, maxMineSize = 45;
        	SCHEMGROUP_TYPE grpType = SCHEMGROUP_TYPE.DEFAULT;
        	
        	SimpleLocation botLeftPos, topRightPos, center;
        	botLeftPos = topRightPos = center = null;
        	
        	//Mat
        	subLoc = "Material";
        	if(fc.contains(configLoc + "." + subLoc))
        		m = Material.getMaterial(fc.getString(configLoc + "." + subLoc));
        	else
        	{
        		m = Material.STONE;
        		LogConfigIssue("Failed to load " + subLoc +  " for item", configLoc);
        	}
        	
        	//Data
        	subLoc = "Data";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		data = (byte) fc.getInt(configLoc + "." + subLoc);
        	else
        	{
        		data = 0;
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Name";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		name = fc.getString(configLoc + "." + subLoc);
        	else
        	{
        		name = "UNDEFINED";
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Lore";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		lore = fc.getStringList(configLoc + "." + subLoc);
        	else
        	{
        		lore.add("ERROR WITH LORE");
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Perks";
        	if(fc.contains(configLoc + "." + subLoc)) 
        	{
        		List<String> perkList = fc.getStringList(configLoc + "." + subLoc);
        		for(String perk : perkList)
        		{
        			String[] split = perk.split(";");
        			PERK_TYPE perkType = PERK_TYPE.PERK_COUNT;
        			float perkMulti = 0.f;
        			try
        			{
        				perkType = PERK_TYPE.valueOf(split[0]);
        			}catch(IllegalArgumentException e)
        			{
        				LogConfigIssue("Failed to load perk: " + perk + "'s PERK TYPE", configLoc);
        				continue;
        			}
        			try 
        			{
        				perkMulti = Float.parseFloat(split[1]);
        			}catch(NumberFormatException e)
        			{
        				LogConfigIssue("Failed to load perk: " + perk + "'s value", configLoc);
        				continue;
        			}
        			
        			perks.put(perkType, perkMulti);
        		}
        	}
        		
        	else
        	{
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	
        	subLoc = "AutoDeployWhenReceived";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		autoDeploy = fc.getBoolean(configLoc + "." + subLoc);
        	else
        	{
        		autoDeploy = false;
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	
        	subLoc = "SupportedMineSize.MinSize";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		minMineSize = fc.getInt(configLoc + "." + subLoc);
        	else
        	{
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	
        	subLoc = "SupportedMineSize.MaxSize";
        	if(fc.contains(configLoc + "." + subLoc)) 
        		maxMineSize = fc.getInt(configLoc + "." + subLoc);
        	else
        	{
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "SchemGroupType";
        	if(fc.contains(configLoc + "." + subLoc)) 
        	{
        		try
        		{
        			grpType = SCHEMGROUP_TYPE.valueOf(fc.getString(configLoc + "." + subLoc));
        		}catch(IllegalArgumentException e)
        		{
        			LogConfigIssue("Failed to load " + subLoc + " for item. " + fc.getString(configLoc + "." + subLoc) + " not a valid group type." , configLoc);
        		}
        	}
        	else
        	{
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Location.BotLeftPos";
        	if(fc.contains(configLoc + "." + subLoc)) 
        	{
        		try
        		{
        			botLeftPos = new SimpleLocation(fc.getString(configLoc + "." + subLoc));
        		}catch(IllegalArgumentException e)
        		{
        			LogConfigIssue("Failed to load " + subLoc + " for item. " + fc.getString(configLoc + "." + subLoc) + " not a valid group type." , configLoc);
        		}
        	}
        	else
        	{
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Location.TopRightPos";
        	if(fc.contains(configLoc + "." + subLoc)) 
        	{
        		try
        		{
        			topRightPos = new SimpleLocation(fc.getString(configLoc + "." + subLoc));
        		}catch(IllegalArgumentException e)
        		{
        			LogConfigIssue("Failed to load " + subLoc + " for item. " + fc.getString(configLoc + "." + subLoc) + " not a valid group type." , configLoc);
        		}
        	}
        	else
        	{
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	subLoc = "Location.Center";
        	if(fc.contains(configLoc + "." + subLoc)) 
        	{
        		try
        		{
        			center = new SimpleLocation(fc.getString(configLoc + "." + subLoc));
        		}catch(IllegalArgumentException e)
        		{
        			LogConfigIssue("Failed to load " + subLoc + " for item. " + fc.getString(configLoc + "." + subLoc) + " not a valid group type." , configLoc);
        		}
        	}
        	else
        	{
        		LogConfigIssue("Failed to load " + subLoc + " for item", configLoc);
        	}
        	
        	//Finished Loading all attributes of Schematic Config
        	SchematicConfig s_config = new SchematicConfig();
        	s_config.setRawID(item);
        	s_config.setIconMaterial(m);
        	s_config.setIconData(data);
        	s_config.setIconName(name);
        	s_config.setIconLore(lore);
        	s_config.setPerks(perks);
        	s_config.setAutoDeploy(autoDeploy);
        	s_config.setMinMineSize(minMineSize);
        	s_config.setMaxMineSize(maxMineSize);
        	s_config.setSchematicGroupType(grpType);
        	s_config.setSchematicWorldLocation(botLeftPos, topRightPos, center);
        	this.schematics.put(item, s_config);
        	
        }
    }
    
    public void SaveSchematicConfig(SchematicConfig sc)
    {
    	String configHeader = "Schematics." + sc.getRawID() + ".";
    	String subLoc;
    	
    	subLoc = "Material";
    	config.setValue(configHeader + subLoc, sc.getIconMaterial().toString());
    	
    	subLoc = "Data";
    	config.setValue(configHeader + subLoc, ""+sc.getIconData());
    	
    	subLoc = "Name";
    	config.setValue(configHeader + subLoc, sc.getIconName());
    	
    	subLoc = "Lore";
    	config.setValue(configHeader + subLoc, sc.getIconLore());
    	
    	subLoc = "Perks";
    	config.setValue(configHeader + subLoc, sc.getRawPerksList());
    	
    	subLoc = "AutoDeployWhenReceived";
    	config.setValue(configHeader + subLoc, ""+sc.autoDeployedOnReceive());
    	
    	subLoc = "SupportedMineSize.MinSize";
    	config.setValue(configHeader + subLoc, ""+sc.getMinMineSize());
    	
    	subLoc = "SupportedMineSize.MaxSize";
    	config.setValue(configHeader + subLoc, ""+sc.getMaxMineSize());
    	
    	subLoc = "SchemGroupType";
    	config.setValue(configHeader + subLoc, sc.getSchematicGroupType().toString());
    	
    	subLoc = "Location.BotLeftPos";
    	config.setValue(configHeader + subLoc, sc.getBotLeftPos().getSerializedString());
    	
    	subLoc = "Location.TopRightPos";
    	config.setValue(configHeader + subLoc, sc.getTopRightPos().getSerializedString());
    	
    	subLoc = "Location.Center";
    	config.setValue(configHeader + subLoc, sc.getCenterPos().getSerializedString());
    }
    
    private void LogConfigIssue(String issue, String configLoc)
    {
    	if(DEBUG)
    	{
    		System.out.println("PlayerMines, Config Issue:");
    		System.out.println("\"" + issue + "at config location: " + configLoc + "\"");
    	}
    }
}















