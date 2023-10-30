package me.despawningbone.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Material;

public class MineOreTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -111083107872317921L;
	
	private Map<MineBlock, Double> distribution;
    private double distSum;

    public MineOreTable(MineOreTable table)
    {
    	distribution = new HashMap<MineBlock, Double>();
    	for(Entry<MineBlock, Double> entry : table.getDistribution().entrySet())
    	{
    		distribution.put(entry.getKey(), entry.getValue());
    	}
    	distSum = 0;
    }
    
    public MineOreTable() {
        distribution = new HashMap<>();
        distSum = 0;
    }

    public void addBlock(MineBlock m, double distribution) {
        if (this.distribution.get(m) != null) {
            distSum -= this.distribution.get(m);
        }
        this.distribution.put(m, distribution);
        distSum += distribution;
    }
    
    public Boolean isEmptyOrDefault()
    {
    	if(distribution.isEmpty()) return true;
    	return false;
    }
    
    public Boolean hasNullBlocks()
    {
    	if(!distribution.isEmpty())
    	{
    		for(MineBlock b : distribution.keySet())
    		{
    			if(b == null) return true;
    		}
    	}
    	return false;
    }
    
    public void removeBlock(MineBlock m)
    {
    	List<MineBlock> rem = new ArrayList<MineBlock>();
    	for(MineBlock b : distribution.keySet())
    	{
    		if(b.getMaterial() == m.getMaterial() && b.getData() == m.getData())
    		{
    			rem.add(b);
    		}
    	}
    	for(MineBlock b : rem)
    	{
    		distribution.remove(b);
    	}
    }
    
    public double getSumOfEntryChances() {
    	return distSum;
    }
    
    public Set<MineBlock> getBlockTypes()
    {
    	return distribution.keySet();
    }
    
    public Map<MineBlock, Double> getDistribution()
    {
    	return distribution;
    }
    
    public double getChanceOf(MineBlock m) {
    	//return distribution.getOrDefault(m, 0.0)/distSum * 100;
    	
    	
    	 for(MineBlock block : distribution.keySet())
    	{
    		if(block.getMaterial() == m.getMaterial() && block.getData() == m.getData())
    		{
    			return distribution.getOrDefault(block, 0.0)/distSum * 100;
    		}
    	}
    	return 0.0;
    	 
    }
    
    public double getDistributionOf(MineBlock m) {
    	return distribution.getOrDefault(m, 0.0);
    }
    
    public void clearTable() {
    	distribution.clear();
    	distSum = 0.0;
    }

    public MineBlock generateBlock() {
    	if(distSum == 0 || distribution.isEmpty()) return null;
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (MineBlock i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return null;
    }

}
