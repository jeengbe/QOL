package jeengbe.qol;

import java.util.Collection;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NetherSmokeGrenade implements Listener {
  @EventHandler
  public void onSplash(ProjectileHitEvent e) {
    if (!(e.getEntity() instanceof ThrownPotion))
      return;
    ThrownPotion potion = (ThrownPotion) e.getEntity();
    if (!potion.getEffects().isEmpty())
      return;
    if (!potion.getWorld().getName().endsWith("_nether"))
      return;
    potion.getWorld().spawnParticle(Particle.SMOKE_LARGE, potion.getLocation(), 10000, 5, 3, 5, 0);
    Collection<Entity> affected = potion.getWorld().getNearbyEntities(potion.getLocation(), 5, 3, 5);
    affected.removeIf(entity -> !(entity instanceof LivingEntity));
    affected.forEach(entity -> ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 15, 0)));
  }
}
