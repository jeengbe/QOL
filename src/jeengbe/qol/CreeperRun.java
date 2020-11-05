package jeengbe.qol;

import java.util.Arrays;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import jeengbe.qol.ai.PathfinderGoalFleeCreeper;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.GenericAttributes;

/**
 * Mobs run away from exploding creepers
 */
public class CreeperRun implements Listener {
  @EventHandler
  public void onLoad(ChunkLoadEvent e) {
    Arrays.stream(e.getChunk().getEntities()).forEach(ent -> patchAI(ent));
  }

  @EventHandler
  public void onSpawn(CreatureSpawnEvent e) {
    patchAI(e.getEntity());
  }

  private void patchAI(org.bukkit.entity.Entity entity) {
    Entity nmsEntityGeneric = ((CraftEntity) entity).getHandle();
    if (!(nmsEntityGeneric instanceof EntityCreature))
      return;
    EntityCreature nmsEntity = (EntityCreature) nmsEntityGeneric;
    nmsEntity.goalSelector.a(1, new PathfinderGoalFleeCreeper(nmsEntity, 9F, nmsEntity.getAttributeMap().a(GenericAttributes.MOVEMENT_SPEED).getValue() * 5.5));
  }
}
