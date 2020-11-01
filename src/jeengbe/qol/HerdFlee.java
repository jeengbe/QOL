package jeengbe.qol;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.PathfinderGoalPanic;

/**
 * Attacking an animal causes all similar animals nearby to panic
 */
public class HerdFlee implements Listener {
  private EntityType[] PANICKING = new EntityType[] { EntityType.CHICKEN, EntityType.COW, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.HORSE, EntityType.LLAMA, EntityType.TRADER_LLAMA, EntityType.PARROT, EntityType.PIG, EntityType.SHEEP, EntityType.STRIDER, EntityType.WANDERING_TRADER };

  @EventHandler
  public void onDamage(EntityDamageByEntityEvent e) {
    if (e.getCause() != DamageCause.ENTITY_ATTACK)
      return;
    if (!(e.getDamager() instanceof LivingEntity))
      return;
    if (!(e.getEntity() instanceof LivingEntity))
      return;
    if (!Arrays.stream(PANICKING).anyMatch(entityType -> e.getEntity().getType() == entityType))
      return;

    Collection<Entity> nearby = e.getEntity().getWorld().getNearbyEntitiesByType(e.getEntity().getClass(), e.getEntity().getLocation(), 5, 4, 5);
    nearby.forEach(entity -> {
      EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
      nmsEntity.goalSelector.getTasks().stream().filter(goal -> goal.getGoal() instanceof PathfinderGoalPanic).forEach(goal -> {
        if (nmsEntity.getLastDamager() != null)
          return;
        nmsEntity.setLastDamager(((CraftLivingEntity) e.getDamager()).getHandle());
        PathfinderGoalPanic panicGoal = ((PathfinderGoalPanic) goal.getGoal());
        if (panicGoal.a()) {
          panicGoal.c();
        }
      });
    });
  }
}
