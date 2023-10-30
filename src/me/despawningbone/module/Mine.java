package me.despawningbone.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;

import me.despawningbone.module.Mines;
import me.despawningbone.modules.Platform;
import me.despawningbone.modules.api.Module;
import me.despawningbone.modules.api.SimpleLocation;


public class Mine implements Serializable {
	private static final long serialVersionUID = -6776859045952961863L;
	
	///Member Variables start
	
	static final String modName = "mines";
	
	String world;
	
	int mineCenterX, mineCenterZ;
	int mineSurfaceY;
	
	//Use these 2 to calculate mine minMax
	int mineXZRadius;
	int mineYLength;
	boolean mineIsExpanding;
	
	//Blocks in mine
	int currentBlocksInMine;
	
	boolean resetAtPercentage;
	float resetPercent;
	
	Long st_ResetIntervalCD; //System Time Reset Interval CD
	float resetIntervalCooldown;
	
	Long st_ForceResetCD;
	float forceResetCooldown;
	
	Long lastResetTime;
	
	HashMap<String, MineFactory> factories;
	String equippedFactory;
	
	//boolean mineLoaded;
	SimpleLocation spawnPoint;
	
	MineOreTable currentOreTable;
	
	///Member Variables end
	
	Mine(String world, int mineCenterX, int mineCenterZ, int mineSurfaceY, int XZRadius, int YLength) {
		this.world = world;
		this.mineCenterX = mineCenterX;
		this.mineCenterZ = mineCenterZ;
		this.mineSurfaceY = mineSurfaceY;
		this.mineXZRadius = XZRadius;
		this.mineYLength = YLength;
		
		this.currentBlocksInMine = 0;
		
		this.resetAtPercentage = true;
		this.resetPercent = 40.f;
		this.resetIntervalCooldown = 1200;
		this.st_ResetIntervalCD = System.currentTimeMillis() + (long)resetIntervalCooldown * 1000L;
		
		this.forceResetCooldown = 600;
		this.st_ForceResetCD = System.currentTimeMillis() + (long)forceResetCooldown * 1000L;
		
		this.mineIsExpanding = false;

		this.lastResetTime = System.currentTimeMillis();
		
		this.factories = new HashMap<String, MineFactory>();
		this.factories.put(PlayerMine.defaultFactory, new MineFactory(PlayerMine.defaultFactory));
		this.setMineFactory(PlayerMine.defaultFactory);
	}
	
	public Collection<MineFactory> getMineFactories()
	{
		return factories.values();
	}
	
	public boolean setMineFactory(String factoryID)
	{
		if(factories.containsKey(factoryID))
		{
			MineFactory factory = factories.get(factoryID);
			if(factory.canEquip())
			{
				this.equippedFactory = factoryID;
				this.currentOreTable = this.factories.get(this.equippedFactory).getFactoryComposition();
				return true;
			}
		}
		return false;
	}
	
	public MineFactory createOrGetMineFactory(String ID)
	{
		MineFactory search = this.factories.get(ID);
		if(search == null)
		{
			if(Mines.getMinesConfig().mineFactories.containsKey(ID))
			{
				MineFactory mf = new MineFactory(ID);
				this.factories.put(ID, mf);
				return mf;	
			}
			return null;
		}
		return search;
	}
	
	public MineFactory getMineFactorySelected()
	{
		return this.factories.get(this.equippedFactory);
	}
	
	public MineFactory getNextMineFactoryTier()
	{
		MineFactory current = this.getMineFactorySelected();
		String futureConfigName = current.getFactoryGroup().getGroupID() + ";" + String.valueOf((current.getLevel() + 1));
		MineFactory futureFactory = createOrGetMineFactory(futureConfigName);
		return futureFactory;
	}
	
	public int getMaxBlocksInMine() {
		return 2 * mineXZRadius * 2 * mineXZRadius * mineYLength;
	}
	
	public void setXZRadius(int xzRad)
	{
		this.mineXZRadius = xzRad;
	}
	
	public void setYRadius(int yRad)
	{
		this.mineYLength = yRad;
	}
	
	public int getCurrentBlocksRemaining() {
		return this.currentBlocksInMine;
	}
	
	public void setResetAtPercentage(boolean resetAtPercentage)
	{
		this.resetAtPercentage = resetAtPercentage;
	}
	
	public void setResetPercent(float percent)
	{
		if(percent < 10) percent = 10;
		else if (percent > 90) percent = 90;
		this.resetPercent = percent;
	}
	
	public void setForceResetCD(float cd)
	{
		this.forceResetCooldown = cd;
	}
	
	public void setAutoResetCD(float cd)
	{
		this.resetIntervalCooldown = cd;
	}
	
	public void breakBlocks(int amt, Platform platform) 
	{
		this.currentBlocksInMine -= amt;
		if(this.currentBlocksInMine < 0) this.currentBlocksInMine = 0;
		handleResetConditions(platform, RESET_TYPE.MINING,false);
	}
	

	
	/**
	 * Breaks blocks in Mine, should be called for blocks that are not broken through BBE.
	 * @param blocks
	 * @param strict - False to not check if block is in mine (More efficient if false)
	 */
	public void breakBlocks(List<Block> blocks, boolean strict, Platform platform)
	{
		int count = 0;
		if(strict)
		{
			for(Block block : blocks)
			{
				if(inMine(block)) count++;
			}
		}
		else
			count = blocks.size();
		
		this.currentBlocksInMine -= count;
		if(this.currentBlocksInMine < 0) this.currentBlocksInMine = 0;
		handleResetConditions(platform, RESET_TYPE.MINING,false);
	}

	public World getWorld() {
		return Bukkit.getWorld(world);
	}
	
	public float getMineResetPercentage() {
		return this.currentBlocksInMine / (float)this.getMaxBlocksInMine() * 100.0f;
	}
	
	public boolean handleResetConditions(Platform platform, RESET_TYPE type, boolean force)
	{
		if( (this.resetAtPercentage && this.getMineResetPercentage() <= this.resetPercent && type == RESET_TYPE.MINING) 
				|| (!this.resetAtPercentage && System.currentTimeMillis() > this.st_ResetIntervalCD && type == RESET_TYPE.AUTOMATIC)
				|| (System.currentTimeMillis() > this.st_ForceResetCD) && type == RESET_TYPE.MANUAL
				|| force)
		{
			Mines mod = (Mines) platform.getModule("mines");
			if(mod != null)
			{
				mod.resetMine(this);
				return true;	
			}
		}
		
		return false;
			
	}
	
	
	public int getMineSize()
	{
		return this.mineXZRadius * 2 + 1;
	}
	
	
	/**
	 * Will force a calculation, this calculation is slow and should not be used frequently.
	 * @return
	 */
	public int findTotalBlocksInMine() {
		int bCount = 0;
		List<Block> blocks = new ArrayList<Block>();
		Location min = this.getMinimumPoint();
		Location max = this.getMaximumPoint();
		
		for(int x = min.getBlockX(); x <= max.getBlockX(); ++x) 
		{
			for (int y = min.getBlockY(); y <= max.getBlockY(); ++y)
			{
				for (int z = min.getBlockY(); z <= max.getBlockZ(); ++z)
				{
					Material mat = min.add(x, y, z).getBlock().getType();
					if(mat != Material.AIR && mat != Material.BEDROCK) 
					{
						bCount++;
					}
				}
			}
		}
		
		return bCount;
		
	}
	
	public boolean inMine(Block block) {
		if(block.getX() >= mineCenterX-mineXZRadius && block.getX() <= mineCenterX+mineXZRadius 
				&& block.getZ() >= mineCenterZ-mineXZRadius && block.getZ() <= mineCenterZ+mineXZRadius 
				&& block.getY() <= mineSurfaceY && block.getY() >= mineSurfaceY-mineYLength)
			return true;
		return false;
	}
	
	public Location getMinimumPoint() {
		return new Location(Bukkit.getWorld(world), mineCenterX-mineXZRadius, mineSurfaceY-mineYLength, mineCenterZ-mineXZRadius);
	}
	
	public Location getMaximumPoint() {
		return new Location(Bukkit.getWorld(world), mineCenterX+mineXZRadius, mineSurfaceY, mineCenterZ+mineXZRadius);
	}
	
	public Location getMineSpawnPoint()
	{
		return new Location(Bukkit.getWorld(world), mineCenterX-mineXZRadius-2, this.mineSurfaceY + 1, this.mineCenterZ);
	}
	
	public boolean canForceResetMine()
	{
		if(System.currentTimeMillis() > this.st_ForceResetCD)
			return true;
		
		return false;
	}
	
	public Long getNextForceResetTime()
	{
		return this.st_ForceResetCD;
	}
	
	public Collection<Entity> getEntitiesInMine()
	{
		World world = Bukkit.getWorld(this.world);
		return world.getNearbyEntities(new Location(world, this.mineCenterX, this.mineSurfaceY - this.mineYLength/2, this.mineCenterZ) , this.mineXZRadius, this.mineYLength/2,this.mineXZRadius );
	}
	
	public boolean resetMine(boolean force, List<MineBlock> genTableBlockIDs) {
		
		World world = Bukkit.getWorld(this.world);
		int totalMaterial = getBlockCount();
		if(genTableBlockIDs.size() < totalMaterial) {
			System.out.println("Generated List of Materials is less than total blocks to reset!");
			return false;
		}
		
		Location mSpawnPoint = getMineSpawnPoint();
		for(Entity entity : getEntitiesInMine())
		{
			if(entity instanceof Player)
			{
				Player p = (Player) entity;
				p.teleport(mSpawnPoint);
			}
		}
		
		
		
		FaweQueue queue = FaweAPI.createQueue(
                FaweAPI.getWorld(world.getBlockAt(mineCenterX, 128, mineCenterZ).getWorld().getName()), false);
        queue.getRelighter().clear();
		int count = 0;
        for (int x = getMineCenterX() - getXZRadius(); x <= getMineCenterX() + getXZRadius(); x++) {
			for (int y = getMineYSurface(); y >= getMineYSurface() - getMineYLength() && y > 0; y--) {
				for (int z = mineCenterZ - getXZRadius(); z <= mineCenterZ + getXZRadius(); z++) {
					if(count < genTableBlockIDs.size())
						{
							MineBlock block = genTableBlockIDs.get(count++);
							if(block == null)
								queue.setBlock(x, y,z, Material.STONE.getId());
							else
								queue.setBlock(x, y, z, block.getMaterial().getId(), block.getData()); 	
						}
					else
						queue.setBlock(x, y, z, 0); 	
				}
			}
		}
        this.currentBlocksInMine = this.getMaxBlocksInMine();
        this.st_ResetIntervalCD = (long) this.resetIntervalCooldown * 1000 + System.currentTimeMillis();
        this.st_ForceResetCD = (long) this.forceResetCooldown * 1000 + System.currentTimeMillis();
        
        this.lastResetTime = System.currentTimeMillis();
        
        System.out.println(count + " <-");
		queue.flush();
		return true;
	}
	
	public Long getLastResetTime()
	{
		if(lastResetTime == null)
			lastResetTime = System.currentTimeMillis();
		return lastResetTime;
	}
	
	public int getBlockCount() {
		int totalMaterial = (mineXZRadius * 2 + 1) * (mineXZRadius * 2 + 1) * (mineYLength+1);
		return totalMaterial;
	}
	
	
	public MineOreTable getOreTable() {
		if(currentOreTable != null)
		{
			if(!currentOreTable.isEmptyOrDefault() && !currentOreTable.hasNullBlocks())
				return currentOreTable;
		}
		currentOreTable = this.factories.get(this.equippedFactory).getFactoryComposition();
		return this.factories.get(this.equippedFactory).getFactoryComposition();
	}
	
	public int getMineCenterX() {
		return mineCenterX;
	}
	
	public int getMineCenterZ() {
		return mineCenterZ;
	}
	
	public int getXZRadius() {
		return mineXZRadius;
	}
	
	public int getMineYSurface() {
		return mineSurfaceY;
	}
	
	public int getMineYLength() {
		return mineYLength;
	}
	
	public boolean mineIsExpanding() {
		return mineIsExpanding;
	}

}
