package me.despawningbone.module;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;

public class LoadedSchematic {
	List<SchematicBlock> blocks;
	static int SEGMANTED_SPLIT = 250000;
	
	LoadedSchematic(List<SchematicBlock> blocks) {
		this.blocks = blocks;
	}
	
	LoadedSchematic()
	{
		this.blocks = new ArrayList<SchematicBlock>();
	}
	
	public boolean pasteSchematic(World world, Location loc) {
		//System.out.println("REACHED??????!!!!");
		if(world == null) return false;
		
		Block center = world.getBlockAt(loc);
		if(center == null) return false;
		
		FaweQueue queue = FaweAPI.createQueue(FaweAPI.getWorld(world.getName()), false);
        queue.getRelighter().clear();
        for(SchematicBlock b : blocks) {
        	
        	int x = center.getX() + b.getXCenterOffset();
        	int y = center.getY() + b.getYCenterOffset();
        	if(y > 255) continue; //We dont want to place blocks that are above YLimit
        	int z = center.getZ() + b.getZCenterOffset();
        	//System.out.println("x: "+x + " y:" + y + " z:" + z + " id?:" + b.getMaterialID());
        	
        	//System.out.println("Pasting block " + b.getMaterialID() + " data: " + b.getData() + " at location " + x + " " + y + " " + z);
        	
        	queue.setBlock(x, y, z, b.getMaterialID(), b.getData());
        }
        queue.optimize();
        queue.getRelighter().clear();
        
		queue.flush();
		
		return true;
	}
	
	public boolean pasteSchematic(World world, Location loc, int segmanted_index) {
		
		if(world == null) return false;
		
		Block center = world.getBlockAt(loc);
		if(center == null) return false;
		
		FaweQueue queue = FaweAPI.createQueue(FaweAPI.getWorld(world.getName()), false);
        queue.getRelighter().clear();
        int start = segmanted_index * SEGMANTED_SPLIT;
        int end = start + SEGMANTED_SPLIT;
        if(end > blocks.size()) end = blocks.size();
        for(int i = start; i < end; ++i)
        {
        	
        	SchematicBlock b = blocks.get(i);
        	int x = center.getX() + b.getXCenterOffset();
        	int y = center.getY() + b.getYCenterOffset();
        	int z = center.getZ() + b.getZCenterOffset();
        	//System.out.println("x: "+x + " y:" + y + " z:" + z + " id?:" + b.getMaterialID());
        	queue.setBlock(x, y, z, b.getMaterialID(), b.getData());
        }
        
        queue.optimize();
        queue.getRelighter().clear();
        
		queue.flush();
		return true;
	}
	
	public int getMaxSegmantedIndex()
	{
		return (int) Math.ceil(blocks.size() / SEGMANTED_SPLIT);
	}
	
	public boolean clearSchematicAt(World world, Location loc)
	{
		if(world == null) return false;
		
		Block center = world.getBlockAt(loc);
		if(center == null) return false;
		
		FaweQueue queue = FaweAPI.createQueue(FaweAPI.getWorld(world.getName()), false);
        queue.getRelighter().clear();
        for(SchematicBlock b : blocks) {
        	int yT = center.getY() + b.getYCenterOffset();
        	int x = center.getX() + b.getXCenterOffset();
        	if(yT > 255) continue;
        	int y = yT;
        	int z = center.getZ() + b.getZCenterOffset();
        	//System.out.println("x: "+x + " y:" + y + " z:" + z + " id?:" + b.getMaterialID());
        	queue.setBlock(x, y, z, 0);
        }
        queue.optimize();
        queue.getRelighter().clear();
        
		queue.flush();
		
		return true;
	}
	
	public int getSize()
	{
		return blocks.size();
	}
	
	
}
