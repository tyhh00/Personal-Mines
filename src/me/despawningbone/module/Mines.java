package me.despawningbone.module;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SplittableRandom;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.vk2gpz.tokenenchant.TokenEnchant;
import com.vk2gpz.tokenenchant.api.CEHandler;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowman;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventPriority;

import me.confuser.barapi.BarAPI;
import me.despawningbone.autominer.PrisonsBlockBreakEvent;
import me.despawningbone.module.Commands.AutominerCMD;
import me.despawningbone.module.Config.ConfigHandler;
import me.despawningbone.module.Config.MineExpansionConfig;
import me.despawningbone.module.Config.MineFactoryConfig;
import me.despawningbone.module.Config.MineFactoryGroup;
import me.despawningbone.module.Config.SCHEMGROUP_TYPE;
import me.despawningbone.module.Config.SchematicConfig;
import me.despawningbone.module.Events.MineFactoryUpgradeEvent;
import me.despawningbone.module.Events.PostChangeSchematicEvent;
import me.despawningbone.module.Utils.ItemNames;
import me.despawningbone.module.Utils.LoreBeauty;
import me.despawningbone.module.Utils.NumberBeauty;
import me.despawningbone.modules.Platform;
import me.despawningbone.modules.api.GUI;
import me.despawningbone.modules.api.Module;
import me.despawningbone.modules.api.ReflectionUtils;
import me.despawningbone.modules.api.SimpleLocation;
import me.despawningbone.modules.api.Variables;
import me.despawningbone.modules.api.GUI.Button;

import me.jet315.prisonmines.JetsPrisonMines;
import me.jet315.prisonmines.JetsPrisonMinesAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

//Headers

/*
 * //PMine Chunk Queue Process
 * //PMine Creation Function
 * //Main PMINE Mine Panel
 * //PMINE ADMIN COMMANDS
 * //PMine SubGUI Function
 * //PMine First Time GUI
 * //PMine Mine Selection GUI
 * //PMine Factory Contribution GUI
 * //PMine Mine Expansion GUI
 * //PMine Mine Factory Upgrade GUI
 * //PMine Mine Factory List GUI
 * //PMine Schematic Confirmation Selection GUI
 * //PMine Schematic Individual Categories GUI
 * //PMine Schematics Overview GUI
 * //PMine Settings GUI
 * //PMine Settings Tax GUI
 * //PMine Module Constructor
 * //PMine Factory Upgrade Repeating Task
 * //PMine Autominer GUI Update Repeating Task
 * //Autominer Repeating Task
 * //PMine Chunk Queue Speed Controller
 * //PMine Autominer Command
 * //PMine pmine Command
 * //PMine Debug Command
 * //AM Shard Redemption
 * //PlayerMine Guarding Events
 */

@SuppressWarnings("unused")
public class Mines extends Module {
	
	public static SplittableRandom ran = new SplittableRandom();  //avoid object instantiation every time
	private Economy econ;
	WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
	
	private final String OP_PERM = "op.perm";
	
	final int MEDIA_MULTIPLIER = 5;
	final float MEDIA_SPEED_INVERTED = 0.25f;
	
	public final String mcVer = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];  //make this in platform?
	public final String nmsBase = "net.minecraft.server." + mcVer + ".";
	
	private final DecimalFormat df = new DecimalFormat("0.00");
	
	JetsPrisonMinesAPI minesAPI = ((JetsPrisonMines) Bukkit.getPluginManager().getPlugin("JetsPrisonMines")).getAPI();
	WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	
	public static final String mineWorld = "PlayerMine";
	public static HashMap<String, LoadedSchematic> loadedSchematics = new HashMap<String, LoadedSchematic>();
	public static Queue<Pair<PlayerMine, List<Chunk>>> chunksToLoad = new LinkedList<Pair<PlayerMine, List<Chunk>>>();
	
	public static final PMINE_STATE ver_state = PMINE_STATE.ALPHA;
	
	private final DecimalFormat df1 = new DecimalFormat("0.0");
	
	private static ConfigHandler configHandler;
	
	public static int CHUNK_LOADCOUNT = 7;
	
	public static boolean PRODUCTION_MODE = true;
	public static boolean HIDE_FEATURES = false;
	
	private final float amInterval = 0.35f;
	
 	private List<UUID> factoryGUIToAdd = new ArrayList<UUID>();
	private List<UUID> factoryGUIPlayers = new ArrayList<UUID>();
	
	private static HashMap<Player, Integer> amSessionBlocksMined = new HashMap<Player, Integer>(); 
	
	private HashMap<Player, GUI> minePanelActive = new HashMap<Player, GUI>();
	
	private List<PlayerMine> publicMines = new ArrayList<PlayerMine>();
	
	//Outcomes from adding new class members to Object classes: Enums return null, primitives like int returns 0, Objects return null.
	
	enum PMINE_STATE {
		ALPHA,
		BETA,
		RELEASE
	}
	
	public static ConfigHandler getMinesConfig()
	{
		if(configHandler == null)
		{
			//System.out.println("is null?");
		}
		else 
			{
			//System.out.print("not null??");
			

			
			}
		return configHandler;
	}
	
	public void refreshPublicMines()
	{
		 delayRun(() -> {
			Long now = System.currentTimeMillis();
			publicMines.clear();
			Set<UUID> players = getGrid().getAllPlayers();
			for(UUID player : players)
			{
				for(PlayerMine mine : getGrid().getPlayersMines(Bukkit.getOfflinePlayer(player)))
				{
					if(mine.isPublic())
					{
						publicMines.add(mine);
					}
				};
				
			}
			Long end = System.currentTimeMillis();
			//System.out.println("Time Taken for PMine Refresh: " + (int) ( (end-now) * 0.001) + "ms");
		},1,true);
		 
		
	}
	
	public void resetPMine(PlayerMine mine) {
		if(mine == null) {
			return;
		}
		Mine theMine = mine.getMine();
		resetMine(theMine);
	}
	
	public void resetMine(Mine theMine)
	{
		MineOreTable table = theMine.getOreTable();
		
		List<MineBlock> mineResetTable = new ArrayList<MineBlock>();
		
		
		delayRun(() -> {
			for(int i = 0; i < theMine.getBlockCount(); i++) 
				mineResetTable.add(table.generateBlock());
		
			
			delayRun(() -> {
				theMine.resetMine(true, mineResetTable);
				
			},0,false);
		},0,true);
	}
	
	public static void loadSchemIntoMemory(String schem) {
		//If Schematic not loaded in Memory
		if(!Mines.loadedSchematics.containsKey(schem)) {
			for(SchematicConfig sc : configHandler.schematics.values())
			{
				if(sc.getRawID().equalsIgnoreCase(schem))
				{
					Schematic deprecatedClassSchematic = new Schematic(sc.getRawID(), sc.getBotLeftPos(), sc.getTopRightPos(), sc.getCenterPos());
					List<SchematicBlock> blocks = deprecatedClassSchematic.getSchematicBlocks();
					debugMsg("Loaded Schematic " + schem + " with " + blocks.size());
					
					Mines.loadedSchematics.put(sc.getRawID(), new LoadedSchematic(blocks));
				}
			}
		}
	}
	
	public static LoadedSchematic getLoadedSchematic(String schem)
	{
		loadSchemIntoMemory(schem);
		return Mines.loadedSchematics.get(schem);
	}
	
	//PMine Chunk Queue Process
	public void chunkQueue() {
		if(!chunksToLoad.isEmpty()) {
			try {
				Pair<PlayerMine, List<Chunk>> pair = chunksToLoad.peek();
				List<Chunk> chunks = pair.getValue();
				if(chunks.size() > 0)
				{
					Chunk chunk = chunks.get(0);
					if(!chunk.isLoaded())
						chunk.load();
					chunks.remove(0);	
				}
				if(chunks.isEmpty()) {
					
					if(pair.getKey().isMineLoaded())
						pair.getKey().unloadMine(this);
					pair.getKey().loadMine(this);
					
					PlayerMine mine = pair.getKey();
					Player owner = Bukkit.getPlayer(mine.getOwnerUUID());
					
					//Try to reduce lag when new chunks are loaded
					Player player = owner;
					updateSurroundingChunks(player);
					
					if(owner != null)
					{
						if(owner.isOnline())
							owner.sendMessage("§2§l(!) §aPMine Loaded! /pmine to access it.");
					}
					
					chunksToLoad.poll();
				}	
			}catch (Exception e)
			{
				//No Overhead if no Exception occurs.
				chunksToLoad.poll();
				System.out.println(e.getStackTrace());
			}
		}
	}
	
	public void updateSurroundingChunks(Player player)
	{
		/*List<net.minecraft.server.v1_12_R1.Chunk> changedChunks = new ArrayList<net.minecraft.server.v1_12_R1.Chunk>();
		for(int x = -20; x < 20; ++x)
		{
			for(int z = -20; z < 20; ++z)
			{
				int currentX = player.getWorld().getChunkAt(player.getLocation()).getX();
				int currentZ = player.getWorld().getChunkAt(player.getLocation()).getZ();
				changedChunks.add( (net.minecraft.server.v1_12_R1.Chunk) ((CraftWorld) player.getWorld()).getHandle().getChunkAt(currentX+x, currentZ+z) );
			}
		}
		
		for (net.minecraft.server.v1_12_R1.Chunk chunk : changedChunks) {
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, 65535));
		}*/
		
		/* List<net.minecraft.server.v1_12_R1.Chunk> changedChunks = new ArrayList<net.minecraft.server.v1_12_R1.Chunk>();
		int currentX = player.getWorld().getChunkAt(player.getLocation()).getX();
		int currentZ = player.getWorld().getChunkAt(player.getLocation()).getZ();
		for(int x = -20; x < 20; ++x)
		{
			for(int z = -7; z < 20; ++z)
			{
				player.getWorld().refreshChunk(currentX+x, currentZ+z);
					
			}
		}
		*/
		
		/* Works but not the neatest
		
		 Location to = player.getLocation();
		Location from = Bukkit.getWorld("Spawn").getSpawnLocation();
		player.teleport(from);
		player.teleport(to);
		 */
		
	}
	
	//PMine Creation Function
	public void createNewPMine(Player player)
	{
			
		//Bukkit.broadcastMessage("Loading from here");
		if(Variables.get("PlayerMineGrid") == null) {
			Variables.set("PlayerMineGrid", new Grid(Bukkit.getWorld("PlayerMine")));
		}
		Grid mineGrid = getGrid();
		PlayerMine mine = mineGrid.generateNewMine(player, "Mine_1");
		
		Pair<PlayerMine, List<Chunk>> pair = new MutablePair<>(mine, mine.getMineSchemChunks());
		chunksToLoad.add(pair);
		if(mine != null) {
			//player.sendMessage("Successfully created");
			
			saveGrid(mineGrid);
		}
	
	}
	
	enum PMINE_GUI
	{
		FIRST_TIME,
		OWNED_MINES,
		FRIEND_MINES,
		PUBLIC_MINES,
		SCHEMATIC_OVERVIEW,
		SCHEMATIC_NORMAL,
		SCHEMATIC_DONOR,
		SCHEMATIC_SPECIAL,
		SCHEMATIC_ALL,
		SCHEMATIC_CONFIRMATION,
		MINEFACTORIES,
		MINEFACTORY_UPGRADE,
		TAXES,
		
		CONTRIBUTE_MONEY,
		CONTRIBUTE_TOKENS,
		CONTRIBUTE_ENERGY,
		CONTRIBUTE_CUSTOMITEM,
		
		SETTINGS,
		TAX_SETTINGS,
		
		MINEEXPANSION,
		
	}
	
	@EventHandler
	public void guiMinePanelClose(InventoryCloseEvent e)
	{
		this.minePanelActive.remove(e.getPlayer());
	}
	
	public ItemStack getPlayerMineIcon(PlayerMine mine)
	{
		
		ItemStack icon = new ItemStack(mine.getMineIcon(), 1, mine.getMineIconData());
		ItemMeta meta = icon.getItemMeta();
		meta.setDisplayName("§a§lPlayer Mine");
		List<String> lore = new ArrayList<String>();
		lore.add("§7Owner: §f" + Bukkit.getOfflinePlayer(mine.getOwnerUUID()).getName());
		lore.add("");
		lore.add("§d§l Stats§7:");
		lore.add("§5§l * §7Type: §ePersonal");
		lore.add("§5§l * §7Blocks Mined: §e" + mine.getTracker("MinedTotal"));
		lore.add("§5§l * §7Mine Size: §e"+ mine.getMine().getMineSize() + "x" + mine.getMine().getMineSize());
		if(mine.getMembers().size() > 0)
		{
			Set<UUID> members = mine.getMembers();
			lore.add("§5§l * §7Members:");
			for(UUID member : members)
			{
				lore.add("§7 §7 §7 §7 §d§l- §7"+Bukkit.getOfflinePlayer(member).getName());
			}
		}
		lore.add("");
		lore.add("§8(( §7§oClick to §a§oOpen §7§oMine Panel §8))");
		meta.setLore(lore);
		
		icon.setItemMeta(meta);
		return icon;
		//.setLore("&7Owner: &f" + Bukkit.getPlayer(mine.getOwnerUUID()).getName(),

		
	}
	
	public void teleportPlayerToMine(Player p, PlayerMine mine)
	{
		p.teleport(mine.getSpawnLocation());
		p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 0.5f, 0.2f);	
	}
	
	private void assignAMSlot(Player player, PlayerMine mine, GUI gui)
	{
		boolean amActive = isAutominerEnabled(player);
		boolean hasAdvancedAM = (getAdvancedAutominerTimeleft(player) > 0);
		float blocksPerSecond = 0.7f;
		if(hasAdvancedAM) blocksPerSecond = 2.1f;
		int timeLeft = getAdvancedAutominerTimeleft(player).intValue();
		
		gui.assignSlotButton(31, 1, new Button(new ItemStack(Material.DIAMOND_PICKAXE), (s,p,c) -> {
			toggleAutominer(player);
		})
				.setName("&8[&6&l!&8]&8&m-------------------&8[&6&l!&8]")
				.setLore("&8 (( " + (amActive ? "&a&lAUTOMINER ON" : "&c&lAUTOMINER OFF") + " &8))",
						"&7",
						"&7Current Speed &8&l<> &f" + blocksPerSecond + " Blocks / s",
						"&7Advanced AM Time &8&l<> &f" + formatTime(timeLeft) + " Left",
						"&7",
						"&eLeft-Click &7to &7&n" + (amActive ? "disable" : "enable") +  "&7 Autominer.",
						"&7",
						"&7Hint: &7&oReceive Advanced AM Shards through",
						"&7&ovoting, crates & the store."));
	}
	
	//Main PMINE Mine Panel
	public void openMinePanelGUI(Player player, PlayerMine mine)
	{
		if(mine.getOwnerUUID().equals(player.getUniqueId()) || mine.isMemberOfMine(player.getUniqueId()))
		{
			GUI gui = new GUI(6, "Mine Panel");
			gui.assignSlotButton(0, 54, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
			gui.assignSlotButton(13, 1, new Button(this.getPlayerMineIcon(mine), (s,p,c) -> {
				
			} ));
			
			String lastResetTime = formatTime( (int)((System.currentTimeMillis() - mine.getMine().getLastResetTime()) / 1000));
			
			String reset = "&aClick to reset mine";
			if(!mine.getMine().canForceResetMine())
			{
				reset = "&cCooldown of &c&n" + formatTime( (int)((mine.getMine().getNextForceResetTime() - System.currentTimeMillis()) / 1000));;
			}
			

			gui.assignSlotButton(25, 1, new Button(new ItemStack(Material.DIAMOND), (s,p,c)-> {
				openMainGUI(PMINE_GUI.TAXES, player, 1);
			})
					.setName("&8&l) &b&lTaxes &8&l(")
					.setLore("&7Collect PMine Taxes here",
							"",
							"&8 * &7Money Taxed &8&l<> &e$" + NumberBeauty.moneyFormat(mine.getTaxedAmount(TAX_TYPE.MONEY)),
							"&8 * &7Tokens Taxed &8&l<> &a" + toCommaFormat((int)mine.getTaxedAmount(TAX_TYPE.TOKEN)),
							"&8 * &7Energy Taxed &8&l<> &b" + toCommaFormat((int)mine.getTaxedAmount(TAX_TYPE.ENERGY)),
							"",
							"&aClick to access claim menu"
							
							
							));
			
			
			//Schematic GUI Icon
			{
				ItemStack icon = new ItemStack(Material.EMPTY_MAP);
				ItemMeta meta = icon.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&l) &b&lMine Schematic &8&l("));
				List<String> lore = new ArrayList<String>();
				lore.add("");
				lore.add(ChatColor.translateAlternateColorCodes('&', "&3&l Active Schematic&7:"));
				lore.add(ChatColor.translateAlternateColorCodes('&', "&b * &7" + mine.getCurrentSchematicName() + " Schematic"));
				SchematicConfig config = configHandler.schematics.get(mine.getCurrentSchematicName());
				if(config != null)
				{
					List<String> perksLore = IconFactory.getPerksLore(config.getPerksList(), "&6", "&7");
					if(perksLore != null)
					{
						lore.add(ChatColor.translateAlternateColorCodes('&', " "));
						lore.add(ChatColor.translateAlternateColorCodes('&', "&e&l Perks&7:"));
						lore.addAll(perksLore);
					}	
				}
					
				lore.add(ChatColor.translateAlternateColorCodes('&', " "));
				lore.add(ChatColor.translateAlternateColorCodes('&', "&aSelect a new schematic here"));
				meta.setLore(lore);
				icon.setItemMeta(meta);
				gui.assignSlotButton(41, 1, new Button(icon, (s,p,c) -> {
					openMainGUI(PMINE_GUI.SCHEMATIC_OVERVIEW, player, 1);
				}));		
			}
			
			//Mine Factory
			{
				MineFactory selectedMF = mine.getMine().getMineFactorySelected();
				
				ItemStack icon = new ItemStack(Material.ENDER_PORTAL_FRAME);
				ItemMeta meta = icon.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&l) &a&lMine Factory &8&l("));
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.translateAlternateColorCodes('&', "&8 (( &f&o"+selectedMF.getFactoryGroup().getGroupID()+" &7Factory &aSelected &8))"));
				lore.add(ChatColor.translateAlternateColorCodes('&', " "));
				lore.add(ChatColor.translateAlternateColorCodes('&', " &8General Stats"));
				lore.add(ChatColor.translateAlternateColorCodes('&', " &7Tier: &a" + selectedMF.getLevel() + "/" + selectedMF.getFactoryGroup().getMaxLevel()));
				lore.add(ChatColor.translateAlternateColorCodes('&', " &7Mined: &a" + selectedMF.getBlocksMined()));
				lore.add(ChatColor.translateAlternateColorCodes('&', " "));
				lore.add(ChatColor.translateAlternateColorCodes('&', " &8Mine Composition"));
				
				MineOreTable table = selectedMF.getFactoryComposition();
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
				for(Entry<PERK_TYPE, Float> perk : selectedMF.getFactoryPerks().entrySet())
				{
					lore.add(ChatColor.translateAlternateColorCodes('&', " &7"+IconFactory.getPerkDisplayName(perk.getKey()) +": &a" 
							+ NumberBeauty.formatNumber(perk.getValue()) + "%"));
				}
				lore.add(ChatColor.translateAlternateColorCodes('&', " "));
				lore.add(ChatColor.translateAlternateColorCodes('&', "&6Hint: &7Click to UPGRADE or RESELECT"));
				lore.add(ChatColor.translateAlternateColorCodes('&', "&7the pmine's mine factory"));
				meta.setLore(lore);
				icon.setItemMeta(meta);
				gui.assignSlotButton(40, 1, new Button(icon, (s,p,c) -> {
					openMainGUI(PMINE_GUI.MINEFACTORIES, player, 1);
				}));
			}	
			
			
			//AM Icon
			
			assignAMSlot(player,mine, gui);
			
			//Mine Expansion button
			gui.assignSlotButton(39, 1, new Button(new ItemStack(Material.BEDROCK), (s,p,c)-> {
				openMainGUI(PMINE_GUI.MINEEXPANSION, player, 1);
			})
					.setName("&8&l) &e&LMine Size &8&l(")
					.setLore("&7",
							"&6&lCURRENT SIZE&7:",
							"&e * &7" + mine.getMine().getMineSize() + "x" + mine.getMine().getMineSize() + "x" + mine.getMine().getMineYLength() 
							, "&7"
							,"&aClick to upgrade Mine Size"));
			
			
			//Manage Members
			{
				ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short)3);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&l) &b&lManage Members &8&l("));
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.translateAlternateColorCodes('&', "&7Slots Filled&8: &b" + mine.getMembers().size() + "&7/" + mine.getMaxMembers()));
				lore.add(ChatColor.translateAlternateColorCodes('&', ""));
				lore.add(ChatColor.translateAlternateColorCodes('&', "&8 * &7Members: " + (mine.getMembers().size() == 0 ?  "&b[]":"")));
				for(UUID member : mine.getMembers())
				{
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7 &7 &7 &b- &7" + Bukkit.getOfflinePlayer(member).getName()));
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				gui.assignSlotButton(33, 1, new Button(item, (s,p,c) -> {
					
				}));
			}
			
			//Settings icon
			gui.assignSlotButton(34, 1, new Button(new ItemStack(Material.IRON_FENCE,1),(s,p,c)-> {
				openMainGUI(PMINE_GUI.SETTINGS, player, 0);
			})
					.setName("&8&l) &e&lSettings &8&l(")
					.setLore("&7Toggle pmine options here, as well",
							"&7as tax options to charge visitors"));
			
			
			{
				ItemStack item = new ItemStack(Material.EXP_BOTTLE);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6&l!&8]&8&m-------------------&8[&6&l!&8]"));
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.translateAlternateColorCodes('&', "&c&L No Booster Active"));
				lore.add(ChatColor.translateAlternateColorCodes('&', "&7 (( &7&oDrag n drop booster here &7))"));
				meta.setLore(lore);
				item.setItemMeta(meta);
				gui.assignSlotButton(43, 1, new Button(item, (s,p,c)-> {
					
				}));
			}
			
			
			//Teleport Home GUI Icon
			gui.assignSlotButton(28, 1, new Button(new ItemStack(Material.GRASS), (s,p,c) -> {
				if(!mine.isMineLoaded())
				{
					Iterator it = chunksToLoad.iterator();
					boolean inQueue = false;
					int queueLength = 0;
					int highestChunks = 0;
					while(it.hasNext())
					{
						Pair<PlayerMine, List<Chunk>> pair = ((Pair<PlayerMine, List<Chunk>>)it.next());
						if(pair.getValue().size() > highestChunks)
						{
							highestChunks = pair.getValue().size();
						}
						queueLength++;
						if(pair.getKey() == mine)
						{
							inQueue = true;
							break;
						}
					}
					if(!inQueue)
					{
						queueLength = chunksToLoad.size();
						Pair<PlayerMine, List<Chunk>> pair = new MutablePair<>(mine, mine.getMineSchemChunks());
						if(pair.getValue().size() > highestChunks)
						{
							highestChunks = pair.getValue().size();
						}
						chunksToLoad.add(pair);
					}
					
					float estWaitTime = (highestChunks * queueLength / (float)CHUNK_LOADCOUNT) / 20.f ;
					
					p.sendMessage("§eYour Player Mine is still being generated... Max wait time: " + (int)estWaitTime + "s.");
					p.sendMessage("§a§l§n"+queueLength+"§7 PlayerMines to be generated before yours...");
				}
				else
				{
					this.teleportPlayerToMine(p, mine);
				}
			})
					.setName("&8&l) &a&lTeleport Home &8&l(")
					.setLore("&7Click to teleport to Mine"));
			
			//Reset Mine Icon
			gui.assignSlotButton(29, 1, new Button(new ItemStack(Material.LAVA_BUCKET), (s,p,c) -> {
				if(mine.getMine().handleResetConditions(super.platform, RESET_TYPE.MANUAL, false))
				{
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.4f, 2.0f);
				}
				else
				{
					
					p.sendMessage("§cCannot reset mine yet. Cooldown §c§n"+formatTime( (int)((mine.getMine().getNextForceResetTime() - System.currentTimeMillis()) / 1000))+"§c.");
				}
				this.openMinePanelGUI(player, mine);
			})
					.setName("&8&l) &e&LReset Mine &8&l(")
					.setLore("&8* &7Mine Blocks &8<> &d" + df1.format(mine.getMine().getMineResetPercentage()) + "%",
							"&8* &7Last Reset &8<> &d" + lastResetTime + " ago" ,
							"&8",
							reset ));
			
			
			gui.open(platform, player);
			this.minePanelActive.put(player, gui);
		}
		else if (mine.isPublic())
		{
			this.teleportPlayerToMine(player, mine);
		}
	}
	
	//PMine SubGUI Function
	public void openMainGUI(PMINE_GUI guiType, Player player, int page)
	{
		switch(guiType)
		{
		
		//PMine First Time GUI
		case FIRST_TIME:
		{
			//No mine owned
			GUI gui = new GUI(3, "Create");
			gui.assignSlotButton(0, 27, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 7)));
			gui.assignSlotButton(13, 1, new Button(new ItemStack(Material.GRASS), (s,p,c) -> {
				createNewPMine(p);
				openMainGUI(PMINE_GUI.OWNED_MINES, player, 1);
			})
					.setName("&a&lCreate Your First PMine")
					.setLore("&7PMines are currently in &b&l&n"+ver_state.toString()+"&7.",
							"",
							"&7Start your Mining Journey by generating",
							"&7your first Player Mine.",
							"",
							" &b&lFeatures&7:",
							" &8&l* &7Upgradable Mine Size",
							" &8&l* &7Mine Schematic Presets",
							"",
							"&eStart your journey now! *Left-Click*"));
			gui.open(platform, player);
			break;
		}
		
		//PMine Mine Selection GUI
		case OWNED_MINES:
		case FRIEND_MINES:
		case PUBLIC_MINES:
		{
			Grid grid = getGrid();
			String title = "Owned Mines";
			List<PlayerMine> ownedPMines = new ArrayList<PlayerMine>();
			if(guiType == PMINE_GUI.OWNED_MINES)
				ownedPMines.addAll(grid.getPlayersMines(player));
			else if (guiType == PMINE_GUI.FRIEND_MINES)
			{
				title = "Friend Mines";
				if(Variables.getPlayer(player, "TrustedPMines") != null)
				{
					List<SimpleLocation> addedMines = (List<SimpleLocation>) Variables.getPlayer(player, "TrustedPMines");
					for(SimpleLocation loc : addedMines)
						ownedPMines.add(grid.getMineAtLocation(loc));	
				}
			}
			else if (guiType == PMINE_GUI.PUBLIC_MINES)
			{
				title = "Public Mines";
				ownedPMines.addAll(publicMines);
			}
			
			int pageOffset = (page-1) * 36;
			int guiRows = (int)Math.ceil((ownedPMines.size() - pageOffset) / 9.0);
			if(guiRows > 4 ) guiRows = 4;
			else if (guiRows < 0) guiRows = 0;
			guiRows += 2;
			GUI gui = new GUI(guiRows, title);
			int slot = 0;
			
			gui.assignSlotButton(0, guiRows*9, new Button(new ItemStack(Material.AIR)));
			
			for(int i = pageOffset; i < pageOffset + 36; ++i)
			{
				if(ownedPMines.size() <= i)
					break; //End of array reached
				
				PlayerMine mine = ownedPMines.get(i);
				ItemStack item = getPlayerMineIcon(mine);
				
				gui.assignSlotButton(slot++, 1, 
						new Button(item, (s,p,c) -> {
							this.openMinePanelGUI(p, mine);
						}));
			}
			
			int offset = (guiRows-2) * 9;
			
			short data = 7;
			if(guiType == PMINE_GUI.FRIEND_MINES) data = 1;
			else if (guiType == PMINE_GUI.PUBLIC_MINES) data = 3;
			gui.assignSlotButton(offset, 18, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) data)).setName("&7"));
			if(page > 1)
				gui.assignSlotButton(offset + 9 , 1, new Button(new ItemStack(Material.ARROW), (s,p,c) -> {
					openMainGUI(guiType, player, page-1);
				})
						.setName("&a&lPrevious Page")
						.setLore("&7Return to previous page"));
			if(ownedPMines.size() > pageOffset+36)
				gui.assignSlotButton(offset + 17, 1, new Button(new ItemStack(Material.ARROW), (s,p,c) -> {
					openMainGUI(guiType, player, page-1);
				})
						.setName("&a&lNext Page")
						.setLore("&7Go to next page"));
			
			gui.assignSlotButton(offset+12, 1, new Button(new ItemStack(Material.GRASS), (s,p,pc) -> {
				if(guiType != PMINE_GUI.OWNED_MINES)
					openMainGUI(PMINE_GUI.OWNED_MINES, player, 1);
			})
					.setName("&a&lYour Mines")
					.setLore("&7View your mines"));
			
			gui.assignSlotButton(offset+13, 1, new Button(new ItemStack(Material.SPONGE), (s,p,pc) -> {
				if(guiType != PMINE_GUI.FRIEND_MINES)
					openMainGUI(PMINE_GUI.FRIEND_MINES, player, 1);
			})
					.setName("&e&lInvited Mines")
					.setLore("&7View mines that people have added",
							"&7you to."));
			
			gui.assignSlotButton(offset+14, 1, new Button(new ItemStack(Material.BEACON), (s,p,pc) -> {
				if(guiType != PMINE_GUI.PUBLIC_MINES)
					openMainGUI(PMINE_GUI.PUBLIC_MINES, player, 1);
			})
					.setName("&b&lPublic Mines")
					.setLore("&7View public mines."));

			gui.open(platform, player);
			break;
		}
		
		//PMine Tax Claim GUI
		case TAXES:
		{
			GUI gui = new GUI(3, "Taxes");
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			
			gui.assignSlotButton(0, 27, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 7)).setName(""));
			
			gui.assignSlotButton(11, 1, new Button(new ItemStack(Material.INK_SACK,1,(short) 12),(s,p,c)-> {
				if(!(mine.getOwnerUUID().equals(player.getUniqueId())) && !mine.getSettings().getBooleanValue("MemberCollectTax"))
				{
					MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
					return;
				}
				
				double amt = mine.getTaxedAmount(TAX_TYPE.ENERGY);
				if(amt > 0)
				{
	
					ItemStack hand = p.getItemInHand();
					if(this.isExtractedEnergy(hand))
					{
						this.setExtractedEnergyValue(hand, this.getExtractedEnergyValue(hand) + (int) amt);
						mine.setTaxedAmount(TAX_TYPE.ENERGY, 0);
						openMainGUI(PMINE_GUI.TAXES, player, 0);
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 1.6f);
						p.sendMessage(MessageHandler.DEFAULT_PREFIX + ChatColor.translateAlternateColorCodes('&', "Successfully claimed &e" + toCommaFormat((int) amt) + " Pickaxe Energy&7."));
					}
					else
					{
						p.sendMessage(MessageHandler.DEFAULT_PREFIX + "Please hold an extracted energy item for PMines to add to it.");
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.3f);
					}
					
					
				}
				else
				{
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.3f);
				}
				
				
			})
					.setName("&b&LPickaxe Energy")
					.setLore("&7Claim the mine's Energy Taxes here",
							"&7",
							"&3&l > &7Total Taxed this week: &f" + toCommaFormat((int)mine.getTaxedAmount(TAX_TYPE.ENERGY)),
							"&7",
							"&7Available to claim: &e" + toCommaFormat((int)mine.getTaxedAmount(TAX_TYPE.ENERGY)),
							"&7(Left-click to claim)"
							
							));
			
			gui.assignSlotButton(13, 1, new Button(new ItemStack(Material.INK_SACK,1,(short) 11),(s,p,c)-> {
				if(!(mine.getOwnerUUID().equals(player.getUniqueId())) && !mine.getSettings().getBooleanValue("MemberCollectTax"))
				{
					MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
					return;
				}

				double amt = mine.getTaxedAmount(TAX_TYPE.MONEY);
				if(amt > 0)
				{
					
					econ.depositPlayer(player, amt);
					openMainGUI(PMINE_GUI.TAXES, player, 0);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 1.6f);
					p.sendMessage(MessageHandler.DEFAULT_PREFIX + ChatColor.translateAlternateColorCodes('&', "Successfully claimed &e$" + NumberBeauty.moneyFormat(mine.getTaxedAmount(TAX_TYPE.MONEY)) + "&7."));
					mine.setTaxedAmount(TAX_TYPE.MONEY, 0);
				}
				else
				{
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.3f);
				}
				
				
			})
					.setName("&e&LMoney")
					.setLore("&7Claim the mine's Money Taxes here",
							"&7",
							"&6&l > &7Total Taxed this week: &f$" + NumberBeauty.moneyFormat(mine.getTaxedAmount(TAX_TYPE.MONEY)),
							"&7",
							"&7Available to claim: &e$" + NumberBeauty.moneyFormat(mine.getTaxedAmount(TAX_TYPE.MONEY)),
							"&7(Left-click to claim)"
							
							));
			
			gui.assignSlotButton(15, 1, new Button(new ItemStack(Material.MAGMA_CREAM),(s,p,c)-> {
				if(!(mine.getOwnerUUID().equals(player.getUniqueId())) && !mine.getSettings().getBooleanValue("MemberCollectTax"))
				{
					MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
					return;
				}

				double amt = mine.getTaxedAmount(TAX_TYPE.TOKEN);
				if(amt > 0)
				{
					mine.setTaxedAmount(TAX_TYPE.TOKEN, 0);
					addTokens(p, amt);
					openMainGUI(PMINE_GUI.TAXES, player, 0);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 1.6f);
					p.sendMessage(MessageHandler.DEFAULT_PREFIX + ChatColor.translateAlternateColorCodes('&', "Successfully claimed &e" + toCommaFormat((int) amt) + " V-Tokens&7."));
					
				}
				else
				{
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 0.3f);
				}
				
				openMainGUI(PMINE_GUI.TAXES, player, 0);
			})
					.setName("&a&lVortex Tokens")
					.setLore("&7Claim the mine's Tokens Taxes here",
							"&7",
							"&6&l > &7Total Taxed this week: &f" + toCommaFormat((int)mine.getTaxedAmount(TAX_TYPE.TOKEN)),
							"&7",
							"&7Available to claim: &e" +toCommaFormat((int)mine.getTaxedAmount(TAX_TYPE.TOKEN)),
							"&7(Left-click to claim)"
							
							));
			
			gui.assignSlotButton(26, 1, new Button(new ItemStack(Material.BARRIER, 1), (s,p,c) -> {
				openMinePanelGUI(p, mine);
			}).setName("&c&lGo Back")
					.setLore("&7Click to go back"));
			
			gui.open(platform, player);
			break;
		}
		
		//PMine Factory Contribution GUI
		case CONTRIBUTE_TOKENS:
		case CONTRIBUTE_MONEY:
		case CONTRIBUTE_ENERGY:
		case CONTRIBUTE_CUSTOMITEM:
		{
			GUI gui = new GUI(3, "Upgrade");
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			
			MineFactory current = mine.getMine().getMineFactorySelected();
			MineFactoryGroup mfg = current.getFactoryGroup();
			
			String futureConfigName = current.getFactoryGroup().getGroupID() + ";" + String.valueOf((current.getLevel() + 1));
			MineFactory futureFactory = mine.getMine().createOrGetMineFactory(futureConfigName);
			//Requirements
			MineFactoryConfig futureMFC = configHandler.mineFactories.get(futureFactory.getID());
			
			boolean completed = false;
			if(guiType == PMINE_GUI.CONTRIBUTE_MONEY)
			{	
				if(futureFactory.getCommitedMoney() >= futureMFC.getMoneyRequired())
					completed = true;
				
				gui.assignSlotButton((completed ? 13 : 10), 1, new Button(new ItemStack(Material.INK_SACK,1,(short)11))
						.setName("&e&lMoney Required")
						.setLore("&7",
								"&6&l * &e&lRequirements",
								" &f" + NumberBeauty.moneyFormat(futureFactory.getCommitedMoney()) + "&8/&f" + NumberBeauty.moneyFormat(futureMFC.getMoneyRequired()) 
								+ "&7 In-Game-Money",
								"&7 ")
						.setGlow(completed));
				
				if(!completed)
				{
					long[] percentages = {10,25,50,75,100};
					long remainingRequired = futureMFC.getMoneyRequired() - futureFactory.getCommitedMoney();
					double completedPercentage = (double)futureFactory.getCommitedMoney() / (double)futureMFC.getMoneyRequired() * 100;
					int slot = 12;
					for(long percentage : percentages)
					{
						final long val = (long) (percentage * 0.01 * remainingRequired);
						final String displayVal = toCommaFormat(Math.ceil(val));
						gui.assignSlotButton(slot++, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 5)
								,(s,p,c)-> {
									if(econ.getBalance(p) > val)
									{
										econ.withdrawPlayer(p, val);
										p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6f, 1.7f);
										futureFactory.addMoney(val);
										
										openMainGUI(PMINE_GUI.CONTRIBUTE_MONEY, p, 1);
									}
									else
									{
										p.sendMessage(MessageHandler.DEFAULT_PREFIX + "Insufficient funds.");
									}
								})
								.setName("&a&l" + (int)percentage + "% of Remainder" )
								.setLore("&7Deposit $" + displayVal + " &8(&f$" + NumberBeauty.moneyFormat(val) + "&8)",
										"",
										"&8 Progress",
										LoreBeauty.percentToBar(completedPercentage, 25, 'c', 'a')));
					}
				}	
			}
			
			else if(guiType == PMINE_GUI.CONTRIBUTE_TOKENS)
			{	
				if(futureFactory.getCommitedTokens() >= futureMFC.getTokensRequired())
					completed = true;
				
				gui.assignSlotButton((completed ? 13 : 10), 1, new Button(new ItemStack(Material.MAGMA_CREAM,1))
						.setName("&a&lTokens Required")
						.setLore("&7",
								"&6&l * &e&lRequirements",
								" &f" + NumberBeauty.moneyFormat(futureFactory.getCommitedTokens()) + "&8/&f" + NumberBeauty.moneyFormat(futureMFC.getTokensRequired()) 
								+ "&7 In-Game-Tokens",
								"&7 ")
						.setGlow(completed));
				
				if(!completed)
				{
					long[] percentages = {10,25,50,75,100};
					long remainingRequired = futureMFC.getTokensRequired() - futureFactory.getCommitedTokens();
					double completedPercentage = (double)futureFactory.getCommitedTokens() / (double)futureMFC.getTokensRequired() * 100;
					int slot = 12;
					for(long percentage : percentages)
					{
						final long val = (long) (percentage * 0.01 * remainingRequired);
						final String displayVal = toCommaFormat(Math.ceil(val));
						gui.assignSlotButton(slot++, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 5)
								,(s,p,c)-> {
									if(getTokens(p) > val)
									{
										addTokens(p, -val);
										p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6f, 1.7f);
										futureFactory.addTokens(val);
										openMainGUI(PMINE_GUI.CONTRIBUTE_TOKENS, p, 1);
									}
									else
									{
										p.sendMessage(MessageHandler.DEFAULT_PREFIX + "Insufficient funds.");
									}
								})
								.setName("&a&l" + (int)percentage + "% of Remainder" )
								.setLore("&7Deposit " + displayVal + " Tokens &8(&f" + NumberBeauty.moneyFormat(val) + "&8)",
										"",
										"&8 Progress",
										LoreBeauty.percentToBar(completedPercentage, 25, 'c', 'a')));
					}
				}	
			}
			
			else if(guiType == PMINE_GUI.CONTRIBUTE_ENERGY)
			{	
				if(futureFactory.getCommitedEnergy() >= futureMFC.getEnergyRequired())
					completed = true;
				
				gui.assignSlotButton((completed ? 13 : 10), 1, new Button(new ItemStack(Material.INK_SACK,1,(short)12))
						.setName("&a&lEnergy Required")
						.setLore("&7",
								"&6&l * &e&lRequirements",
								" &f" + NumberBeauty.moneyFormat(futureFactory.getCommitedEnergy()) + "&8/&f" + NumberBeauty.moneyFormat(futureMFC.getEnergyRequired()) 
								+ "&7 Energy",
								"&7 ")
						.setGlow(completed));
				
				if(!completed)
				{
					long[] percentages = {10,25,50,75,100};
					long remainingRequired = futureMFC.getEnergyRequired() - futureFactory.getCommitedEnergy();
					double completedPercentage = (double)futureFactory.getCommitedEnergy() / (double)futureMFC.getEnergyRequired() * 100;
					int slot = 12;
					for(long percentage : percentages)
					{
						final long val = (long) (percentage * 0.01 * remainingRequired);
						final String displayVal = toCommaFormat(Math.ceil(val));
						gui.assignSlotButton(slot++, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 5)
								,(s,p,c)-> {
									int energyVal = getExtractedEnergyValue(player.getItemInHand());
									if(energyVal > val)
									{
										int resultant = (int) (energyVal - val);
										setExtractedEnergyValue(player.getItemInHand(), resultant);
										p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6f, 1.7f);
										futureFactory.addEnergy(val);
										openMainGUI(PMINE_GUI.CONTRIBUTE_ENERGY, p, 1);
									}
									else
									{
										p.sendMessage(MessageHandler.DEFAULT_PREFIX + "Hold enough energy on your hand!");
									}
								})
								.setName("&a&l" + (int)percentage + "% of Remainder" )
								.setLore("&7Deposit " + displayVal + " Energy &8(&f" + NumberBeauty.moneyFormat(val) + "&8)",
										"",
										"&8 Progress",
										LoreBeauty.percentToBar(completedPercentage, 25, 'c', 'a')));
					}
				}
			}
			
			gui.assignSlotButton(26, 1, new Button(new ItemStack(Material.BARRIER, 1), (s,p,c) -> {
				if(!factoryGUIPlayers.contains(player.getUniqueId()))
				{
					factoryGUIToAdd.add(player.getUniqueId());	
				}
				openMainGUI(PMINE_GUI.MINEFACTORY_UPGRADE, player, 1);
			}).setName("&c&lGo Back")
					.setLore("&7Click to go back"));
			
			gui.open(platform, player);
			
		}
			break;
			
		//PMine Mine Expansion GUI
		case MINEEXPANSION:
		{
			GUI gui = new GUI(5, "Mine Expansion");
			gui.assignSlotButton(0, 45, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)7)));
			
			Grid grid = getGrid();
			final PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			
			gui.assignSlotButton(10, 1, new Button(new ItemStack(Material.BEACON))
					.setName("&e&lMine Width Expansion")
					.setLore("&7Expand your mines width here",
							"&7",
							"&6&l * &e&lCurrent Width",
							"&7 &7 &7 &7 &7" + mine.getMine().getMineSize() + " by " +  mine.getMine().getMineSize()));
			
			//Width Expansions
			{
				int width = mine.getMine().getMineSize();
				List<Integer> expansions = new ArrayList<>( Mines.getMinesConfig().mineWidthExpansion.keySet());
				
				
				int index = 0;
				for(Integer adw : expansions)
				{
					if(adw.equals(width)) 
					{
						break;
					}
					index++;
				}
				
				int pageOffset = (int) Math.floor( (index+1) / 5.0);
				int slot = 12;
				
				int nextWidth = 0;
				if(Mines.getMinesConfig().mineWidthExpansion.higherKey(width) != null)
					nextWidth = Mines.getMinesConfig().mineWidthExpansion.higherKey(width);
				final int fnextWidth = nextWidth;
				for(int i = (pageOffset + page-1) * 5 ; i < (pageOffset + page) * 5; ++i)
				{
					if(expansions.size() <= i) break;
					MineExpansionConfig expansion = Mines.getMinesConfig().mineWidthExpansion.get(expansions.get(i));
					
					//Bukkit.broadcastMessage("loading width " + expansion.getExpansionValue() + " at slot " + slot);
					if(expansion.getExpansionValue() <= width)
					{
						gui.assignSlotButton(slot++, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)5))
								.setName("&a&lWidth Expansion")
								.setLore("&7",
										"&8 Expansion Info",
										"&7 Mine Width: &a"+expansion.getExpansionValue() + "x" + expansion.getExpansionValue(),
										"&7",
										(width == expansion.getExpansionValue() ? "&aThis is equipped" : "&aCannot be equipped")));
					}
					else
					{
						
						gui.assignSlotButton(slot++, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14)
								,(s,p,c) -> {
									if(fnextWidth == expansion.getExpansionValue())
									{
										if(mine.getOwnerUUID().equals(player.getUniqueId()))
										{
											String var = (String) Variables.getPlayer(player, "rankup");
											//System.out.println(var + " eee " + (var == null));
											if(var == null) {  //not needed since placeholder should always be the first to execute?
												Variables.setPlayer(player, "rankup", "1;0;0");
												var = "1;0;0";
												//System.out.println("set");
											}
											String[] split = var.split(";");  //using ; because it will not compile to regex and is much faster
											double xp = Double.parseDouble(split[1]);  //only using double to avoid int / int round down problem
											int lv = Integer.parseInt(split[0]);
											int pres = Integer.parseInt(split[2]);
											
											if(lv >= expansion.getOwnerLevelRequired() && pres >= expansion.getOwnerPrestigeRequired())
											{
												if(econ.getBalance(player) >= expansion.getMoneyRequired())
												{
													econ.withdrawPlayer(player, expansion.getMoneyRequired());
													player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
													mine.getMine().setXZRadius((fnextWidth-1)/2);
													mine.clearMineSpace();
													mine.buildBedrock();
													mine.getMine().handleResetConditions(platform, RESET_TYPE.MANUAL, true);
													openMainGUI(PMINE_GUI.MINEEXPANSION, player, page);
													
													for(Entity entity : mine.getEntitiesInPMine())
													{
														Player r = (Player) entity;
														r.playSound(r.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 1.2f);
														r.playSound(r.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.2f);
														r.playSound(r.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.8f);
														r.sendMessage("");
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8================ "));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7This player mine has just been Expanded!"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " &2&l> &a&lNew Mine Width"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " &a ◎ &c&m" + width+"x"+width +  "&7 to &a"+fnextWidth+"x"+fnextWidth + " &7Mine Width"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oView all Mine Expansions in /pmine"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8================ "));
														r.sendMessage("");
													}
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b&l◎ &3&lPMINES &7&l- &f" + Bukkit.getOfflinePlayer(mine.getOwnerUUID()).getName() + "&7's Player Mine has been upgraded to &a"+fnextWidth+"x"+fnextWidth + " &7Mine Width"));
												}
												else
												{
													player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.7f, 0.2f);
													player.sendMessage(MessageHandler.DEFAULT_PREFIX + "Insufficient Funds");
												}	
											}
											else
											{
												player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.7f, 0.2f);
												player.sendMessage(MessageHandler.DEFAULT_PREFIX + "Leveling requirements not met");
											}
										}
										else
										{
											MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
										}
										
									}
								})
								.setName("&c&lWidth Expansion")
								.setLore("&7",
										"&8 Expansion Info",
										"&7 Mine Width: &a"+expansion.getExpansionValue() + "x" + expansion.getExpansionValue(),
										"&7",
										"&8 Upgrade Requirements",
										"&7 Owner Prestige: &a" + expansion.getOwnerPrestigeRequired(),
										"&7 Owner Leveling: &a" + expansion.getOwnerLevelRequired(),
										"&7",
										"&8 Upgrade Cost",
										"&7 Money: &a$" + toCommaFormat(expansion.getMoneyRequired()) + " &8(&f" + NumberBeauty.moneyFormat(expansion.getMoneyRequired()) + "&8)" ,
										
										"&7",
										(nextWidth == expansion.getExpansionValue() ? "&7Left-Click to &7&nupgrade&7." : "&7Unlock previous first")));
					}
					
				}
			}
			
			
			
			gui.assignSlotButton(28, 1, new Button(new ItemStack(Material.BEDROCK))
					.setName("&a&lMine Height Expansion")
					.setLore("&7Expand your mines total length here",
							"&7",
							"&2&l * &a&lCurrent Height",
							"&7 &7 &7 &7 &7" + mine.getMine().getMineYLength() + " Blocks"));
			
			//Height Expansion
			{
				int height = mine.getMine().getMineYLength();
				List<Integer> expansions = new ArrayList<>( Mines.getMinesConfig().mineHeightExpansion.keySet());
				//Bukkit.broadcastMessage(expansions.size() + "da" );
				
				int index = 0;
				for(Integer adw : expansions)
				{
					//Bukkit.broadcastMessage(adw +  " == height?");
					if(adw.equals(height)) 
					{
						break;
					}
					index++;
				}
				
				int pageOffset = (int) Math.floor( (index+1) / 5.0);
				int slot = 30;
				
				int nextHeight = 0;
				if(Mines.getMinesConfig().mineHeightExpansion.higherKey(height) != null)
					nextHeight = Mines.getMinesConfig().mineHeightExpansion.higherKey(height);
				final int fnextHeight = nextHeight;
				for(int i = (pageOffset + page-1) * 5 ; i < (pageOffset + page) * 5; ++i)
				{
					if(expansions.size() <= i) break;
					MineExpansionConfig expansion = Mines.getMinesConfig().mineHeightExpansion.get(expansions.get(i));
					
					//Bukkit.broadcastMessage("loading height " + expansion.getExpansionValue() + " at slot " + slot);
					if(expansion.getExpansionValue() <= height)
					{
						gui.assignSlotButton(slot++, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)5))
								.setName("&a&lHeight Expansion")
								.setLore("&7",
										"&8 Expansion Info",
										"&7 Mine Height: &a"+expansion.getExpansionValue() + "x" + expansion.getExpansionValue(),
										"&7",
										(height == expansion.getExpansionValue() ? "&aThis is equipped" : "&aCannot be equipped")));
					}
					else
					{
						
						gui.assignSlotButton(slot++, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14)
								,(s,p,c) -> {
									if(fnextHeight == expansion.getExpansionValue())
									{
										if(mine.getOwnerUUID().equals(player.getUniqueId()))
										{
											String var = (String) Variables.getPlayer(player, "rankup");
											//System.out.println(var + " eee " + (var == null));
											if(var == null) {  //not needed since placeholder should always be the first to execute?
												Variables.setPlayer(player, "rankup", "1;0;0");
												var = "1;0;0";
												//System.out.println("set");
											}
											String[] split = var.split(";");  //using ; because it will not compile to regex and is much faster
											double xp = Double.parseDouble(split[1]);  //only using double to avoid int / int round down problem
											int lv = Integer.parseInt(split[0]);
											int pres = Integer.parseInt(split[2]);
											
											if(lv >= expansion.getOwnerLevelRequired() && pres >= expansion.getOwnerPrestigeRequired())
											{
												if(econ.getBalance(player) >= expansion.getMoneyRequired())
												{
													econ.withdrawPlayer(player, expansion.getMoneyRequired());
													
													mine.getMine().setYRadius(fnextHeight);
													mine.clearMineSpace();
													mine.buildBedrock();
													mine.getMine().handleResetConditions(platform, RESET_TYPE.MANUAL, true);
													openMainGUI(PMINE_GUI.MINEEXPANSION, player, page);
													for(Entity entity : mine.getEntitiesInPMine())
													{
														Player r = (Player) entity;
														r.playSound(r.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 1.2f);
														r.playSound(r.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.2f);
														r.playSound(r.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.8f);
														r.sendMessage("");
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8================ "));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7This player mine's length of mine has just been Upgraded!"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " &6&l> &e&lNew Mine Depth"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e ◎ &c&m" + height +  "&7 to &a"+fnextHeight + " &7Mine Depth"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oView all Mine Expansions in /pmine"));
														r.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8================ "));
														r.sendMessage("");
													}
												}
												else
												{
													player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.7f, 0.2f);
													player.sendMessage(MessageHandler.DEFAULT_PREFIX + "Insufficient Funds");
												}	
											}
											else
											{
												player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.7f, 0.2f);
												player.sendMessage(MessageHandler.DEFAULT_PREFIX + "Leveling requirements not met");
											}
										}
										else
										{
											MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
										}
										
									}
								})
								.setName("&c&lHeight Expansion")
								.setLore("&7",
										"&8 Expansion Info",
										"&7 Mine Height: &a"+expansion.getExpansionValue() + "x" + expansion.getExpansionValue(),
										"&7",
										"&8 Upgrade Cost",
										"&7 Money: &a$" + toCommaFormat(expansion.getMoneyRequired()) + " &8(&f" + NumberBeauty.moneyFormat(expansion.getMoneyRequired()) + "&8)" ,
										
										"&7",
										(nextHeight == expansion.getExpansionValue() ? "&7Left-Click to &7&nupgrade&7." : "&7Unlock previous first")));
					}
					
				}
			}
			
			
			gui.open(platform, player);
			break;
		}
		
		//PMine Mine Factory Upgrade GUI
		case MINEFACTORY_UPGRADE:
		{
			
			GUI gui = new GUI(6, "Upgrade Factory");
			gui.assignSlotButton(0, 54, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)7)));
			
			Grid grid = getGrid();
			final PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			
			MineFactory current = mine.getMine().getMineFactorySelected();
			MineFactoryGroup mfg = current.getFactoryGroup();
			
			String futureConfigName = current.getFactoryGroup().getGroupID() + ";" + String.valueOf((current.getLevel() + 1));
			MineFactory pmineFactory = mine.getMine().createOrGetMineFactory(futureConfigName );
			//Requirements
			
			if(pmineFactory == null)
			{
				openMainGUI(PMINE_GUI.MINEFACTORIES,player,1);
				//System.out.println("Null Factory");
				break;	
			}
			else if (pmineFactory.canEquip())
			{
				openMainGUI(PMINE_GUI.MINEFACTORIES,player,1);
				//System.out.println("Already equippable");
				break;
			}
			
			MineFactoryConfig mfc = configHandler.mineFactories.get(pmineFactory.getID());
			
			
			//Main Icon
			{
				ItemStack icon = new ItemStack(Material.BEACON);
				ItemMeta meta = icon.getItemMeta();
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&LUPGRADE TO LEVEL " + pmineFactory.getLevel()));
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.translateAlternateColorCodes('&', " "));
				lore.add(ChatColor.translateAlternateColorCodes('&', " &8Level "+ pmineFactory.getLevel() +" Composition"));
				
				lore.addAll(IconFactory.getMineFactoryCompositionLoreWithDifference(current, pmineFactory));
				
				lore.add(ChatColor.translateAlternateColorCodes('&', " "));
				lore.add(ChatColor.translateAlternateColorCodes('&', " &8Buffs"));
				for(Entry<PERK_TYPE, Float> perk : pmineFactory.getFactoryPerks().entrySet())
				{
					lore.add(ChatColor.translateAlternateColorCodes('&', " &7"+IconFactory.getPerkDisplayName(perk.getKey()) +": &a" 
							+ NumberBeauty.formatNumber(perk.getValue()) + "%"));
				}
				
				lore.add(ChatColor.translateAlternateColorCodes('&', " "));
				
				
				
				
				meta.setLore(lore);
				icon.setItemMeta(meta);
				
				gui.assignSlotButton(10, 1, new Button(icon).setGlow(true));
			}
			
			
			Set<MineBlock> blocks = pmineFactory.getFactoryComposition().getBlockTypes();
			int blockRows = 1;
			gui.assignSlotButton(12, 5, new Button(new ItemStack(Material.AIR)));
			if(blocks.size() > 5) {
				gui.assignSlotButton(21, 5, new Button(new ItemStack(Material.AIR)));
				blockRows = 2;
			}
			
			int slot = 12;
			for(MineBlock block : blocks)
			{
				ItemStack stack = new ItemStack(block.getMaterial(), 1, block.getData());
				double oldVal = 0.0;
				double newVal = pmineFactory.getFactoryComposition().getChanceOf(block);
				for(MineBlock loop : current.getFactoryComposition().getBlockTypes())
				{
					if(loop.getMaterial() == block.getMaterial() && loop.getData() == block.getData()) 
					{
						oldVal = current.getFactoryComposition().getChanceOf(loop);
					}
				}
				float xp = MineBlock.getBaseXPDropped(block.getMaterial(), block.getData());
				gui.assignSlotButton(slot++, 1, new Button(stack)
						.setName("&7&l" + ItemNames.lookup(stack) + (xp > 1.0 ? " &8(&b"+xp+"x Level XP&8)":""))
						.setLore("&7","&a&l " + NumberBeauty.formatNumber(newVal) + "%" + ( oldVal > 0.0 && newVal > oldVal ? " &8(&7From " + NumberBeauty.formatNumber(oldVal) + "%&8)" : ""))
						.setGlow(xp > 1.0));
				if(slot >= 17) slot += 4;
				else if (slot >= 26) break; //Only show max 10 items
			}
			
			
			//Completion/Completing Display
			long progressiveBlocksMined = pmineFactory.getBlocksMined();
			long totalBlocksMined = mine.getTracker("MinedTotal");
			long moneyContributed = pmineFactory.getCommitedMoney();
			long tokensContributed = pmineFactory.getCommitedTokens();
			long energyContributed = pmineFactory.getCommitedEnergy();
			
			boolean completed = false;
			
			if(progressiveBlocksMined >= mfc.getProgressiveBlocksMinedRequired() 
				&& totalBlocksMined >= mfc.getTotalBlocksMinedRequired()
				&& moneyContributed >= mfc.getMoneyRequired()
				&& tokensContributed >= mfc.getTokensRequired()
				&& energyContributed >= mfc.getEnergyRequired())
			{
				boolean customItems = true;
				for(CustomItem required : mfc.getCustomItemsRequired())
				{
					boolean found = false;
					for(CustomItem contributed : pmineFactory.getCommitedItems())
					{
						if(required.isEqual(contributed, false))
						{
							if(contributed.getQuantity() >= required.getQuantity())
							{
								found = true;
								break;
							}
						}
					}
					if(!found)
					{
						customItems = false;
						break;
					}
				}
				
				completed = true;
				pmineFactory.unlock(player, totalBlocksMined, progressiveBlocksMined, mine.getMine().getMineFactories());
				Long remainingTime = pmineFactory.getTimeUnlockedAt() - System.currentTimeMillis();
				double unlockProgress = (mfc.getDurationToUnlock() * 1000 - remainingTime) / (mfc.getDurationToUnlock() * 10);
				
				if(pmineFactory.isUnlocking() && unlockProgress < 100.0)
				{
					gui.assignSlotButton(49,1, new Button(new ItemStack(Material.EMERALD_BLOCK))
							.setName("&a&lUPGRADING...")
							.setLore("&7This factory is being upgraded..."
									, ""
									, "&8 Progress"
									, LoreBeauty.percentToBar(unlockProgress, 25, '7', 'a')
									, "&7 Time Remaining: &f" + formatTime( (int)(remainingTime/1000) )));	
				}
			}else
			{
				gui.assignSlotButton(49, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1, (short) 14 ))
						.setName("&c&lUpgrade Not Started")
						.setLore("&7Starts automatically when all",
								"&7requirements are complete."));
			}
			
			//Brewing Stand
			
			ItemStack dis = new ItemStack(Material.BREWING_STAND_ITEM);
			ItemMeta meta = dis.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lUpgrade Requirements"));
			List<String> sLore = new ArrayList<String>();
			sLore.add(ChatColor.translateAlternateColorCodes('&', "&7Complete the remainder to start upgrade"));
			sLore.add(ChatColor.translateAlternateColorCodes('&', "&7 "));
			

			
			int progSlot = 21+blockRows*9;
			gui.assignSlotButton(progSlot, 5, new Button(new ItemStack(Material.AIR)));
			
			String botLore;
			boolean done;
			
			//Progressive Blocks Mined
			if(mfc.getProgressiveBlocksMinedRequired() > 0)
			{
				botLore = "&7Mine to Contribute";
				done = false;
				if (progressiveBlocksMined >= mfc.getProgressiveBlocksMinedRequired())
				{
					done = true;
					botLore = "&aThese requirements have been met";
				}
				
				double progress = progressiveBlocksMined / (double)mfc.getProgressiveBlocksMinedRequired() * 100.0;
				if(progress < 100.0)
				{
					sLore.add(ChatColor.translateAlternateColorCodes('&', "&e Progressive Blocks Mined"));
					sLore.add(ChatColor.translateAlternateColorCodes('&', "&7 " + 
					LoreBeauty.percentToBar(progress, 25, '7', 'a')));
					sLore.add(" ");	
				}
				
				gui.assignSlotButton(progSlot++, 1, new Button(new ItemStack((done ? Material.EMERALD_BLOCK:Material.IRON_PICKAXE),1),
						(s,p,c) -> {
							
						})
						.setName("&f&LProgressive Blocks Mined")
						.setLore("&7",
								"&8&l * &f&lRequirements",
								" &f" + NumberBeauty.moneyFormat(progressiveBlocksMined) + "&8/&f" + NumberBeauty.moneyFormat(mfc.getProgressiveBlocksMinedRequired()) 
								+ "&7 Progressive Blocks Mined",
								"&7 ",
								botLore)
						.setGlow( botLore.startsWith("&a") ));	
			}
			
			
			//Total Blocks Mined
			if(mfc.getTotalBlocksMinedRequired() > 0)
			{
				botLore = "&7Mine to Contribute";
				done = false;
				if (totalBlocksMined >= mfc.getTotalBlocksMinedRequired())
				{
						botLore = "&aThese requirements have been met";
						done = true;
				}
				
				double progress = totalBlocksMined / (double)mfc.getTotalBlocksMinedRequired() * 100.0;
				sLore.add(ChatColor.translateAlternateColorCodes('&', "&e Total Blocks Mined"));
				sLore.add(ChatColor.translateAlternateColorCodes('&', "&7 " + 
				LoreBeauty.percentToBar(progress, 25, '7', 'a')));
				sLore.add(" ");
				
				gui.assignSlotButton(progSlot++, 1, new Button(new ItemStack((done ? Material.EMERALD_BLOCK : Material.IRON_PICKAXE),1),
						(s,p,c) -> {
							
						})
						.setName("&b&LTotal Blocks Mined")
						.setLore("&7",
								"&3&l * &b&lRequirements",
								" &f" + NumberBeauty.moneyFormat(totalBlocksMined) + "&8/&f" + NumberBeauty.moneyFormat(mfc.getTotalBlocksMinedRequired()) 
								+ "&7 Total Blocks Mined",
								"&7 ",
								botLore)
						.setGlow( botLore.startsWith("&a") ));	
			}
			
			
			//Money Required
			if(mfc.getMoneyRequired() > 0)
			{
				botLore = "&7Left-Click to Contribute";
				done = false;
				if (moneyContributed >= mfc.getMoneyRequired())
				{
					botLore = "&aThese requirements have been met";
					done = true;
				}
				
				double progress = moneyContributed / (double)mfc.getMoneyRequired() * 100.0;
				if(progress < 100.0)
				{
					sLore.add(ChatColor.translateAlternateColorCodes('&', "&e Money Required"));
					sLore.add(ChatColor.translateAlternateColorCodes('&', "&7 " + 
					LoreBeauty.percentToBar(progress, 25, '7', 'a')));
					sLore.add(" ");	
				}
				
				
				gui.assignSlotButton(progSlot++, 1, new Button(new ItemStack((done? Material.EMERALD_BLOCK : Material.INK_SACK),1,(short)11),
						(s,p,c) -> {
							openMainGUI(PMINE_GUI.CONTRIBUTE_MONEY, p, 1);
						})
						.setName("&e&lMoney Required")
						.setLore("&7",
								"&6&l * &e&lRequirements",
								" &f" + NumberBeauty.moneyFormat(moneyContributed) + "&8/&f" + NumberBeauty.moneyFormat(mfc.getMoneyRequired()) 
								+ "&7 In-Game-Money",
								"&7 ",
								botLore)
						.setGlow( botLore.startsWith("&a") ));	
			}
			
			
			//Tokens Required
			if(mfc.getTokensRequired() > 0)
			{
				botLore = "&7Left-Click to Contribute";
				done = false;
				if (tokensContributed >= mfc.getTokensRequired())
					{
						botLore = "&aThese requirements have been met";
						done = true;
					}
				
				double progress = tokensContributed / (double)mfc.getTokensRequired() * 100.0;
				if(progress < 100.0)
				{
				sLore.add(ChatColor.translateAlternateColorCodes('&', "&e Tokens Required"));
				sLore.add(ChatColor.translateAlternateColorCodes('&', "&7 " + 
				LoreBeauty.percentToBar(progress, 25, '7', 'a')));
				sLore.add(" ");
				}
				
				gui.assignSlotButton(progSlot++, 1, new Button(new ItemStack((done ? Material.EMERALD_BLOCK : Material.MAGMA_CREAM)),
						(s,p,c) -> {
							openMainGUI(PMINE_GUI.CONTRIBUTE_TOKENS, p, 1);
						})
						.setName("&a&lTokens Required")
						.setLore("&7",
								"&2&l * &a&lRequirements",
								" &f" + NumberBeauty.moneyFormat(tokensContributed) + "&8/&f" + NumberBeauty.moneyFormat(mfc.getTokensRequired()) 
								+ "&7 VTokens",
								"&7 ",
								botLore)
						.setGlow( botLore.startsWith("&a") ));	
			}
			
			
			//Energy Required
			if(mfc.getEnergyRequired() > 0)
			{
				botLore = "&7Left-Click to Contribute";
				done = false;
				if (energyContributed >= mfc.getEnergyRequired())
				{
					done = true;
					botLore = "&aThese requirements have been met";
				}
				
				
				double progress = energyContributed / (double)mfc.getEnergyRequired() * 100.0;
				if(progress < 100.0)
				{
				sLore.add(ChatColor.translateAlternateColorCodes('&', "&e Energy Blocks Mined"));
				sLore.add(ChatColor.translateAlternateColorCodes('&', "&7 " + 
				LoreBeauty.percentToBar(progress, 25, '7', 'a')));
				sLore.add(" ");
				}
				
				gui.assignSlotButton(progSlot++, 1, new Button(new ItemStack((done? Material.EMERALD_BLOCK:Material.INK_SACK),1,(short)12),
						(s,p,c) -> {
							openMainGUI(PMINE_GUI.CONTRIBUTE_ENERGY, p, 1);
						})
						.setName("&b&lEnergy Required")
						.setLore("&7",
								"&3&l * &b&lRequirements",
								" &f" + NumberBeauty.moneyFormat(energyContributed) + "&8/&f" + NumberBeauty.moneyFormat(mfc.getEnergyRequired()) 
								+ "&7 Pickaxe Energy",
								"&7 ",
								botLore)
						.setGlow( botLore.startsWith("&a") ));		
			}
		
			/*
			//Custom Items
			int cI_slot = 36;
			for(CustomItem item : mfc.getCustomItemsRequired())
			{
				for(CustomItem compare : pmineFactory.getCommitedItems())
				{
					if(compare.isEqual(item, false))
					{
						
						ItemStack icon = (pmineFactory.canEquip() ? new ItemStack(mfg.getIconMaterial(), 1, mfg.getIconData()) : new ItemStack(Material.BARRIER,1));
						ItemMeta meta = icon.getItemMeta();
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.getDisplayName()));
						List<String> lore = new ArrayList<String>();
						lore.addAll(item.getLore());
						lore.add(ChatColor.translateAlternateColorCodes('&', " "));
						lore.add(ChatColor.translateAlternateColorCodes('&', " &8&l* &7&lRequirements"));
						lore.add(ChatColor.translateAlternateColorCodes('&', " &f"+compare.getQuantity() + "&8/&f" + item.getQuantity() + " &7of this Item"));
						lore.add(ChatColor.translateAlternateColorCodes('&', " "));
						lore.add(ChatColor.translateAlternateColorCodes('&', botLore));
						meta.setLore(lore);
						icon.setItemMeta(meta);
						
						botLore = "&7Left-Click to Contribute";
						if (compare.getQuantity() >= item.getQuantity())
							botLore = "&aThese requirements have been met";
						gui.assignSlotButton(++cI_slot, 1, new Button((icon),
								(s,p,c) -> {
									
								})
								.setGlow( botLore.startsWith("&a") ));
					
						
						break;
					}
				}
			}*/
			
			meta.setLore(sLore);
			dis.setItemMeta(meta);
			gui.assignSlotButton(19+blockRows*9, 1, new Button(dis));
			
			gui.assignSlotButton(53, 1, new Button(new ItemStack(Material.BARRIER, 1), (s,p,c) -> {
				openMainGUI(PMINE_GUI.MINEFACTORIES, player, 1);
			}).setName("&c&lGo Back")
					.setLore("&7Click to go back"));
			
			gui.open(platform, player);
			
			break;
		}
		
		//PMine Mine Factory List GUI
		case MINEFACTORIES:
		{
			
			GUI gui = new GUI(5, "Mine Factories");
			gui.assignSlotButton(0, 45, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)7)));
			
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			
			boolean nextPage = true;
			
			List<MineFactoryGroup> groups = new ArrayList<MineFactoryGroup>();
			groups.addAll(configHandler.mineFactoryGroups.values());
			int slot = 9;
			for(int i = (page-1) * 28; i < page * 28; ++i)
			{
				if( (++slot + 1) % 9 == 0 ) slot += 2;
				if(groups.size() > i)
				{
					MineFactoryGroup mfg = groups.get(i);
					
					int pm_factoryLevel = 0;
					MineFactory pmineFactory = new MineFactory(mfg.getGroupID()+";1");
					boolean isEquipped = false;
					
					
					for(MineFactory fact : mine.getMine().getMineFactories())
					{
						if(!fact.canEquip() && fact.unlock(player, 0, 0, null))
						{
							player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.8f);
							for(Entity e : mine.getEntitiesInPMine())
							{
								if(e instanceof Player)
								{
									Player p = (Player) e;
									if(mine.isPartOfMine(p.getUniqueId()))
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMine Factory \"" + fact.getFactoryGroup().getGroupID() + "\" can now be equipped."));
								}
							}
						}
					}
					
					int level = 0;
					String grpID = "";
					for(MineFactory fact : mine.getMine().getMineFactories())
					{
						if(fact.getFactoryGroup().equals(mfg) && fact.canEquip() && fact.getLevel() > level )
						{
							grpID = fact.getFactoryGroup().getGroupID();
							pmineFactory = fact;
							isEquipped = mine.getMine().getMineFactorySelected().getFactoryGroup().equals(fact.getFactoryGroup());
							level = fact.getLevel();
							
							if(fact.getLevel() > mine.getMine().getMineFactorySelected().getLevel() && fact.getFactoryGroup().equals(mine.getMine().getMineFactorySelected().getFactoryGroup()))
							{
								//Just found new level of minefactory group type, aka JUST UNLOCKED
								MineFactory prev = mine.getMine().getMineFactorySelected();
								Bukkit.getServer().getPluginManager().callEvent(new MineFactoryUpgradeEvent(player, mine, prev));
								mine.getMine().setMineFactory(fact.getID());
								mine.getMine().handleResetConditions(platform, RESET_TYPE.MANUAL, true);
								for(Entity entity : mine.getEntitiesInPMine())
								{
									if(entity instanceof Player)
									{
										Player p = (Player) entity;
										p.sendMessage("");
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8================ "));
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7This player mine's Mine Factory has just been Upgraded!"));
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &6&l> &e&lNew Composition"));
										for(String s : IconFactory.getMineFactoryCompositionLoreWithDifference(prev, fact))
										{
											p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7◎" + s));
										}
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &3&l> &b&LNew Buffs"));
										for(String s : IconFactory.getPerksLore(fact.getFactoryPerks(), "&7", "&e"))
										{
											p.sendMessage(s);
										}
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oView all Mine Factories in /pmine"));
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8================ "));
										p.sendMessage("");
										
										p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.1f);
									}
								}
							}
						}
					}
					ItemStack icon = (pmineFactory.canEquip() ? new ItemStack(mfg.getIconMaterial(), 1, mfg.getIconData()) : new ItemStack(Material.BARRIER,1));
					ItemMeta meta = icon.getItemMeta();
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&"+ (pmineFactory.canEquip() ? "a":"c") +"&l"+ pmineFactory.getFactoryGroup().getGroupID() + " Mine Factory"));
					List<String> lore = new ArrayList<String>();
					lore.addAll(mfg.getIconLore());
					lore.add(ChatColor.translateAlternateColorCodes('&', " "));
					lore.add(ChatColor.translateAlternateColorCodes('&', " &8General Stats"));
					
					if(pmineFactory.canEquip())
					{
						lore.add(ChatColor.translateAlternateColorCodes('&', " &7Tier: &a" + pmineFactory.getLevel() + "/" + pmineFactory.getFactoryGroup().getMaxLevel()));
						lore.add(ChatColor.translateAlternateColorCodes('&', " &7Mined: &a" + pmineFactory.getBlocksMined()));		
					}
					else
					{
						lore.add(ChatColor.translateAlternateColorCodes('&', "&7 Max Tier: &a"+mfg.getMaxLevel()));	
					}
					
					
				
					lore.add(ChatColor.translateAlternateColorCodes('&', " "));
					lore.add(ChatColor.translateAlternateColorCodes('&', " &8Mine Composition"));
					
					
					lore.addAll(IconFactory.getMineFactoryCompositionLore(pmineFactory));
					
					lore.add(ChatColor.translateAlternateColorCodes('&', " "));
					if(pmineFactory.canEquip())
					{
						
						if(mine.getMine().getMineFactorySelected().equals(pmineFactory))
						{
							if(pmineFactory.getLevel() < mfg.getMaxLevel())
							{
								lore.add(ChatColor.translateAlternateColorCodes('&', "&aUpgradeable"));
								lore.add(ChatColor.translateAlternateColorCodes('&', "&aLeft-Click to see progress"));	
							}
							else
							{
								lore.add(ChatColor.translateAlternateColorCodes('&', "&eMaxed"));
								lore.add(ChatColor.translateAlternateColorCodes('&', "&7Cannot be upgraded further"));	
							}
						}
						else
						{
							lore.add(ChatColor.translateAlternateColorCodes('&', "&aLeft-Click to select"));
							lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shift-Left-Click to view all Levels"));
						}
						
					}
					else
					{
						lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shift-Left-Click to view all Levels"));
					}
					
					
					meta.setLore(lore);
					icon.setItemMeta(meta);
					
					final boolean fisEquipped = isEquipped;
					final boolean canEquip = pmineFactory.canEquip();
					final String id = pmineFactory.getID();
					final MineFactory fpmineFactory = pmineFactory;
					final int tier = pmineFactory.getLevel();
					
					gui.assignSlotButton(slot, 1, new Button(icon, (s,p,c) -> {
						if(fisEquipped)
						{
							if(mine.getMine().getMineFactorySelected().equals(fpmineFactory))
							{
								if(!factoryGUIPlayers.contains(player.getUniqueId()))
								{
									factoryGUIToAdd.add(player.getUniqueId());	
								}
								openMainGUI(PMINE_GUI.MINEFACTORY_UPGRADE, player, 1);		
							}
							
						}
						else
						{
							//Bukkit.broadcastMessage("rrrea");
							//Newly select factory
							if(mine.getMine().createOrGetMineFactory(id).equals(fpmineFactory)
									&& canEquip)
							{
								
									mine.getMine().setMineFactory(fpmineFactory.getID());
									mine.getMine().handleResetConditions(platform, RESET_TYPE.MANUAL, true);
									
									openMainGUI(PMINE_GUI.MINEFACTORIES, player, 1);
									
									for(Entity e : mine.getEntitiesInPMine())
									{
										if(e instanceof Player)
										{
											Player a = (Player) e;
											a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMine Factory \"" + fpmineFactory.getFactoryGroup().getGroupID() + "\" has been equipped."));
										}
									}
									player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
									
									
								
							}
						}
					}).setGlow(mine.getMine().getMineFactorySelected().equals(fpmineFactory)));
				}
				else
				{
					gui.assignSlotButton(slot, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)7)));
					nextPage = false;
				}
			}
			
			
			
			gui.assignSlotButton(44, 1, new Button(new ItemStack(Material.BARRIER, 1), (s,p,c) -> {
				openMinePanelGUI(p, mine);
			}).setName("&c&lGo Back")
					.setLore("&7Click to go back"));
			
			gui.open(platform, player);
			break;	
		}
		
		//PMine Schematic Confirmation Selection GUI
		case SCHEMATIC_CONFIRMATION:
		{
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			if(!mine.hasNextSchematicLoaded())
			{
				return;
			}
			
			GUI gui = new GUI(3, "Confirm Reschematic");
			gui.assignSlotButton(0, 27, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
			
			ItemStack icon = IconFactory.BuildSchematicConfigIcon(configHandler.schematics.get(mine.getCurrentSchematicName()), true, true);
			ItemMeta meta = icon.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add("");
			lore.add("§8§l<< §7Current Schematic §8§l>>");
			meta.setLore(lore);
			icon.setItemMeta(meta);
			gui.assignSlotButton(11, 1, new Button(icon));
			
			icon = IconFactory.BuildSchematicConfigIcon(configHandler.schematics.get(mine.getNextSchematic()), true, true);
			meta = icon.getItemMeta();
			lore = meta.getLore();
			lore.add("");
			lore.add("§8§l<< §a§lCLICK TO REPLACE §8§l>>");
			meta.setLore(lore);
			icon.setItemMeta(meta);
			gui.assignSlotButton(15, 1, new Button(icon));
			
			gui.assignSlotButton(12, 3, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
					.setName("&8&l>&7&l>&e&l>&6&l>"));
			
			for(int i = 8; i < 27; i += 9)
			{
				gui.assignSlotButton(i, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5)
						,(s,p,c) -> {
							boolean inQueue = false;
							for(Pair<PlayerMine, List<Chunk>> pair : chunksToLoad)
							{
								if(pair.getKey().getMineCenter().equals(mine.getMineCenter()))
								{
									inQueue = true;
									break;
								}
							}
							if(!inQueue)
							{
								Pair<PlayerMine, List<Chunk>> pair = new MutablePair<>(mine, mine.getMineSchemChunks());
								chunksToLoad.add(pair);
								p.sendMessage("§7");
								p.sendMessage("§a§lSchematic Replacement Commencing Soon");
								p.sendMessage("§7Mine may lag a little during the process...");
								p.sendMessage("§7");
							}
						})
						.setName("&a&lCONFIRM PROCESS")
						.setLore("&7This action cannot be undone")
						);
			}
			
			for(int i = 0; i < 26; i+= 9 )
			{
				gui.assignSlotButton(i, 1, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14)
						,(s,p,c) -> {
							openMainGUI(PMINE_GUI.SCHEMATIC_OVERVIEW, p, 1);
						})
						.setName("&c&lCANCEL PROCESS")
						.setLore("&7Cancel reupdating of schematic."));
			}
			
			gui.open(platform, player);
			break;
		}
		
		case TAX_SETTINGS:
		{
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			
			
			GUI gui = new GUI(1, "Settings");
			gui.assignSlotButton(0, 9, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15)).setName(""));
			gui.assignSlotButton(2, 5, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)7)).setName(""));
			
			gui.assignSlotButton(8, 1, new Button(new ItemStack(Material.BARRIER,1),(s,p,c)-> {
				openMainGUI(PMINE_GUI.SETTINGS, player, 0);
				player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.7f, 1.2f);
			}));
			
			SettingsManager settings = mine.getSettings();
			settings.refreshSettings();
			
			List<String> types = new ArrayList<String>();
			types.add("MoneyTax");
			types.add("TokenTax");
			types.add("EnergyTax");
			
			HashMap<String, Setting> defaultSettings = SettingsManager.getDefaultSettings();
			
			int slot = 3;
			for(String settingID : types)
			{
				if (settings.isValueType(settingID))
				{
					double val = settings.getNumericalValue(settingID);
					
					ItemStack item = new ItemStack(Material.STAINED_CLAY,1,(short) 9);
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&l" + defaultSettings.get(settingID).getName() ));
					List<String> lore = new ArrayList<String>();
					lore.addAll(defaultSettings.get(settingID).getLore());
					
					lore.add("");
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Value &f" + df1.format(val) + "%"));
					lore.add("");
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shift-/Left-Click to add &f+1/+0.1%&7." ));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shift-/Right-Click to add &f-1/-0.1%&7." ));
					
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					gui.assignSlotButton(slot++, 1, new Button((item),(s,p,c) -> {
						if(!(mine.getOwnerUUID().equals(player.getUniqueId())))
						{
							MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
							return;
						}
						
						double newVal = val;
						switch(c)
						{
						case LEFT:
							newVal += 0.1;
							break;
						case SHIFT_LEFT:
							newVal += 1;
							break;
						case RIGHT:
							newVal -= 0.1;
							break;
						case SHIFT_RIGHT:
							newVal -= 1;
							break;							
						}
						
						settings.updateValue(settingID, newVal);
						player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.7f, 1.2f);
						openMainGUI(PMINE_GUI.TAX_SETTINGS,player, 0);
					}));	
				}
				
				if( (slot + 1) % 9 == 0 ) slot += 2;
			}
			gui.open(platform, player);
			saveGrid(grid);
			
		}
			break;
		
		//PMine Settings GUI
		case SETTINGS:
		{
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			
			
			GUI gui = new GUI(6, "Settings");
			gui.assignSlotButton(0, 54, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15)).setName(""));
			
			SettingsManager settings = mine.getSettings();
			settings.refreshSettings();
			
			boolean s_public = settings.getBooleanValue("Public");
			if(player.hasPermission("pmine.makepublic"))
			{
				mine.setPermanentPublic(true);
			}
			
			gui.assignSlotButton(13, 1, new Button(new ItemStack(Material.STAINED_CLAY, 1, (short) (s_public ? 5 : 6)), (s,p,c) -> {
				if(!(mine.getOwnerUUID().equals(player.getUniqueId())))
				{
					MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
					return;
				}
				
				
				if(mine.getPermanentPublic() || mine.getTracker("MinedWeekly") >= PlayerMine.REQUIRED_WEEKLYBLOCKS_FOR_PUBLIC)
				{
					
					settings.updateBooleanValue("Public", !s_public);
					player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.7f, 1.2f);
					openMainGUI(PMINE_GUI.SETTINGS,player, 0);		
				}
				else if (mine.getTracker("MinedWeekly") < PlayerMine.REQUIRED_WEEKLYBLOCKS_FOR_PUBLIC)
				{
					player.sendMessage(MessageHandler.DEFAULT_PREFIX 
							+ ChatColor.translateAlternateColorCodes('&', "You cannot enable Public Mine yet! Your PMine has to mine another &f&n" + toCommaFormat(PlayerMine.REQUIRED_WEEKLYBLOCKS_FOR_PUBLIC - mine.getTracker("MinedWeekly")) + " Blocks&7 this week to enable this.") );
					
				}
				
				
			})
					.setName((s_public ? "&a&lOpened for Public" : "&c&lClosed for Public"))
					.setLore("&7Player Mine is currently " + (s_public ? "opened" : "closed"),
							"&7for public",
							" ",
							"&8&l <> &7Click to " + (s_public ? "&c&LCLOSE" : "&a&LOPEN") + " &7Player Mine to public."
							));
			
			gui.assignSlotButton(28, 7, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 7)).setName(""));
			gui.assignSlotButton(37, 7, new Button(new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 7)).setName(""));
			
			double mTax, tTax, eTax;
			mTax = settings.getNumericalValue("MoneyTax");
			tTax = settings.getNumericalValue("TokenTax");
			eTax = settings.getNumericalValue("EnergyTax");
			
			gui.assignSlotButton(28, 1, new Button(new ItemStack(Material.STAINED_CLAY,1,(short) 9),(s,p,c) -> {
				openMainGUI(PMINE_GUI.TAX_SETTINGS, player, 0);
			})
					.setName("&b&lPublic Tax Options")
					.setLore("&7Tax players that mine at your PMine.",
							"&7Define the rules here!",
							"&7",
							"&3&l * &7Money Tax &8&l<> &f" + df1.format(mTax) + "%",
							"&3&l * &7Token Tax &8&l<> &f" + df1.format(tTax) + "%",
							"&3&l * &7Energy Tax &8&l<> &f" + df1.format(eTax) + "%",
							"&7",
							"&7(( Click to Edit Public Taxes ))"));
			
			HashMap<String, Setting> defaultSettings = SettingsManager.getDefaultSettings();
			//Remove specials
			defaultSettings.remove("Public");
			defaultSettings.remove("MoneyTax");
			defaultSettings.remove("TokenTax");
			defaultSettings.remove("EnergyTax");
			
			int slot = 29;
			for(String settingID : defaultSettings.keySet())
			{
				if(settings.isBooleanType(settingID))
				{
					boolean enabled = settings.getBooleanValue(settingID);
					
					ItemStack item = new ItemStack(Material.STAINED_CLAY,1,(short) 9);
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&l" + defaultSettings.get(settingID).getName() ));
					List<String> lore = new ArrayList<String>();
					lore.addAll(defaultSettings.get(settingID).getLore());
					
					lore.add("");
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Status " + (enabled ? "&A&LENABLED": "&c&lDISABLED")));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Left-Click to toggle " + (enabled ? "&c&lOFF" : "&a&LON") + "&7." ));
					
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					gui.assignSlotButton(slot++, 1, new Button((item),(s,p,c) -> 
					{
						if(!(mine.getOwnerUUID().equals(player.getUniqueId())))
						{
							MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
							return;
						}
						settings.updateBooleanValue(settingID, !enabled);
						player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.7f, 1.2f);
						openMainGUI(PMINE_GUI.SETTINGS,player, 0);
					}));	
				}
				else if (settings.isValueType(settingID))
				{
					double val = settings.getNumericalValue(settingID);
					
					ItemStack item = new ItemStack(Material.STAINED_CLAY,1,(short) 9);
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&l" + defaultSettings.get(settingID).getName() ));
					List<String> lore = new ArrayList<String>();
					lore.addAll(defaultSettings.get(settingID).getLore());
					
					lore.add("");
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Value &f" + df1.format(val) + "%"));
					lore.add("");
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shift-/Left-Click to add &f+1/+0.1%&7." ));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shift-/Right-Click to add &f-1/-0.1%&7." ));
					
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					gui.assignSlotButton(slot++, 1, new Button((item),(s,p,c) -> {
						if(!(mine.getOwnerUUID().equals(player.getUniqueId())))
						{
							MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
							return;
						}
						
						double newVal = val;
						switch(c)
						{
						case LEFT:
							newVal += 0.1;
							break;
						case SHIFT_LEFT:
							newVal += 1;
							break;
						case RIGHT:
							newVal -= 0.1;
							break;
						case SHIFT_RIGHT:
							newVal -= 1;
							break;							
						}
						
						settings.updateValue(settingID, newVal);
						player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.7f, 1.2f);
						openMainGUI(PMINE_GUI.SETTINGS,player, 0);
					}));	
				}
				
				if( (slot + 1) % 9 == 0 ) slot += 2;
			}
			saveGrid(grid);
			
			gui.open(platform, player);
		}
			break;
		
		//PMine Settings Tax GUI
		
		//PMine Schematic Individual Categories GUI
		case SCHEMATIC_NORMAL:
		case SCHEMATIC_DONOR:
		case SCHEMATIC_SPECIAL:
		case SCHEMATIC_ALL:
		{
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			
			int schems_default_indexFound = -1, schems_donor_indexFound = -1,
					schems_special_indexFound = -1;
			List<SchematicConfig> schems_default = new ArrayList<SchematicConfig>();
			List<SchematicConfig> schems_donor = new ArrayList<SchematicConfig>();
			List<SchematicConfig> schems_special = new ArrayList<SchematicConfig>();
			List<SchematicConfig> schems = new ArrayList<SchematicConfig>();
			int schemEquippedID = -1;
			HashMap<String, SchematicConfig> schemConfigs = configHandler.schematics;
			
			
			for(SchematicConfig schemConfig : schemConfigs.values())
			{
				switch(schemConfig.getSchematicGroupType())
				{
				case DEFAULT:
					schems_default.add(schemConfig);
					if(schemConfig.getRawID().equalsIgnoreCase(mine.getCurrentSchematicName()))
					{
						schems_default_indexFound = schems_default.size()-1;
					}
					break;
				case DONOR:
					schems_donor.add(schemConfig);
					if(schemConfig.getRawID().equalsIgnoreCase(mine.getCurrentSchematicName()))
					{
						schems_donor_indexFound = schems_donor.size()-1;
					}
					break;
				case SPECIAL:
					schems_special.add(schemConfig);
					if(schemConfig.getRawID().equalsIgnoreCase(mine.getCurrentSchematicName()))
					{
						schems_special_indexFound = schems_special.size()-1;
					}
					break;
				}
			}
			
			
			int schemRows = 0;
			if(guiType == PMINE_GUI.SCHEMATIC_NORMAL)
			{
				schemEquippedID = schems_default_indexFound;
				schems.addAll(schems_default);
			}
			else if (guiType == PMINE_GUI.SCHEMATIC_DONOR)
			{
				schemEquippedID = schems_donor_indexFound;
				schems.addAll(schems_donor);
			}
			else if (guiType == PMINE_GUI.SCHEMATIC_SPECIAL)
			{
				schemEquippedID = schems_special_indexFound;
				schems.addAll(schems_special);
			}
			else if (guiType == PMINE_GUI.SCHEMATIC_ALL)
			{
				schems.addAll(schems_default);
				schems.addAll(schems_donor);
				schems.addAll(schems_special);
				
				if(schems_default_indexFound != -1)
				{
					schemEquippedID = schems_default_indexFound;
				}
				else if (schems_donor_indexFound != -1)
				{
					schemEquippedID = schems_default.size() + schems_donor_indexFound;
				}
				else if (schems_special_indexFound != -1)
				{
					schemEquippedID = schems_default.size() + schems_donor.size() + schems_special_indexFound;
				}
			}
			schemRows = (int) Math.ceil(schems.size() / 9.0);
			if(schemRows > 4)
				schemRows = 4;
			
			GUI gui = new GUI(schemRows + 2, "Schematics");
			gui.assignSlotButton(0, 54, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
			
			int slot = 9;
			for(int i = 0; i < (schems.size() > 28 ? 28 : schems.size()); ++i)
			{
				if( (++slot) % 9 == 0 ) slot += 2; 
				final String schemID = schems.get(i).getRawID();
				gui.assignSlotButton(slot, 1, new Button(
						IconFactory.BuildSchematicConfigIcon(schems.get(i), (schemEquippedID == i), mine.hasSchematicUnlocked(schems.get(i).getRawID()))
						, (s,p,c) -> {
							if(mine.hasSchematicUnlocked(schemID))
							{
								if(mine.isReadyToChangeSchematic() || p.hasPermission("pmines.admin.schemcooldownbypass"))
								{
									mine.setNextSchematic(schemID);
									openMainGUI(PMINE_GUI.SCHEMATIC_CONFIRMATION, p, 1);	
								}
								else
								{
									p.sendMessage(MessageHandler.DEFAULT_PREFIX + "Can't reselect schematic for another §f"+formatTime(mine.getTimeTillSchemChangeReady()));
								}	
							}
							else
							{
								p.sendMessage(MessageHandler.DEFAULT_PREFIX + "This mine schematic hasn't been unlocked just yet.");
							}
						}).setGlow((schemEquippedID == i)));
			}
			
			gui.assignSlotButton((schemRows+2) * 9 - 1, 1, new Button(new ItemStack(Material.BARRIER, 1), (s,p,c) -> {
				openMainGUI(PMINE_GUI.SCHEMATIC_OVERVIEW, p, 1);
			}).setName("&c&lGo Back")
					.setLore("&7Click to go back"));
			
			//Schematic Timer ICON
			int remainingTime = mine.getTimeTillSchemChangeReady();
			ItemStack timer = new ItemStack(Material.WATCH, 1);
			ItemMeta meta = timer.getItemMeta();
			meta.setDisplayName("§a§lSchematic Timer");
			List<String> lore = new ArrayList<String>();
			lore.add("§7You have a cooldown of §f"+formatTime(mine.getSchematicCDDuration()));
			lore.add("§7to load and switch schematics");
			lore.add("");
			if(remainingTime == 0)
			{
				lore.add("§7 (( §a§lSCHEMATIC SWITCHABLE §7))");
				lore.add("§7Select any unlocked schematic to");
				lore.add("§7override present schematic");
			}
			else
			{
				lore.add("§7 (( §c§lON COOLDOWN §7))");
				lore.add("§7Come back later to change schematic");
			}
			meta.setLore(lore);
			timer.setItemMeta(meta);
			
			gui.assignSlotButton((schemRows+2) * 9 - 5, 1, new Button(timer));
			
			gui.open(platform, player);
			
			break;
		}
			
			
		//PMine Schematics Overview GUI
		case SCHEMATIC_OVERVIEW:
		{
			Grid grid = getGrid();
			PlayerMine mine = grid.getMineAtLocation(player.getLocation());
			if(mine == null)
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
				return;
			}
			if(!(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId())))
			{
				MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
				return;
			}
			
			
			GUI gui = new GUI(4, "Schematics");
			gui.assignSlotButton(0, 36, new Button(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
			
			ItemStack icon;
			int showMax;
			ItemMeta meta;
			int schems_default_indexFound = -1, schems_donor_indexFound = -1,
					schems_special_indexFound = -1;
			List<String> lore = new ArrayList<String>();
			List<String> schems_default = new ArrayList<String>();
			List<String> schems_donor = new ArrayList<String>();
			List<String> schems_special = new ArrayList<String>();
			HashMap<String, SchematicConfig> schemConfigs = configHandler.schematics;
			
			lore.clear();
			
			for(SchematicConfig schemConfig : schemConfigs.values())
			{
				if(player.hasPermission("pmine.schematicauto"))
				{
					if(mine.getOwnerUUID().equals(player.getUniqueId()))
					{
						if(player.hasPermission("pmine.schematics."+schemConfig.getRawID()))
						{
							if(!mine.hasSchematicUnlocked(schemConfig.getRawID()))
							{
								mine.addSchematic(schemConfig.getRawID());
							}
						}
					}	
				}
				switch(schemConfig.getSchematicGroupType())
				{
				case DEFAULT:
					schems_default.add(schemConfig.getRawID());
					if(schemConfig.getRawID().equalsIgnoreCase(mine.getCurrentSchematicName()))
					{
						schems_default_indexFound = schems_default.size()-1;
					}
					break;
				case DONOR:
					schems_donor.add(schemConfig.getRawID());
					if(schemConfig.getRawID().equalsIgnoreCase(mine.getCurrentSchematicName()))
					{
						schems_donor_indexFound = schems_donor.size()-1;
					}
					break;
				case SPECIAL:
					schems_special.add(schemConfig.getRawID());
					if(schemConfig.getRawID().equalsIgnoreCase(mine.getCurrentSchematicName()))
					{
						schems_special_indexFound = schems_special.size()-1;
					}
					break;
				}
			}
			
			//Default Schem Icon
			icon = new ItemStack(Material.GRASS,1);
			meta = icon.getItemMeta();
			meta.setDisplayName("§a§lRegular Mine Schematics");
			lore.add("§7Select from standard mine schematics here");
			lore.add("");
			lore.add("§8§l * §2§lListed Here§8:");
			showMax = 5;
			if(schems_default.size() < 5) showMax = schems_default.size();
			for(int i = 0; i < showMax; i++)
			{
				if(mine.getCurrentSchematicName().equals( schems_default.get(i) ))
					lore.add("§8§l - §a§n" + schems_default.get(i) + " Schematic§7 (Active)");
				else if (mine.hasSchematicUnlocked(schems_default.get(i)))
					lore.add("§8§l - §a" + schems_default.get(i));
				else
					lore.add("§8§l - §7" + schems_default.get(i));
			}
			if(showMax == 5 && schems_default.size() > 5)
				lore.add("§8§l - §7... §8(§7" + (schems_default.size() - showMax) + " More§8)");
			lore.add("");
			lore.add("§8§l<< §eClick to access §8§l>>");
			meta.setLore(lore);
			icon.setItemMeta(meta);
			gui.assignSlotButton(10, 1, new Button(icon, (s,p,c) -> {
				openMainGUI(PMINE_GUI.SCHEMATIC_NORMAL, p, 1);
			}));
			
			//Donor Schem Icon
			icon = new ItemStack(Material.GOLD_BLOCK,1);
			meta = icon.getItemMeta();
			meta.setDisplayName("§e§lDonor Mine Schematics");
			lore.clear();
			lore.add("§7Donor Rank schematcs can be found here");
			lore.add("");
			lore.add("§8§l * §6§lListed Here§8:");
			showMax = 5;
			if(schems_donor.size() < 5) showMax = schems_donor.size();
			for(int i = 0; i < showMax; i++)
			{
				if(mine.getCurrentSchematicName().equals( schems_donor.get(i) ))
					lore.add("§8§l - §a§n" + schems_donor.get(i) + " Schematic§7 (Active)");
				else if (mine.hasSchematicUnlocked(schems_donor.get(i)))
					lore.add("§8§l - §a" + schems_donor.get(i));
				else
					lore.add("§8§l - §7" + schems_donor.get(i));
			}
			if(showMax == 5 && schems_donor.size() > 5)
				lore.add("§8§l - §7... §8(§7" + (schems_donor.size() - showMax) + " More§8)");
			lore.add("");
			lore.add("§8§l<< §bClick to access §8§l>>");
			meta.setLore(lore);
			icon.setItemMeta(meta);
			gui.assignSlotButton(13, 1, new Button(icon, (s,p,c) -> {
				openMainGUI(PMINE_GUI.SCHEMATIC_DONOR, p, 1);
			}));
			
			//Special Schem Icon
			icon = new ItemStack(Material.OBSIDIAN,1);
			meta = icon.getItemMeta();
			meta.setDisplayName("§c§l??? Mine Schematics");
			lore.clear();
			lore.add("§7Special schematic mines are found here");
			lore.add("");
			lore.add("§8§l * §4§lListed Here§8:");
			showMax = 5;
			if(schems_donor.size() < 5) showMax = schems_donor.size();
			for(int i = 0; i < showMax; i++)
			{
				if(mine.getCurrentSchematicName().equals( schems_donor.get(i) ))
					lore.add("§8§l - §a§n" + schems_donor.get(i) + " Schematic§7 (Active)");
				else if (mine.hasSchematicUnlocked(schems_donor.get(i)))
					lore.add("§8§l - §a" + schems_donor.get(i));
				else
					lore.add("§8§l - §7" + schems_donor.get(i));
			}
			if(showMax == 5 && schems_donor.size() > 5)
				lore.add("§8§l - §7... §8(§7" + (schems_donor.size() - showMax) + " More§8)");
			lore.add("");
			lore.add("§8§l<< §7Click to access §8§l>>");
			meta.setLore(lore);
			icon.setItemMeta(meta);
			gui.assignSlotButton(16, 1, new Button(icon, (s,p,c) -> {
				openMainGUI(PMINE_GUI.SCHEMATIC_SPECIAL, p, 1);
			}));
			
			
			//View all schems icon
			gui.assignSlotButton(31, 1, new Button(new ItemStack(Material.EMPTY_MAP,1), (s,p,c) -> {
				openMainGUI(PMINE_GUI.SCHEMATIC_ALL, p, 1);
			} )
					.setName("&b&lView All Schematics")
					.setLore("&7Skip category view and choose schematics",
							"&7directly from the collage of schematics here",
							"",
							"&8&l<< &dClick to access &8&l>>"));
			
			gui.assignSlotButton(35, 1, new Button(new ItemStack(Material.BARRIER, 1), (s,p,c) -> {
				openMinePanelGUI(p, mine);
			}).setName("&c&lGo Back")
					.setLore("&7Click to go back"));
			
			gui.open(platform, player);
			
			break;
		}
		
		}
	}
	
	public static boolean isAutominerEnabled(Player player)
	{
		if(Variables.getPlayer(player, "Autominer") == null)
		{
			Variables.setPlayer(player, "Autominer", "Off;0;0");
		}
		
		String var = (String) Variables.getPlayer(player, "Autominer");
		String[] split = var.split(";", 3);
		if(split[0].equalsIgnoreCase("On"))
			return true;
		else
			return false;
	}
	
	public static Float getAdvancedAutominerTimeleft(Player player)
	{
		if(Variables.getPlayer(player, "Autominer") == null)
		{
			Variables.setPlayer(player, "Autominer", "Off;0;0");
		}
		
		String var = (String) Variables.getPlayer(player, "Autominer");
		String[] split = var.split(";", 3);
		Float timeLeft = Float.parseFloat(split[1]);
		return timeLeft;
	}
	
	public static void setAutominerActive(Player player, boolean active)
	{
		if(Variables.getPlayer(player, "Autominer") == null)
		{
			Variables.setPlayer(player, "Autominer", "Off;0;0");
		}
		
		String var = (String) Variables.getPlayer(player, "Autominer");
		String[] split = var.split(";", 3);
		if(!active)
		{
			MessageHandler.playMessage(player, MESSAGE_TYPE.TOGGLE_AUTOMINER_OFF, true);
			split[0] = "Off";
		}
		else
		{
			MessageHandler.playMessage(player, MESSAGE_TYPE.TOGGLE_AUTOMINER_ON, true);
			split[0] = "On";
		}
		Variables.setPlayer(player, "Autominer", split[0]+";"+split[1]+";"+split[2]);
	}
	
	public static void toggleAutominer(Player player)
	{
		if(Variables.getPlayer(player, "Autominer") == null)
		{
			Variables.setPlayer(player, "Autominer", "Off;0;0");
		}
		
		String var = (String) Variables.getPlayer(player, "Autominer");
		String[] split = var.split(";", 3);
		if(split[0].equalsIgnoreCase("On"))
		{
			MessageHandler.playMessage(player, MESSAGE_TYPE.TOGGLE_AUTOMINER_OFF, true);
			split[0] = "Off";
			
		}
		else
		{
			MessageHandler.playMessage(player, MESSAGE_TYPE.TOGGLE_AUTOMINER_ON, true);
			split[0] = "On";
			amSessionBlocksMined.put(player, 0);
		}
		Variables.setPlayer(player, "Autominer", split[0]+";"+split[1]+";"+split[2]);
	}
	
	//PMine Module Constructor
	public Mines(Platform platform) {
		super(platform);
		System.out.println("HashCode: " + Grid.class.hashCode());
		
		configHandler = new ConfigHandler(getConfig());
		econ = platform.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		
		//PAPI Hook Implementation
		platform.getPAPIHandler().registerHook(this, (player, id) -> {
//			if(id.startsWith("blocksmined")) {
//				Object mined = Variables.getPlayer(player, "BlocksMined");
//				if(mined == null) mined = 1;
//				switch(id) {
//				case "blocksmined_chat": return String.valueOf((int) mined);
//				}
//			}
			return null;
		});
		
		//Public Mine Page Refresh
		startRepeatingTask(() -> {
			refreshPublicMines();
		},35,35,false);
		
		//PMine Factory Upgrade Repeating Task
		startRepeatingTask(() -> {
			
			List<UUID> toRemove = new ArrayList<UUID>();
			factoryGUIPlayers.addAll(factoryGUIToAdd);
			factoryGUIToAdd.clear();
			for (UUID uuid : factoryGUIPlayers) {
			    Player player = Bukkit.getServer().getPlayer(uuid);
				if(player != null) {
					if(player.getOpenInventory().getTitle().contains("Upgrade Factory")) {
						openMainGUI(PMINE_GUI.MINEFACTORY_UPGRADE,player,1);
					}
					else
					{
						toRemove.add(uuid);
					}
				}
				else
				{
					toRemove.add(uuid);
				}
			}
			factoryGUIPlayers.removeAll(toRemove);
			
		},20,20,false);
		
		//PMine Autominer GUI Update Repeating Task
		startRepeatingTask(() -> {
			Grid grid = getGrid();
			for(Entry<Player, GUI> minePanelGUIs : this.minePanelActive.entrySet())
			{
				PlayerMine mine = grid.getMineAtLocation(minePanelGUIs.getKey().getLocation());
				if(mine != null)
				{
					assignAMSlot(minePanelGUIs.getKey(), mine, minePanelGUIs.getValue());	
				}
			}
				
		}, 5,5,false);
		
		//Lazy loading Class?
		List<Schematic> schematic = new ArrayList<Schematic>();
		schematic.add(new Schematic());
		schematic.get(0).getSchemName();
		
		List<LoadedSchematic> c = new ArrayList<LoadedSchematic>();
		c.add(new LoadedSchematic());
		c.get(0).getSize();
		
		List<SchematicBlock> b = new ArrayList<SchematicBlock>();
		b.add(new SchematicBlock(0,0,0,0,0));
		b.get(0).getXCenterOffset();
		
		List<Grid> grids = new ArrayList<Grid>();
		Grid gridomg = new Grid(Bukkit.getWorlds().get(0));
		grids.add(gridomg);
		grids.get(0).getMineRange();
		
		
		//Non-Important Grid Saves (Blocks Mined, Mine Reset etc)
		startRepeatingTask(() -> {
			Grid mineGrid = getGrid();
			saveGrid(mineGrid);
		},120,120, false);
		
		
		//Autominer Repeating Task
		startRepeatingTask(() -> {
			Collection<? extends Player> players = Bukkit.getOnlinePlayers();
			for(Player player : players)
			{
				if(player != null)
				{
					if(Variables.getPlayer(player, "Autominer") == null)
					{
						Variables.setPlayer(player, "Autominer", "Off;0;0");
					}
					String var = (String) Variables.getPlayer(player, "Autominer");
					String[] split = var.split(";", 3);
					if(split[0].equalsIgnoreCase("On"))
					{
						
						ItemStack item = player.getItemInHand();
						
						boolean disable = false;

						Grid grid = getGrid();
						PlayerMine mine = grid.getMineAtLocation(player.getLocation());
						
						if(mine == null)
						{
							disable = true;
						}
						else if(!mine.getOwnerUUID().equals(player.getUniqueId()))
						{
							disable = true;
						}
						else if(item.getType() != Material.DIAMOND_PICKAXE)
						{
							disable = true;
						}
						if(isAutominerEnabled(player) && disable)
						{
							setAutominerActive(player, false);
							return;
						}
						
						Float timeLeft = Float.parseFloat(split[1]);
						boolean processMining = false;
						if(timeLeft > 0)
						{
							processMining = true;
							timeLeft -= amInterval;
							if(timeLeft < 0) timeLeft = 0f;
							
							split[1] = timeLeft.toString();
							//Bukkit.broadcastMessage(split[1] + " ??? " + timeLeft);
						}else
						{
							int it = Integer.parseInt(split[2]);
							if(++it >= 3)
							{
								it = 0;
								processMining = true;
							}
							split[2] = String.valueOf(it);
						}
						
						
						//Update Variable
						Variables.setPlayer(player, "Autominer", split[0]+";"+split[1]+";"+split[2]);
						
						if(processMining)
						{
							{
								if(mine.isPartOfMine(player.getUniqueId()))
								{
									Location min,max;
									min = mine.getMine().getMinimumPoint();
									max = mine.getMine().getMaximumPoint();
									Block target = null;
									for(int i = 0; i < 5; ++i)
									{
										target = mine.getWorld().getBlockAt(
												ran.nextInt(min.getBlockX(), max.getBlockX() + 1),
												ran.nextInt(min.getBlockY(), max.getBlockY() + 1),
												ran.nextInt(min.getBlockZ(), max.getBlockZ() + 1));
										if(target.getType() == Material.AIR)
											target = null;
										if(target != null) break;
									}
									if(target != null)
									{
										me.despawningbone.module.Events.PrisonsBlockBreakEvent e = new me.despawningbone.module.Events.PrisonsBlockBreakEvent(target, player, true);
										Bukkit.getServer().getPluginManager().callEvent(e);	
									}
									
									
								}
								else
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_IN_TEAM, false);
									setAutominerActive(player, false);	
								}
							}
								
									
						}
					}
				}
				
				
			}
		},7,7,false);
		
		//PMine Chunk Queue Speed Controller
		startRepeatingTask(() -> {
			double tps = MinecraftServer.getServer().recentTps[0];
			CHUNK_LOADCOUNT = 7;
			if(tps < 19) CHUNK_LOADCOUNT--;
			if(tps < 17) CHUNK_LOADCOUNT--;
			if(tps < 15) CHUNK_LOADCOUNT--;
			if(tps < 10) CHUNK_LOADCOUNT--;
			if(tps < 7) CHUNK_LOADCOUNT--;
			if(tps < 2) CHUNK_LOADCOUNT--;
			for(int i = CHUNK_LOADCOUNT; i > 0; i--)
			chunkQueue();
		},1,1,false);
		
		/*
		startRepeatingTask(() -> {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "modules reload mines"); 
		},5000,5000,false);
		*/
		
		
		//AM Alias temporary fix whilst desp finds solution for command aliases
		registerCommand("autominer", new BukkitCommand("autominer", "Official command for Autominer", "[subcmd]",
				Arrays.asList()) {
			@Override
			public boolean execute(CommandSender sender, String label, String[] args) {
				if(sender instanceof Player)
				{
					((Player) sender).performCommand("am");
				}
				return true;
			}
			
		});
		
		//PMine Autominer Command
		registerCommand("am", new BukkitCommand("am", "Official command for Autominer", "[subcmd]",
				Arrays.asList()) {
			@Override
			public boolean execute(CommandSender sender, String label, String[] args) {
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
				
				if(args.length == 0)
				{
					Grid grid = getGrid();
					final PlayerMine mine = grid.getMineAtLocation(player.getLocation());
					if(mine == null)
					{
						MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
						return true;
					}
					if(!mine.getOwnerUUID().equals(player.getUniqueId()))
					{
						MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
						return true;
					}
					
					openMinePanelGUI(player, mine);
				}
				else if (args[0].equalsIgnoreCase("give"))
				{
					Player target = Bukkit.getPlayer(args[1]);
					int time = Integer.parseInt(args[2]);
					int quantity = 1;
					
					if(args.length == 4)
					{
						quantity = Integer.parseInt(args[3]);
						if(quantity > 64) quantity = 64;
					}
					
					ItemStack amShard = new ItemStack(Material.PRISMARINE_SHARD, quantity);
					ItemMeta meta = amShard.getItemMeta();
					List<String> lore = new ArrayList<String>();
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lAdvanced &3&lAM &f&lShard &8(&7Right-Click&8)"));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Redeem this Time Shard to receive Advanced AutoMiner time."));
					lore.add(ChatColor.translateAlternateColorCodes('&', " "));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8&l * &7Time: &e"+time+" Minutes"));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8&l * &7Join an autominer by running &6/am"));
					meta.setLore(lore);
					amShard.setItemMeta(meta);
					
					if(target.getInventory().firstEmpty() != -1)
					{
						target.getInventory().addItem(amShard);
					}
					
				}
				
				return true;
			}

		
		/*registerCommand("am", new BukkitCommand("am", "Official command for Autominer", "[subcmd]",
				Arrays.asList("autominer")) {
			@Override
			public boolean execute(CommandSender sender, String label, String[] args) {
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
				
				if(args.length == 0)
				{
					Grid grid = getGrid();
					final PlayerMine mine = grid.getMineAtLocation(player.getLocation());
					if(mine == null)
					{
						MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
						return true;
					}
					if(!mine.getOwnerUUID().equals(player.getUniqueId()))
					{
						MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
						return true;
					}
					
					openMinePanelGUI(player, mine);
				}
				else if (args[0].equalsIgnoreCase("give"))
				{
					Player target = Bukkit.getPlayer(args[1]);
					int time = Integer.parseInt(args[2]);
					int quantity = 1;
					
					if(args.length == 4)
					{
						quantity = Integer.parseInt(args[3]);
						if(quantity > 64) quantity = 64;
					}
					
					ItemStack amShard = new ItemStack(Material.PRISMARINE_SHARD, quantity);
					ItemMeta meta = amShard.getItemMeta();
					List<String> lore = new ArrayList<String>();
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lAdvanced &3&lAM &f&lShard &8(&7Right-Click&8)"));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&7Redeem this Time Shard to receive Advanced AutoMiner time."));
					lore.add(ChatColor.translateAlternateColorCodes('&', " "));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8&l * &7Time: &e"+time+" Minutes"));
					lore.add(ChatColor.translateAlternateColorCodes('&', "&8&l * &7Join an autominer by running &6/am"));
					meta.setLore(lore);
					amShard.setItemMeta(meta);
					
					if(target == null)
					{
						OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[1]);
						core().giveItem(offlineTarget, amShard);
					}else
					{
						core().giveItem(target, amShard);
					}
					
				}
				
				return true;
			}*/
		});
		
		//PMine pmine Command
		registerCommand("pmine", new BukkitCommand("pmine", "Official command for Player Mines", "[subcmd]",
				Arrays.asList("playermine", "mine")) {
			
			/*
			 * Commands:
			 * /mine - Opens up PMine Main GUI
			 * /mine sethome - Sets the Spawn Location of the Mine (has to be in pmine & owner)
			 * /mine trust/remove - Opens the PMine Members GUI
			 * /mine trust/add <player> - Adds a player to decorate and mine in the mine
			 * /mine remove <player> - Removes a player from the mine
			 * /mine togglepublic - Toggles mine public settings. Requires Paid PMine to make public
			 * /mine settings - Opens up PMine Settings GUI
			 * /mine am/autominer - Opens up PMine Autominer GUI
			 * /mine visit <player> - Visit a player's PMine if its Public 
			 * /mine resetmine - Resets the mine that you are in
			 * /mine 
			 * */
			
			@Override
			public boolean execute(CommandSender sender, String label, String[] args) {
				
				boolean admin = false;
				if(sender instanceof ConsoleCommandSender) admin  = true;
				Player player = null;
				
				if (sender instanceof Player) {
					player = (Player) sender;
					if(player.hasPermission("pmine.admin.usecommands"))
						admin = true;
				}
				
				//PMINE ADMIN COMMANDS
				if(admin && args.length > 0)
				{
					sender.sendMessage("Admin");
					if(args[0].equalsIgnoreCase("givefactory"))
					{
						sender.sendMessage("Giving factory");
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						String toAdd = args[2];
						if(configHandler.mineFactories.containsKey(toAdd))
						{
							List<PlayerMine> mines = getGrid().getPlayersMines(target);
							for(PlayerMine mine : mines)
							{
								mine.getMine().createOrGetMineFactory(toAdd).forceUnlock();
								mine.getMine().setMineFactory(toAdd);
								sender.sendMessage("Set MineFactory to " + toAdd);
								
							}
							Player online = Bukkit.getPlayer(args[1]);
							if(online != null)
							{
								online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou've received a new Mine Factory! /pmine"));
							}
						}else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCouldn't give " + target.getName() + " mine factory named: " + args[2] + " as it does not exist."));
						}
					}
				
					
					if(args[0].equalsIgnoreCase("giveschematic"))
					{
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						if(Variables.getPlayer(target, "PMineSchematics") == null)
							Variables.setPlayer(target, "PMineSchematics", new HashSet<String>());
						Set<String> schematics = (Set<String>) Variables.getPlayer(target, "PMineSchematics");
						String toAdd = args[2];
						
						if(configHandler.schematics.containsKey(toAdd))
						{
							if(schematics.add(toAdd))
							{
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aGiven " + target.getName() + " schematic named: " + args[2]));
								Player online = Bukkit.getPlayer(args[1]);
								if(online != null) // online
								{
									online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUnlocked the \"" + args[2] + "\" schematic for all your pmines! /pmine"));
								}	
								Variables.setPlayer(target, "PMineSchematics", schematics);
								List<PlayerMine> mines = getGrid().getPlayersMines(target);
								for(PlayerMine mine : mines)
								{
									mine.addSchematic(toAdd);
									for(Entity e : mine.getEntitiesInPMine())
									{
										if(e instanceof Player)
										{
											Player p = (Player) e;
											if(mine.isPartOfMine(p.getUniqueId()))
											{
												p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.8f);
												p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aNew schematic can be chosen! /pmine"));
											}
										}
									}
								}
							}
							
							
							

						}else
						{
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCouldn't give " + target.getName() + " schematic named: " + args[2] + " as it does not exist."));
						}
						
					}
				}
					
				if(player != null)
				{
					{
						List<SimpleLocation> mines = (List<SimpleLocation>) Variables.getPlayer(player, "OwnedPMines");
						List<SimpleLocation> toRemove = new ArrayList<SimpleLocation>();
						if(mines != null)
						{
							for(SimpleLocation loc : mines)
							{
								if(getGrid().getMineAtLocation(loc) == null)
								{
									Bukkit.getLogger().log(Level.SEVERE, "[PMINES] " + player.getName() + "'s PMine at " + loc.getSerializedString() + " in personal variable is NULL. Removing from list of mines owned... THIS SHOULD BE UNINTENTIONAL.");
									Bukkit.getLogger().log(Level.SEVERE, "[PMINES] " + player.getName() + "'s PMine at " + loc.getSerializedString() + " in personal variable is NULL. Removing from list of mines owned... THIS SHOULD BE UNINTENTIONAL.");
									Bukkit.getLogger().log(Level.SEVERE, "[PMINES] " + player.getName() + "'s PMine at " + loc.getSerializedString() + " in personal variable is NULL. Removing from list of mines owned... THIS SHOULD BE UNINTENTIONAL.");
									toRemove.add(loc);
								}
							}
							mines.removeAll(toRemove);
							if(toRemove.size() > 0)
							{
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went severely wrong, if you have missing mines, please contact coolfire02 immediately."));
							}
							Variables.setPlayer(player, "OwnedPMines", mines);		
						}
					}
					
					

					if(args.length == 0) {						
						Grid grid = getGrid();
						List<PlayerMine> ownedPMines = grid.getPlayersMines(player);
						
						if(ownedPMines == null || ownedPMines.size() == 0)
						{
							openMainGUI(PMINE_GUI.FIRST_TIME, player, 1);
							return true;
						}
						
						else 
						{
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							if(mine != null && mine.isPartOfMine(player.getUniqueId()))
							{
								openMinePanelGUI(player, mine);
							}else
							{
								openMainGUI(PMINE_GUI.OWNED_MINES, player, 1);	
							}
							return true;
						}
						
						
						
						
					}else {
						if(args.length == 1) {
							
							/**
							 * Autominer
							 */
							if(args[0].equalsIgnoreCase("am") || args[0].equalsIgnoreCase("autominer"))
							{
								player.performCommand("am");
								return true;
							}
							
							/**
							 * Settings access
							 */
							if(args[0].equalsIgnoreCase("settings"))
							{
								Grid grid = getGrid();
								PlayerMine mine = grid.getMineAtLocation(player.getLocation());
								if(mine == null)
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
									return true;
								}
								openMainGUI(PMINE_GUI.SETTINGS,player,0);
								return true;
							}
							
							if(args[0].equalsIgnoreCase("help"))
							{
								player.sendMessage("§b§lMines §7(1/1) §8//");
								player.sendMessage("§3/pmine §8- §7Opens up the main PMine Menu");
								player.sendMessage("§3/pmine settings §8- §7Opens up the Settings Menu");
								player.sendMessage("§3/pmine autominer §8- §7Opens up the Autominer Menu");
								player.sendMessage("§3/pmine visit <player> §8- §7Visits a player's PMine");
								player.sendMessage("§3/pmine resetmine §8- §7Resets the mine you are in");
								player.sendMessage("§3/pmine trust/add <player> §8- §7Adds player to mine");
								player.sendMessage("§3/pmine remove <player> §8- §7Removes a player from mine");
								player.sendMessage("§3/pmine sethome <player> §8- §7Set home for the player mine");
								player.sendMessage("§3/pmine home §8- §7Teleport to your player mine");
								return true;
							}
							
							/**
							 * SET HOME OF MINE
							 */
							if(args[0].equalsIgnoreCase("sethome"))
							{
								Grid grid = getGrid();
								PlayerMine mine = grid.getMineAtLocation(player.getLocation());
								if(mine == null)
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
									return true;
								}
								if(mine.isMemberOfMine(player.getUniqueId()) || mine.getOwnerUUID().equals(player.getUniqueId()))
								{
									mine.setSpawnLocation(player.getLocation());
									player.sendMessage(MessageHandler.DEFAULT_PREFIX + "Successfully set spawn location for your PMine");
									saveGrid(grid);
								}
								return true;
							}
							
							/**
							 * TELEPORT TO FIRST HOME
							 */
							
							else if(args[0].equalsIgnoreCase("home") ||
									args[0].equalsIgnoreCase("h"))
							{
								int ID = 0;
								Grid grid = getGrid();
								List<PlayerMine> mines = grid.getPlayersMines(player);
								if(mines.size() > ID)
								{
									teleportPlayerToMine(player, mines.get(ID));
								}else
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.INVALID_HOME, false);
									return true;
								}
								return true;
							}
							
							/**
							 * RESET MINE
							 */
							
							else if(args[0].equalsIgnoreCase("resetmine") ||
									args[0].equalsIgnoreCase("reset"))
							{
								Grid grid = getGrid();
								PlayerMine mine = grid.getMineAtLocation(player.getLocation());
								mine.getMine().setForceResetCD(60);
								if(mine.isPartOfMine(player.getUniqueId()))
								{
									if(mine.getMine().handleResetConditions(platform, RESET_TYPE.MANUAL, (player.hasPermission("op.lel")) ))
									{
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.4f, 2.0f);
									}
									else
									{
										player.sendMessage("§cCannot reset mine yet. Cooldown §c§n"+formatTime( (int)((mine.getMine().getNextForceResetTime() - System.currentTimeMillis()) / 1000))+"§c.");
									}
								}
								return true;
							}
							
							else
							{
								MessageHandler.playMessage(player, MESSAGE_TYPE.INVALID_SYNTAX, false);
							}
							
							
						}
						
						else if(args.length >= 2)
						{
							
							
							/**
							 * TELEPORT TO SPECIFIED HOME
							 */
							
							if(args[0].equalsIgnoreCase("home") ||
									args[0].equalsIgnoreCase("h") ||
									args[0].equalsIgnoreCase("visit"))
							{
								Grid grid = getGrid();
								boolean visitOnly = false;
								if(args[0].equalsIgnoreCase("visit"))
								{
									visitOnly = true;
								}
								
								int ID = 0;
								try
								{
									ID = Integer.parseInt(args[1]);	
								}catch(NumberFormatException e)
								{
									
									//Check if its a Player's Mine
									List<SimpleLocation> mineLocs = grid.getPlayersMineLocations(Bukkit.getOfflinePlayer(args[1])); 
									if(mineLocs != null)
									{
										if(args.length == 3)
										{
											ID = 1;
											try
											{
												ID = Integer.parseInt(args[2]);
											}
											catch(NumberFormatException e2)
											{}
											if(ID < 0) ID = 1;
											
											if(mineLocs.size() >= ID)
											{
												teleportPlayerToMine(player, grid.getMineAtLocation(mineLocs.get(ID-1)));
											}else
											{
												MessageHandler.playMessage(player, MESSAGE_TYPE.INVALID_HOME, false);
												return true;
											}	
									
											return true;
										}
									}
									
									
									MessageHandler.playMessage(player, MESSAGE_TYPE.INVALID_HOME, false);
									return true;
								}
								
								if(!visitOnly)
								{
									List<PlayerMine> mines = grid.getPlayersMines(player);
									if(mines.size() > ID)
									{
										teleportPlayerToMine(player, mines.get(ID));
									}else
									{
										MessageHandler.playMessage(player, MESSAGE_TYPE.INVALID_HOME, false);
										return true;
									}	
								}
								else
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.INVALID_HOME, false);
								}
								return true;
							}
					
							
							
							/*
							 * REMOVE PLAYER FROM MINE
							 * */
							else if(args[0].equalsIgnoreCase("remove") ||
									args[0].equalsIgnoreCase("untrust"))
							{
								OfflinePlayer target = Bukkit.getPlayer(args[1]);
								
								
								Grid grid = getGrid();
								PlayerMine mine = grid.getMineAtLocation(player.getLocation());
								if(mine == null)
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
									return true;
								}
								
								if(!mine.getOwnerUUID().equals(player.getUniqueId()))
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
									return true;
								}
								
								if(mine.removeMember(target.getUniqueId()))
								{
									//Successfully removed from mine
									if(Variables.getPlayer(target, "TrustedPMines") != null)
									{
										List<SimpleLocation> addedMines = (List<SimpleLocation>) Variables.getPlayer(target, "TrustedPMines");

										SimpleLocation toRemove = null;
										SimpleLocation mineLoc = new SimpleLocation(mine.getMineCenter());
										for(SimpleLocation loc : addedMines)
										{
											if(compareSimpleLocations(loc, mineLoc))
											{
												toRemove = loc;
												break;
											}
										}
										if(toRemove != null)
										{
											addedMines.remove(toRemove);
											Variables.setPlayer(target, "TrustedPMines", addedMines); //Used in GUI, will do a first iteration to check if they still exist in member list of the mines.		
										}
									
										saveGrid(grid);
									}
									
									Player online = Bukkit.getPlayer(target.getName());
									if(online != null)
									{
										online.performCommand("spawn");
										online.sendMessage(MessageHandler.DEFAULT_PREFIX + "You've been removed from " + Bukkit.getOfflinePlayer(mine.getOwnerUUID()).getName() + "'s PlayerMine.");
										//Send messgae you've been removed from x's mine.
									}
									player.sendMessage(MessageHandler.DEFAULT_PREFIX + "You've removed " + target.getName() + " from this PMine.");
								}
					
								
								return true;
								
							}
							
							/*
							 * ADD PLAYER TO MINE
							 * */
							else if(args[0].equalsIgnoreCase("trust") ||
									args[0].equalsIgnoreCase("add"))
							{
								Player target = Bukkit.getPlayer(args[1]);
								if(target == null)
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.PLAYER_NOT_ONLINE, false);
									return true;
								}
								
								Grid grid = getGrid();
								PlayerMine mine = grid.getMineAtLocation(player.getLocation());
								if(mine == null)
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_STANDING_ON_MINE, false);
									return true;
								}
								
								if(!mine.getOwnerUUID().equals(player.getUniqueId()))
								{
									MessageHandler.playMessage(player, MESSAGE_TYPE.NOT_MINE_OWNER, false);
									return true;
								}
								
								Set<UUID> members = mine.getMembers();
								UUID targetUUID = target.getUniqueId();
								if(members.size() < mine.getMaxMembers())
								{
									if(mine.addMember(targetUUID))
									{
										//Successfully added
										if(Variables.getPlayer(target, "TrustedPMines") == null)
											Variables.setPlayer(target, "TrustedPMines", new ArrayList<SimpleLocation>());
										List<SimpleLocation> addedMines = (List<SimpleLocation>) Variables.getPlayer(target, "TrustedPMines");
										addedMines.add(new SimpleLocation(mine.getMineCenter()));
										Variables.setPlayer(target, "TrustedPMines", addedMines); //Used in GUI, will do a first iteration to check if they still exist in member list of the mines.
										saveGrid(grid);
										
										player.sendMessage("Added " + target.getName() + " to your PMine.");
										target.sendMessage(MessageHandler.DEFAULT_PREFIX + "You've been added to §a" + Bukkit.getPlayer(mine.getOwnerUUID()).getName() + "§7's PlayerMine. §8(§e/pmine to access§8)" + MessageHandler.DEFAULT_SUFFIX);
									}else
									{
										player.sendMessage("Player is part of mine");
									}
									
								}else
								{
									player.sendMessage("No more slots remaining");
									//Mine no more slots msg. Upgrade by redeeming Expansion Tokens
								}
							}
							
							
							
							else
							{
								MessageHandler.playMessage(player, MESSAGE_TYPE.INVALID_SYNTAX, false);
							}
							
							return true;
						}
						
						
						
						//ADMIN COMMANDS
						
						if(!player.hasPermission("op.lel"))
							return true;

						if (args[0].equalsIgnoreCase("gen"))
						{
							createNewPMine(player);
							openMainGUI(PMINE_GUI.OWNED_MINES, player, 1);
							return true;
						}
						
						if (args[0].equalsIgnoreCase("fix"))
						{
							List<PlayerMine> old = (List<PlayerMine>) Variables.getPlayer(player, "OwnedPMines");
							List<SimpleLocation> newMines = new ArrayList<SimpleLocation>();
							for(PlayerMine mine : old)
							{
								newMines.add(new SimpleLocation(mine.getMineCenter()));
							}
							
							Variables.setPlayer(player, "OwnedPMines", newMines);
							return true;
						}
						
						
					}
					
					//If nothing is reached
					player.sendMessage("§b§lMines §7(1/1) §8//");
					player.sendMessage("§3/mines §8- §7Opens up the main PMine Menu");
					player.sendMessage("§3/mines settings §8- §7Opens up the Settings Menu");
					player.sendMessage("§3/mines autominer §8- §7Opens up the Autominer Menu");
					player.sendMessage("§3/mines visit <player> §8- §7Visits a player's PMine");
					player.sendMessage("§3/mines resetmine §8- §7Resets the mine you are in");
					player.sendMessage("§3/mines trust/add <player> §8- §7Adds player to mine");
					player.sendMessage("§3/mines remove <player> §8- §7Removes a player from mine");
					player.sendMessage("§3/mines sethome <player> §8- §7Set home for the player mine");
					player.sendMessage("§3/mines home §8- §7Teleport to your player mine");
					
					
				}
				
				
			
				
					
			
				
				
				return true;
			}
		});
		
		//PMine Debug Command
		registerCommand("pmine3", new BukkitCommand("pmine3", "Player Mines", "[subcmd]",
				Arrays.asList("playermine3")) {
			@Override
			public boolean execute(CommandSender sender, String label, String[] args) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if(!player.hasPermission("op.lele")) return true;
					if(args.length == 0) {
						
					}else {
						if(args[0].equalsIgnoreCase("mineinfo"))
						{
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							if(mine != null)
							{
								player.sendMessage("No mine at location");
							}else
							{
								player.sendMessage("=======================");
								player.sendMessage("Mine Owner " + Bukkit.getOfflinePlayer(mine.getOwnerUUID()).getName());
								player.sendMessage("");
								player.sendMessage(" ");
								player.sendMessage(" ");
								player.sendMessage(" ");
								player.sendMessage(" ");
								player.sendMessage(" ");
								player.sendMessage(" ");
								
							}
						}
						
						if(args[0].equalsIgnoreCase("flushUpdateCache"))
						{
							Variables.flushUpdateCache();
						}
						
						if(args[0].equalsIgnoreCase("findMine"))
						{
							Long now = System.currentTimeMillis();
							Player target = Bukkit.getPlayer(args[1]);
							if(target != null)
							{
								List<PlayerMine> mines = getGrid().extensiveMineSearch(target);
								player.sendMessage("Found " + mines.size() + " entries");
								for(PlayerMine mine : mines)
								{
									player.sendMessage("Found mine with " + mine.getTracker("MinedTotal") + " Blocks Mined, at X:" + mine.getCenterX() + " Z:" + mine.getCenterZ());
								}
								player.sendMessage("For player: " + target.getName());
							}
							Long end = System.currentTimeMillis();
							player.sendMessage("Search process took: " + ((now-end)*0.001) + "ms");
						}
						
						if(args[0].equalsIgnoreCase("restoremines"))
						{
							Long now = System.currentTimeMillis();
							Player target = Bukkit.getPlayer(args[1]);
							if(target != null)
							{
								List<PlayerMine> mines = getGrid().extensiveMineSearch(target);
								player.sendMessage("Found " + mines.size() + " entries");
								for(PlayerMine mine : mines)
								{
									player.sendMessage("Found mine with " + mine.getTracker("MinedTotal") + " Blocks Mined, at X:" + mine.getCenterX() + " Z:" + mine.getCenterZ());
								}
								
								List<SimpleLocation> locs = new ArrayList<SimpleLocation>();
								for(PlayerMine mine : mines)
									locs.add(new SimpleLocation(mine.getMineCenter()));
								getGrid().setMinesOwnedP(target, mines);
								Variables.setPlayer(target, "OwnedPMines", locs);
								player.sendMessage("RESTORED MINES FOR PLAYER: " + target.getName());
								
							}
							Long end = System.currentTimeMillis();
							player.sendMessage("Search process took: " + ((now-end)*0.001) + "ms");
						}
						
						if(args[0].equalsIgnoreCase("killallsettings"))
						{
							Set<UUID> players = getGrid().getAllPlayers();
							for(UUID p : players)
							{
								for(PlayerMine mine : getGrid().getPlayersMines(Bukkit.getOfflinePlayer(p)))
								{
									mine.resetSettings();
									
								};
								
							}
						}
						
						if(args[0].equalsIgnoreCase("viewTaxBalance"))
						{
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							player.sendMessage("Money Taxed: $" + mine.getTaxedAmount(TAX_TYPE.MONEY));
							player.sendMessage("Tokens Taxed: " + mine.getTaxedAmount(TAX_TYPE.TOKEN));
							player.sendMessage("Energy Taxed: " + mine.getTaxedAmount(TAX_TYPE.ENERGY));
						}
						
						if(args[0].equalsIgnoreCase("getcurrentmulti"))
							
						{
							PERK_TYPE type = PERK_TYPE.valueOf(args[1]);
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							player.sendMessage("Bonus % of type " + type.toString() + ": " + mine.getPerkBonus(type));
						}
						
						if(args[0].equalsIgnoreCase("refreshchunks"))
						{
							List<net.minecraft.server.v1_12_R1.Chunk> changedChunks = new ArrayList<net.minecraft.server.v1_12_R1.Chunk>();
							for(int x = -3; x < 3; ++x)
							{
								for(int z = -3; z < 3; ++z)
								{
									int currentX = player.getWorld().getChunkAt(player.getLocation()).getX();
									int currentZ = player.getWorld().getChunkAt(player.getLocation()).getZ();
									changedChunks.add( (net.minecraft.server.v1_12_R1.Chunk) ((CraftWorld) player.getWorld()).getHandle().getChunkAt(currentX+x, currentZ+z) );
								}
							}
							
							
							int diffx, diffz;
							int view = Bukkit.getServer().getViewDistance() << 4;
							for (net.minecraft.server.v1_12_R1.Chunk chunk : changedChunks) {
								((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, 20));
							}
						}
						if(args[0].equalsIgnoreCase("resettaxes"))
						{
							Set<UUID> players = getGrid().getAllPlayers();
							for(UUID p : players)
							{
								for(PlayerMine mine : getGrid().getPlayersMines(Bukkit.getOfflinePlayer(p)))
								{
									if(mine.getTaxedAmount(TAX_TYPE.ENERGY) > 0)
									{
										
										Bukkit.getPlayer("coolfire02").sendMessage("Reset players energy tax" + mine.getTaxedAmount(TAX_TYPE.ENERGY));
										mine.setTaxedAmount(TAX_TYPE.ENERGY, 0);
									}
									if(mine.getTaxedAmount(TAX_TYPE.TOKEN) > 0)
									{
										
										Bukkit.getPlayer("coolfire02").sendMessage("Reset players token tax" + mine.getTaxedAmount(TAX_TYPE.TOKEN));
										mine.setTaxedAmount(TAX_TYPE.TOKEN, 0);
									}
									if(mine.getTaxedAmount(TAX_TYPE.MONEY) > 0)
									{
										
										Bukkit.getPlayer("coolfire02").sendMessage("Reset players money tax" + mine.getTaxedAmount(TAX_TYPE.MONEY));
										mine.setTaxedAmount(TAX_TYPE.MONEY, 0);
									}
									
								};
								
							}
							
						}
						if(args[0].equalsIgnoreCase("checktokens"))
						{
							for(Player p : Bukkit.getOnlinePlayers())
							{
								player.sendMessage(p.getName() + "has " + toCommaFormat(getTokens(p)) + " tokens");
							}
						}
						
						if(args[0].equalsIgnoreCase("resetplayerowned"))
						{
							Player target = Bukkit.getPlayer(args[1]);
							Variables.setPlayer(target, "OwnedPMines", null);
						}
						if(args[0].equalsIgnoreCase("refreshchunks"))
						{
							player.sendMessage("refresh chunks");
							updateSurroundingChunks(player);
						}
						if(args[0].equalsIgnoreCase("readenergy"))
						{
							player.sendMessage("" + getExtractedEnergyValue(player.getItemInHand()));
						}
						
						if(args[0].equalsIgnoreCase("toggleam"))
						{
							setAutominerActive(player, true);
						}
						
						if(args[0].equalsIgnoreCase("toggleamoff"))
						{
							setAutominerActive(player, false);
						}
						
						
						if(args[0].equalsIgnoreCase("gen")) {
							System.out.println("Reached");
							Variables.setPlayer(player, "MaxPlayerMines", 100);
							if(Variables.get("PlayerMineGrid") == null) {
								Variables.set("PlayerMineGrid", new Grid(Bukkit.getWorld("PlayerMine")));
							}
							Grid mineGrid = getGrid();
							PlayerMine mine = mineGrid.generateNewMine(player, "Mine_1");
							if(mine != null)
								saveGrid(mineGrid);
							
							Pair<PlayerMine, List<Chunk>> pair = new MutablePair<>(mine, mine.getMineSchemChunks());
							chunksToLoad.add(pair);
						}
						
						if(args[0].equalsIgnoreCase("deleteschemvarCONFIRMED")) {
							Variables.set("PlayerMineSchematics", null);
						}
						
						if(args[0].equalsIgnoreCase("saveworldschematic")) {
							Selection selection = worldEdit.getSelection(player);
							
							if(selection != null) {
								World w = selection.getWorld();
								
								if(w != null && selection.getArea() < 100000000)
								{
									boolean exist = false;
									SchematicConfig sc = null;
									for(SchematicConfig scFinder : configHandler.schematics.values())
									{
										if(scFinder.getRawID().equalsIgnoreCase(args[1]))
										{
											sc = scFinder;
											exist = true;
											break;
										}
									}
									
									if(!exist)
									{
										sc = new SchematicConfig();
										sc.setRawID(args[1]);
										
										sc.setIconMaterial(Material.STONE);
										sc.setIconName("&f&LUnset Name");
										
										List<String> lore = new ArrayList<String>();
										lore.add("&7Unset Lore");
										sc.setIconLore(lore);
										
										sc.setAutoDeploy(false);
										sc.setMinMineSize(20);
										sc.setMaxMineSize(45);
										
										sc.setSchematicGroupType(SCHEMGROUP_TYPE.DEFAULT);
									}
									
									if(sc != null)
									{
										Block botLeftBlock, topRightBlock;
										botLeftBlock = w.getBlockAt(selection.getMinimumPoint());
										topRightBlock = w.getBlockAt(selection.getMaximumPoint());
										Location botLeft, topRight;
										botLeft = new Location(w, Math.min(botLeftBlock.getX(), topRightBlock.getX()),
												Math.min(botLeftBlock.getY(), topRightBlock.getY()),
												Math.max(botLeftBlock.getZ(), topRightBlock.getZ()));
										
										topRight = new Location(w, Math.max(botLeftBlock.getX(), topRightBlock.getX()),
												Math.max(botLeftBlock.getY(), topRightBlock.getY()),
												Math.min(botLeftBlock.getZ(), topRightBlock.getZ()));
										
										sc.setSchematicWorldLocation(
												new SimpleLocation(botLeft),
												new SimpleLocation(topRight),
												new SimpleLocation(w.getBlockAt(player.getLocation()).getRelative(0, -1, 0).getLocation()));
										
										configHandler.SaveSchematicConfig(sc);
										configHandler.schematics.put(sc.getRawID(), sc);	
										player.sendMessage("&aSuccessfully saved");
									}
									
								}
								
								
							}
						}
						
						else if (args[0].equalsIgnoreCase("makepublic"))
						{
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							mine.setPublic(true);
						}
						
						else if (args[0].equalsIgnoreCase("configsaving"))
						{
							for(SchematicConfig sc : configHandler.schematics.values())
							{
								if(sc.getRawID().equalsIgnoreCase("PM_Mine6"))
								{
									sc.setSchematicWorldLocation(new SimpleLocation("Spawn;-704;185;109"), sc.getTopRightPos(), sc.getCenterPos());
									configHandler.SaveSchematicConfig(sc);
								}
							}
						}
						
						else if (args[0].equalsIgnoreCase("printschemlocs"))
						{
							ArrayList<Schematic> schematics = Variables.getAndCast("PlayerMineSchematics", (Class<ArrayList<Schematic>>) (Class<?>) ArrayList.class);
							for (Schematic schem : schematics) {
								debugMsg("=================");
								debugMsg("Name: "+schem.getSchemName());
								debugMsg("BotLeft: " + schem.getSimpleBotLeftPosition().getSerializedString());
								debugMsg("TopRight: " + schem.getSimpleTopRightPosition().getSerializedString());
								debugMsg("Center: " + schem.getSimpleCenterPosition().getSerializedString());
								debugMsg("=================");
							}
						}
						
						//Schem loading
						if(!loadedSchematics.containsKey("PM_Bedrock")) {
							
							if(Variables.get("PlayerMineSchematics") == null) return true;
							
							ArrayList<Schematic> schematics = Variables.getAndCast("PlayerMineSchematics", (Class<ArrayList<Schematic>>) (Class<?>) ArrayList.class);
							for(Schematic schem : schematics) {
								if(schem.getSchemName().equalsIgnoreCase("PM_Bedrock")) {
									List<SchematicBlock> blocks = schem.getSchematicBlocks();
									
									loadedSchematics.put("PM_Bedrock", new LoadedSchematic(blocks));
									
								}
							}
						}
						
						else if (args[0].equalsIgnoreCase("resetwholethingCONFIRMED"))
						{
							Variables.set("PlayerMineGrid", null);
						}
						
						

						
						if (args[0].equalsIgnoreCase("pastehere")) {
							loadedSchematics.get("PM_Bedrock").pasteSchematic(player.getWorld(), player.getLocation());
						}
						
						else if (args[0].equalsIgnoreCase("setchonkCONFIRMED"))
						{
							int rad = Integer.parseInt(args[1]);
							
							//Clear mine space
							Grid grid = getGrid();
							PlayerMine mine = grid.getMineAtLocation(player.getLocation());
							if(mine == null) {
								
								return true;
							}
							
							mine.getMine().setXZRadius(25);
							mine.getMine().setYRadius(60);
							mine.setPublic(true);
							
							mine.clearMineSpace();
							
							
							mine.buildBedrock();
							
							saveGrid(grid);
							
							resetPMine(mine);
							
							
						}
						
						
						
						else if (args[0].equalsIgnoreCase("setbedrock")) {
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							if(mine == null) {
								debugMsg("Mine Null");
								return true;
							}
							mine.buildBedrock();
							debugMsg("Bedrock Built");
						}
						
						else if (args[0].equalsIgnoreCase("clearMineSpace")) {
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							if(mine == null) {
								debugMsg("Mine Null");
								return true;
							}
							mine.clearMineSpace();
							debugMsg("Clear Mine Space");
						}
						
						else if (args[0].equalsIgnoreCase("resetmine")) {
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							if(mine == null) {
								debugMsg("Mine Null");
								return true;
							}
							resetPMine(mine);
							
						}
						
						else if (args[0].equalsIgnoreCase("setblock")) {
							PlayerMine mine = getGrid().getMineAtLocation(player.getLocation());
							if(mine == null) {
								debugMsg("Mine Null");
								return true;
							}
							mine.getMine().getOreTable().addBlock(new MineBlock(Material.valueOf(args[1]),(short)0), 50.0);
							resetPMine(mine);
							
						}
						
						
					}
					
					if (player.hasPermission("miningmodule.debug")) {
						if (args.length == 0) {
							player.sendMessage("Mining Module debug commands:");
							player.sendMessage("/debugmining resetblocksmined <player>");
							player.sendMessage("/debugmining setblocksmined <player> <amt>");
							player.sendMessage("----end----");
						} else {
							if (args.length > 1) {
								if (args[0].equalsIgnoreCase("resetblocksmined")) {
									OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
									Variables.setPlayer(targetPlayer, "BlocksMined", 0);
									player.sendMessage(ChatColor.GREEN + "Successfully reset " + targetPlayer.getName()
											+ "'s BlocksMined variable");
								} else if (args[0].equalsIgnoreCase("setblocksmined")) {
									OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
									int newAmt = 0;
									try {
										newAmt = Integer.parseInt(args[2]);
									} catch (IndexOutOfBoundsException e) {
										player.sendMessage("noobhead you're missing an argument");
										return true;
									} catch (NumberFormatException e) {
										player.sendMessage(args[2] + " is not a number lmao");
										return true;
									}

									Variables.setPlayer(targetPlayer, "BlocksMined", newAmt);
									player.sendMessage(ChatColor.GREEN + "Successfully set " + targetPlayer.getName()
											+ "'s BlocksMined variable to value: " + newAmt);
								}
							}
						}

					} else
						player.sendMessage(ChatColor.RED + "Insufficient permission");
				}
				return true;
			}
		});
	}
	
	//On Schematic Reselect
	@EventHandler
	public void onSchematicReselect(PostChangeSchematicEvent e)
	{
		HashMap<PERK_TYPE, Float> perks = e.getNextSchematic().getPerksList();
		if(perks != null && !perks.isEmpty())
		{
			float percen = perks.getOrDefault(PERK_TYPE.OBSIDIAN_BOOST, 0.0f);
			if(percen > 0.f)
			{
				e.getMine().getMine().getOreTable().removeBlock(new MineBlock(Material.OBSIDIAN, (short) 0));
				e.getMine().getMine().getOreTable().addBlock(new MineBlock(Material.OBSIDIAN, (short) 0), percen);
			}
		}
	}
	
	@EventHandler
	public void onFactoryUpgrade(MineFactoryUpgradeEvent e)
	{
		HashMap<PERK_TYPE, Float> perks = Mines.configHandler.schematics.get(e.getMine().getCurrentSchematicName()).getPerksList();
		if(perks != null && !perks.isEmpty())
		{
			float percen = perks.getOrDefault(PERK_TYPE.OBSIDIAN_BOOST, 0.0f);
			if(percen > 0.f)
			{
				e.getMine().getMine().getOreTable().removeBlock(new MineBlock(Material.OBSIDIAN, (short) 0));
				e.getMine().getMine().getOreTable().addBlock(new MineBlock(Material.OBSIDIAN, (short) 0), percen);
			}
		}
	}
	
	//AM Shard Redemption
		@EventHandler
		public void shardRedeem(PlayerInteractEvent e)
		{
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Player player = e.getPlayer();
				ItemStack hand = player.getItemInHand();
				if(hand != null && hand.getType() == Material.PRISMARINE_SHARD)
				{
					ItemMeta meta = hand.getItemMeta();
					if(meta.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&6&lAdvanced &3&lAM &f&lShard &8(&7Right-Click&8)")))
					{
						try
						{
							Integer mins = Integer.parseInt(ChatColor.stripColor(meta.getLore().get(2).split(" ")[3]));
							if(mins > 0)
							{
								if(Variables.getPlayer(player, "Autominer") == null)
								{
									Variables.setPlayer(player, "Autominer", "Off;0;0");
								}
								
								String var = (String) Variables.getPlayer(player, "Autominer");
								String[] split = var.split(";", 3);
								Float timeLeft = Float.parseFloat(split[1]);
								timeLeft += mins * 60;
								hand.setAmount(hand.getAmount() -1);
								Variables.setPlayer(player, "Autominer", split[0]+";"+timeLeft+";"+split[2]);
								player.sendMessage(ChatColor.GREEN + "+" + mins + " minutes Advanced Automining Time (/am)");
							}
						}catch(Exception e1)
						{
							e1.printStackTrace();
						}
					}
				}
			}
		}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void amBlockBreak(me.despawningbone.module.Events.PrisonsBlockBreakEvent e)
	{
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		
		
		
		boolean disable = false;

		Grid grid = getGrid();
		PlayerMine mine = grid.getMineAtLocation(p.getLocation());
		
		if(p.hasPermission("op.lel"))
			mine.addTaxedAmount(TAX_TYPE.MONEY, 1.0);
		
		if(mine == null)
		{
			disable = true;
		}
		else if(!mine.getOwnerUUID().equals(p.getUniqueId()))
		{
			disable = true;
		}
		else if(item.getType() != Material.DIAMOND_PICKAXE)
		{
			disable = true;
		}
		if(isAutominerEnabled(p) && disable)
		{
			setAutominerActive(p, false);
			e.setCancelled(true);
		}else
		{
			amSessionBlocksMined.put(p, amSessionBlocksMined.getOrDefault(p, 0)+1);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7Autominer has mined §7§n"+amSessionBlocksMined.get(p) +"§7 Blocks."));
			if(amSessionBlocksMined.get(p) % 10 == 0)
				p.playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, 0.4f, 1.4f);
		}
	}
	
	@EventHandler
	public void onJoinCheck(PlayerJoinEvent e)
	{
		Grid grid = getGrid();
		List<PlayerMine> mines = grid.getPlayersMines(e.getPlayer());
		if(!mines.isEmpty())
		{
			List<SimpleLocation> locations = new ArrayList<SimpleLocation>();
			for(PlayerMine mine : mines)
			{
				locations.add(new SimpleLocation(mine.getMineCenter()));
			}
			grid.setMinesOwned(e.getPlayer(), locations);
			saveGrid(grid);
		}
	}
	
	
	protected static void debugMsg(String msg) {
		if(!PRODUCTION_MODE)
			Bukkit.broadcastMessage(msg);
		System.out.println(msg);
	}
	
	public static Grid getGrid() {
		if(Variables.get("PlayerMineGrid") == null) {
			Variables.set("PlayerMineGrid", new Grid(Bukkit.getWorld("PlayerMine")));
		}
		
		
		Grid mineGrid = (Grid) Variables.get("PlayerMineGrid");
		return mineGrid;
	}
	
	public static void saveGrid(Grid grid)
	{
		Variables.set("PlayerMineGrid", grid);
	}
	
	public static PlayerMine getMineAtLocation(Location loc) {
		Grid mineGrid = getGrid();
		PlayerMine currentMine = mineGrid.getMineAtLocation(loc);
		return currentMine;
	}
	
	public boolean compareSimpleLocations(SimpleLocation loc, SimpleLocation loc2)
	{
		return (loc.getBlockX() == loc2.getBlockX() &&
				loc.getBlockY() == loc2.getBlockY() &&
				loc.getBlockZ() == loc2.getBlockZ() &&
				loc.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()));
	}
	
	
	
	//PlayerMine Guarding Events
	
	//Move Guards
	@EventHandler
	public void damageVehicle(VehicleDamageEvent e) {
		if(e.getVehicle().getWorld().getName().equalsIgnoreCase(Mines.mineWorld)) {
			if(e.getAttacker() instanceof Player) {
				Player p = (Player) e.getAttacker();
				PlayerMine mine = getMineAtLocation(e.getVehicle().getLocation());
				if(!mine.canEditDecoration(p)) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true) 
	public void onVehicleMove(VehicleMoveEvent e) {
		if(e.getVehicle().getWorld().getName().equalsIgnoreCase(Mines.mineWorld)) {
			Entity rider = e.getVehicle().getPassenger();
			if(!(rider instanceof Player)) {
				return;
			}
			
			Player player = (Player) rider;
			PlayerMine from = getMineAtLocation(e.getFrom());
			PlayerMine to = getMineAtLocation(e.getTo());
			if(from != to && !to.canEnterMine(player)) {
				org.bukkit.util.Vector v = new org.bukkit.util.Vector();
				e.getVehicle().setVelocity(v);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
        // Only do something if there is a definite x or z movement
		// System.out.println("onPlayerMove hash: " + Grid.class.hashCode());
		if (e.getTo().getBlockX() - e.getFrom().getBlockX() == 0 && e.getTo().getBlockZ() - e.getFrom().getBlockZ() == 0) {
            return;
        }
        PlayerMine from = getMineAtLocation(e.getFrom());
		PlayerMine to = getMineAtLocation(e.getTo());
		if(to != null && to != from && !to.canEnterMine(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	//Handles Block Placing as a visitor
	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockPlace(BlockPlaceEvent e)
	{
		Block b = e.getBlock();
		if(b.getWorld().getName().equalsIgnoreCase("PlayerMine")) {
			
			Grid grid = getGrid();
			PlayerMine currentMine = grid.getMineAtLocation(b.getLocation());
			if(currentMine != null) {
				
				//If within Mineable Mine, cancel
				if(currentMine.getMine().inMine(b))
				{
					e.setCancelled(true);
					return;
				}
				
				else if (!currentMine.canEditDecoration(e.getPlayer()))
				{
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void inventoryOpen(InventoryOpenEvent e)
	{
		if(e.getInventory().getLocation() != null && e.getInventory().getLocation().getWorld().getName().equalsIgnoreCase("PlayerMine")
				&& e.getInventory().getType() != InventoryType.PLAYER)
		{
			if(!e.getPlayer().hasPermission("Pmine.storeitems"))
			{
				e.getPlayer().sendMessage(MessageHandler.DEFAULT_PREFIX + "Storing items is disabled in PMines.");
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof ItemFrame)
		{
			if(e.getEntity().getWorld().getName().equalsIgnoreCase("PlayerMine"))
			{
				Grid grid = getGrid();
				PlayerMine currentMine = grid.getMineAtLocation(e.getEntity().getLocation());
				Player attacker = null;
				if(e.getDamager() instanceof Player)
				{
					attacker = (Player) e.getDamager();
				}
				else if (e.getDamager() instanceof Projectile &&
						((Projectile) e.getDamager()).getShooter() instanceof Player)
				{
					attacker = (Player) ((Projectile) e.getDamager()).getShooter();
				}
				
				if(!currentMine.canEditDecoration(attacker))
				{
					e.setCancelled(true);
				}
					
			}
		}
	}
	
	//Handles Block Breaking as a visitor
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		if(b.getWorld().getName().equalsIgnoreCase("PlayerMine")) {
			Grid grid = getGrid();
			PlayerMine currentMine = grid.getMineAtLocation(b.getLocation());
			

			if(currentMine != null) {
			
				
				Player p = e.getPlayer();
				
				currentMine.addToTracker("MinedTotal", 1);
				currentMine.addToTracker("MinedWeekly", 1);
				currentMine.addToTracker("MinedDaily", 1);
				MineFactory future = currentMine.getMine().getNextMineFactoryTier();
				if(future != null)
					future.addBlocksMined(1);
				
				
				if(currentMine.getMine().inMine(b)) {
					if(!currentMine.canMineInMine(p)) {
						
						e.setCancelled(true);
						return;
					}
					currentMine.getMine().breakBlocks(1, super.platform);
					//e.getPlayer().sendMessage("Current Percentage: " + currentMine.getMine().getCurrentBlocksRemaining() / (float) currentMine.getMine().getMaxBlocksInMine() * 100 + "%, Current Blocks: " + currentMine.getMine().getCurrentBlocksRemaining());
					
				} else if (!currentMine.canEditDecoration(p)) {
					e.setCancelled(true);
				}
				
/*				p.sendMessage("Main Mine Loc: " + currentMine.getCenterX() + "x " + currentMine.getCenterZ() + "z.");
				p.sendMessage("Player: " + Bukkit.getPlayer(currentMine.getOwnerUUID()).getName());
				p.sendMessage("Can mine " + (currentMine.canMine(p) == true ? "true":"false") );
				p.sendMessage(" ");*/
				
			}
		}
	}
	
	//Handles Teleporting to Mines as a visitor
	@EventHandler
	public void teleportEvent(PlayerTeleportEvent e) {
		if(e.getTo() != null && e.getTo().getWorld().getName().equalsIgnoreCase(Mines.mineWorld)) {
			PlayerMine toMine = Mines.getMineAtLocation(e.getTo());
			if(toMine != null) {
				//System.out.println("Mine at teleport loc: " + toMine.getOwnerUUID().toString());
				if(!toMine.canEnterMine(e.getPlayer())) {
					e.setCancelled(true);
					MessageHandler.playMessage(e.getPlayer(), MESSAGE_TYPE.MINE_LOCKED_FOR_VISITORS, true);	
				}
			}
		}
	}
	
	//Handles Frost Walking as a visitor
	@EventHandler
	public void onFrostForm(EntityBlockFormEvent e) {
		if(e.getEntity() instanceof Player && e.getNewState().getType().equals(Material.FROSTED_ICE)) {
			Player p = (Player) e.getEntity();
			PlayerMine mine = Mines.getMineAtLocation(e.getBlock().getLocation());
			if(mine != null) {
				if(!mine.canEditDecoration(p)) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	//Handles interaction with ender crystals
	@EventHandler 
	public void onHitEndCrystal(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() != null && e.getRightClicked().getType().equals(EntityType.ENDER_CRYSTAL)) {
			PlayerMine mine = Mines.getMineAtLocation(e.getRightClicked().getLocation());
			if(mine != null) {
				if(!mine.canEditDecoration(e.getPlayer())) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	void onPlaceEndCrystal(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() == Material.END_CRYSTAL) {
			PlayerMine mine = Mines.getMineAtLocation(e.getClickedBlock().getLocation());
			if(mine != null) {
				if(!mine.canEditDecoration(e.getPlayer())) {
					e.setCancelled(true);
				}
			}
		}
	}
	
    @EventHandler
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent e) {
        if (e.getVehicle().getType() == EntityType.BOAT) {
        	PlayerMine mine = Mines.getMineAtLocation(e.getVehicle().getLocation());
        	if(mine != null) {
        		e.setCancelled(true);
        		if(e.getEntity() instanceof Player)
        		{
    				if(mine.canEditDecoration((Player)e.getEntity())) {
    					e.setCancelled(false);
    				}
        		}
			}
        }
    }
    

    
    @EventHandler
    public void handleInteraction(PlayerInteractEvent e)
    {
    	if(e.getPlayer().getWorld().getName().equalsIgnoreCase("PlayerMine")) {
    		Block b = e.getClickedBlock();
    		Grid grid = getGrid();
    		if(e.getClickedBlock() != null)
    		{
        		PlayerMine m1 = grid.getMineAtLocation(e.getClickedBlock().getLocation());
        		if(!m1.getMine().inMine(e.getClickedBlock()) && !m1.canEditDecoration(e.getPlayer()))
        		{
        			e.setCancelled(true);
        		}
    		}
    		
    	}
    }
 
	
	@EventHandler
	public void onEndCrystalDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() != null && e.getEntity() instanceof EnderCrystal) {
	        Player p = null;
	        if (e.getDamager() instanceof Player) {
	            p = (Player) e.getDamager();
	        } else if (e.getDamager() instanceof Projectile) {
	            // Get the shooter
	            Projectile projectile = (Projectile)e.getDamager();
	            ProjectileSource shooter = projectile.getShooter();
	            if (shooter instanceof Player) {
	                p = (Player)shooter;
	            }
	        }
	        if(p != null) {
	        	PlayerMine mine = Mines.getMineAtLocation(e.getEntity().getLocation());
	        	if(mine != null && !mine.canEditDecoration(p)) {
	        		e.setCancelled(true);
	        	}
	        }
		}
		else if (e.getEntity() != null && e.getEntity() instanceof ItemFrame) {
			if(e.getDamager() instanceof Projectile) {
				Projectile p = (Projectile) e.getDamager();
				if(p.getShooter() instanceof Player) {
					Player player = (Player) p.getShooter();
					PlayerMine mine = Mines.getMineAtLocation(e.getEntity().getLocation());
					if(!mine.canEditDecoration(player)) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
	

	
	
	
	
	public static boolean hasAdminBypassToMines(Player player) {
		if(player.hasPermission("PlayerMine.bypassrestrictions")) {
			
			if(Variables.getPlayer(player, "PlayerMineAdminBypass") == null) 
				Variables.setPlayer(player, "PlayerMineAdminBypass", true);
			
			if(Variables.getPlayer(player, "PlayerMineAdminBypass") != null) {
				if((Boolean)Variables.getPlayer(player, "PlayerMineAdminBypass") == true)
					return true;
			}	
		}
		return false;
	}
	
	private String getLevelingPrefix(int level, int pres) {
		String prefix = "6";
		switch(pres) {
		case 1:
			prefix = "5";
		case 2:
			prefix = "2";
		case 3:
			prefix = "3";
		}
		return "§8[§8" + (pres > 0 ? "§"+prefix+"§l" + toRoman(pres) + "§7-": "") + "§f" + level + "§8]";
	}
	
	
	
	public WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
	    return null;
	    }
	 
	    return (WorldGuardPlugin)plugin;
	}
	
	protected boolean isInRegion(Location loc, String region)
	{
		WorldGuardPlugin wgPlugin = getWorldGuard();
		
	    if (wgPlugin != null)
	    {
	    	ApplicableRegionSet set = wgPlugin.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
	    	for(ProtectedRegion loopR : set.getRegions()) {
	    		if(loopR.getId().equals(region)) {
	    			return true;
	    		}
	    	}
	    }
	    return false;
	}
	
	protected boolean isInAnyRegionStartingWith(Location loc, String region)
	{
		WorldGuardPlugin wgPlugin = getWorldGuard();
		
	    if (wgPlugin != null)
	    {
	    	ApplicableRegionSet set = wgPlugin.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
	    	for(ProtectedRegion loopR : set.getRegions()) {
	    		if(loopR.getId().startsWith(region)) {
	    			return true;
	    		}
	    	}
	    }
	    return false;
	}
	
	private Boolean outpostBenefits(Player player, String outpost) {
		Boolean a = false;
		if(Variables.getPlayer(player, "PlayerGangStat") != null) {
			String gang = ((String) Variables.getPlayer(player, "PlayerGangStat")).split(";")[0];
			if(Variables.get("Outpost", outpost, "Captured") != null) {
				if(gang.equals((String) Variables.get("Outpost", outpost, "Captured"))) a=true;
			}
		}
		return a;
	}
	
	private String percentToBar(double percent) {
		double finalPercent = percent;
		if(percent > 100) percent = 100;
		int n = (int) Math.round(percent / 100 * 30); 
		String init = n <= 0 ? "§7 §l|" : "§a §l|";
		for(int i = 1; i < 30; i++) {
			String bar = i == n ? "§7§l|" : "|";
			init += bar;
		}
		return init + " §8(§7" + finalPercent + "%§8)";
	}
	
	
	
    private double skillPercentage(OfflinePlayer player, String skillid, double percentPerLvl) {
        double percentbuff = 0;
        try{
            for(String skillType : ((String) Variables.getPlayer(player, "Advancement")).split(";")) {
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
    
    private String getPrefixStyle(int level) {
		return "§8[§6§lA§7-§e" + level + "§8]";
	}
           
	
	/*private double gUpgradesInfo(Player player, String skillid, double percentPerLvl) {
	    double percentbuff = 0;
	    if(Variables.getPlayer(player, "PlayerGangStat") != null) {
	    	String playerVar = (String) Variables.getPlayer(player, "PlayerGangStat");
	    	String[] playerData = playerVar.split(";");
			String gN = playerData[0];
			String gUpgradesVar = (String) Variables.get("Gangs", gN, "Upgrades");
			 for(String skillType : (gUpgradesVar.split(";"))) {
		            String[] tokens = skillType.split("-");
		            if(tokens[0].equals(skillid)){
		            	int levelOfID;
		            	try {
		            		levelOfID = Integer.parseInt(tokens[1]);
		            	}catch (NumberFormatException e) {
		            		player.sendMessage(ChatColor.RED + "Error - Gangs upgrade not parsed properly! Contact an admin to get this resolved.");
		            		return 0;
		            	}
		                percentbuff = percentPerLvl * levelOfID;
		                break;
		            }
		        }
	    }
	    
	    try{

	    	String playerVar = (String) Variables.getPlayer(player, "PlayerGangStat");
			String[] playerData = playerVar.split(";");
			String gN = playerData[0];
			String gUpgradesVar = (String) Variables.get("Gangs", gN, "Upgrades");
			
	        for(String skillType : (gUpgradesVar.split(";"))) {
	            String[] tokens = skillType.split("-");
	            if(tokens[0].equals(skillid)) {
	                int levelOfID = Integer.parseInt(tokens[1]);
	                percentbuff = percentPerLvl * levelOfID;
	                break;
	            }
	        }
	    } catch (NullPointerException e) {
	        return 0;
	    	}
	    return percentbuff;
		}*/
    
    ConfigHandler getConfigHandler() {
        return configHandler;
    }
	
    
    
	@Deprecated
    private int findTime(Player player, String ability, String varType) {
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
    private double findPerkMulti(OfflinePlayer player, int skillid) {
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
	private String getSettings(Player player, String settings) {
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
	
	public boolean isExtractedEnergy(ItemStack item)
	{
		if(item.getType() == Material.INK_SACK && item.getData().getData() == (short) 12)
		{
			List<String> lore = item.getItemMeta().getLore();
			if(lore != null && !lore.isEmpty())
			{
				if(lore.get(0).contains(ChatColor.translateAlternateColorCodes('&', "&b&lPickaxe Energy")))
				{
					return true;
				}	
			}
		}
		return false;
	}
	
	public int getExtractedEnergyValue(ItemStack item)
	{
		if(isExtractedEnergy(item))
		{
			String line = item.getItemMeta().getLore().get(0);
			try
			{
				return Integer.parseInt(ChatColor.stripColor(line).split(" ")[2]);	
			}catch(Exception e)
			{
				return 0;
			}
		}
		return 0;
	}
	
	public void setExtractedEnergyValue(ItemStack item, int newVal)
	{
		if(isExtractedEnergy(item))
		{
			String name = item.getItemMeta().getDisplayName();
			List<String> lore = item.getItemMeta().getLore();
			
			String line = lore.get(0);
			int val = Integer.parseInt(ChatColor.stripColor(line).split(" ")[2]);
			line = line.replace(String.valueOf(val), String.valueOf(newVal));
			name = name.replace(String.valueOf(val), String.valueOf(newVal));
			lore.set(0, line);
			
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			
			item.setItemMeta(meta);
		}
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
	
	

	private double gUpgradesInfoCore(String gN, String skillid, double percentPerLvl) {
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
	
	public String toCommaFormat(int value) {
		return String.format("%,d",value);
	}
	
	public String toCommaFormat(double value) {
		return String.format("%,.2f",value);
	}
	
	private static double getTokens(Player player) {
		Object tokenObj = Variables.getSkript("vtokens.balance." + player.getUniqueId());
		return tokenObj instanceof Long ? ((Long) tokenObj).doubleValue() : (double) tokenObj;
	}
	
	private static void addTokens(Player player, double amount) {  //a more efficient approach would be to use operate on vtokens then save after each events instead of each operation, but i am not sure if there would be async modifications so to be safe this is the way i chose
		Object tokenObj = Variables.getSkript("vtokens.balance." + player.getUniqueId());
		if(tokenObj == null) return;
		double vtokens = tokenObj instanceof Long ? ((Long) tokenObj).doubleValue() : (double) tokenObj;
		Variables.setSkript("vtokens.balance." + player.getUniqueId(), vtokens + amount);  //storing this value as double and using serialization since double serialization isnt that expensive and if i dont store as double i would need to parse it everytime which is even more expensive
	}
}