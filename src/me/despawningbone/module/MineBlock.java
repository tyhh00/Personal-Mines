package me.despawningbone.module;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.despawningbone.module.Utils.ItemNames;

public class MineBlock implements Serializable {
	private static final long serialVersionUID = 415418354604401475L;
	
	final Material m;
	final short data;
	
	public MineBlock(Material m, short data)
	{
		this.m = m;
		this.data = data;
	}
	
	public String getBlockName()
	{
		return ItemNames.lookup(new ItemStack(m,1,data));
	}
	
	public Material getMaterial()
	{
		return m;
	}
	
	public short getData()
	{
		return data;
	}
	
	public static float getBaseXPDropped(Material m, short data)
	{
		switch(m)
		{
		case COAL_ORE:
			return 1.1f;
		case IRON_ORE:
			return 1.15f;
		case GOLD_ORE:
			return 1.2f;
		case DIAMOND_ORE:
			return 1.3f;
		case EMERALD_ORE:
			return 1.4f;
		case DIAMOND_BLOCK:
			return 1.65f;
		case EMERALD_BLOCK:
			return 2.0f;
		default:
			return 1.0f;
		}
	}
	
}
