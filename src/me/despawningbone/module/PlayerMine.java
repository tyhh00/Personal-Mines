package me.despawningbone.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;

import me.despawningbone.module.Events.PostChangeSchematicEvent;
import me.despawningbone.module.Utils.Pair;
import me.despawningbone.modules.Platform;
import me.despawningbone.modules.api.SimpleLocation;
import me.despawningbone.modules.api.Variables;

public class PlayerMine implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2293628182790092447L;
	static final Material DEFAULT_MINEICON = Material.GRASS;
	static final int DEFAULT_MEMBER_MAX = 3;
	static final String defaultFactory = "LEVEL1;1";
	
	static final int REQUIRED_WEEKLYBLOCKS_FOR_PUBLIC = 300000;
	
	String world;
	String schematicName;
	SimpleLocation schematicLocation;
	
	SimpleLocation spawnLocation;
	
	int minX, minZ;
	
	int centerX, centerZ;
	Mine mine;
	UUID ownerUUID;
	
	boolean openedForPublic;
	Long openedForPublicTill;
	Set<UUID> memberUUID;
	int maxMembers;
	
	boolean hasTax;
	double taxPercent;
	
	boolean mineLoaded;
	int mineCornerSpacing;
	
	Material mineIcon;
	short mineIconData;
	
	List<String> unlockedSchematics;
	Long lastChangedSchematic;
	int changeSchematicCD;
	String nextSchematic; //Used for Swapping Schematics
	
	double taxedMoney, taxedTokens, taxedEnergy;
	
	boolean publicPermanently;
	
	HashMap<String, StatTrack> trackingData;
	//Keep track:
	/*
	 * MinedDaily
	 * MinedWeekly
	 * MinedTotal
	 */
	
	SettingsManager settings;
	
	PlayerMine(World world, UUID ownerUUID, int pMineCenterX, int pMineCenterZ, int pMineMineSurfaceY, int mineXZRadius, int mineYLength, String schem, Location schematicLocation) {
		this.world = world.getName();
		this.ownerUUID = ownerUUID;
		this.centerX = pMineCenterX;
		this.centerZ = pMineCenterZ;
		this.minX = this.centerX - Grid.getMineRange()/2;
		this.minZ = this.centerZ - Grid.getMineRange()/2;
		
		this.spawnLocation = new SimpleLocation(world, this.centerX, 128, this.centerZ);
		
		this.mine = new Mine(this.world, pMineCenterX, pMineCenterZ, pMineMineSurfaceY, mineXZRadius, mineYLength);
		
		this.openedForPublic = false;
		this.memberUUID = new HashSet<UUID>();
		
		this.taxedMoney = 0;
		this.taxedTokens = 0;
		this.taxedEnergy = 0;
		
		this.publicPermanently = false;
		
		this.hasTax = true;
		this.taxPercent = 5.0;
		this.mineLoaded = false;
		this.mineCornerSpacing = 1;
		
		this.openedForPublicTill = 0L;
		
		this.maxMembers = DEFAULT_MEMBER_MAX;
		
		this.schematicName = schem;
		this.schematicLocation = new SimpleLocation(schematicLocation);
		this.unlockedSchematics = new ArrayList<String>();
		this.unlockedSchematics.add(schem);
		
		this.changeSchematicCD = 60;
		this.lastChangedSchematic = System.currentTimeMillis() - changeSchematicCD * 1000;
		
		this.mineIcon = DEFAULT_MINEICON;
		this.mineIconData = 0;
		
		this.trackingData = new HashMap<String, StatTrack>();
		this.trackingData.put("MinedDaily", new StatTrack(0, DURATION_INTERVAL.DAILY, true));
		this.trackingData.put("MinedWeekly", new StatTrack(0, DURATION_INTERVAL.WEEKLY, true));
		this.trackingData.put("MinedTotal", new StatTrack(0, DURATION_INTERVAL.CUSTOM, false));
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(ownerUUID);
		if(Variables.getPlayer(target, "PMineSchematics") == null)
			Variables.setPlayer(target, "PMineSchematics", new HashSet<String>());
		Set<String> schematics = (Set<String>) Variables.getPlayer(target, "PMineSchematics");
		for(String schematic : schematics)
		{
			if(!this.unlockedSchematics.contains(schematic))
			{
				this.unlockedSchematics.add(schematic);
			}
		}
		
		this.settings = new SettingsManager();
		
		//Todo test if create object, add new data, will it be null for objs that were created before the addition? if so thats ok.
		//Implement mine block break status
		
	}

	public void setPermanentPublic(boolean enabled)
	{
		this.publicPermanently = enabled;
	}
	
	public boolean getPermanentPublic()
	{
		return this.publicPermanently;
	}
	
	public SettingsManager getSettings()
	{
		if(this.settings == null)
		{
			this.settings = new SettingsManager();
		}
		return this.settings;
	}
	
	public float getPerkBonus(PERK_TYPE type)
	{
		float val = 0.f;
		if(Mines.getMinesConfig().schematics.containsKey(this.schematicName))
		{
			val += Mines.getMinesConfig().schematics.get(this.schematicName).getValueOfPerk(type);
		}
		val += this.mine.getMineFactorySelected().getFactoryPerks().getOrDefault(type, 0.f);
		
		//Add PMine Boosters code here as well
			
		return val;
	}
	
	public int getMaxMembers()
	{
		if(this.maxMembers < DEFAULT_MEMBER_MAX)
			this.maxMembers = DEFAULT_MEMBER_MAX;
		return this.maxMembers;
	}

	public void setPublic(boolean change)
	{
		this.openedForPublic = change;
	}
	
	public void setMaxMembers(int maxMembers)
	{
		this.maxMembers = maxMembers;
		if(this.maxMembers < DEFAULT_MEMBER_MAX)
			this.maxMembers = DEFAULT_MEMBER_MAX;
	}
	
	public boolean addMember(UUID uuid)
	{
		if(uuid != this.ownerUUID)
			return this.memberUUID.add(uuid);
		return false;
	}
	
	public boolean removeMember(UUID uuid)
	{
		return this.memberUUID.remove(uuid);
	}
	
	public Location getMineCenter()
	{
		return new Location(Bukkit.getWorld(world), this.centerX, 128, this.centerZ);
	}
	
	public boolean addToTracker(String trackingID, int count)
	{
		if(this.trackingData.containsKey(trackingID))
		{
			StatTrack track = this.trackingData.get(trackingID);
			track.addCount(count);
			this.trackingData.put(trackingID, track);
			
		}
		else
		{
			this.trackingData.put(trackingID, new StatTrack(count, DURATION_INTERVAL.CUSTOM, false));
		}
		
		return true;
	}
	
	public long getTracker(String trackingID)
	{
		if(this.trackingData.containsKey(trackingID))
		{
			StatTrack track = this.trackingData.get(trackingID);
			
			return track.getCount();
		}
		return -1;
	}
	
	//Clears area of mining, including bedrock
	public void clearMineSpace() {
		int mineCornerWidth = mineCornerSpacing + 1;
		FaweQueue queue = FaweAPI.createQueue(
                FaweAPI.getWorld(Bukkit.getWorld(world).getBlockAt(centerX, 128, centerZ).getWorld().getName()), false);
		queue.getRelighter().clear();
		for (int x = mine.getMineCenterX() - mine.getXZRadius() - mineCornerWidth; x <= mine.getMineCenterX() + mine.getXZRadius() + mineCornerWidth; x++) {
			for (int y = mine.getMineYSurface(); y >= mine.getMineYSurface() - mine.getMineYLength() - 1 && y > 0; y--) {
				for (int z = centerZ - mine.getXZRadius() - mineCornerWidth; z <= centerZ + mine.getXZRadius() + mineCornerWidth; z++) {
					queue.setBlock(x, y, z, 0); 
				}
			}
		}
		queue.flush();
	}
	
	public boolean areMineSchemChunksLoaded() {
		List<Chunk> chunks = getMineSchemChunks();
		for(Chunk chunk : chunks) {
			if(!chunk.isLoaded())
				return false;
		}
		return true;
	}
	
	public void setSpawnLocation(Location loc)
	{
		this.spawnLocation = new SimpleLocation(loc);
	}
	
	public Location getSpawnLocation()
	{
		return this.spawnLocation.getTrueLocation();
	}
	
	public List<Chunk> getMineSchemChunks() {
		List<Chunk> chunks = new ArrayList<Chunk>();

		World world = Bukkit.getWorld(this.world);
		Chunk center = world.getChunkAt(new Location(world, centerX, 128, centerZ));
		for(int x = -6; x <= 6; x++) {
			for(int z = -6; z <= 6; z++) {
				chunks.add(world.getChunkAt(center.getX()+x, center.getZ()+z));
			}
		}
		
		return chunks;
	}
	
	public List<Chunk> getMineSchemChunks(Pair<Integer, Integer> chunkRad) {
		List<Chunk> chunks = new ArrayList<Chunk>();

		World world = Bukkit.getWorld(this.world);
		Chunk center = world.getChunkAt(new Location(world, centerX, 128, centerZ));
		for(int x = -chunkRad.getKey(); x <= chunkRad.getKey(); x++) {
			for(int z = -chunkRad.getValue(); z <= chunkRad.getValue(); z++) {
				chunks.add(world.getChunkAt(center.getX()+x, center.getZ()+z));
			}
		}
		
		return chunks;
	}
	
	public String getCurrentSchematicName()
	{
		return this.schematicName;
	}
	
	public boolean hasSchematicUnlocked(String schem)
	{
		if(this.unlockedSchematics == null)
		{
			this.unlockedSchematics = new ArrayList<String>();
			this.unlockedSchematics.add(this.schematicName);
		}
		if(this.unlockedSchematics.isEmpty()) return false;
		for(String s : this.unlockedSchematics)
		{
			if(schem.equalsIgnoreCase(s))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean addSchematic(String schem)
	{
		if(Mines.getMinesConfig().schematics.containsKey(schem))
		{
			if(this.unlockedSchematics == null)
			{
				this.unlockedSchematics = new ArrayList<String>();
				this.unlockedSchematics.add(schem);
				return true;
			}
			else if (this.unlockedSchematics.isEmpty())
			{
				this.unlockedSchematics.add(schem);
				return true;
			}
			else if(!this.unlockedSchematics.contains(schem))
			{
				this.unlockedSchematics.add(schem);
				return true;
			}
		}
		return false;
	}
	
	//Schematic cooldown duration in seconds
	public int getSchematicCDDuration()
	{
		return this.changeSchematicCD;
	}
	
	//Returns System CurrentTime comparable Long of lastchanged schematic
	public Long getLastChangedSchematic()
	{
		//this var updates at LoadMine()
		return this.lastChangedSchematic;
	}
	
	public int getTimeTillSchemChangeReady()
	{
		Long difference = System.currentTimeMillis() - this.lastChangedSchematic;
		int secondsDiff = (int) (difference * 0.001);
		if(secondsDiff >= this.changeSchematicCD)
		{
			return 0;
		}
		else 
			return this.changeSchematicCD - secondsDiff;
	}
	
	public boolean isReadyToChangeSchematic()
	{
		if(this.getTimeTillSchemChangeReady() == 0)
			return true;
		return false;
	}
	
	public boolean isMineLoaded()
	{
		return mineLoaded;
	}
	
	public boolean hasNextSchematicLoaded()
	{
		return (nextSchematic != null);
	}
	
	public void setNextSchematic(String nextSchem)
	{
		nextSchematic = nextSchem;
	}
	
	public String getNextSchematic()
	{
		return nextSchematic;
	}
	
	public boolean unloadMine(Mines mines)
	{
		Long now = System.currentTimeMillis();
		if(mineLoaded)
		{
			LoadedSchematic schem = Mines.getLoadedSchematic(this.schematicName);
			schem.clearSchematicAt(Bukkit.getWorld(world), this.schematicLocation.getTrueLocation());
			this.mineLoaded = false;
			
			this.clearMineSpace();
			
			if(nextSchematic != null)
			{
				this.schematicName = nextSchematic;
				nextSchematic = null;
			}
			
			Long end = System.currentTimeMillis();
			System.out.println("Took " + (end-now) + "ms to unload schematic");
			
			return true;
		}
		
		return false;
	}
	
	public boolean loadMine(Mines mines) {
		if(!mineLoaded)
		{
			System.out.println("Pasting " + this.schematicName + " schematic");
			Long now = System.currentTimeMillis();
			
			Mines.loadSchemIntoMemory(this.schematicName);
			Bukkit.getServer().getPluginManager().callEvent(new PostChangeSchematicEvent(Bukkit.getOfflinePlayer(this.getOwnerUUID()), this, Mines.getMinesConfig().schematics.get(this.schematicName)));
			
			//System.out.println("REACHED??????");
			LoadedSchematic schem = Mines.loadedSchematics.get(this.schematicName);
			
			schem.pasteSchematic(Bukkit.getWorld(world), this.schematicLocation.getTrueLocation());
			
			
			clearMineSpace();
			buildBedrock();
			this.mineLoaded = true;
			mines.resetPMine(this);
			
			Long end = System.currentTimeMillis();
			System.out.println("Took " + (end-now) + "ms to load schematic");
			
			System.out.println("Min Location: " + this.minX + "x 128y " + this.minZ + "z");
			
			this.lastChangedSchematic = System.currentTimeMillis();
			
			return true;
		}
		return false;

	}
	
	public void buildBedrock() {
		
		int mineCornerWidth = mineCornerSpacing + 1;
		FaweQueue queue = FaweAPI.createQueue(
                FaweAPI.getWorld(Bukkit.getWorld(world).getBlockAt(centerX, 128, centerZ).getWorld().getName()), false);
        queue.getRelighter().clear();
        	
        	
        	int z;
        	
    		//2DView Top Of Mine
        	z = centerZ + mine.getXZRadius() + mineCornerWidth;
    		for (int x = mine.getMineCenterX() - mine.getXZRadius() - mineCornerWidth; x <= mine.getMineCenterX() + mine.getXZRadius() + mineCornerWidth; x++) {
    			for (int y = mine.getMineYSurface(); y >= mine.getMineYSurface() - mine.getMineYLength() && y > 0; y--)
    				queue.setBlock(x, y, z, 7); 
        	}	
    		
    		//2DView Bottom Of Mine
    		z = centerZ - mine.getXZRadius() - mineCornerWidth;
    		for (int x = mine.getMineCenterX() - mine.getXZRadius() - mineCornerWidth; x <= mine.getMineCenterX() + mine.getXZRadius() + mineCornerWidth; x++) {
    			for (int y = mine.getMineYSurface(); y >= mine.getMineYSurface() - mine.getMineYLength() && y > 0; y--)
    				queue.setBlock(x, y, z, 7); 
        	}
    		
    		int x;
    		//2DView Left Of Mine
    		x = centerX - mine.getXZRadius() - mineCornerWidth;
    		for (z = mine.getMineCenterZ() - mine.getXZRadius() - mineCornerWidth; z <= mine.getMineCenterZ() + mine.getXZRadius() + mineCornerWidth; z++) {
    			for (int y = mine.getMineYSurface(); y >= mine.getMineYSurface() - mine.getMineYLength() && y > 0; y--)
    				queue.setBlock(x, y, z, 7);
    		}
    		
    		//2DView Right Of Mine
    		x = centerX + mine.getXZRadius() + mineCornerWidth;
    		for (z = mine.getMineCenterZ() - mine.getXZRadius() - mineCornerWidth; z < mine.getMineCenterZ() + mine.getXZRadius() + mineCornerWidth; z++) {
    			for (int y = mine.getMineYSurface(); y >= mine.getMineYSurface() - mine.getMineYLength() && y > 0; y--)
    				queue.setBlock(x, y, z, 7);
    		}
    		
    		//3DView Bottom Of Mine
    		for (x = centerX - mine.getXZRadius() - mineCornerWidth; x <= centerX + mine.getXZRadius() + mineCornerWidth; x++) {
    			for (z = centerZ - mine.getXZRadius() - mineCornerWidth; z <= centerZ + mine.getXZRadius() + mineCornerWidth; z++) {
    				queue.setBlock(x, mine.getMineYSurface() - mine.getMineYLength() - 1, z, 7);
    			}
    		}

            queue.flush();
	}
	
	
	
	public Mine getMine() {
		return mine;
	}
	
	public boolean canEnterMine(Player player) {
		if(Mines.hasAdminBypassToMines(player)) {
			return true;
		}
		if(!isPublic()) {
			if(!isPartOfMine(player.getUniqueId()))
			{
				//System.out.println("Cannot enter mine?");
				return false;
			}
			//System.out.println(Bukkit.getPlayer(this.getOwnerUUID()).getName());
		}
		return true;
	}
	
	public boolean canMineInMine(Player player) {
		if(player.getUniqueId().equals(ownerUUID)) return true;
		if(!memberUUID.isEmpty()) {
			if(memberUUID.contains(player.getUniqueId())) {
				return true;
			}
		}
		if(Mines.hasAdminBypassToMines(player)) {
			return true;
		
		}
		if(isPublic()) {
			return true;
		}
		return false;
	}
	
	public boolean canEditDecoration(Player player) {
		if(!settings.getBooleanValue("ExteriorEdit"))
		{
			return false;
		}
		if(Mines.hasAdminBypassToMines(player)) {
			return true;
		}

		if(isPartOfMine(player.getUniqueId()))
			return true;
		return false;
	}
	
	public boolean canMine(Player player) {
		if(player.getUniqueId().equals(ownerUUID)) return true;
		if(!memberUUID.isEmpty()) {
			if(memberUUID.contains(player.getUniqueId())) {
				return true;
			}
		}
		if(Mines.hasAdminBypassToMines(player)) {
			return true;
		}
		return false;
	}
	
	public Set<UUID> getMembers() {
		return this.memberUUID;
	}
	
	public boolean isMemberOfMine(UUID player) {
		if(Mines.hasAdminBypassToMines(Bukkit.getPlayer(player))) {
			return true;
		}
		if(!memberUUID.isEmpty() && memberUUID.contains(player))
			return true;
		return false;
	}
	
	public boolean isPartOfMine(UUID player) {
		if(Mines.hasAdminBypassToMines(Bukkit.getPlayer(player))) {
			return true;
		}
		if(!memberUUID.isEmpty() && memberUUID.contains(player))
			return true;
		
		else if(ownerUUID.equals(player)) {
			return true;
		}
		return false;
	}
	
	public Collection<Entity> getEntitiesInPMine()
	{
		World world = Bukkit.getWorld(this.world);
		return world.getNearbyEntities(
				new Location(world, this.centerX, 128, this.centerZ) 
				, this.centerX-this.minX, 128, this.centerZ-this.minZ);
	}
	
	public void setMineIcon(Material mat)
	{
		this.mineIcon = mat;
	}
	
	public Material getMineIcon()
	{
		if(this.mineIcon == null)
			return DEFAULT_MINEICON;
		return this.mineIcon;
	}
	
	public short getMineIconData()
	{
		return this.mineIconData;
	}
	
	public boolean isPublic() {
		if(settings == null) settings = new SettingsManager();
		
		if(settings.getBooleanValue("Public") && !this.publicPermanently && this.trackingData.get("MinedWeekly").getCount() < PlayerMine.REQUIRED_WEEKLYBLOCKS_FOR_PUBLIC  )
		{
			//Bukkit.getPlayer("coolfire02").sendMessage("toggled off???? for " + Bukkit.getOfflinePlayer(this.ownerUUID).getName());
			if(settings.getBooleanValue("Public"))
			{
				settings.updateBooleanValue("Public", false);
			}
		}
		
		return settings.getBooleanValue("Public");
	}
	
	public void resetSettings()
	{
		settings = new SettingsManager();
	}
	
	public UUID getOwnerUUID() {
		return ownerUUID;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(world);
	}
	
	public int getMinX() {
		return minX;
	}
	
	public int getMinZ() {
		return minZ;
	}
	
	public int getCenterX() {
		return centerX;
	}
	
	public int getCenterZ() {
		return centerZ;
	}
	
	public double getTaxedAmount(TAX_TYPE type)
	{
		switch(type)
		{
		case TOKEN:
			return taxedTokens;
		case MONEY:
			return taxedMoney;
		case ENERGY:
			return taxedEnergy;
		}
		return 0.0;
	}
	
	public void addTaxedAmount(TAX_TYPE type, double amt)
	{
		if(amt < 0) return;
		
		switch(type)
		{
		case TOKEN:
			taxedTokens += amt;
			break;
		case MONEY:
			taxedMoney += amt;
			break;
		case ENERGY:
			taxedEnergy += amt;
			break;
		}
	}
	
	public void setTaxedAmount(TAX_TYPE type, double amt)
	{
		if(amt < 0) return;
		switch(type)
		{
		case TOKEN:
			taxedTokens = amt;
			break;
		case MONEY:
			taxedMoney = amt;
			break;
		case ENERGY:
			taxedEnergy = amt;
			break;
		}
	}
}
