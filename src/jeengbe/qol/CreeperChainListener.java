package jeengbe.qol;

import org.bukkit.Difficulty;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Creeper explosions create a chain reaction on Normal or Hard Difficulty
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/irammk/in_hard_mode_when_the_explosion_of_a_creeper_hits/}
 */
public class CreeperChainListener implements Listener {
  @EventHandler
  public void onExplode(EntityDamageByEntityEvent e) {
    if (e.getEntityType() != EntityType.CREEPER)
      return;
    if (e.getDamager().getType() != EntityType.CREEPER)
      return;
    if (e.getCause() != DamageCause.ENTITY_EXPLOSION)
      return;
    if (!(e.getEntity().getWorld().getDifficulty() == Difficulty.NORMAL || e.getEntity().getWorld().getDifficulty() == Difficulty.HARD))
      return;

    ((Creeper) e.getEntity()).setMaxFuseTicks(5);
    ((Creeper) e.getEntity()).ignite();
  }
}
