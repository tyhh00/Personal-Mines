package me.despawningbone.module;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.despawningbone.module.Config.MineFactoryConfig;
import me.despawningbone.module.Config.SchematicConfig;
import me.despawningbone.module.Utils.LoreBeauty;
import me.despawningbone.module.Utils.NumberBeauty;
import net.md_5.bungee.api.ChatColor;

public class IconFactory {
	
	//Schematic Icon
	public static ItemStack BuildSchematicConfigIcon(SchematicConfig sc, boolean equipped, boolean unlocked)
	{
		ItemStack icon;
		if(unlocked)
			icon = new ItemStack(sc.getIconMaterial(), 1, sc.getIconData());
		else
			icon = new ItemStack(Material.BARRIER);
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', 
				"&a&l" + sc.getRawID() + " Schematic"
				));
		List<String> lore = new ArrayList<String>();
		lore.add("§7(( §7§oThis schematic is... " + (equipped ? "§a§lEQUIPPED" : "§c§lUNEQUIPPED") + " §7))");
		lore.add("");
		lore.add(" §e§lPerks§7:");
		lore.addAll(getPerksLore(sc.getPerksList(), "&6&l", "&7"));
		if(!equipped)
		{
			lore.add("");
			lore.add("§aUse this Schematic! *Left-Click*");
		}
		if(sc.getIconLore() != null)
			{
			lore.add("");
			lore.add("§8Description");
			lore.addAll(sc.getIconLore());
			}
		meta.setLore(lore);
		icon.setItemMeta(meta);
		return icon;
	}
	
	//Perks Lore
	public static List<String> getPerksLore(HashMap<PERK_TYPE, Float> perks, String pointColor, String perkDescColor)
	{
		List<String> lore = new ArrayList<String>();
		for(Entry<PERK_TYPE, Float> perk : perks.entrySet())
		{
			String val = NumberBeauty.formatNumber(perk.getValue());
			lore.add(ChatColor.translateAlternateColorCodes('&', " " +pointColor+"◎ "+perkDescColor+" "+val + "% " + getPerkDisplayName(perk.getKey())));
			
		}
		return lore;
	}
	
	public static List<String> getMineFactoryCompositionLore(MineFactory pmineFactory)
	{
		List<String> lore = new ArrayList<String>();
		MineOreTable table = pmineFactory.getFactoryComposition();
		Set<MineBlock> types = table.getBlockTypes();
		for(MineBlock block : types)
		{
			float xpDropped = MineBlock.getBaseXPDropped(block.getMaterial(), block.getData());
			lore.add(ChatColor.translateAlternateColorCodes('&', 
					" &7" + block.getBlockName() 
					+ (xpDropped > 1.0f ? " &8(&7"+NumberBeauty.formatNumber(xpDropped) + "XP&8)" : "")
					+ ": &a"+ NumberBeauty.formatNumber(table.getChanceOf(block)) + "%" ));
		}
		lore.add(ChatColor.translateAlternateColorCodes('&', " "));
		lore.add(ChatColor.translateAlternateColorCodes('&', " &8Buffs"));
		for(Entry<PERK_TYPE, Float> perk : pmineFactory.getFactoryPerks().entrySet())
		{
			lore.add(ChatColor.translateAlternateColorCodes('&', " &7"+IconFactory.getPerkDisplayName(perk.getKey()) +": &a" 
					+ NumberBeauty.formatNumber(perk.getValue()) + "%"));
		}
		return lore;
	}
	
	public static List<String> getMineFactoryCompositionLoreWithDifference(MineFactory current, MineFactory future)
	{
		
		List<String> lore = new ArrayList<String>();
		
		MineOreTable table = future.getFactoryComposition();
		Set<MineBlock> types = table.getBlockTypes();
		
		//ORDERING OF BLOCKS IN DISPLAY
		TreeMap<Double, List<MineBlock>> sorting = new TreeMap<Double, List<MineBlock>>(Collections.reverseOrder());
		for(MineBlock block : types)
		{
			List<MineBlock> blocks = sorting.getOrDefault(table.getChanceOf(block), new ArrayList<MineBlock>());
			blocks.add(block);
			sorting.put(table.getChanceOf(block), blocks);
		}
		List<MineBlock> ordered = new ArrayList<MineBlock>();
		for(Entry<Double, List<MineBlock>> entry : sorting.entrySet())
		{
			ordered.addAll(entry.getValue());
		}
		MineFactoryConfig currentLevelConfig = Mines.getMinesConfig().mineFactories.get(current.getID());
		
		//Used to beautify display so all (+...) are put at the same column in text
		//ARGUMENTS Lore, diffDisplay
		HashMap<String, String> compLore = new HashMap<String, String>();
		int maxCharLength = 0;
		
		for(MineBlock block : ordered)
		{
			String diffDisplay = "";
			double diff = 0.0;
			boolean found = false;
			for(MineBlock prevBlock : currentLevelConfig.getMineComposition().getBlockTypes())
			{
				if(block.getMaterial() == prevBlock.getMaterial() && block.getData() == prevBlock.getData())
				{
					diff = (table.getChanceOf(block) - currentLevelConfig.getMineComposition().getChanceOf(prevBlock)) * 100;
					found = true;
					break;
				}
			}
			
			if(!found)
			{
				diff = table.getChanceOf(block);
			}
			
			if(diff < -0.01 || diff > 0.01)
				diffDisplay = " &8(&f&o" + (diff < 0.0 ? "" : "+") + NumberBeauty.formatNumber(diff) + "%&8)";
			
			float xpDropped = MineBlock.getBaseXPDropped(block.getMaterial(), block.getData());
			
			//Lore Handling -- Adding into composition lore 
			String loreStr = ChatColor.translateAlternateColorCodes('&', 
					" &7" + block.getBlockName() 
					+ (xpDropped > 1.0f ? " &8(&7"+NumberBeauty.formatNumber(xpDropped) + "XP&8)" : "")
					+ ": &a"+ NumberBeauty.formatNumber(table.getChanceOf(block)) + "%");
			compLore.put(loreStr, diffDisplay);
			int charLength = ChatColor.stripColor(loreStr).length();
			if(charLength > maxCharLength) maxCharLength = charLength;
		}
		
		//Adding Composition Lore
		for(Entry<String, String> entry : compLore.entrySet())
		{
			String loreStr = entry.getKey();
			int diffDisplayCharPos = maxCharLength + 3;
			int diff = diffDisplayCharPos - ChatColor.stripColor(loreStr).length();
			for(int i = 0; i < diff; ++i)
				loreStr += " ";
			loreStr += entry.getValue();
			lore.add(ChatColor.translateAlternateColorCodes('&', loreStr));
		}
		
		return lore;
	}
	
	public static String getPerkDisplayName(PERK_TYPE type)
	{
		if(type != PERK_TYPE.PERK_COUNT)
		{
			return LoreBeauty.toDisplayCase(type.toString()).replace('_', ' ');
		}
		return "NULL";
	}
}

