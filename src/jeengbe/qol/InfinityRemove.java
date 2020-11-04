package jeengbe.qol;

import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Arrows that cannot be picked up disappear burst into particles
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/7x50vt/small_changes_to_the_infinity_enchantment/}
 */
public class InfinityRemove implements Listener {
  @EventHandler
  public void onHit(ProjectileHitEvent e) {
    if (!(e.getEntity() instanceof Arrow))
      return;
    Arrow arrow = (Arrow) e.getEntity();

    if (arrow.getPickupStatus() != PickupStatus.DISALLOWED && arrow.getPickupStatus() != PickupStatus.CREATIVE_ONLY)
      return;

    arrow.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, arrow.getLocation(), 15, 0, 0, 0, 0.05);

    if (e.getHitEntity() != null) {
      if (e.getHitEntity() instanceof LivingEntity) {
        LivingEntity entity = (LivingEntity) e.getHitEntity();
        entity.setArrowsInBody(entity.getArrowsInBody());
      }
    }

    arrow.remove();
  }
}
