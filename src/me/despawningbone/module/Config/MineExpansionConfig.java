package me.despawningbone.module.Config;

public class MineExpansionConfig {
	final int newMineValue;
	final int ownerPrestigeRequired;
	final int ownerLevelingRequired;
	final double moneyRequired;
	
	public MineExpansionConfig(int newMineValue, int ownerPrestigeRequired, int ownerLevelingRequired, double moneyRequired)
	{
		this.newMineValue = newMineValue;
		this.ownerPrestigeRequired = ownerPrestigeRequired;
		this.ownerLevelingRequired = ownerLevelingRequired;
		this.moneyRequired = moneyRequired;
	}
	
	public int getExpansionValue()
	{
		return this.newMineValue;
	}
	
	public int getOwnerLevelRequired()
	{
		return this.ownerLevelingRequired;
	}
	
	public int getOwnerPrestigeRequired()
	{
		return this.ownerPrestigeRequired;
	}
	
	public double getMoneyRequired()
	{
		return this.moneyRequired;
	}
	
}
