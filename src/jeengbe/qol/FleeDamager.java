package jeengbe.qol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import jeengbe.qol.ai.PathfinderGoalAvoidDamager;
import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.GenericAttributes;
import net.minecraft.server.v1_16_R2.PathfinderGoal;
import net.minecraft.server.v1_16_R2.PathfinderGoalPanic;
import net.minecraft.server.v1_16_R2.PathfinderGoalWrapped;

/**
 * Animals flee from their last damager
 */
public class FleeDamager implements Listener {
  private EntityType[] FLEE = new EntityType[] { EntityType.CHICKEN, EntityType.COW, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.HORSE, EntityType.LLAMA, EntityType.TRADER_LLAMA, EntityType.PARROT, EntityType.PIG, EntityType.SHEEP, EntityType.STRIDER, EntityType.WANDERING_TRADER };

  @EventHandler
  public void onLoad(ChunkLoadEvent e) {
    List<Entity> patch = new ArrayList<>();
    for (Entity ent : e.getChunk().getEntities()) {
      if (!Arrays.stream(FLEE).anyMatch(entityType -> ent.getType() == entityType)) {
        continue;
      }
      patch.add(ent);
    }

    patch.forEach(ent -> patchAI(ent));
  }

  @EventHandler
  public void onSpawn(CreatureSpawnEvent e) {
    if (!Arrays.stream(FLEE).anyMatch(entityType -> e.getEntity().getType() == entityType))
      return;
    patchAI(e.getEntity());
  }

  private void patchAI(Entity entity) {
    EntityCreature nmsEntity = (EntityCreature) ((CraftLivingEntity) entity).getHandle();
    PathfinderGoalWrapped panicGoalWrapper = nmsEntity.goalSelector.getTasks().stream().filter(goal -> goal.getGoal() instanceof PathfinderGoalPanic).findFirst().orElseGet(() -> null);
    if (panicGoalWrapper == null)
      return;
    PathfinderGoal panicGoal = panicGoalWrapper.getGoal();

    nmsEntity.goalSelector.a(panicGoal);
    nmsEntity.goalSelector.a(panicGoalWrapper.h(), new PathfinderGoalAvoidDamager(nmsEntity, 9F, nmsEntity.getAttributeMap().a(GenericAttributes.MOVEMENT_SPEED).getValue() * 5.5));
  }
}