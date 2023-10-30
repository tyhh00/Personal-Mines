package me.despawningbone.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.despawningbone.module.Utils.Pair;
import me.despawningbone.modules.api.SimpleLocation;


public class Schematic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4642861671853785649L;
	SimpleLocation botLeftPos, topRightPos;
	SimpleLocation center;
	String world;
	
	String schemName;
	
	Schematic() {
		
	}
	
	Schematic(String schemName, String world, Block botLeftBlock, Block topRightBlock, Player player) {
		Location botLeft, topRight;
		botLeft = new Location(Bukkit.getWorld(world), Math.min(botLeftBlock.getX(), topRightBlock.getX()),
				Math.min(botLeftBlock.getY(), topRightBlock.getY()),
				Math.max(botLeftBlock.getZ(), topRightBlock.getZ()));
		
		topRight = new Location(Bukkit.getWorld(world), Math.max(botLeftBlock.getX(), topRightBlock.getX()),
				Math.max(botLeftBlock.getY(), topRightBlock.getY()),
				Math.min(botLeftBlock.getZ(), topRightBlock.getZ()));
		
		center = new SimpleLocation(Bukkit.getWorld(world).getBlockAt(player.getLocation()).getRelative(0, -1, 0).getLocation());
		
		this.botLeftPos = new SimpleLocation(botLeft);
		this.topRightPos = new SimpleLocation(topRight);
		this.world = world;
		this.schemName = schemName;
	}
	
	Schematic(String schemName, SimpleLocation botLeftPos, SimpleLocation topRightPos, SimpleLocation center)
	{
		this.schemName = schemName;
		this.botLeftPos = botLeftPos;
		this.topRightPos = topRightPos;
		this.center = center;
		this.world = center.getWorld().getName();
	}
	
	public List<SchematicBlock> getSchematicBlocks() {
		
		List<SchematicBlock> blocks = new ArrayList<SchematicBlock>();
		
		World world = Bukkit.getWorld(this.world);
		if(world == null) return null;
		
		//Bukkit.broadcastMessage(this.botLeftPos.getSerializedString());
		//Bukkit.broadcastMessage(this.topRightPos.getSerializedString());
		//Bukkit.broadcastMessage(this.center.getSerializedString());
		
		//Find all blocks and its IDs
		for(int x = botLeftPos.getBlockX(); x <= topRightPos.getBlockX(); x++) {
			for (int y = botLeftPos.getBlockY(); y <= topRightPos.getBlockY(); y++) {
				for (int z = topRightPos.getBlockZ(); z <= botLeftPos.getBlockZ(); z++) {
					Block b = world.getBlockAt(x, y, z);
					if(b.getType() != Material.AIR) {
						int theY = y - center.getBlockY();
						if(y > 255) continue;
						blocks.add(new SchematicBlock(
								x - center.getBlockX(),
								theY,
								z - center.getBlockZ(),
								b.getTypeId(),
								(int)b.getData()));
					}
				}
			}
		}
		
		return blocks;
	}
	
	
	public Pair<Integer, Integer> getChunkRadius()
	{
		Chunk botLeft = botLeftPos.getTrueLocation().getChunk();
		Chunk topRight = topRightPos.getTrueLocation().getChunk();
		
		return new Pair<Integer, Integer>(
				(topRight.getX() - botLeft.getX()) / 2 + 1,
				(botLeft.getZ() - topRight.getZ()) / 2 + 1);
	}
	
	public Location getBotLeftPosition()
	{
		return this.botLeftPos.getTrueLocation();
	}
	
	public Location getTopRightPosition()
	{
		return this.topRightPos.getTrueLocation();
	}
	
	public SimpleLocation getSimpleBotLeftPosition()
	{
		return botLeftPos;
	}
	
	public SimpleLocation getSimpleTopRightPosition()
	{
		return topRightPos;
	}
	
	public SimpleLocation getSimpleCenterPosition()
	{
		return center;
	}
	
	
	
	public String getSchemName() {
		return schemName;
	}
}
