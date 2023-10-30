package me.despawningbone.module.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class AutominerCMD {
	public static boolean cmd(CommandSender sender, String label, String[] args)
	{
		boolean admin = false;
		Player player = null;
		if(sender instanceof ConsoleCommandSender)
			admin = true;
		if(sender instanceof Player)
		{
			player = (Player) sender;
			if(player.hasPermission("am.admin.give"))
				admin = true;
		}
		
		
		
		return true;
	}
}
