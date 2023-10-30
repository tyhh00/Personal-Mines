package me.despawningbone.module.Events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class PrisonsBlockBreakEvent extends BlockBreakEvent {
  private final boolean isAutomining;
  
  public PrisonsBlockBreakEvent(Block theBlock, Player player, boolean isAutomining) {
    super(theBlock, player);
    this.isAutomining = isAutomining;
  }
  
  public boolean isAutomining() {
    return this.isAutomining;
  }
}
