package me.despawningbone.module;

import java.io.Serializable;

public class NumericalSetting extends Setting implements Serializable {
	private static final long serialVersionUID = 3025432937272954074L;

	double val;
	double min, max;
	
	public NumericalSetting clone()
	{
		return new NumericalSetting(settingID, name, lore, val, min, max);
	}
	
	public NumericalSetting(String settingID, String name, String lore, double initialValue, double minVal, double maxVal) {
		super(settingID, name, lore, true);
		this.min = minVal;
		this.max = maxVal;
		this.val = initialValue;
	}
	
	public double getRawValue()
	{
		return val;
	}
	
	public void addValue(double add)
	{
		this.val += add;
		if(this.val > max) this.val = max;
	}
	
	public void setValue(double val)
	{
		this.val = val;
		if(val > max) this.val = max;
		else if (val < min) this.val = min;
	}
	
	public void removeValue(double remove)
	{
		this.val -= remove;
		if(this.val < min) this.val = min;
	}
	

}
