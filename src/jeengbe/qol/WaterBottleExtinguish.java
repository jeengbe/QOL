package jeengbe.qol;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Thrown water bottles extinguish entitites around
 */
public class WaterBottleExtinguish implements Listener {
  @EventHandler
  public void onSplash(ProjectileHitEvent e) {
    if (!(e.getEntity() instanceof ThrownPotion))
      return;
    ThrownPotion potion = (ThrownPotion) e.getEntity();
    if (!potion.getEffects().isEmpty())
      return;
    Collection<Entity> affected = potion.getWorld().getNearbyEntities(potion.getLocation(), 5, 3, 5);
    affected.removeIf(entity -> !(entity instanceof LivingEntity));
    affected.forEach(entity -> ((LivingEntity) entity).setFireTicks(0));
  }
}
