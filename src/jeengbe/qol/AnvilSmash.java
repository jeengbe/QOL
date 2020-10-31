package jeengbe.qol;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftFallingBlock;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import net.minecraft.server.v1_16_R2.EntityFallingBlock;

/**
 * Falling anvils break leaves
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/50a50s/falling_anvils_should_break_leaves/}
 */
public class AnvilSmash implements Listener {
  private Material[] BREAKABLE = new Material[] { Material.ACACIA_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES, Material.JUNGLE_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.GLASS, Material.GLASS_PANE, Material.ICE };
  private Material[] ANVILS    = new Material[] { Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL };

  @EventHandler
  public void onLand(EntityChangeBlockEvent e) {
    if (!Arrays.stream(ANVILS).anyMatch(material -> material == e.getTo()))
      return;
    Block beneath = e.getBlock().getLocation().add(0, -1, 0).getBlock();
    if (!Arrays.stream(BREAKABLE).anyMatch(material -> material == beneath.getType()))
      return;
    if (!beneath.breakNaturally())
      return;
    if (!(e.getEntity() instanceof FallingBlock))
      return;
    FallingBlock falling = (FallingBlock) e.getEntity();
    EntityFallingBlock efb = ((CraftFallingBlock) falling).getHandle();
    efb.dead = false;
    e.setCancelled(true);
  }
}
