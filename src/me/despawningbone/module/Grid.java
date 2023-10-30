package me.despawningbone.module;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;

import me.despawningbone.modules.api.SimpleLocation;
import me.despawningbone.modules.api.Variables;

/**
 * Handles the location of Private Mines
 * @author coolfire02
 *
 */

public class Grid implements Serializable {
	private static final long serialVersionUID = -3035265969265394553L;
	
	private static final int mineRange = 500;
	
	String world;
	private TreeMap<Integer, TreeMap<Integer, PlayerMine>> mineGrid = new TreeMap<Integer, TreeMap<Integer, PlayerMine>>();
	private HashMap<UUID, List<SimpleLocation>> ownedMines = new HashMap<UUID, List<SimpleLocation>>();
	int lastX, lastZ;
	
	public Grid()
	{
		this.world = null;
	}
	
	public Grid(World world) {
		this.world = world.getName();
	}
	
	public static int getMineRange() {
		return mineRange;
	}
	
	public Set<UUID> getAllPlayers()
	{
		return ownedMines.keySet();
	}
	
	public List<PlayerMine> extensiveMineSearch(Player player)
	{
		List<PlayerMine> mines = new ArrayList<PlayerMine>();
		
		for(Entry<Integer, TreeMap<Integer, PlayerMine>> entry : mineGrid.entrySet())
		{
			for(Entry<Integer, PlayerMine> search : entry.getValue().entrySet())
			{
				if(search.getValue().getOwnerUUID().equals(player.getUniqueId()))
				{
					mines.add(search.getValue());
				}
			}
		}
		
		return mines;
	}
	
	public List<PlayerMine> getPlayersMines(OfflinePlayer player)
	{
		List<SimpleLocation> mines = new ArrayList<SimpleLocation>();
		List<PlayerMine> updated = new ArrayList<PlayerMine>(); //Test to see if this is a not actually a reference.. I'd be sad if it isnt :clown:
		try {
			mines = (List<SimpleLocation>) Variables.getPlayer(player, "OwnedPMines");
			
			if(mines != null && !mines.isEmpty())
			{
				for(SimpleLocation mine : mines)
				{
					updated.add(this.getMineAtLocation(mine));
				}	
			}
			
		}catch (ClassCastException e) //Can use trycatch since overhead is only caused when CCE is triggered.
		{
			e.printStackTrace();
			//Converts old variables to SimpleLocations since we cannot directly reference the pointer of the object.
			List<PlayerMine> old = (List<PlayerMine>) Variables.getPlayer(player, "OwnedPMines");
			List<SimpleLocation> newMines = new ArrayList<SimpleLocation>();
			if(old != null && !old.isEmpty())
			{
				for(PlayerMine mine : old)
				{
					newMines.add(new SimpleLocation(mine.getMineCenter()));
					updated.add(this.getMineAtLocation(mine.getMineCenter()));
				}	
			}
			
			Variables.setPlayer(player, "OwnedPMines", newMines);
			
		}
		

		return updated;
	}
	
	public List<SimpleLocation> getPlayersMineLocations(OfflinePlayer player)
	{	
		if(ownedMines.containsKey(player.getUniqueId()))
		{
			return ownedMines.get(player.getUniqueId());	
		}
		
		return null;
	}
	
	public void setMinesOwnedP(Player player, List<PlayerMine> mines)
	{
		List<SimpleLocation> locs = new ArrayList<SimpleLocation>();
		for(PlayerMine mine : mines)
			locs.add(new SimpleLocation(mine.getMineCenter()));
		setMinesOwned(player, locs);
	}
	
	public void setMinesOwned(Player player, List<SimpleLocation> locations)
	{
		if(ownedMines == null)
		{
			ownedMines = new HashMap<UUID, List<SimpleLocation>>();
		}
		
		this.ownedMines.put(player.getUniqueId(), locations);
	}
	
	public PlayerMine generateNewMine(Player player, String schem) {
		
		final int mineSurfaceY = 128;
		
		int pMaxMines = 1;
		if(Variables.getPlayer(player, "MaxPlayerMines") != null) {
			pMaxMines = (int) Variables.getPlayer(player, "MaxPlayerMines");
			if(pMaxMines < 1) pMaxMines = 1;
		}
		
		//Reached Max Mines
		
		if(Variables.getPlayer(player, "OwnedPMines") == null)
		{
			Variables.setPlayer(player, "OwnedPMines", new ArrayList<SimpleLocation>());
		}
		List<SimpleLocation> mines = Variables.getPlayerAndCast(player, "OwnedPMines", (Class<ArrayList<SimpleLocation>>) (Class<?>) ArrayList.class);
		
		if(!player.hasPermission("playermines.bypassmaxmines")) {
			if(mines.size() >= pMaxMines) {
				System.out.println("max mines reached");
				return null;
			}	
		}
		
		
		Location nextMine = nextGridLocation();
		if(nextMine == null)
			return null;
		//System.out.println("next mine loc: " + nextMine.getBlockX() + "x " + nextMine.getBlockY() + "y " + nextMine.getBlockZ() + "z ");
		
		Location schemLocation = nextMine;
		schemLocation.setY(mineSurfaceY);
		
		//If Schematic not loaded in Memory
		if(!Mines.loadedSchematics.containsKey(schem)) {
			if(Variables.getAndCast("PlayerMineSchematics", List.class) != null) {
				List<Schematic> schematics = (ArrayList<Schematic>) Variables.getAndCast("PlayerMineSchematics", List.class);
				for(Schematic schematic : schematics) {
					if(schematic.getSchemName().equalsIgnoreCase(schem)) {
						List<SchematicBlock> blocks = schematic.getSchematicBlocks();
						Mines.loadedSchematics.put(schem, new LoadedSchematic(blocks));
					}
				}	
			}
		}
		
		PlayerMine mine = new PlayerMine(Bukkit.getWorld(world), player.getUniqueId(), nextMine.getBlockX(), nextMine.getBlockZ(), mineSurfaceY, 12, 30, schem, schemLocation);
		
		
		//Add to MineGrid
		if(mineGrid.containsKey(mine.getMinX())) {
			TreeMap<Integer, PlayerMine> zEntry = mineGrid.get(mine.getMinX());
			if(zEntry.containsKey(mine.getMinZ())) {
				//Conflict
				PlayerMine conflictedMine = mineGrid.get(mine.getMinX()).get(mine.getCenterZ());
				if(conflictedMine.getOwnerUUID() == player.getUniqueId()) {
					Bukkit.getLogger().log(Level.WARNING, "Tried adding mine at a memory location already existed that the original owner owns!");
					lastX = conflictedMine.getCenterX();
					lastZ = conflictedMine.getCenterZ();
				}
				Bukkit.getLogger().log(Level.WARNING, "Mine added at " + mine.getMinX() + "x , " + mine.getMinZ() + "z is CONFLICTED.");
				return null;
			}else {
				//Add Mine, zEntry exists
				zEntry.put(mine.getMinZ(), mine);
				mineGrid.put(mine.getMinX(), zEntry);

				lastX = mine.getCenterX();
				lastZ = mine.getCenterZ();
			}
			
		}else {
			TreeMap<Integer, PlayerMine> zEntry = new TreeMap<Integer, PlayerMine>();
			zEntry.put(mine.getMinZ(), mine);
			mineGrid.put(mine.getMinX(), zEntry);
			
			lastX = mine.getCenterX();
			lastZ = mine.getCenterZ();
		}
		
		mines.add(new SimpleLocation(mine.getMineCenter()));
		if(ownedMines == null)
		{
			ownedMines = new HashMap<UUID, List<SimpleLocation>>();
		}
		if(!ownedMines.containsKey(player.getUniqueId()))
		{
			ownedMines.put(player.getUniqueId(), new ArrayList<SimpleLocation>());
		}
		List<SimpleLocation> playersMines = ownedMines.get(player.getUniqueId());
		playersMines.add(new SimpleLocation(mine.getMineCenter()));
		ownedMines.put(player.getUniqueId(), playersMines);
		
		//Physical checks for mine conflcits. This block should never be broken
		Bukkit.getWorld(world).getBlockAt(mine.getMinX(), 0, mine.getMinZ()).setType(Material.BEDROCK);
		
		Variables.setPlayer(player, "OwnedPMines", mines);
		
		return mine;
	}
	
	public PlayerMine getMineAtLocation(Location loc) {
		if(loc.getWorld().getName().equalsIgnoreCase(Mines.mineWorld)) {
	        Entry<Integer, TreeMap<Integer, PlayerMine>> en = mineGrid.floorEntry(loc.getBlockX());
	        if (en != null) {
	            Entry<Integer, PlayerMine> ent = en.getValue().floorEntry(loc.getBlockZ());
	            if (ent != null) {
	                // Check if in the island range
	                PlayerMine mine = ent.getValue();
	                return mine;
	            }
	        }	
		}
        return null;
	}
	
	public PlayerMine getMineAtLocation(SimpleLocation loc) {
		if(loc.getWorld().getName().equalsIgnoreCase(Mines.mineWorld)) {
	        Entry<Integer, TreeMap<Integer, PlayerMine>> en = mineGrid.floorEntry(loc.getBlockX());
	        if (en != null) {
	            Entry<Integer, PlayerMine> ent = en.getValue().floorEntry(loc.getBlockZ());
	            if (ent != null) {
	                // Check if in the island range
	                PlayerMine mine = ent.getValue();
	                return mine;
	            }
	        }	
		}
        return null;
	}
	
    private Location nextGridLocation() {
	    Location nextPos = new Location(Bukkit.getWorld(world), lastX, 0, lastZ);
    	for(int i = 0; i < 100; ++i)
    	{
    	    if (lastX < lastZ) {
    	        if (-1 * lastX < lastZ) {
    	            nextPos.setX(nextPos.getX() + mineRange);
    	            return nextPos;
    	        }
    	        nextPos.setZ(nextPos.getZ() + mineRange);
    	        return nextPos;
    	    }
    	    if (lastX > lastZ) {
    	        if (-1 * lastX >= lastZ) {
    	            nextPos.setX(nextPos.getX() - mineRange);
    	            return nextPos;
    	        }
    	        nextPos.setZ(nextPos.getZ() - mineRange);
    	        return nextPos;
    	    }
    	    if (lastX <= 0) {
    	        nextPos.setZ(nextPos.getZ() + mineRange);
    	        return nextPos;
    	    }
    	    nextPos.setZ(nextPos.getZ() - mineRange);
    	    
    	    
    	    System.out.println(world
    	    		+ Bukkit.getWorld(world).getBlockAt(nextPos.getBlockX() - Grid.getMineRange()/2, 0, nextPos.getBlockZ() - Grid.getMineRange()/2).getType().toString()+ " ssss " + (nextPos.getBlockX() - Grid.getMineRange()/2) + " x " + (nextPos.getBlockZ() - Grid.getMineRange()/2));
    		//Checks for Mine Existing
    		if(Bukkit.getWorld(world).getBlockAt(nextPos.getBlockX() - Grid.getMineRange()/2, 0, nextPos.getBlockZ() - Grid.getMineRange()/2).getType() != Material.BEDROCK)
    		{
    			return nextPos;
    		}	
    		else
    		{
    			System.out.println("Tried Generating Mine at Pos: " + nextPos.getBlockX() + "x " + nextPos.getBlockZ() + "z but a Physical Mine already existed. Shifting lastMine cords to next available space...");
    			
    		}
    	}
		
		
	    System.out.println("Can't find a free spot for the PMine being generated.");
	    return null;
	}
}
