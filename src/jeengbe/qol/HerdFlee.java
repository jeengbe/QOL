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
import net.minecraft.server.v1_16_R2.PathfinderGoal;
import net.minecraft.server.v1_16_R2.PathfinderGoalPanic;

/**
 * Attacking an animal causes all similar animals nearby to panic
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/8v9haa/when_an_animal_gets_hurt_by_a_player_not_only_the/}
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

    Collection<Entity> nearby = e.getEntity().getWorld().getNearbyEntities(e.getEntity().getLocation(), 10, 5, 10, entity -> Arrays.stream(PANICKING).anyMatch(entityType -> entity.getType() == entityType));
    nearby.forEach(entity -> {
      EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
      nmsEntity.goalSelector.getTasks().stream().filter(goal -> goal.getGoal() instanceof PathfinderGoalPanic || goal.getGoal() instanceof PathfinderGoalAvoidDamager).forEach(goal -> {
        if (nmsEntity.getLastDamager() != null && !nmsEntity.getLastDamager().getUniqueID().equals(((CraftLivingEntity) e.getDamager()).getHandle().getUniqueID()))
          return;
        nmsEntity.setLastDamager(((CraftLivingEntity) e.getDamager()).getHandle());
        PathfinderGoal panicGoal = goal.getGoal();
        if (panicGoal.a()) {
          panicGoal.c();
        }
      });
    });
  }
}
