package jeengbe.qol;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

/**
 * Falling anvils dry wet sponges
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/50a50s/falling_anvils_should_break_leaves/d72m2xq?utm_source=share&utm_medium=web2x&context=3}
 */
public class AnvilSpongeDry implements Listener {
  private Material[] ANVILS = new Material[] { Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL };

  @EventHandler
  public void onLand(EntityChangeBlockEvent e) {
    if (!Arrays.stream(ANVILS).anyMatch(material -> material == e.getTo()))
      return;
    Block beneath = e.getBlock().getLocation().add(0, -1, 0).getBlock();
    if (beneath.getType() != Material.WET_SPONGE)
      return;
    beneath.setType(Material.SPONGE);
    beneath.getWorld().spawnParticle(Particle.WATER_SPLASH, beneath.getLocation().add(0.5, 1, 0.5), 100, 0.35, 0, 0.35, 0);
  }
}
