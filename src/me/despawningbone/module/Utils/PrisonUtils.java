package me.despawningbone.module.Utils;

import java.util.List;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.despawningbone.modules.api.Variables;

public class PrisonUtils {
	
	//Skript Module Functions
	
	@Deprecated
	public static int findTime(Player player, String ability, String varType) {
        int time = -1;
        double timeTillStop = 0;
        try{
            for(String trinket : ((String) Variables.getSkript(varType + "." + player.getUniqueId())).split("\\|")) {
                if(trinket.startsWith(ability)){
                    timeTillStop = Double.parseDouble(trinket.replace(ability, ""));
                    double globalTime = Double.parseDouble((String) Variables.getSkript("Globaltime"));
                    if(timeTillStop < globalTime) {
                        time = 0;
                    } else {
                        time = (int) Math.round((timeTillStop - globalTime) * 100000);
                    }
                    break;
                }
            }
        } catch (NullPointerException e) {
            return -1;
        }
        return time;
    }
	
	@Deprecated
	public static double findPerkMulti(OfflinePlayer player, int skillid) {
        double multi = 1.0;
        
        try{
            String split[] = ((String) Variables.getSkript("PersonalPerk." + player.getUniqueId())).split(";");
            if(Integer.parseInt(split[0]) == skillid) {
                if(Double.parseDouble(split[2]) > Double.parseDouble((String) Variables.getSkript("Globaltime"))) {
                    multi = Double.parseDouble(split[1]);
                }else {
                	multi = 1.0;
                	
                	Variables.setSkript("PersonalPerk." + player.getUniqueId(), null);
                	Player onlinePlayer = Bukkit.getServer().getPlayer("" + player);
                	if(onlinePlayer != null) onlinePlayer.sendMessage("§6§l(!) §7Personal perk has ran out of time!");
                	
                }
            }else multi = 1.0;
            
        } catch (NullPointerException e) {
            return 1.0;
        }
       // onlinePlayer.sendMessage("multi: " + multi);
        return multi;
    }
	
	@Deprecated
	public static String getSettings(Player player, String settings) {
		String outcome = "Unset";
        try{
			for(String setting : ((String) Variables.getSkript("Settings." + player.getUniqueId())).split("\\|")) {
				
				if(setting.contains(settings)) {
					//if(player.hasPermission("op.el")) player.sendMessage("splitted");
					String split[] = setting.split("-");
					outcome = split[1];
				}
			}
        } catch (IndexOutOfBoundsException e) {
            //String outcome = "Unset";
        }
		return outcome;
	}
	
	public static double getTokens(Player player) {
		Object tokenObj = Variables.getSkript("vtokens.balance." + player.getUniqueId());
		return tokenObj instanceof Long ? ((Long) tokenObj).doubleValue() : (double) tokenObj;
	}
	
	public static void addTokens(Player player, double amount) {  //a more efficient approach would be to use operate on vtokens then save after each events instead of each operation, but i am not sure if there would be async modifications so to be safe this is the way i chose
		Object tokenObj = Variables.getSkript("vtokens.balance." + player.getUniqueId());
		if(tokenObj == null) return;
		double vtokens = tokenObj instanceof Long ? ((Long) tokenObj).doubleValue() : (double) tokenObj;
		Variables.setSkript("vtokens.balance." + player.getUniqueId(), vtokens + amount);  //storing this value as double and using serialization since double serialization isnt that expensive and if i dont store as double i would need to parse it everytime which is even more expensive
	}
	
	
	
	public static Integer getLevel(List<String> lore, String ench) {
		int level = 0;
		
		for(int i=8 ; i < lore.size(); i++) {
			String line = lore.get(i);
			if(line.contains(ench)) {
				String sLv = ChatColor.stripColor(line).substring(ench.length() + 1).trim();
				try {
					level = Integer.parseInt(sLv); 
				} catch (NumberFormatException e) {
					level = toNumber(sLv);
				}
				break;
			}
		}
		return level;
	}
	
	public static Integer getLevel(List<String> lore, String ench, boolean isInternallyColored) {
		int level = 0;
		
		for(int i=8 ; i < lore.size(); i++) {
			String line = ChatColor.stripColor(lore.get(i));
			if(line.contains(ench)) {
				String sLv = line.substring(ench.length() + 1).trim();
				try {
					level = Integer.parseInt(sLv); 
				} catch (NumberFormatException e) {
					level = toNumber(sLv);
				}
				break;
			}
		}
		return level;
	}
	
	
	public static Integer toNumber(String roman) {
		if (roman.isEmpty()) return 0;
        if (roman.startsWith("X")) return 10 + toNumber(roman.substring(1));
        if (roman.startsWith("IX")) return 9 + toNumber(roman.substring(2));
        if (roman.startsWith("V")) return 5 + toNumber(roman.substring(1));
        if (roman.startsWith("IV")) return 4 + toNumber(roman.substring(2));
        if (roman.startsWith("I")) return 1 + toNumber(roman.substring(1));
        throw new IllegalArgumentException("Out Of Range");
	}
	
	public static String formatTime(int secs) {
		if(secs < 1) {
			return "0s";
		}
		int remainder = secs % 86400;

		int days 	= secs / 86400;
		int hours 	= remainder / 3600;
		int minutes	= (remainder / 60) - (hours * 60);
		int seconds	= (remainder % 3600) - (minutes * 60);

		String fDays 	= (days > 0 	? " " + days + "d" : "");
		String fHours 	= (hours > 0 	? " " + hours + "h" : "");
		String fMinutes = (minutes > 0 	? " " + minutes + "m" : "");
		String fSeconds = (seconds > 0 	? " " + seconds + "s" : "");
		
		String time = new StringBuilder().append(fDays).append(fHours)
				.append(fMinutes).append(fSeconds).toString();
		if(time.startsWith(" "))
			time = time.replaceFirst(" ", "");
			
		return time;
	}
	
	
	
	

	public static double gUpgradesInfoCore(String gN, String skillid, double percentPerLvl) {
	    double percentbuff = 0;
	    try{
			String gUpgradesVar = (String) Variables.get("Gangs", gN, "Upgrades");
			
	        for(String skillType : (gUpgradesVar.split(";"))) {
	            String[] tokens = skillType.split("-");
	            if(tokens[0].equals(skillid)){
	                int levelOfID = Integer.parseInt(tokens[1]);
	                percentbuff = percentPerLvl * levelOfID;
	                break;
	            }
	        }
	    } catch (NullPointerException e) {
	        return 0;
	    	}
	    return percentbuff;
	}
	
	public static String toRoman(int num) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		map.put(10, "X");
	    map.put(9, "IX");
	    map.put(5, "V");
	    map.put(4, "IV");
	    map.put(1, "I");
        int l =  map.floorKey(num);
        if (num == l) {
            return map.get(num);
        }
        return map.get(l) + toRoman(num - l);
	}
	
	public static String toCommaFormat(int value) {
		return String.format("%,d",value);
	}
	
	public static String toCommaFormat(double value) {
		return String.format("%,.2f",value);
	}
}
