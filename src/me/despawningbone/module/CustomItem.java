package me.despawningbone.module;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem {
	
	String rawID;
	
	Material material;
	short data;
	
	String displayName;
	List<String> lore;
	
	int quantity;
	
	public CustomItem(String rawID, Material m, short data, String displayName, List<String> lore, int quantity)
	{
		this.rawID = rawID;
		this.material = m;
		this.data = data;
		this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
		for(int i = 0 ; i < lore.size(); ++i)
		{
			lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
		}
		this.quantity = quantity;
	}
	
	public ItemStack createItem(int amount)
	{
		if(amount > 64) amount = 64;
		ItemStack item = new ItemStack(material, amount, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		List<String> newLore = new ArrayList<String>();
		newLore.addAll(lore);
		meta.setLore(newLore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public boolean isEqual(ItemStack comparison, boolean checkQuantity)
	{
		ItemMeta c_meta = comparison.getItemMeta();
		ItemStack customItem = createItem(1);
		ItemMeta meta = customItem.getItemMeta();
		
		if(comparison.getType() == this.material
				&& comparison.getData() == customItem.getData())
		{
			if(checkQuantity)
			{
				if(this.quantity != comparison.getAmount())
					return false;
			}
			
			if(meta.getDisplayName().equals(c_meta.getDisplayName())
					&& meta.getLore().equals(c_meta.getLore()))
			{
				return true;
			}
			
		}
		
		return false;
	}
	
	public boolean isEqual(CustomItem comparison, boolean checkQuantity)
	{
		if(this.displayName.equalsIgnoreCase(comparison.getDisplayName())
				&& this.data == comparison.getData()
				&& this.lore.equals(comparison.getLore())
				&& this.material == comparison.getMaterial())
		{
			if(checkQuantity && this.quantity != comparison.getQuantity())
				return false;
			return true;
		}
		
		return false;
	}
	
	public String getDisplayName()
	{
		return this.displayName;
	}
	
	public Material getMaterial()
	{
		return this.material;
	}
	
	public short getData()
	{
		return this.data;
	}
	
	public List<String> getLore()
	{
		return this.lore;
		
	}
	
	public int getQuantity()
	{
		return this.quantity;
	}
	
	public void addQuantity(int amt)
	{
		
		this.quantity += amt;
		if(quantity <= 0) quantity = 1;
	}
}
