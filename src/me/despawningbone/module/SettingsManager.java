package me.despawningbone.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

public class SettingsManager implements Serializable {
	private static final long serialVersionUID = -7687974814013702390L;
	
	HashMap<String, Setting> settings;
	
	private static HashMap<String, Setting> defaultSettings = new HashMap<String, Setting>();
	static {
		defaultSettings.put("Public", new Setting("Public", "Public Mine", "Enable Public Mine here", false));
		defaultSettings.put("MoneyTax", new NumericalSetting("MoneyTax", "Money Tax", "Modify money tax percent here", 5, 0, 10.0));
		defaultSettings.put("TokenTax", new NumericalSetting("TokenTax", "Token Tax", "Modify token tax percent here",5, 0, 10.0));
		defaultSettings.put("EnergyTax", new NumericalSetting("EnergyTax", "Energy Tax", "Modify energy tax percent here", 5, 0, 10.0));
		defaultSettings.put("ExteriorEdit", new Setting("ExteriorEdit", "Exterior Modification", "Modify blocks surrounding the mine", true));
	}
	
	public static HashMap<String, Setting> getDefaultSettings()
	{
		return new HashMap<String, Setting>(defaultSettings);
	}
	
	public SettingsManager()
	{
		settings = new HashMap<String, Setting>();
		refreshSettings();
	}
	
	public double getNumericalValue(String id)
	{
		if(!settings.isEmpty()  && settings.containsKey(id))
		{
			Setting setting = settings.get(id);
			if(setting instanceof NumericalSetting)
			{
				NumericalSetting nS = (NumericalSetting) setting;
				return nS.getRawValue();
			}
		}else
		{
			refreshSettings();
		}
		return 0.0;
	}
	
	public boolean getBooleanValue(String id)
	{
		if(!settings.isEmpty() && settings.containsKey(id))
		{
			Setting setting = settings.get(id);
			if(!(setting instanceof NumericalSetting))
			{
				return setting.getEnabled();
			}
		}
		else
		{
			refreshSettings();
		}
		
		return false;
	}
	
	public boolean updateBooleanValue(String id, boolean enabled)
	{
		System.out.println("yee0 " + settings.size());
		if(!settings.isEmpty() && settings.containsKey(id))
		{
			System.out.println("yee1");
			Setting setting = settings.get(id);
			if(!(setting instanceof NumericalSetting))
			{
				System.out.println("yee2");
				setting.setEnabled(enabled);
				return true;
			}
		}
		else
		{
			refreshSettings();
		}
		return false;
	}
	
	public boolean updateValue(String id, double newValue)
	{
		if(!settings.isEmpty() && settings.containsKey(id))
		{
			Setting setting = settings.get(id);
			if((setting instanceof NumericalSetting))
			{
				NumericalSetting nS = (NumericalSetting) setting;
				nS.setValue(newValue);
				return true;
			}
		}
		else
		{
			refreshSettings();
		}
		return false;
	}
	
	public boolean isValidSetting(String id)
	{
		return defaultSettings.containsKey(id);
	}
	
	public boolean isBooleanType(String id)
	{
		if(!defaultSettings.isEmpty() && defaultSettings.containsKey(id))
		{
			Setting setting = settings.get(id);
			if(!(setting instanceof NumericalSetting))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isValueType(String id)
	{
		if(!defaultSettings.isEmpty() && defaultSettings.containsKey(id))
		{
			Setting setting = settings.get(id);
			if((setting instanceof NumericalSetting))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds in missing settings that may have been added
	 */
	public void refreshSettings()
	{
		for(Entry<String, Setting> entry : defaultSettings.entrySet())
		{
			if(!settings.containsKey(entry.getKey()))
			{
				settings.put(new String(entry.getKey()), entry.getValue().clone());
			}
		}
	}
}
